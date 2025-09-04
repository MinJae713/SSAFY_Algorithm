package homeworks.m9.d2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - MinimumCount
 * 	  ㄴ 시작 지점에서부터 도달하는데 걸리는 최소값(movement)
 *   ㄴ 말처럼 이동한 횟수 포함(horseCount)
 * 1. 원숭이 초기 위치 및 말 이동 수 큐에 offer
 * 2. 원숭이 초기 위치 이동 최소 비용(movementMemo) 0으로 지정
 * 3. 큐에 요소가 있는 동안 반복
 * 4. 큐에서 요소 반환(current)
 * 5. horseCount가 말처럼 이동할 수 있는 횟수(maxHorseCount) 이상이라면? 
 * 	5.1. 시작 deltaIndex는 8, 아니라면 0으로 지정
 * 6. 원숭이가 이동할 수 있는 위치에 대해 반복(deltaIndex)
 * 	6.1. 다음 위치가 범위 내에 있고, 1이 아닌지 확인
 * 	6.2. 다음 위치의 movementMemo가 현재 위치의 movementMemo+1보다 크다면?
 * 		6.2.1. 다음 위치의 movementMemo를 현재 위치의 movementMemo+1로 지정
 * 		6.2.2. 다음 위치 offer
 * 			6.2.2.1. 말 이동 수는 시작 deltaIndex가 
 * 					 8보다 작으면 말 이동 수+1, 아니면 그대로 입력
 */
public class MonkeyLikeHorseBFSDP {
	static class MinimumCount {
		int movement;
		int horseCount;
		public MinimumCount(int movement, int horseCount) {
			super();
			this.movement = movement;
			this.horseCount = horseCount;
		}
	}
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int maxHorseCount;
	private static int width;
	private static int height;
	private static int[][] map;
	private static MinimumCount[][] movementMemo;
	private static final int INF = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("resources/re_sample_input.txt"));
		initialize();
		Queue<int[]> queue = new ArrayDeque<int[]>();
		queue.offer(new int[] {0, 0}); // y, x, 말 이동수
		movementMemo[0][0].movement = 0;
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			int currentHorseCount = movementMemo[current[0]][current[1]].horseCount;
			int deltaIndex = currentHorseCount < maxHorseCount ? 0 : 8;
			for (; deltaIndex<12; deltaIndex++) {
				int nextY = current[0]+delta[deltaIndex][0];
				int nextX = current[1]+delta[deltaIndex][1];
				if (!checkNext(nextY, nextX)) continue;
				if (movementMemo[nextY][nextX].movement > 
					movementMemo[current[0]][current[1]].movement+1 || 
					(movementMemo[nextY][nextX].movement == 
					movementMemo[current[0]][current[1]].movement+1) &&
					movementMemo[nextY][nextX].horseCount > currentHorseCount) {
					movementMemo[nextY][nextX].movement = 
							movementMemo[current[0]][current[1]].movement+1;
					currentHorseCount += deltaIndex < 8 ? 1 : 0;
					movementMemo[nextY][nextX].horseCount = currentHorseCount;
					queue.offer(new int[] {nextY, nextX});
				}
			}
		}
		int result = movementMemo[height-1][width-1].movement;
		System.out.println(result == INF ? -1 : result);
		reader.close();
	}
	
	private static boolean checkNext(int row, int column) {
		return 0<=row && row<height &&
				0<=column && column<width &&
				map[row][column] != 1;
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
		movementMemo = new MinimumCount[height][width];
		for (int row=0; row<height; row++)
			for (int column=0; column<width; column++)
				movementMemo[row][column] = new MinimumCount(INF, 0);
	}
}
