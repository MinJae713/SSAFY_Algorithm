package homeworks.m8.d21;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 선행 작업이 없는 작업 번호들을 큐에 enque
 * 2. 큐에 작업이 있는 동안 반복
 * 3. 큐에서 작업 하나 deque(work)
 * 4. work 해당 작업의 번호를 builder에 추가
 * 5. work 작업 바로 다음의 선행 작업들에 대해 반복(nextWork)
 * 	5.1. nextWork 해당 작업의 후행 작업 개수 1 감소
 * 	5.2. nextWork 후행 작업 개수가 0이라면?
 * 		5.2.1. nextWork 해당 작업의 번호 큐에 추가
 */
public class WorkFlow {
	static class Work {
		int number;
		Work next;
		public Work(int number, Work next) {
			super();
			this.number = number;
			this.next = next;
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int workCount;
	private static Work[] nextWorks;
	private static int[] beforeWorkCount;
	
	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("sample_input.txt"));
		initializeTest();
		for (int testCase=1; testCase<=10; testCase++) {
			initialize();
			// logic
			getWorkFlow(testCase);
		}
		System.out.print(builder);
		reader.close();
	}

	private static void getWorkFlow(int testCase) {
		builder.append("#").append(testCase);
		Queue<Integer> queue = new ArrayDeque<Integer>();
		for (int workNumber=1; workNumber<=workCount; workNumber++)
			if (beforeWorkCount[workNumber] == 0)
				queue.offer(workNumber);
		while (!queue.isEmpty()) {
			int work = queue.poll();
			builder.append(" ").append(work);
			for (Work nextWork=nextWorks[work]; nextWork!=null; nextWork=nextWork.next) 
				if (--beforeWorkCount[nextWork.number] == 0)
					queue.offer(nextWork.number);
		}
		builder.append("\n");
	}

	private static void initializeTest() {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		workCount = Integer.parseInt(tokenizer.nextToken());
		nextWorks = new Work[workCount+1];
		beforeWorkCount = new int[workCount+1];
		int edgeCount = Integer.parseInt(tokenizer.nextToken());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<edgeCount; index++) {
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			nextWorks[from] = new Work(to, nextWorks[from]);
			beforeWorkCount[to]++;
		}
	}
}
