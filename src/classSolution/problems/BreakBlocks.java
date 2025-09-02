package classSolution.problems;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

public class BreakBlocks {
	
	private static int[] dr = {-1, 1, 0, 0};
	private static int[] dc = {0, 0, -1, 1};
	private static int N,W,H,min;
	
	static class Point {
		int r, c, cnt;
		public Point(int r, int c, int cnt) {
			super();
			this.r = r;
			this.c = c;
			this.cnt = cnt;
		}
	}
	
	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("sample_input.txt"));
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int TC = Integer.parseInt(in.readLine().trim());
		for (int tc=1; tc<=TC; tc++) {
			StringTokenizer st = new StringTokenizer(in.readLine().trim());
			N = Integer.parseInt(st.nextToken());
			W = Integer.parseInt(st.nextToken());
			H = Integer.parseInt(st.nextToken());
			int[][] map = new int[H][W];
			
			for (int r=0; r<H; r++) {
				st = new StringTokenizer(in.readLine());
				for (int c=0; c<W; c++)
					map[r][c] = Integer.parseInt(st.nextToken());
			}
			min = Integer.MAX_VALUE;
			drop(0, map);
			System.out.println("#"+tc+" "+min);
		}
		
		in.close();
	}
	private static boolean drop(int count, int[][] map) {
		int remaintCnt = getRemain(map);
		if (remaintCnt == 0) {
			min = 0;
			return true; // 남은 벽돌이 없음
		}
		if (count == N) {
			min = Math.min(min, remaintCnt);
			return false; // 남은 벽돌이 있음 -> 아직 최적해가 아님
		}
		
		// 전달 받은 배열 백업
		int[][] newMap = new int[H][W];
		
		// 구슬을 모든 열에 던짐 (중복순열)
		for (int c=0; c<W; c++) {
			// step1. c열에 던졌을 때 깨지는 맨 위 벽돌 찾기
			int r = 0;
			while (r<H && map[r][c]==0) r++;
			// 벽돌이 없다면 비어있는 열 -> 다음 열 구슬 던지기
			if (r == H) continue;
			
			// step2. 깨진 벽돌 찾음 -> 연쇄 작업 처리
			copy(map, newMap);
			// 깨지는 벽돌 주변 함께 깨지는 벽돌 찾기
			boom(newMap, r, c);
			// 남은 벽돌 정리
			down(newMap);
			// 다음 구슬 던지러 가기
			if (drop(count+1, newMap)) return true;
		}
		return false;
	}
	
	private static int getRemain(int[][] map) {
		int count = 0;
		for (int r=0; r<H; r++)
			for (int c=0; c<W; c++)
				count += map[r][c] > 0 ? 1 : 0;
		return count;
	}

	static Stack<Integer> stack = new Stack<>();
	private static void down(int[][] map) {
		for (int c=0; c<W; c++) {
			for (int r=0; r<H; r++) {
				// 벽돌 찾기
				if (map[r][c] == 0) continue;
				stack.push(map[r][c]);
				map[r][c] = 0;
			}
			
			int r = H-1;
			while (!stack.isEmpty())
				map[r--][c] = stack.pop();
		}
	}
	private static void boom(int[][] map, int r, int c) {
		Queue<Point> queue = new ArrayDeque<Point>();
		if (map[r][c] > 1) 
			queue.offer(new Point(r, c, map[r][c]));
		map[r][c] = 0;
		
		while (!queue.isEmpty()) {
			Point cur = queue.poll();
			for (int d=0; d<4; d++) {
				int nr = cur.r, nc = cur.c;
				for (int k=1; k<cur.cnt; k++) {
					nr += dr[d];
					nc += dc[d];
					if (0<=nr && nr<H && 0<=nc && nc<W && map[nr][nc] != 0) {
						if (map[nr][nc] > 1)
							queue.offer(new Point(nr, nc, map[nr][nc]));
						map[nr][nc] = 0;
					}
				}
			}
		}
	}
	private static void copy(int[][] map, int[][] newMap) {
		for (int r=0; r<H; r++)
			for (int c=0; c<W; c++)
				newMap[r][c] = map[r][c];
	}
//	private static void print(int i, int j, int[][] map, boolean flag) {
//		System.out.println(i+","+j+(flag ? "   전":"   후")+" ===============");
//		for (int[] is : map) System.out.println(Arrays.toString(is));
//	}
}
