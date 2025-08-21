package m8.d21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 키가 가장 작은 학생들(lowerCount 값이 0인)을 큐(queue)에 enque
 * 2. 큐에 학생이 있는 동안 반복
 * 3. 큐에서 학생 deque (current)
 * 4. current 해당 학생 번호 builder에 추가
 * 5. current 해당 학생 보다 큰 것으로 확인된 학생들(higher)에 대해 반복
 * 	5.1. higher 학생보다 키가 작은 학생 수(lowerCount) 1 감소
 * 	5.2. lowerCount 값이 0이라면 해당 학생 enque
 */
public class Lining {
	static class Student {
		int number;
		Student next;
		public Student(int number, Student next) {
			super();
			this.number = number;
			this.next = next;
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int studentCount;
	private static Student[] moreHeigher;
	private static int[] lowerCount;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		lining();
		System.out.print(builder);
		reader.close();
	}

	private static void lining() {
		Queue<Integer> queue = new ArrayDeque<Integer>();
		for (int student=1; student<=studentCount; student++)
			if (lowerCount[student] == 0)
				queue.offer(student);
		while (!queue.isEmpty()) {
			int current = queue.poll();
			builder.append(current).append(" ");
			for (Student higher=moreHeigher[current]; higher!=null; higher=higher.next)
				if (--lowerCount[higher.number] == 0)
					queue.offer(higher.number);
		}
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		tokenizer = new StringTokenizer(reader.readLine().trim());
		studentCount = Integer.parseInt(tokenizer.nextToken());
		moreHeigher = new Student[studentCount+1];
		lowerCount = new int[studentCount+1];
		int compareCount = Integer.parseInt(tokenizer.nextToken());
		for (int index=0; index<compareCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			moreHeigher[from] = new Student(to, moreHeigher[from]);
			lowerCount[to]++;
		}
	}
}
