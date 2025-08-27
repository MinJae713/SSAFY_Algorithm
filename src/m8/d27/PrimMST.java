package m8.d27;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 0번째 정점 선택
 * 2. 해당 정점의 최소 가중치(minimumWeights) 0으로 지정
 * 3. 정점의 갯수 만큼 반복
 * 4. 선택되지 않은 정점중, 최소 가중치가 제일 작은 정점 선택(vertex)
 * 	4.1. 해당 정점 방문 표시, 최소 가중치 합산
 * 5. 모든 정점에 대해 반복
 * 	5.1. vertex와 인접한 정점이 아니라면(가중치 값이 0이라면) 다음 반복 수행
 * 	5.1. 해당 정점을 이미 방문했다면 다음 반복 수행
 * 	5.2. vertex와 해당 정점간 가중치보다 해당 정점의 최소 가중치가 더 작다면 다음 반복 수행
 * 	5.3. 해당 정점의 최소 가중치 값을 vertex와 해당 정점간 가중치 값으로 갱신
 */
public class PrimMST {
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
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int vertexCount;
	private static Vertex[] adjacentList;
	private static int[] minimumWeights;
	private static boolean[] visited;
	private static long totalWeight;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			int start = 1;
			minimumWeights[start] = 0;
			for (int count=0; count<vertexCount; count++) {
				int currentWeight = Integer.MAX_VALUE;
				// 인접 간선으로 인한 가중치가 가장 작은 정점 선택
				int current = -1;
				for (int vertex=1; vertex<=vertexCount; vertex++) {
					if (visited[vertex]) continue;
					if (currentWeight > minimumWeights[vertex]) {
						currentWeight = minimumWeights[vertex];
						current = vertex;
					}
				}
				// 위에서 선택한 정점 방문 처리
				visited[current] = true;
				totalWeight += minimumWeights[current];
				// 선택된 정점과 인접한 정점들의 인접 간선 가중치 값 작은 값으로 초기화
				for (Vertex vertex=adjacentList[current]; vertex!=null; vertex=vertex.next) {
					if (visited[vertexCount] || 
						minimumWeights[vertex.number] < vertex.weight)
						continue;
					minimumWeights[vertex.number] = vertex.weight;
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
		minimumWeights = new int[vertexCount+1];
		Arrays.fill(minimumWeights, Integer.MAX_VALUE);
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
