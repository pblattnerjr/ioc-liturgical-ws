package net.ages.alwb.utils.core.misc;

/**
 * This class is used by junit test cases.
 * Its purpose is to avoid putting the password in source code.
 * 
 * 1. In the run configuration for this class, put the password in the arguments tab.
 * 2. Run the main class for SystemTestProperties.
 * 3. Then the junit test cases can retrieve the password.
 * @author mac002
 *
 */
public class SystemTestProperties {
	
	public enum PROPS {
		PASSWORD;
	}

	public static String getPassword() {
		return System.getProperty(PROPS.PASSWORD.toString());
	}
	public static void main(String[] args) {
		System.setProperty(PROPS.PASSWORD.toString(), args[0]);
	}

}
