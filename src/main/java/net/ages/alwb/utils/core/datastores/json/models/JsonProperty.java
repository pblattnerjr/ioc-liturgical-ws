package net.ages.alwb.utils.core.datastores.json.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.misc.Constants;

public class JsonProperty<T> extends SimpleObjectProperty<T> {
    private static final Logger logger = LoggerFactory.getLogger(JsonProperty.class);
    protected Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private TKVJsonPrimitive tkvJsonPrimitive;

    /**
     * Version with a specified topic
     * @param bean
     * @param topic
     * @param name
     * @param initialValue
     */
    public JsonProperty(Object bean, String topic, String name, T initialValue) {
		super(bean, name, initialValue);
		setPrimitive(
				topic
				, name
				, initialValue
		);
		addListener();
	}
	
    /**
     * Version whose topic will be the name of the class of the bean.
     * This is suitable when the bean is a singleton.
     * If you plan to instantiate the bean (i.e. have multiple occurrences)
     * use the constructor with the topic parameter.
     * @param bean
     * @param name
     * @param initialValue
     */
	public JsonProperty(Object bean, String name, T initialValue) {
		super(bean, name, initialValue);
		setPrimitive(
				bean.getClass().getSimpleName()
				, name
				, initialValue
		);
		addListener();
	}
		
	private void setPrimitive(String topic, String key, T value) {
		tkvJsonPrimitive = new TKVJsonPrimitive();
		tkvJsonPrimitive.setTopic(topic);
		tkvJsonPrimitive.setKey(key);
		tkvJsonPrimitive.setValue(value.toString());
	}
	
	private void addListener() {
		    super.addListener(new ChangeListener<Object>(){
		        @Override
		        public void changed(ObservableValue<?> value, Object oldValue, Object newValue) {
		        	tkvJsonPrimitive.setValue(newValue.toString());
		        }
		    });
		}

	public String get_Id() {
		return getTopic() + Constants.ID_DELIMITER + super.getName();
	}
	
	public String getTopic() {
		return tkvJsonPrimitive.getTopic();
	}
	
	public String getKey() {
		return this.getName();
	}
	
	public TKVJsonPrimitive getTkvJsonPrimitive() {
		return this.tkvJsonPrimitive;
	}
	
	public void setPrettyPrint(boolean printPretty) {
    	if (printPretty) {
        	gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    	} else {
        	gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();        		
    	}
	}

}
