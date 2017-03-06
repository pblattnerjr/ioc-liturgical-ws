package ioc.liturgical.ws.manager.database.external.neo4j.utils;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class Node  extends AbstractModel {
	
   @Expose private Map<String, Node> children = new TreeMap<>();
    
    public Node() {
    	super();
    	this.setPrettyPrint(true);
    }

    public Node getChild(String name) {
        if (children.containsKey(name))
            return children.get(name);
        Node result = new Node();
        children.put(name, result);
        return result;
    }

    public void setChildren(Map<String, Node> children) {
    	this.children = children;
    }
    public Map<String, Node> getChildren() {
        return Collections.unmodifiableMap(children);
    }
}
