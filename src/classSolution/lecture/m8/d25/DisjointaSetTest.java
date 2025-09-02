package classSolution.lecture.m8.d25;
import java.util.Arrays;
import java.util.Scanner;

public class DisjointaSetTest {
	
	static int N;
	static int[] parents;
	
	private static void make() {
		for (int i=0; i<N; i++)
			parents[i] = i; // 자신이 루트며, 대표자
	}
	
	private static int find(int a) {
		if (a == parents[a]) return a;
		return parents[a] = find(parents[a]);
	}
	
	private static boolean union(int a, int b) {
		int aRoot = find(a);
		int bRoot = find(b);
		if (aRoot == bRoot) return false;
		parents[bRoot] = aRoot;
		return true;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = 5;
		parents = new int[N];
		
		make();
		System.out.println(Arrays.toString(parents));
		System.out.println(union(0, 1));
		System.out.println(Arrays.toString(parents));
		System.out.println(union(2, 1));
		System.out.println(Arrays.toString(parents));
		System.out.println(union(3, 2));
		System.out.println(Arrays.toString(parents));
		System.out.println(union(4, 3));
		System.out.println(Arrays.toString(parents));
		sc.close();
		System.out.println(find(0));
		System.out.println(find(1));
		System.out.println(find(2));
		System.out.println(find(3));
		System.out.println(find(4));
		System.out.println("===union fail===");
		System.out.println(union(2, 3));
	}

}
