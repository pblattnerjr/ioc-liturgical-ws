package net.ages.alwb.utils.nlp.models;

public class CharacterInfo {
	private char c;
	public CharacterInfo(char c) {
		this.c = c;
	}
	
	public int value() {
		return (int) c;
	}
	
	public String block() {
		return Character.UnicodeBlock.of(c).toString();
	}
	
	public String toString() {
		return c + " : " + this.value() + " :" + this.block();
	}
}
