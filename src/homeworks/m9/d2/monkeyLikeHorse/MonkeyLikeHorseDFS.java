package homeworks.m9.d2.monkeyLikeHorse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. nextMovements(): 원숭이의 움직임 구현
 * 	1.1. 파라미터는 원숭이 위치(currentX, currentY), 
 * 		  움직인 횟수(count), 말 이동 횟수(horseCount)
 * 2. 현재 위치가 마지막 위치라면?
 * 	2.1. 움직인 횟수와 최소 움직인 횟수 비교, 더 작은 값으로 입력
 * 3. 시작 위치는 말 이동 횟수가 최대 말 이동 
 * 	횟수보다 적다면 0, 아니면 8로 지정
 * 4. 원숭이가 움직일 수 있는 방향에 대해 반복(next)
 * 5. 다음 위치가 범위 밖에 있거나 이미 방문했거나 장애물이 있는 위치면 continue
 * 6. 다음 위치 방문 여부 true
 * 7. 다음 위치, count+1, 말 이동 횟수를 파라미터로 nextMovements 재귀 호출
 * 	7.1. 현재 방향이 8보다 작다면 horseCount+1, 
 * 		  아니라면 horseCount 그대로를 말 이동 수 파라미터로 지정
 * 8. 다음 위치 방문 여부 false
 */
public class MonkeyLikeHorseDFS {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int maxHorseCount;
	private static int width;
	private static int height;
	private static int[][] map;
	private static boolean[][] visited;
	private static int minimumCount;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		visited[0][0] = true;
		nextMovements(0, 0, 0, 0);
		visited[0][0] = false;
		System.out.println(minimumCount == 
				Integer.MAX_VALUE ? -1 : minimumCount);
		reader.close();
	}
	
	private static void nextMovements(int currentX, 
			int currentY, int count, int horseCount) {
		if (currentY == height-1 && currentX == width-1) {
			minimumCount = Integer.min(minimumCount, count);
			return;
		}
		for (int deltaIndex=horseCount<maxHorseCount ? 0 : 8; 
				deltaIndex<12; deltaIndex++) {
			int nextY = currentY+delta[deltaIndex][0];
			int nextX = currentX+delta[deltaIndex][1];
			if (!checkNext(nextX, nextY)) continue;
			visited[nextY][nextX] = true;
			nextMovements(nextX, nextY, count+1, 
				deltaIndex<8 ? horseCount+1 : horseCount);
			visited[nextY][nextX] = false;
		}
	}

	private static boolean checkNext(int nextX, int nextY) {
		return 0<=nextY && nextY<height &&
					0<=nextX && nextX<width &&
					!visited[nextY][nextX] && 
					map[nextY][nextX] != 1;
					
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		maxHorseCount = Integer.parseInt(reader.readLine().trim());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		width = Integer.parseInt(tokenizer.nextToken());
		height = Integer.parseInt(tokenizer.nextToken());
		map = new int[height][width];
		delta = new int[][] {
			{-2, 1}, {-1, 2}, {1, 2}, {2, 1},
			{2, -1}, {1, -2}, {-1, -2}, {-2, -1},
			{-1, 0}, {0, 1}, {1, 0}, {0, -1}
		};
		for (int row=0; row<height; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<width; column++)
				map[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
		visited = new boolean[height][width];
		minimumCount = Integer.MAX_VALUE;
	}
}
