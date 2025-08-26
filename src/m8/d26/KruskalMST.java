package m8.d26;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 입력 받은 간선 리스트(edges)를 가중치 기준 오름차순으로 정렬
 * 2. 각 간선들에 대해 반복, 선택 간선의 수(selectedCount)가 정점의 수-1라면 반복 종료
 * 3. 간선 양쪽의 두 정점에 대해 unionVertex
 * 	3.1. 파라미터는 양쪽 정점(from, to)
 * 	3.2. 양쪽 정점이 속한 집합의 대표 반환(findRoot)
 * 		3.2.1. 파라미터는 현재 찾으려는 정점의 번호(vertex)
 * 		3.2.2. vertex의 부모에 해당하는 번호[parents]가 vertex와 같다면 함수 실행 종료
 * 		3.2.3. vertex의 부모 번호를 파라미터로 find 재귀 호출
 * 		3.2.4. 호출 결과를 vertex 부모 번호로 입력 -> vertex의 부모는 vertex 소속 집합 대표가 됨
 * 	3.3. 두 정점의 대표 번호가 같다면? 두 정점이 포함된 집합이 동일하다면 false 반환
 * 	3.4. 두 대표 번호의 대소 비교 -> 더 작은 번호의 부모에 더 큰 번호를 입력
 * 4. 연산 결과가 false라면? 다음 반복 수행
 * 5. selectedCount 1 증가
 * 6. 최소 가중치(minimumWeight)에 해당 간선의 가중치 합산
 */
public class KruskalMST {
	static class Edge implements Comparable<Edge>{
		int from, to, weight;
		public Edge(int from, int to, int weight) {
			super();
			this.from = from;
			this.to = to;
			this.weight = weight;
		}
		@Override
		public int compareTo(Edge o) {
			return Integer.compare(weight, o.weight);
		}
		@Override
		public String toString() {
			return String.format("(%d, %d), %d", from, to, weight);
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int vertexCount;
	private static int edgeCount;
	private static Edge[] edges;
	private static int[] parents;
	private static long minimumWeight;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			int selectedCount = 0;
			for (int edgeIndex=0; edgeIndex<edgeCount && selectedCount<vertexCount-1; edgeIndex++) {
				Edge edge = edges[edgeIndex];
				if (!unionVertex(edge.from, edge.to)) continue;
				selectedCount++;
				minimumWeight += edge.weight;
			}
			
			builder.append("#").append(testCase).
					append(" ").append(minimumWeight).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static boolean unionVertex(int from, int to) {
		int rootFrom = findRoot(from);
		int rootTo = findRoot(to);
		if (rootFrom == rootTo) return false;
		if (rootFrom > rootTo) parents[rootTo] = rootFrom;
		else parents[rootFrom] = rootTo;
		return true;
	}

	private static int findRoot(int vertex) {
		if (vertex == parents[vertex]) return vertex;
		return parents[vertex] = findRoot(parents[vertex]);
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
		edgeCount = Integer.parseInt(tokenizer.nextToken());
		edges = new Edge[edgeCount];
		for (int index=0; index<edgeCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			int weight = Integer.parseInt(tokenizer.nextToken());
			edges[index] = new Edge(from, to, weight);
		}
		Arrays.sort(edges);
		parents = new int[vertexCount+1];
		for (int vertex=1; vertex<=vertexCount; vertex++)
			parents[vertex] = vertex;
		minimumWeight = 0;
	}
}
