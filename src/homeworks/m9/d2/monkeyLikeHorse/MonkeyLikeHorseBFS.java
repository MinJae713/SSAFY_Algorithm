package homeworks.m9.d2.monkeyLikeHorse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - visited: 방문 위치 정보 저장, 한 위치에는 말 이동 수에 따라 방문 표시가 지정됨
 *   ㄴ 3차원 배열! -> 말처럼 이동한 경우, 다음 위치 다음 말 이동수에 true 지정
 * 1. 현재 위치 정보 큐에 입력 (Position)
 * 	1.1. Position은 현재 위치(x, y) 및 이동 횟수(count), 말 이동 횟수(horseCount) 저장
 * 	1.2. 현재 위치 방문 표시 -> 말 이동 수는 0으로 입력
 * 2. 큐에 요소가 있는 동안 반복
 * 3. 큐에서 요소 poll (current)
 * 4. 현재 위치가 마지막 위치라면?
 * 	4.1. 현재까지 이동 횟수와 최소 이동 횟수 비교 -> 더 작은 값 입력
 * 5. 시작 인덱스는 말 이동 횟수가 최대 말 이동 횟수보다 적다면 0, 아니면 8로 지정
 * 6. 움직일 수 있는 모든 방향에 대해 반복
 * 	6.1. 다음 말 이동 수(nextHorseCount)는 인덱스가 8보다 작다면 1, 아니면 0을 더해서 입력
 * 7. 다음 위치가 범위 내에 있거나, 방문하지 않았거나, 벽이 아니라면 수행
 * 	7.1. checkNext(): 다음 위치 및 다음 말 이동수를 파라미터로 입력
 * 8. 다음 위치 및 이동 횟수, 말 이동 횟수를 파라미터로 Position 생성 및 큐에 입력
 * 	8.1. 이동 횟수는 1 증가하여 입력
 * 9. 다음 위치 방문 표시 -> 다음 위치 다음 말 이동 수 해당 위치에 true 지정
 */
public class MonkeyLikeHorseBFS {
	static class Position {
		int x;
		int y;
		int count;
		int horseCount;
		public Position(int x, int y, int count, int horseCount) {
			super();
			this.x = x;
			this.y = y;
			this.count = count;
			this.horseCount = horseCount;
		}
	}
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int maxHorseCount;
	private static int width;
	private static int height;
	private static int[][] map;
	private static int[][] delta;
	private static boolean[][][] visited;
	private static int minimumDistance;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		Queue<Position> queue = new ArrayDeque<Position>();
		queue.offer(new Position(0, 0, 0, 0));
		visited[0][0][0] = true;
		while (!queue.isEmpty()) {
			Position current = queue.poll();
			if (current.y == height-1 && 
					current.x == width-1) {
				minimumDistance = Math.min(minimumDistance, current.count);
				continue;
			}
			for (int deltaIndex=current.horseCount<maxHorseCount ? 0 : 8; 
					deltaIndex<12; deltaIndex++) {
				int nextY = current.y+delta[deltaIndex][0];
				int nextX = current.x+delta[deltaIndex][1];
				int nextHorseCount = current.horseCount+(deltaIndex<8 ? 1 : 0); 
				if (!checkNext(nextX, nextY, nextHorseCount)) continue;
				queue.offer(new Position(nextX, nextY, current.count+1, nextHorseCount));
				visited[nextY][nextX][nextHorseCount] = true;
			}
		}
		System.out.println(minimumDistance == Integer.MAX_VALUE ? 
				-1 : minimumDistance);
		reader.close();
	}

	private static boolean checkNext(int nextX, 
			int nextY, int horseCount) {
		return 0<=nextX && nextX<width && 
				0<=nextY && nextY<height && 
				map[nextY][nextX] != 1 &&
				!visited[nextY][nextX][horseCount];
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		maxHorseCount = Integer.parseInt(reader.readLine().trim());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		width = Integer.parseInt(tokenizer.nextToken());
		height = Integer.parseInt(tokenizer.nextToken());
		map = new int[height][width];
		for (int row=0; row<height; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<width; column++)
				map[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
		delta = new int[][] {
			{-2, 1}, {-1, 2}, {1, 2}, {2, 1},
			{2, -1}, {1, -2}, {-1, -2}, {-2, -1},
			{-1, 0}, {0, 1}, {1, 0}, {0, -1}
		};
		visited = new boolean[height][width][maxHorseCount+1];
		minimumDistance = Integer.MAX_VALUE;
	}

}
