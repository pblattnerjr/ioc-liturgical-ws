package net.ages.alwb.utils.core.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;

public class CheckSum {
	
  public static void main(String args[]) throws Exception {
   
    String datafile = "/Users/mac002/Git/common-utilities/common-utilities/target/common-utilities-v1.0.jar";

    MessageDigest md = MessageDigest.getInstance("SHA1");
    System.out.println("SHA1: " + compute(datafile,md));
    md = MessageDigest.getInstance("MD5");
    System.out.println("MD5: " + compute(datafile,md));
  }
  
  private static String compute(String datafile, MessageDigest md) {
	    FileInputStream fis;
	    StringBuffer sb = new StringBuffer("");
		try {
			fis = new FileInputStream(datafile);
		    byte[] dataBytes = new byte[1024];
		    
		    int nread = 0; 
		    
		    while ((nread = fis.read(dataBytes)) != -1) {
		      md.update(dataBytes, 0, nread);
		    };

		    byte[] mdbytes = md.digest();
		   
		    //convert the byte to hex format
		    for (int i = 0; i < mdbytes.length; i++) {
		    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		    }
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return sb.toString();

  }
}

