package m8.d20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 도착 시간 배열(destinationTime) -1로 초기화
 * 2. 인접 노드 큐 (queue)에 시작 위치(start) 및 연락 시간(0) enque
 * 3. start 위치 도착 시간 0 지정
 * 4. 큐에 요소가 없을 때 까지 반복
 * 	4.1. deque -> current
 * 	4.2. 모든 요소에 대해 반복(node, 1~100)
 * 		4.2.1. node가 currentNode와 인접하지 않거나, 
 * 			   node가 이미 방문했다면?(도착 시간이 -1이 아니라면?) -> 다음 반복 실행
 * 		4.2.2. queue에 node, currentTime+1 enque
 * 		4.2.3. 도착 시간에 currentTime+1 입력
 * 5. 모든 요소에 대해 반복(node)
 * 	5.1. node의 도착 시간이 -1이라면? 다음 반복 수행
 * 	5.2. 가장 늦은 시간(latestTime)보다 현재 노드의 도착 시간보다 크다면?
 * 		5.2.1. latestTime은 현재 노드 도착 시간, latestNode는 현재 노드로 변경
 * 	5.3. 가장 늦은 시간과 현재 노드 도착 시간이 동일하다면?
 * 		5.3.1. latestNode는 번호가 더 큰 노드로 변경
 */
public class Contact {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int size;
	private static boolean[][] contactTable;
	private static int start;
	private static int latestTime;
	private static int latestNode;
	private static int[] destinationTime;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		initializeTest();
		for (int testCase=1; testCase<=10; testCase++) {
			initialize();
			// logic
			contactToEachNumber();
			getLatest();
			builder.append("#").append(testCase).
					append(" ").append(latestNode).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void contactToEachNumber() {
		Arrays.fill(destinationTime, -1);
		Queue<int[]> queue = new ArrayDeque<int[]>();
		queue.offer(new int[] {start, 0});
		destinationTime[start] = 0;
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			for (int node=1; node<=size; node++) {
				if (!contactTable[current[0]][node] 
						|| destinationTime[node] != -1) continue;
				queue.offer(new int[] {node, current[1]+1});
				destinationTime[node] = current[1]+1;
			}
		}
	}

	private static void getLatest() {
		for (int node=1; node<=size; node++) {
			if (destinationTime[node] == -1) continue;
			if (latestTime < destinationTime[node]) {
				latestTime = destinationTime[node];
				latestNode = node;
			} else if (latestTime == destinationTime[node])
				latestNode = Math.max(latestNode, node);
		}
	}

	private static void initializeTest() {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		size = 100;
	}

	private static void initialize() throws IOException {
		contactTable = new boolean[size+1][size+1];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		int contactSize = Integer.parseInt(tokenizer.nextToken())/2;
		start = Integer.parseInt(tokenizer.nextToken());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<contactSize; index++) {
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			contactTable[from][to] = true;
		}
		latestTime = latestNode = 0;
		destinationTime = new int[size+1];
	}

}
