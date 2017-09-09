package net.ages.alwb.utils.transformers.adapters.models;

public class OslwElement {
	StringBuffer command = new StringBuffer();
	StringBuffer keys = new StringBuffer();
	
	public void addCommand(String c) {
		command.append(c);
	}
	
	public void addKey(String k) {
		keys.append(k);
	}
	
	public void appendAll(OslwElement a) {
		this.addCommand(a.command.toString());
		this.addKey(a.keys.toString());
	}
	
	public String toString() {
		return (this.command.length() > 0 ?  this.command.toString() +"~" : "") + this.keys.toString();
	}

}
