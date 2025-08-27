package m8.d27;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 0번째 정점 선택
 * 2. 해당 정점의 최소 가중치를 0으로 지정하여 큐에 입력
 * 3. 큐에 요소 있는 동안 반복
 * 4. 우선순위 큐에서 요소 poll
 * 	4.1. 가중치가 가장 작은 정점(current)의 번호와 그 가중치가 나옴
 * 5. 해당 정점을 방문했다면 다음 반복 실행
 * 6. 해당 정점 방문 true, 가중치 총합 합산
 * 7. 해당 정점과 인접한 정점들에 대해 반복 (adjacent)
 * 	7.1. 인접 정점을 이미 방문했다면 다음 반복 실행
 * 	7.2. 우선순위 큐에 인접 정점 번호와 인접 정점-해당 정점(current)의 
 * 		  가중치를 파라미터로 MinimumWeight 생성 및 큐에 offer
 * 		7.2.1. 큐에 들어간 이후, weight가 가장 낮은 값을 root로 보내며 재정렬이 일어남
 */
public class PrimMSTPQList {
	static class Vertex {
		int number;
		int weight;
		Vertex next;
		public Vertex(int number, int weight, Vertex next) {
			super();
			this.number = number;
			this.weight = weight;
			this.next = next;
		}
	}
	static class MinimumWeight implements Comparable<MinimumWeight> {
		int number;
		int weight;
		public MinimumWeight(int number, int weight) {
			super();
			this.number = number;
			this.weight = weight;
		}
		@Override
		public int compareTo(MinimumWeight other) {
			return Integer.compare(weight, other.weight);
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int vertexCount;
	private static Vertex[] adjacentList;
	private static boolean[] visited;
	private static long totalWeight;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			int start = 1;
			Queue<MinimumWeight> queue = new PriorityQueue<>();
			queue.offer(new MinimumWeight(start, 0));
			while (!queue.isEmpty()) {
				MinimumWeight current = queue.poll();
				if (visited[current.number]) continue;
				// current에는 선택되지 않은 정점 중, 
				// 가중치가 가장 작은 간선의 가중치와 그 간선으로 이어지는 정점 번호가 담김
				// 초기 위치를 제외하면 무조건 이전 과정을 통해 이미 선택이 완료된 정점에서
				// 인접한 정점과 그 정점과 이어진 간선의 가중치가 들어가게 되어있음
				visited[current.number] = true;
				totalWeight += current.weight;
				for (Vertex adjacent=adjacentList[current.number];
					adjacent != null; adjacent = adjacent.next) {
					if (visited[adjacent.number]) continue;
					queue.offer(new MinimumWeight(adjacent.number, adjacent.weight));
					// 요소가 들어가면서 힙 구조를 유지하면서 weight가 최소인 값이 맨 앞으로 옮겨짐
					// 동일한 정점 번호가 들어갈 수 있음, 동일한 번호라면 가중치가 제일 작은 값이 우선이 됨
				}
			}
			
			builder.append("#").append(testCase).
					append(" ").append(totalWeight).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		vertexCount = Integer.parseInt(tokenizer.nextToken());
		adjacentList = new Vertex[vertexCount+1];
		visited = new boolean[vertexCount+1];
		int edgeCount = Integer.parseInt(tokenizer.nextToken());
		for (int edge=0; edge<edgeCount; edge++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int vertexA = Integer.parseInt(tokenizer.nextToken());
			int vertexB = Integer.parseInt(tokenizer.nextToken());
			int weight = Integer.parseInt(tokenizer.nextToken());
			adjacentList[vertexA] = new Vertex(vertexB, weight, adjacentList[vertexA]);
			adjacentList[vertexB] = new Vertex(vertexA, weight, adjacentList[vertexB]);
		}
		totalWeight = 0;
	}
}
