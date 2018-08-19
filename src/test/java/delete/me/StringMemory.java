package delete.me;

public class StringMemory {

	public static int space(int charCnt, int nbr) {
		int r = 8 * (int) ((((charCnt) * 2) + 45) / 8);
		return nbr * r;
	}
	public static void main(String[] args) {
		int charCnt = 90;
		int nbr = 438215;
		System.out.println("Bytes: " + StringMemory.space(charCnt, nbr));
		// megabytes = 1024^2
		System.out.println("mb: " + StringMemory.space(charCnt, nbr) / Math.pow(1024,2));
		// 1,073,741,824
		System.out.println("gb: " + StringMemory.space(charCnt, nbr) / 1073741824);
	}

}
