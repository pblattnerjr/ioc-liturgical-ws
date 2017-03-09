package ioc.liturgical.ws.managers.databases.external.neo4j;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.managers.databases.external.neo4j.constants.MATCHERS;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.DomainTopicMapBuilder;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.QueryToDb;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;;


/**
 * @author Michael Colburn
 * @since 2016
 */
public class ExternalDbManager {
	
	/**
	 * TODO: need to make sure that the dropdownItems are rebuilt when a put or post
	 * invalidates them.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExternalDbManager.class);
	  private boolean logAllQueries = false;
	  private boolean logQueriesWithNoMatches = false;
	  
	  JsonParser parser = new JsonParser();
	  Pattern punctPattern = Pattern.compile("[˙·,.;!?(){}\\[\\]<>%]"); // punctuation 
	  DomainTopicMapBuilder domainTopicMapbuilder = new DomainTopicMapBuilder();
	  JsonObject dropdownItems = new JsonObject();
	  public static QueryToDb queryToDb = null;
	  
	  public ExternalDbManager(
			  String domain
			  , boolean logQueries
			  , boolean logQueriesWithNoMatches
			  ) {
		  queryToDb = new QueryToDb(
				  domain
				  ,ServiceProvider.ws_usr
				  , ServiceProvider.ws_pwd
				  );
		  this.logAllQueries = logQueries;
		  this.logQueriesWithNoMatches = logQueriesWithNoMatches;
		  buildDomainTopicMap();
	  }
	  
	  public void buildDomainTopicMap() {
		 dropdownItems = domainTopicMapbuilder.getDropdownItems();
	  }
	  
	  public JsonObject getForQuery(String query) {
			ResultJsonObjectArray result = queryToDb.getResultObjectForQuery(query, true);
			if (logAllQueries
					|| 
					(logQueriesWithNoMatches && result.getResultCount() == 0) 
					) {
				logger.info(query);
				logger.info("Result count: " + result.getResultCount());
			}
			return result.toJsonObject();
	}
	  
		public JsonObject search(
				String type
				, String domain
				, String book
				, String chapter
				, String query
				, String property
				, String matcher
				) {
			JsonObject result = null;
			if (type == null) {
				type = "id";
			}
			/**
			 * This is a workaround for ambiguity between the codes used with the Hieratikon sections
			 * and those of the Horologion.  Fr. Seraphim uses "s01" etc for both, but with different meaning.
			 * So the dropdown uses "his01" for Hieratikon sections.  But the database uses "s01". 
			 * So we will intercept this and change his into s for the db search.
			 */
			if (chapter.startsWith("his")) {
				chapter = chapter.replaceFirst("hi", "");
			}
			result = getForQuery(getCypherQuery(type, domain, book, chapter, query, property, matcher));
			return result;
		}

		private String removePunctuation(String s) {
			try {
				return punctPattern.matcher(s).replaceAll("");
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
				return s;
			}
		}
		
	   private String normalized(String s) {
		   return Normalizer.normalize(s, Normalizer.Form.NFD)
					.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
	   }
	   
		private String getCypherQuery(
				String type
				, String domain
				, String book
				, String chapter
				, String query
				, String property
				, String matcher
				) {
			String theQuery = query;
			if (matcher.startsWith("rx")) {
				// ignore
			} else {
				// remove accents and punctuation if requested
				if (property.startsWith("nnp") || property.startsWith("nwp")) {
					theQuery = normalized(theQuery);
					if (property.startsWith("nnp")) {
						theQuery = removePunctuation(normalized(theQuery));
					}
				}
			}
			CypherQueryBuilder builder = new CypherQueryBuilder()
					.MATCH()
					.LABEL(type)
					.LABEL(domain)
					.LABEL(book)
					.LABEL(chapter)
					.WHERE(property)
					;
			
			MATCHERS matcherEnum = MATCHERS.forLabel(matcher);
			
			switch (matcherEnum) {
			case STARTS_WITH: {
				builder.STARTS_WITH(theQuery);
				break;
			}
			case ENDS_WITH: {
				builder.ENDS_WITH(theQuery);
				break;
			}
			case REG_EX: {
				builder.MATCHES_PATTERN(theQuery);
				break;
			} 
			default: {
				builder.CONTAINS(theQuery);
				break;
			}
			}
			builder.RETURN("id, value");
			builder.ORDER_BY("doc.seq"); // TODO: in future this could be a parameter in REST API

			CypherQuery q = builder.build();
			
			return q.toString();
		}
		
		public JsonObject getDropdownItems() {
			return dropdownItems;
		}
}
