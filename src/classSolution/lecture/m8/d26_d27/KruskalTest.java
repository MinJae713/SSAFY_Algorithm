package classSolution.lecture.m8.d26_d27;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class KruskalTest {
	
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
			// 10-20: 음수->뒤 값이 크다! 그대로
			// 20-10: 양수->앞 값이 크다! 교환
			return Integer.compare(weight, o.weight);
			// 앞에 것이 나, 뒤에 것이 너
		}
	}
	
	static Edge[] edgeList;
	static int[] parents;
	static int V,E;
	
	private static void make() {
		for (int i=0; i<V; i++)
			parents[i] = i;
	}
	private static int find(int a) {
		if (a == parents[a]) return a;
		return parents[a] = find(parents[a]);
	}
	private static boolean unionMember(int a, int b) {
		int aRoot = find(a);
		int bRoot = find(b);
		if (aRoot == bRoot) return false;
		if (aRoot > bRoot) parents[bRoot] = aRoot;
		else parents[aRoot] = bRoot;
		return true;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine().trim());
		V = Integer.parseInt(st.nextToken());
		E = Integer.parseInt(st.nextToken());
		for (int i=0; i<E; i++) {
			st = new StringTokenizer(in.readLine().trim());
			int from = Integer.parseInt(st.nextToken());
			int to = Integer.parseInt(st.nextToken());
			int weight = Integer.parseInt(st.nextToken());
			edgeList[i] = new Edge(from, to, weight);
		}
		
		Arrays.sort(edgeList);
		make();
		int result = 0; // 최소신장트리 비용
		int cnt = 0; // 처리된 간선 수
		for (Edge edge : edgeList) {
			if (!unionMember(edge.from, edge.to)) continue;
			result += edge.weight;
			if (++cnt == V-1) break;
		}
		System.out.println(result);
		in.close();
	}

}
