package homeworks.m8.d6;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 모든 컴퓨터(computer)에 대해 반복
 * 	1.1. 그 컴퓨터가 다른 컴퓨터를 신뢰하는 컴퓨터라면 continue
 * 2. 해킹한 컴퓨터(visited) 배열, 해킹 횟수(count) 초기화
 * 3. 신뢰하는 컴퓨터를 담는 큐(trustQueue)에 computer enque
 * 4. computer 방문 표시 -> 시작 위치인 computer를 넣음
 * 5. trustQueue에 요소가 하나 이상 있는 경우 반복
 * 6. trustQueue에서 deque(current), 해킹 횟수(count) 1 증가
 * 7. current 해당 컴퓨터의 인접 컴퓨터(trust)들에 대해 반복 수행
 * 	7.1. 인접 컴퓨터가 이미 방문한 컴퓨터면 다음 반복 수행
 * 	7.2. 인접 컴퓨터의 번호를 trustQueue에 enque
 * 	7.3. 인접 컴퓨터의 번호 방문 표시 -> 시작 위치인 computer를 넣음
 * 8. trustQueue가 비어있을 때, count와 maxHackingCount 비교
 * 	8.1. count가 더 크면 maxHackingCount 초기화, 
 * 		 maxHackingComputers 리셋 및 computer 추가
 * 	8.2. 서로 같으면 maxHackingComputers에 computer 추가
 */
public class Hacking {
	static class Computer {
		int number;
		Computer next;
		public Computer(int number, Computer next) {
			super();
			this.number = number;
			this.next = next;
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int computerCount;
	private static int trustCount;
	private static boolean[] hasFrom;
	private static Computer[] adjacents;
	private static int maxHackingCount;
	private static List<Integer> maxHackingComputers;
	private static int[] visited;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("input.txt"));
		initialize();
		for (int computer=1; computer<=computerCount; computer++) {
			if (hasFrom[computer]) continue;
			hackingComputer(computer);
		}
		print();
		reader.close();
	}

	private static void hackingComputer(int computer) {
		int count = 0;
		Queue<Integer> trustQueue = new ArrayDeque<Integer>();
		trustQueue.offer(computer);
		visited[computer] = computer;
		while (!trustQueue.isEmpty()) {
			int current = trustQueue.poll();
			count++;
			for (Computer trust=adjacents[current]; trust!=null; trust=trust.next) {
				if (visited[trust.number] == computer) continue;
				trustQueue.offer(trust.number);
				visited[trust.number] = computer;
			}
		}
		if (count > maxHackingCount) {
			maxHackingCount = count;
			maxHackingComputers.clear();
		}
		if (count == maxHackingCount)
			maxHackingComputers.add(computer);
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		tokenizer = new StringTokenizer(reader.readLine().trim());
		computerCount = Integer.parseInt(tokenizer.nextToken());
		trustCount = Integer.parseInt(tokenizer.nextToken());
		hasFrom = new boolean[computerCount+1];
		adjacents = new Computer[computerCount+1];
		for (int trust=0; trust<trustCount; trust++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int to = Integer.parseInt(tokenizer.nextToken());
			int from = Integer.parseInt(tokenizer.nextToken());
			hasFrom[to] = true;
			adjacents[from] = new Computer(to, adjacents[from]);
		}
		maxHackingCount = 0;
		maxHackingComputers = new ArrayList<>();
		visited = new int[computerCount+1];
	}
	private static void print() {
		Collections.sort(maxHackingComputers);
		for (int computer : maxHackingComputers)
			builder.append(computer).append(" ");
		System.out.print(builder);
	}
}
