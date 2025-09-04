package homeworks.m9.d2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - MinimumCount
 * 	  ㄴ 시작 지점에서부터 도달하는데 걸리는 최소값(movement)
 *   ㄴ 말처럼 이동한 횟수 포함(horseCount)
 * 1. 원숭이의 현재 위치 movement 0으로 지정
 * 2. 격자의 크기 만큼 반복
 * 3. minimumCount가 가장 작은 위치의 row, column, horseCount 반환
 * 	3.1. 이미 방문한 위치는 확인하지 않음
 * 4. minimumCount가 가장 작은 위치가 없다면?
 * 	4.1. 원숭이가 다음으로 이동할 위치를 못 찾은 경우임
 * 	4.2. 반복 종료
 * 5. 해당 위치 방문 true
 * 6. horseCount가 말처럼 이동할 수 있는 횟수(maxHorseCount) 이상이라면? 
 * 	6.1. 시작 deltaIndex는 8, 아니라면 0으로 지정
 * 7. 원숭이가 이동할 수 있는 위치에 대해 반복(deltaIndex)
 * 	7.1. 다음 위치가 범위 내에 있고, 방문하지 않았으며, 1이 아닌지 확인
 * 	7.2. 다음 위치의 MinimumCount movement와 현재 위치의
 * 		 MinimumCount movement+1 비교 -> 후자가 더 작다거나,
 * 		 두 값이 같지만 현재 위치의 말 이동수가 다음 위치의 말 이동수 보다 작다면?
 * 		7.2.1. 다음 위치의 MinimumCount movement는 현재 위치의
 * 		 	   MinimumCount movement+1로 입력
 * 		7.2.2. 다음 위치의 MinimumCount horseCount는
 * 			      현재 위치의 MinimumCount horseCount로 입력
 * 		7.2.3. deltaIndex가 8보다 작다면 다음 위치의 MinimumCount 
 * 			   horseCount 1 증가
 */
public class MonkeyLikeHorse {
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
	private static boolean[][] visited;
	private static MinimumCount[][] minimumCounts;
	private static final int INF = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("resources/re_sample_input.txt"));
		initialize();
		// logic
		minimumCounts[0][0].movement = 0;
		for (int index=0; index<height*width; index++) {
			forLog();
			int minimumMovement = Integer.MAX_VALUE;
			int currentRow = -1, currentColumn = -1;
			int horseCount = 0;
			// 시작점으로부터 최소 비용 위치 탐색
			for (int row=0; row<height; row++)
				for (int column=0; column<width; column++) {
					if (visited[row][column]) continue;
					else if (minimumMovement > 
						minimumCounts[row][column].movement) {
						minimumMovement = minimumCounts[row][column].movement;
						currentRow = row;
						currentColumn = column;
						horseCount = minimumCounts[row][column].horseCount;
					}
				}
			// 모든 위치를 방문했거나, 방문하지 않았어도 모든 위치 최소 비용이 MAX_VALUE
			if (currentRow == -1 && currentColumn == -1) break;
			visited[currentRow][currentColumn] = true;
			// 찾은 위치가 마지막 위치
			if (currentRow == height-1 && currentColumn == width-1) break;
			int startDeltaIndex = horseCount>=maxHorseCount ? 8 : 0;
			System.out.printf("(%d, %d) 인접들![말 카운트:%d]\n================\n", currentColumn, currentRow, horseCount);
			for (int deltaIndex=startDeltaIndex; deltaIndex<12; deltaIndex++) {
				int nextRow = currentRow+delta[deltaIndex][0];
				int nextColumn = currentColumn+delta[deltaIndex][1];
				System.out.printf("(%d, %d)[%b]\n", nextColumn, nextRow, checkNext(nextRow, nextColumn));
				if (!checkNext(nextRow, nextColumn)) continue;
				else if (minimumCounts[nextRow][nextColumn].movement > 
						minimumCounts[currentRow][currentColumn].movement+1 ||
						(minimumCounts[nextRow][nextColumn].movement == 
						minimumCounts[currentRow][currentColumn].movement+1 && 
						minimumCounts[nextRow][nextColumn].horseCount > 
						minimumCounts[currentRow][currentColumn].horseCount)) {
					// 움직임 수가 더 적은 경우 뿐만 아니라, 움직임 수가 같더라도 말 움직임 수가 더 적으면 초기화
					minimumCounts[nextRow][nextColumn].movement = 
							minimumCounts[currentRow][currentColumn].movement+1;
					minimumCounts[nextRow][nextColumn].horseCount = 
							minimumCounts[currentRow][currentColumn].horseCount;
					if (deltaIndex < 8)
						minimumCounts[nextRow][nextColumn].horseCount++;
				}
			}
			System.out.println();
		}
		System.out.println(minimumCounts[height-1][width-1].movement == INF ? 
				-1 : minimumCounts[height-1][width-1].movement);
		reader.close();
	}

	private static boolean checkNext(int row, int column) {
		return 0<=row && row<height &&
				0<=column && column<width &&
				!visited[row][column] && 
				map[row][column] != 1;
	}
	
	private static void forLog() {
		System.out.println("[최소 비용]");
		for (int row=0; row<height; row++) {
			for (int column=0; column<width; column++)
				System.out.printf("%-3d", minimumCounts[row][column].movement == INF ? 99 : minimumCounts[row][column].movement);
			System.out.println();
		}
		System.out.println("[말 카운트]");
		for (int row=0; row<height; row++) {
			for (int column=0; column<width; column++)
				System.out.printf("%-3d", minimumCounts[row][column].horseCount);
			System.out.println();
		}
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		maxHorseCount = Integer.parseInt(reader.readLine().trim());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		width = Integer.parseInt(tokenizer.nextToken());
		height = Integer.parseInt(tokenizer.nextToken());
		map = new int[height][width];
		visited = new boolean[height][width];
		minimumCounts = new MinimumCount[height][width];
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
		for (int row=0; row<height; row++) 
			for (int column=0; column<width; column++)
				minimumCounts[row][column] = new MinimumCount(Integer.MAX_VALUE, 0);
	}

}
