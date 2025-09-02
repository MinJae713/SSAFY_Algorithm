package classSolution.lecture.m9.d1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class DijkstraTest {
	
	static class Node {
		int to, weight;
		Node next;
		public Node(int to, int weight, Node next) {
			super();
			this.to = to;
			this.weight = weight;
			this.next = next;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine().trim());
		int V = Integer.parseInt(st.nextToken());
		int E = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(in.readLine().trim());
		int start = Integer.parseInt(st.nextToken());
		int end = Integer.parseInt(st.nextToken());
		Node[] adjList = new Node[V]; // 인접 노드 리스트
		for (int i=0; i<E; i++) {
			st = new StringTokenizer(in.readLine().trim());
			int from = Integer.parseInt(st.nextToken());
			int to = Integer.parseInt(st.nextToken());
			int weight = Integer.parseInt(st.nextToken());
			adjList[from] = new Node(to, weight, adjList[from]); // 유향 그래프라 한쪽으로만 넣음
		}
		int[] minDistance = new int[V]; // start -> 자신으로의 최소 비용
		boolean[] visited = new boolean[V]; // 정점 처리 여부
		// step0 -> 초기화
		Arrays.fill(minDistance, Integer.MAX_VALUE); // 큰 값들로 초기화
		minDistance[start] = 0; // 출발지 가중치는 0
		for (int i=0; i<V; i++) {
			// step1 -> 미방문 정점 중 가장 가까운 정점 찾기
			int min = Integer.MAX_VALUE;
			int stopOver = -1;
			for (int j=0; j<V; j++) {
				if (visited[j]) continue; // 이미 방문한 정점
				if (minDistance[j] < min) {
					min = minDistance[j];
					stopOver = j;
				}
				// 정점 중 출발지에서 자신으로 들어오는 거리가 최소인 정점이 담김(stopOver)
			}
			if (stopOver == -1) break; // 출발지에서 이어지는 정점이 아무도 없는 경우!
			visited[stopOver] = true;
			if (stopOver == end) break; // 도착지점이면 경로 탐색 완
			// step2 -> 출발지에서 거리가 최소인 정점의 인접들 최단 거리 값 비교 -> 더 작은 값으로 넣어줌
			for (Node temp=adjList[stopOver]; temp!=null; temp=temp.next) 
				if (!visited[temp.to] && min+temp.weight < minDistance[temp.to])
					minDistance[temp.to] = min+temp.weight;
			// 선택된 정점의 인접 정점중, 처리되지 않았고, 
			// 출발지에서 인접 정점으로 들어오는 최단 거리가
			// 출발지에서 현재 정점으로 들어오는 최단 거리+현재 정점과 인접 정점의 거리보다 크다면?
			// 출발지에서 인접 정점까지의 최단 거리를 현재 정점 최단+현재-인접거리 값으로 바꿔줌
			// 그니까 현재 정점을 '경유'하니까 더 가깝더라! 라는 의미일세
		}
		
		in.close();
	}

}
