package classSolution.problems;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ControlMicroganismWithSort {
	
	static class Micro implements Comparable<Micro>{
		int r, c, cnt, dir;// total: 군집 합쳐지는 상황 크기
		boolean isDead;
		public Micro(int r, int c, int cnt, int dir) {
			super();
			this.r = r;
			this.c = c;
			this.cnt = cnt;
			this.dir = dir;
			this.isDead = false;
		}
		@Override
		public int compareTo(Micro o) {
			return Integer.compare(cnt, o.cnt) * (-1);
		}
	}
	
	static int N,M,K;
	static int[] dr = {0, -1, 1, 0, 0};
	static int[] dc = {0, 0, 0, -1, 1};
	static Micro[] list;
	static Micro[][] map;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("sample_input.txt"));
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int TC = Integer.parseInt(in.readLine().trim());
		
		for (int tc=1; tc<=TC; tc++) {
			StringTokenizer st = new StringTokenizer(in.readLine().trim());
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			K = Integer.parseInt(st.nextToken());
			list = new Micro[K];
			map = new Micro[N][N];
			
			for (int i=0; i<K; i++) {
				st = new StringTokenizer(in.readLine().trim());
				list[i] = new Micro(
					Integer.parseInt(st.nextToken()), 
					Integer.parseInt(st.nextToken()), 
					Integer.parseInt(st.nextToken()), 
					Integer.parseInt(st.nextToken())
				);
			}
			System.out.println("#"+tc+" "+move());
		}
		in.close();
	}
	private static int move() { // 살아있는 미생물 수 반환
		// M시간동안 군집 이동 처리
		Arrays.sort(list);
		int time = M,nr,nc,remainCnt = 0;
		while (time-- > 0) {
			for (Micro cur : list) {
				if (cur.isDead) continue;
				// step1) 한칸 이동
				nr = cur.r += dr[cur.dir];
				nc = cur.c += dc[cur.dir];
				// step2) 약품  칸 처리
				if (nr==0 || nr==N-1 || nc==0 || nc==N-1) {
					// 군집 크기 절반 줄임, 방향 바꿈, 크기가 0이면 소멸
					cur.cnt /= 2;
					if (cur.cnt == 0) {
						cur.isDead = true;
						continue;
					}
					// 방향 반대로
					if (cur.dir%2 == 1) cur.dir++;
					else cur.dir--;
				}
				// step3) 군집 병합 관련 처리
				// 그 셀에 처음 도착
				if (map[nr][nc] == null) map[nr][nc] = cur;
				else { // 그 셀에 나중에 도착하는 군집 -> 크기가 작다!
					map[nr][nc].cnt += cur.cnt;
					cur.isDead = true;
				}
			}
			remainCnt = reset();
		}
		return remainCnt;
	}
	private static int reset() {
		int total = 0;
		for (int r=0; r<N; r++)
			for (int c=0; c<N; c++) {
				if (map[r][c] == null) continue;
				total += map[r][c].cnt;
				map[r][c] = null;
			}
		return total;
	}
}
