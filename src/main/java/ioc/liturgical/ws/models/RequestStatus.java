package ioc.liturgical.ws.models;

import org.neo4j.driver.v1.summary.SummaryCounters;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Holds the status of a Request.
 * 
 * The constructor sets the status code to 200 (OK).
 * 
 * If the code is different (i.e. there are errors), call the set methods.
 * 
 * @author mac002
 *
 */
public class RequestStatus extends AbstractModel {
	@Expose public int code = HTTP_RESPONSE_CODES.OK.code;
	@Expose public String developerMessage = HTTP_RESPONSE_CODES.OK.message;
	@Expose public String userMessage = HTTP_RESPONSE_CODES.OK.message;
	@Expose public int constraintsAdded = 0;
	@Expose public int constraintsRemoved = 0;
	@Expose public int indexesAdded = 0;
	@Expose public int indexesRemoved = 0;
	@Expose public int labelsAdded = 0;
	@Expose public int labelsRemoved = 0;
	@Expose public int nodesCreated = 0;
	@Expose public int nodesDeleted = 0;
	@Expose public int propertiesSet = 0;
	@Expose public int relationshipsCreated = 0;
	@Expose public int relationshipsDeleted = 0;
	@Expose int counterTotal = 0;
	@Expose boolean containsUpdates = false;
	
	public void recordCounters(SummaryCounters summary) {
		this.constraintsAdded = summary.constraintsAdded();
		this.counterTotal = this.counterTotal + constraintsAdded;
		this.constraintsRemoved = summary.constraintsRemoved();
		this.counterTotal = this.counterTotal + constraintsRemoved;
		this.indexesAdded = summary.indexesAdded();
		this.counterTotal = this.counterTotal + indexesAdded;
		this.indexesRemoved = summary.indexesRemoved();
		this.counterTotal = this.counterTotal + indexesRemoved;
		this.labelsAdded = summary.labelsAdded();
		this.counterTotal = this.counterTotal + labelsAdded;
		this.labelsRemoved = summary.labelsRemoved();
		this.counterTotal = this.counterTotal + labelsRemoved;
		this.nodesCreated = summary.nodesCreated();
		this.counterTotal = this.counterTotal + nodesCreated;
		this.nodesDeleted = summary.nodesDeleted();
		this.counterTotal = this.counterTotal + nodesDeleted;
		this.propertiesSet = summary.propertiesSet();
		this.counterTotal = this.counterTotal + propertiesSet;
		this.relationshipsCreated = summary.relationshipsCreated();
		this.counterTotal = this.counterTotal + relationshipsCreated;
		this.relationshipsDeleted = summary.relationshipsDeleted();
		this.counterTotal = this.counterTotal + relationshipsDeleted;
		this.containsUpdates = summary.containsUpdates();
	}

	public boolean wasSuccessful() {
		return this.counterTotal > 0 || this.containsUpdates;
	}
	/**
	 * Defaults to code = 200 (OK)
	 */
	public RequestStatus() {
		super();
	}
	
	public RequestStatus(HTTP_RESPONSE_CODES code) {
		super();
		this.code = code.code;
		this.userMessage = code.message;
	}
	
	/**
	 * Both developerMessage and userMessage will be set to the value of message
	 * @param code
	 * @param message
	 */
	public RequestStatus(int code, String message) {
		super();
		this.code = code;
		this.developerMessage = message;
		this.userMessage = message;
	}
	
	public RequestStatus(int code, String developerMessage, String userMessage) {
		super();
		this.code = code;
		this.developerMessage = developerMessage;
		this.userMessage = userMessage;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDeveloperMessage() {
		return developerMessage;
	}
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	public String getUserMessage() {
		return userMessage;
	}
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	
	public void setMessage(String message) {
		setDeveloperMessage(message);
		setUserMessage(message);
	}
	public int getConstraintsAdded() {
		return constraintsAdded;
	}
	public void setConstraintsAdded(int constraintsAdded) {
		this.constraintsAdded = constraintsAdded;
	}
	public int getConstraintsRemoved() {
		return constraintsRemoved;
	}
	public void setConstraintsRemoved(int constraintsRemoved) {
		this.constraintsRemoved = constraintsRemoved;
	}
	public int getIndexesAdded() {
		return indexesAdded;
	}
	public void setIndexesAdded(int indexesAdded) {
		this.indexesAdded = indexesAdded;
	}
	public int getIndexesRemoved() {
		return indexesRemoved;
	}
	public void setIndexesRemoved(int indexesRemoved) {
		this.indexesRemoved = indexesRemoved;
	}
	public int getLabelsAdded() {
		return labelsAdded;
	}
	public void setLabelsAdded(int labelsAdded) {
		this.labelsAdded = labelsAdded;
	}
	public int getLabelsRemoved() {
		return labelsRemoved;
	}
	public void setLabelsRemoved(int labelsRemoved) {
		this.labelsRemoved = labelsRemoved;
	}
	public int getNodesCreated() {
		return nodesCreated;
	}
	public void setNodesCreated(int nodesCreated) {
		this.nodesCreated = nodesCreated;
	}
	public int getNodesDeleted() {
		return nodesDeleted;
	}
	public void setNodesDeleted(int nodesDeleted) {
		this.nodesDeleted = nodesDeleted;
	}
	public int getPropertiesSet() {
		return propertiesSet;
	}
	public void setPropertiesSet(int propertiesSet) {
		this.propertiesSet = propertiesSet;
	}
	public int getRelationshipsCreated() {
		return relationshipsCreated;
	}
	public void setRelationshipsCreated(int relationshipsCreated) {
		this.relationshipsCreated = relationshipsCreated;
	}
	public int getRelationshipsDeleted() {
		return relationshipsDeleted;
	}
	public void setRelationshipsDeleted(int relationshipsDeleted) {
		this.relationshipsDeleted = relationshipsDeleted;
	}
	public boolean isContainsUpdates() {
		return containsUpdates;
	}
	public void setContainsUpdates(boolean containsUpdates) {
		this.containsUpdates = containsUpdates;
	}
	public int getCounterTotal() {
		return counterTotal;
	}
	public void setCounterTotal(int counterTotal) {
		this.counterTotal = counterTotal;
	}
}
