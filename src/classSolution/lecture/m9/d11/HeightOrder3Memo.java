package classSolution.lecture.m9.d11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class HeightOrder3Memo {

	static int N,M,adj[][];
	
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int TC = Integer.parseInt(in.readLine().trim());
		for (int tc=1; tc<=TC; tc++) {
			N = Integer.parseInt(in.readLine().trim());
			M = Integer.parseInt(in.readLine().trim());
			
			adj = new int[N+1][N+1]; // 1번부터!
			// 메모 안된 상태 초기화 작업
			for (int i=0; i<=N; i++) adj[i][0] = -1;
			
			for (int m=0; m<M; m++) {
				StringTokenizer st = new StringTokenizer(in.readLine().trim());
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());
				adj[a][b] = 1; // a보다 b가 큼!
			}
			int ans = 0;
			// 자기보다 큰 학생 카운트
			for (int i=1; i<=N; i++)
				if (adj[i][0] == -1) gtDFS(i);
			// 자기보다 작은 학생 카운트
			for (int i=1; i<=N; i++)
				for (int j=1; j<=N; j++)
					adj[0][i] += adj[j][i];
			// 큰 학생+작은 학생 카운트
			for (int i=1; i<=N; i++)
				if (adj[i][0]+adj[0][i] == N-1) 
					ans++;
			System.out.println("#"+tc+" "+ans);
		}
		
		in.close();
	}
	
	private static void gtDFS(int cur) {
		for (int i=1; i<=N; i++) {
			if (adj[cur][i]==1) {
				if (adj[i][0] == -1) gtDFS(i);
				if (adj[i][0]>0) {
					// i보다 큰 학생이 있다면? cur<i<j를 cur<j로 표현 (경로 압축)
					for (int j=1; j<=N; j++) {
						// i보다 큰 j 학생(i<j) -> cur보다 크다고 표시
						if (adj[i][j] == 1) adj[cur][j] = 1;
					}
				}
			}
		}
		// 자기보다 큰 학생 수 카운팅 - 메모
		int cnt = 0;
		for (int i=1; i<=N; i++) cnt += adj[cur][i];
		adj[cur][0] = cnt;
	}

}
