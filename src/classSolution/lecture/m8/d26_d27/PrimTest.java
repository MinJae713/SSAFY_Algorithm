package classSolution.lecture.m8.d26_d27;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class PrimTest {

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("prim_input.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int V = Integer.parseInt(reader.readLine().trim());
		int[][] adjMatrix = new int[V][V];
		boolean[] visited = new boolean[V];
		int[] minEdge = new int[V];
		
		for (int i=0; i<V; i++) {
			StringTokenizer tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int j=0; j<V; j++)
				adjMatrix[i][j] = Integer.parseInt(tokenizer.nextToken());
		}
		Arrays.fill(minEdge, Integer.MAX_VALUE);
		minEdge[0] = 0;
		int result = 0; // MST 비용
		int c=0;
		for (; c<V; c++) {
			// step1: 비트리 정점 중 간선 비용 최솟값 정점 찾기
			int min = Integer.MAX_VALUE;
			int minVertex = -1;
			for (int i=0; i<V; i++) {
				if (visited[i]) continue; // 트리 정점은 패스
				if (min > minEdge[i]) {
					min = minEdge[i];
					minVertex = i;
				}
			}
			// 모든 정점이 연결된 그래프가 아닌 경우
			if (minVertex == -1) break;
			// 해당 정점을 트리에 포함
			result += minEdge[minVertex];
			visited[minVertex] = true;
			// step2: minVertex 인접 정점의 최소 가중치 값 초기화
			for (int i=0; i<V; i++)
				if (!visited[i] && 
					adjMatrix[minVertex][i]!=0 && 
					minEdge[i]>adjMatrix[minVertex][i])
					minEdge[i] = adjMatrix[minVertex][i];
			
		}
		System.out.println(c==V?result:-1);
		reader.close();
	}

}
