package classSolution;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ControlMicroganism {
	
	static class Micro {
		int r, c, cnt, dir, total;// total: 군집 합쳐지는 상황 크기
		boolean isDead;
		public Micro(int r, int c, int cnt, int dir) {
			super();
			this.r = r;
			this.c = c;
			this.total = this.cnt = cnt;
			this.dir = dir;
			this.isDead = false;
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
					cur.total = cur.cnt /= 2;
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
				else { // 그 셀에 나중에 도착하는 군집
					// 더 큰 쪽에 흡수, 작은 쪽은 소멸
					if (map[nr][nc].cnt > cur.cnt) {
						// 이미 박힌 놈이 더 큰 경우
						// cnt로 합치지 않는 이유?
						// 같은 방향에 갯수 5, 3인 애들 만나고, 이후 6인 애를 만나면?
						// 문제는 6인 애가 남도록 해야 함!
						// 근디 cnt로 해부리면 마지막에 5인 애가 남아버림 -> 5+3 > 6 이렇게 되서...
						map[nr][nc].total += cur.cnt;
						cur.isDead = true;
					} else {
						// 굴러들어온 놈이 더 큰 경우 -> 이미 박힌 놈과 비교
						// cnt가 아니라 total임!! -> 얘는 이미 고정되어 있는 애랑 비교하는 것
						cur.total += map[nr][nc].total;
						map[nr][nc].isDead = true;
						map[nr][nc] = cur;
					}
				}
			}
			remainCnt = reset();
			System.out.println(remainCnt);
		}
		return remainCnt;
	}
	private static int reset() {
		int total = 0;
		for (int r=0; r<N; r++)
			for (int c=0; c<N; c++) {
				if (map[r][c] == null) continue;
				map[r][c].cnt = map[r][c].total;
				total += map[r][c].cnt;
				map[r][c] = null;
			}
		return total;
	}
}
