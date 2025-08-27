package m8.d27;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - 우선순위 큐가 어색해서... 인접 행렬 버전으로 프림 구현
 * - 공간 복잡도 땜시 에러가 빵!! -> 지헌이가 그랬음
 */
public class PrimMSTPQMatrix {
	static class VertexWeight implements Comparable<VertexWeight>{
		int number;
		int weight;
		public VertexWeight(int number, int weight) {
			super();
			this.number = number;
			this.weight = weight;
		}
		@Override
		public int compareTo(VertexWeight other) {
			return Integer.compare(weight, other.weight);
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int vertexCount;
	private static int[][] adjacentMatrix;
	private static int[] minimumWeights;
	private static boolean[] visited;
	private static long totalWeight;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			int start = 1;
			Queue<VertexWeight> queue = new PriorityQueue<>();
			queue.offer(new VertexWeight(start, 0));
			while (!queue.isEmpty()) {
				VertexWeight current = queue.poll();
				// 이 시점에 나오는 것은 정점들 중에 그 간선 가중치가 가장 낮은 놈
				// 그 정점이 이미 방문했는지를 확인해줘야 함
				if (visited[current.number]) continue;
				// 이렇게 되면 포함되지 않은 정점 중에 간선 가중치가 가장 낮은 놈을 고를 수 있음
				// 해당 정점에 대해 방문 처리 및 연결된 간선 가중치를 총합에 합산
				visited[current.number] = true;
				totalWeight += current.weight;
				// 방문처리 이후, 처리한 정점 인접 정점에 대해 반복 수행
				// 반복하면서 방문하지 않은 정점에 대해 그 정점과 처리한 정점간 간선 가중치 값을 큐에 넣음
				// 우선순위 큐이기 때문에, 큐에 들어간 순간 큐 내 간선 가중치가 가장 작은 요소가 맨 앞에 배치됨
				for (int adjacent=1; adjacent<=vertexCount; adjacent++) {
					// 방문했다면 다음 정점 확인 -> 이는 다시 들어가는 경우를 방지하기 위함임
					// 0이라면 인접 정점이 아님(양, 음수인 경우만 인접 정점 해당)
					if (visited[adjacent] || adjacentMatrix[current.number][adjacent] == 0) 
						continue;
					queue.offer(new VertexWeight(adjacent, 
							adjacentMatrix[current.number][adjacent]));
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
		adjacentMatrix = new int[vertexCount+1][vertexCount+1];
		minimumWeights = new int[vertexCount+1];
		Arrays.fill(minimumWeights, Integer.MAX_VALUE);
		visited = new boolean[vertexCount+1];
		int edgeCount = Integer.parseInt(tokenizer.nextToken());
		for (int edge=0; edge<edgeCount; edge++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int vertexA = Integer.parseInt(tokenizer.nextToken());
			int vertexB = Integer.parseInt(tokenizer.nextToken());
			int weight = Integer.parseInt(tokenizer.nextToken());
			adjacentMatrix[vertexA][vertexB] = weight;
			adjacentMatrix[vertexB][vertexA] = weight;
		}
		totalWeight = 0;
	}
}
