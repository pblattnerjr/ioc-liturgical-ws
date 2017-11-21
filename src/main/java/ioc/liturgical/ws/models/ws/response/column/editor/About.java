package ioc.liturgical.ws.models.ws.response.column.editor;

import java.time.LocalDateTime;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Provides metadata about the collections of templates / topics keys and library keys
 * @author mac002
 *
 */
public class About extends AbstractModel {
	@Expose String template = "";
	@Expose String library = "gr_gr_cog";
	@Expose String dateGenerated = LocalDateTime.now().toString();
	@Expose String logname = "";
	@Expose int templateKeyCount = 0;
	@Expose int libraryKeyCount = 0;
	@Expose int redundantKeyCount = 0;
	@Expose String compression = "";
	
	public About() {
		super();
	}

	public About(boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getDateGenerated() {
		return dateGenerated;
	}

	public void setDateGenerated(String dateGenerated) {
		this.dateGenerated = dateGenerated;
	}

	public String getLogname() {
		return logname;
	}

	public void setLogname(String logname) {
		this.logname = logname;
	}

	public int getTemplateKeyCount() {
		return templateKeyCount;
	}

	public void setTemplateKeyCount(int templateKeyCount) {
		this.templateKeyCount = templateKeyCount;
	}

	public int getLibraryKeyCount() {
		return libraryKeyCount;
	}

	public void setLibraryKeyCount(int libraryKeyCount) {
		this.libraryKeyCount = libraryKeyCount;
	}

	public int getRedundantKeyCount() {
		return redundantKeyCount;
	}

	public void setRedundantKeyCount(int redundantKeyCount) {
		this.redundantKeyCount = redundantKeyCount;
	}

	public String getCompression() {
		return compression;
	}

	public void setCompression(String compression) {
		this.compression = compression;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}
}
