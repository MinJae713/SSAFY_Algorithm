package testReview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Test3_유민재 {
	static class Turnel {
		int from;
		int to;
		public Turnel(int from, int to) {
			super();
			this.from = from;
			this.to = to;
		}
	}
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int templeCount;
	private static int turnelCount;
	private static Queue<Turnel> queue;
	private static int[] templeParents;
	private static int connectedCount;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/Test3.txt"));
		initialize();
		// logic
		boolean notCycle = true; // 수정
		while (!queue.isEmpty()) {
			Turnel turnel = queue.poll(); 
			notCycle = unionTemples(turnel.from, turnel.to);
			connectedCount++;
			if (!notCycle) break; 
			// 사이클이 생겨서 union이 되지 않음 -> 반복 종료
		}
		System.out.println(notCycle ? 0 : connectedCount);
		// 문제좀 잘 읽어봐라;;; 이거 System.out.println(connectedCount); 썼어요 너;;
		reader.close();
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		templeCount = Integer.parseInt(tokenizer.nextToken());
		turnelCount = Integer.parseInt(tokenizer.nextToken());
		templeParents = new int[templeCount];
		for (int temple=0; temple<templeCount; temple++)
			templeParents[temple] = temple;
		queue = new ArrayDeque<>();
		for (int index=0; index<turnelCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			queue.offer(new Turnel(from, to));
		}
		connectedCount = 0;
	}
	
	private static boolean unionTemples(int from, int to) {
		int rootFrom = findRoot(from);
		int rootTo = findRoot(to);
		if (rootFrom == rootTo) return false;
		if (rootFrom > rootTo) 
			templeParents[rootTo] = rootFrom;
		else templeParents[rootFrom] = rootTo;
		return true;
	}
	private static int findRoot(int temple) {
		if (temple == templeParents[temple]) return temple;
		return templeParents[temple] = findRoot(templeParents[temple]);
	}

}
