package classSolution.lecture.m9.d11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class HeightOrder2 {

	static int N,M,adj[][], radj[][];
	static int count; // 자신보다 키가 크거나 작은 학생 수
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int TC = Integer.parseInt(in.readLine().trim());
		for (int tc=1; tc<=TC; tc++) {
			N = Integer.parseInt(in.readLine().trim());
			M = Integer.parseInt(in.readLine().trim());
			
			adj = new int[N+1][N+1]; // 1번부터!
			// 나보다 큰 관계
			radj = new int[N+1][N+1];
			// 나보다 작은 관계
			
			for (int m=0; m<M; m++) {
				StringTokenizer st = new StringTokenizer(in.readLine().trim());
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());
				radj[b][a] = adj[a][b] = 1; // a보다 b가 큼!
			}
			int ans = 0;
			for (int i=1; i<=N; i++) {
				count = 0;
				boolean[] visited = new boolean[N+1];
				dfs(i, adj, visited); 
				// 수행 후, 나보다 큰 학생 수 저장
				dfs(i, radj, visited);
				// 수행 후, 나보다 작은 학생 수 저장
				if (count == N-1) ans++;
			}
			System.out.println("#"+tc+" "+ans);
		}
		
		in.close();
	}
	
	private static void dfs(int cur, 
			int adj[][], boolean[] visited) {
		visited[cur] = true;
		for (int i=1; i<=N; i++) {
			if (adj[cur][i]==1 && !visited[i]) {
				count++;
				dfs(i, adj, visited);
			}
		}
	}

}
