import java.util.Random;

public class test {
	static int size = 10000000;
	static Random rand = new Random();
	static int n = rand.nextInt(size);
	static int m = rand.nextInt(size);
	static int crit = rand.nextInt(10);
	public static void main(String[] args) {
		System.out.println(n);
		if (crit == 9) {
			System.out.println("Critical Hit!");
			System.out.println(m * 100);
		}
		else {
			System.out.println(m);
		}
	}
}
