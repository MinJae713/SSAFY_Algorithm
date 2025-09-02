package classSolution.lecture.m9.d2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class DijkstraPQTest {
	
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
	static class Vertex implements Comparable<Vertex>{
		int no, totalDistance;
		public Vertex(int no, int totalDistance) {
			super();
			this.no = no;
			this.totalDistance = totalDistance;
		}
		@Override
		public int compareTo(Vertex other) {
			return Integer.compare(totalDistance, other.totalDistance);
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
		Queue<Vertex> pq = new PriorityQueue<Vertex>();
		Arrays.fill(minDistance, Integer.MAX_VALUE); // 큰 값들로 초기화
		minDistance[start] = 0; // 출발지 가중치는 0
		pq.offer(new Vertex(start, minDistance[start]));
		
		while (!pq.isEmpty()) {
			// step1 -> 미방문 정점 중 가장 가까운 정점 찾기
			Vertex stopOver = pq.poll();
			if (visited[stopOver.no]) continue;
			// 이미 처리된 정점이면? 재처리하지 않음(어차피 더 큰 minimumTotal임)
			visited[stopOver.no] = true;
			if (stopOver.no == end) break;
			// step2 -> 출발지에서 거리가 최소인 정점의 인접들 최단 거리 값 비교 -> 더 작은 값으로 넣어줌
			for (Node temp=adjList[stopOver.no]; temp!=null; temp=temp.next) 
				if (!visited[temp.to] && stopOver.totalDistance+temp.weight < minDistance[temp.to]) {
					minDistance[temp.to] = stopOver.totalDistance+temp.weight;
					pq.offer(new Vertex(temp.to, minDistance[temp.to]));
				}
		}
		
		in.close();
	}

}
