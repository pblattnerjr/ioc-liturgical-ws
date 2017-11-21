package net.ages.alwb.utils.nlp.constants;

import java.util.regex.Pattern;

public class Constants {
	public static final String sentenceTerminators = ".|";
	public static final  String PUNCTUATION = "[˙·,.;!?()'\"{}\\[\\]<>%]"; 
	public static final  Pattern punctPattern = Pattern.compile(PUNCTUATION); // punctuation 

}
