package ioc.liturgical.ws.models.ws.response;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Model for return a Resource description to a REST requestor.
 * 
 * @author mac002
 *
 */
public class Resource extends AbstractModel {
	@Expose String name;
	@Expose String description;
}
