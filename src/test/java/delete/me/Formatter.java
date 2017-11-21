package delete.me;

public class Formatter {

   private static String done(long x, long y) {
	   int percentage = (int)(x * 100.0 / y + 0.5);
    	return String.format("%,d%% done.", percentage);
    }

   public static void main(String[] args) {
	   Long x = Long.parseLong("10");
	   Long y = Long.parseLong("100");
	   System.out.println(done(x,y));
	}

}
