package net.ages.alwb.utils.core.auth;

	import java.math.BigInteger;
	import java.security.NoSuchAlgorithmException;
	import java.security.SecureRandom;
	import java.security.spec.InvalidKeySpecException;

	import javax.crypto.SecretKeyFactory;
	import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	/**
	 * Utility for creating password hashes and validating passwords.
	 * 
	 * Note that there is an issue using this on Linux.  
	 * 
	 * edit the file
	 *      /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/java.security 
	 * and change
	 * 	    securerandom.source=file:/dev/random
	 * to 
	 *     securerandom.source=file:/dev/urandom
	 * 
	 * See https://docs.oracle.com/cd/E13209_01/wlcp/wlss30/configwlss/jvmrand.html
	 * 
	 * @author mac002
	 *
	 */
	public class PasswordHasher {
		private static final Logger logger = LoggerFactory.getLogger(PasswordHasher.class);

		
		public static void main(String[] args) {
			try {
				if (args.length == 2) {
					System.out.println(checkPassword(args[0], args[1]));
				} else if (args.length == 1) {
					String password = args[0];
					String hashed = createHash(args[0]);
					boolean isOk = checkPassword(password,hashed);
					System.out.println("\nPassword: " + password);
					System.out.println("Hash:\n\n" + hashed + "\n");
					System.out.println("Valid = " + isOk);
				} else {
					System.out.println("Usage: java -jar {jarname}.jar arg1 arg2");
					System.out.println("arg1 is a order");
					System.out.println("arg2 is a hash");
					System.out.println("Only arg1 is required.  If arg2 is null, a hash will be created.");
					System.out.println("If both args are provided, the order will be validated against the hash.");
					System.out.println("Here is a test...");
					String order = "IamAorder";
					String hash = createHash(order);
					boolean isOk = checkPassword(order,hash);
					System.out.println("order: " + order);
					System.out.println("Hash: " + hash);
					System.out.println("Valid = " + isOk);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		public static String createHash(
				String password
				) throws NoSuchAlgorithmException, InvalidKeySpecException
	    {
	        int iterations = 1000;
	        char[] chars = password.toCharArray();
	        byte[] salt = getSalt().getBytes();
	        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
	        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        byte[] hash = skf.generateSecret(spec).getEncoded();
	        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
	    }
	     
	    private static String getSalt() throws NoSuchAlgorithmException
	    {
	        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
	        byte[] salt = new byte[16];
	        sr.nextBytes(salt);
	        return salt.toString();
	    }
	     
	    private static String toHex(byte[] array) throws NoSuchAlgorithmException
	    {
	        BigInteger bi = new BigInteger(1, array);
	        String hex = bi.toString(16);
	        int paddingLength = (array.length * 2) - hex.length();
	        if(paddingLength > 0)
	        {
	            return String.format("%0"  +paddingLength + "d", 0) + hex;
	        }else{
	            return hex;
	        }
	    }
	    
	    public static boolean checkPassword(
	    		String password
	    		, String hashedPassword
	    		) throws NoSuchAlgorithmException, InvalidKeySpecException
	    {
	        String[] parts = hashedPassword.split(":");
	        int iterations = Integer.parseInt(parts[0]);
	        byte[] salt = fromHex(parts[1]);
	        byte[] hash = fromHex(parts[2]);
	         
	        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
	        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        byte[] testHash = skf.generateSecret(spec).getEncoded();
	         
	        int diff = hash.length ^ testHash.length;
	        for(int i = 0; i < hash.length && i < testHash.length; i++)
	        {
	            diff |= hash[i] ^ testHash[i];
	        }
	        return diff == 0;
	    }
	    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
	    {
	        byte[] bytes = new byte[hex.length() / 2];
	        for(int i = 0; i<bytes.length ;i++)
	        {
	            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
	        }
	        return bytes;
	    }


	}