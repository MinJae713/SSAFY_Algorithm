package homeworks.m8.d25;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 마을의 모든 사람에 대해 반복 (number)
 * 2. 해당 사람이 이미 방문한 사람이라면? 다음 반복 수행
 * 3. number와 이어져있는 사람 확인(checkKnowPeople)
 * 	3.1. number를 큐에 enque
 * 	3.2. number 해당 사람 방문 여부 true
 * 	3.3. 큐에 요소가 있는 동안 반복
 * 		3.3.1. 큐에서 요소 deque (current)
 * 		3.3.2. current 해당 사람을 아는 사람들에 대해 반복 수행 (know)
 * 		3.3.3. know 해당 사람을 이미 방문했다면 다음 반복 수행
 * 		3.3.4. 방문하지 않았다면 해당 사람 큐에 입력 및 방문 여부 true
 * 	3.4. 무리의 수(personCount) 1 증가
 */
public class Villagers {
	static class Person {
		int number;
		Person nextKnow;
		public Person(int number, Person nextKnow) {
			super();
			this.number = number;
			this.nextKnow = nextKnow;
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int personCount;
	private static Person[] knowList;
	private static boolean[] visited;
	private static int groupCount;
	
	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("s_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int number=1; number<=personCount; number++) {
				if (visited[number]) continue;
				checkKnowPeople(number);
			}
			builder.append("#").append(testCase).
					append(" ").append(groupCount).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void checkKnowPeople(int number) {
		Queue<Integer> queue = new ArrayDeque<>();
		queue.offer(number);
		visited[number] = true;
		while (!queue.isEmpty()) {
			int current = queue.poll();
			for (Person know=knowList[current]; know!=null; know=know.nextKnow) {
				if (visited[know.number]) continue;
				queue.offer(know.number);
				visited[know.number] = true;
			}
		}
		groupCount++;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		personCount = Integer.parseInt(tokenizer.nextToken());
		int knowCount = Integer.parseInt(tokenizer.nextToken());
		knowList = new Person[personCount+1];
		visited = new boolean[personCount+1];
		groupCount = 0;
		for (int index=0; index<knowCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			if (!tokenizer.hasMoreTokens()) continue; // 다음 요소가 없을 수도 있음 -> 그 경우는 관계 형성 불가
			int from = Integer.parseInt(tokenizer.nextToken());
			if (!tokenizer.hasMoreTokens()) continue; // 다음 요소가 없을 수도 있음 -> 그 경우는 관계 형성 불가
			int to = Integer.parseInt(tokenizer.nextToken());
			knowList[from] = new Person(to, knowList[from]);
		}
	}
}
