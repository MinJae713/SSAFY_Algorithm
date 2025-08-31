package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 처음 위치 및 난이도(Position), 위치별 방문 표시(visited) 큐에 enque 
 * 	1.1. 처음 위치 방문 표시 후 visited enque
 * 2. 큐에 요소가 있는 동안 반복
 * 3. 요소 deque(current)
 * 4. 현 위치가 3이라면?
 * 	4.1. 현재 난이도와 최소 난이도(minimumRank) 비교 -> 더 낮은 값으로 값 초기화
 * 5. 4방향에 대해 반복 (nextX, nextY)
 * 	5.1. 다음 위치가 이동할 수 없거나, 이미 방문한 위치라면 continue
 * 	5.2. 방향이 위(0)라면?
 * 		5.2.1. nextY에서 맨 위 위치까지 반복 -> 현 위치가 0이면 nextY 감소
 * 		5.2.2. nextY가 -1이라면 continue
 * 		5.2.3. 현재 y위치와 nextY의 차이 계산(nextRank)
 * 		5.2.4. nextRank를 현재 난이도와 비교, 더 큰 값으로 nextRank 입력
 * 		5.2.5. 현재 방문(visited) 상태 복제본 생성(copied)
 * 		5.2.6. 현재 위치(current.y+1)~nextY 범위 내 방문 처리
 * 		5.2.7. nextX, nextY, nextRank, copied를 파라미터로 Position 생성, 큐에 enque
 * 	5.3. 방향이 아래(2)라면?
 * 		5.3.1. nextY에서 맨 아래 위치(height-1)까지 반복 -> 현 위치가 0이면 nextY 증가
 * 		5.3.2. nextY가 height라면 continue
 * 		5.3.3. nextY와 현재 y위치와 차이 계산(nextRank)
 * 		5.3.4. nextRank를 현재 난이도와 비교, 더 큰 값으로 nextRank 입력
 * 		5.3.5. 현재 방문(visited) 상태 복제본 생성(copied) 및 해당 위치 방문 처리
 * 		5.3.6. 현재 위치(current.y-1)~nextY 범위 내 방문 처리
 * 		5.3.7. nextX, nextY, nextRank, copied를 파라미터로 Position 생성, 큐에 enque
 * 	5.4. 방향이 양옆(1, 3)라면?
 * 		5.4.1. 해당 위치가 0이라면 continue
 * 		5.4.2. 현재 방문(visited) 상태 복제본 생성(copied) 및 해당 위치 방문 처리
 * 		5.4.2. nextX, nextY, 현재의 난이도, copied를 파라미터로 Position 생성, 큐에 enque
 */
public class MountainWalking {
	static class Position {
		int x;
		int y;
		int rank;
//		boolean[][] visited;
		public Position(int x, int y, int rank) {
			super();
			this.x = x;
			this.y = y;
			this.rank = rank;
//			this.visited = visited;
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int height;
	private static int width;
	private static int[][] mountain;
	private static int startX;
	private static int startY;
	private static int endX;
	private static int endY;
	private static int minimumRank;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			getMinimumRank();
//			getMinimumRankDFS(startX, startY, 0, new boolean[height][width]);
			builder.append("#").append(testCase).
						append(" ").append(minimumRank).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void getMinimumRank() {
		Queue<Position> queue = new ArrayDeque<Position>();
//		boolean[][] visited = new boolean[height][width];
//		visited[startY][startX] = true;
		int[][] rankMemo = new int[height][width];
		for (int row=0; row<height; row++)
			Arrays.fill(rankMemo[row], Integer.MAX_VALUE);
		rankMemo[startY][startX] = 0;
		queue.offer(new Position(startX, startY, 0));
		while (!queue.isEmpty()) {
			Position current = queue.poll();
			int rank = rankMemo[current.y][current.x];
			if (current.y == endY && current.x == endX) {
				minimumRank = Math.min(minimumRank, current.rank);
				if (minimumRank == 1) return;
				continue;
			}
			for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
				int nextY = current.y+delta[deltaIndex][0];
				int nextX = current.x+delta[deltaIndex][1];
				if (!moveAble(nextY, nextX)) continue;
//				int nextRank = current.rank;
				int nextRank = rank;
//				boolean[][] copied = copyVisited(current.visited);
				if (deltaIndex == 0) {
					while (nextY>=0 && mountain[nextY][nextX] == 0) nextY--;
					if (nextY < 0) continue;
					nextRank = Math.max(current.rank, current.y-nextY);
//					for (int index=nextY; index<current.y; index++)
//						copied[index][nextX] = true;
				} else if (deltaIndex == 2) {
					while (nextY<height && mountain[nextY][nextX] == 0) nextY++;
					if (nextY >= height) continue;
					nextRank = Math.max(current.rank, nextY-current.y);
//					for (int index=nextY; index>current.y; index--)
//						copied[index][nextX] = true;
				} else {
					if (mountain[nextY][nextX] == 0)	continue;
//					copied[nextY][nextX] = true;
				}
				if (nextRank < rankMemo[nextY][nextX]) {
					rankMemo[nextY][nextX] = nextRank;
					queue.offer(new Position(nextX, nextY, nextRank));
				}
//				queue.offer(new Position(nextX, nextY, nextRank));
			}
		}
	}
//	private static void getMinimumRankDFS(int x, int y, int rank, boolean[][] visited) {
//		visited[y][x] = true;
//		if (y == endY && x == endX) {
//			minimumRank = Math.min(minimumRank, rank);
//			return;
//		}
//		for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
//			int nextY = y+delta[deltaIndex][0];
//			int nextX = x+delta[deltaIndex][1];
//			if (!(0<=nextY && nextY<height &&
//					0<=nextX && nextX<width &&
//					!visited[nextY][nextX])) continue;
//			int nextRank = rank;
//			boolean[][] copied = copyVisited(visited);
//			if (deltaIndex == 0) {
//				while (nextY>=0 && mountain[nextY][nextX] == 0) nextY--;
//				if (nextY < 0) continue;
//				nextRank = y-nextY;
//				nextRank = Math.max(rank, nextRank);
//				for (int index=nextY; index<y; index++)
//					copied[index][nextX] = true;
//			} else if (deltaIndex == 2) {
//				while (nextY<height && mountain[nextY][nextX] == 0) nextY++;
//				if (nextY >= height) continue;
//				nextRank = nextY-y;
//				nextRank = Math.max(rank, nextRank);
//				for (int index=nextY; index>y; index--)
//					copied[index][nextX] = true;
//			} else {
//				if (mountain[nextY][nextX] == 0)	continue;
//				copied[nextY][nextX] = true;
//			}
//			getMinimumRankDFS(nextX, nextY, nextRank, copied);
//		}
//	}
	
//	private static void forLog(Position current) {
//		System.out.printf("현재 - (%d, %d)\n", current.x, current.y);
//		for (int row=0; row<height; row++) {
//			for (int column=0; column<width; column++)
//				System.out.printf("%-2d", current.visited[row][column] ? 1 : 0);
//			System.out.println();
//		}
//	}

	private static boolean moveAble(int nextY, int nextX) {
		return 0<=nextY && nextY<height &&
				0<=nextX && nextX<width;
//				!current.visited[nextY][nextX];
	}
	
//	private static boolean[][] copyVisited(boolean[][] origin) {
//		boolean[][] copied = new boolean[height][width];
//		for (int row=0; row<height; row++)
//			for (int column=0; column<width; column++)
//				copied[row][column] = origin[row][column];
//		return copied;
//	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		height = Integer.parseInt(tokenizer.nextToken());
		width = Integer.parseInt(tokenizer.nextToken());
		mountain = new int[height][width];
		for (int row=0; row<height; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<width; column++) {
				mountain[row][column] = Integer.parseInt(tokenizer.nextToken());
				if (mountain[row][column] == 2) {
					startX = column;
					startY = row;
				} else if (mountain[row][column] == 3) {
					endX = column;
					endY = row;
				}
			}
		}
		minimumRank = Integer.MAX_VALUE;
	}
}
