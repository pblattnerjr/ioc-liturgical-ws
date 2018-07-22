package delete.me;

public class SubVerse {

	public static void main(String[] args) {
		String s = "10b";
		String subVerse = "";
		if (! s.matches("^.+?\\d$")) {
			subVerse = s.substring(s.length()-1);
			s = s.substring(0, s.length()-1);
		}
		System.out.println(s + " + " + subVerse);
	}

}
