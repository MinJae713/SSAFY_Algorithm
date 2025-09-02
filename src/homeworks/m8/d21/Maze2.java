package homeworks.m8.d21;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * [문제 풀이]
 * 1. 초기 위치 값 큐에 입력
 * 2. 초기 위치 방문 표시(visited)
 * 3. 큐에 요소가 하나 이상 있는 동안 반복
 * 4. 큐에서 현재 좌표 반환 (current)
 * 	4.1. 반환된 좌표의 값이 3 이라면?
 * 	4.2. 목적지 도달(targetSucceed) 성공 표시 및 반복 종료
 * 5. 큐에 인접해있는 4방향에 대해 반복
 * 	5.1. 인접한 해당 위치가 범위 내에 있고, 
 * 		  해당 위치가 1이 아니며,
 * 		  이미 방문한 위치가 아니라면?
 * 	5.2. 해당 위치 큐에 입력
 * 	5.3. 해당 위치 방문 표시
 * 6. targetSucceed가 true면 1, 아니면 0을 builder에 입력
 */
public class Maze2 {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static int[] deltaX;
	private static int[] deltaY;
	private static int size;
	private static char[][] maze;
	private static boolean[][] visited;
	private static int startX;
	private static int startY;
	private static boolean targetSucceed;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		initializeTest();
		for (int testCase=1; testCase<=10; testCase++) {
			initialize();
			// logic
			Queue<int[]> pointQueue = new ArrayDeque<int[]>();
			pointQueue.offer(new int[] {startY, startX});
			visited[startY][startX] = true;
			while (!pointQueue.isEmpty()) {
				int[] current = pointQueue.poll();
				int y = current[0];
				int x = current[1];
				if (maze[y][x] == '3') {
					targetSucceed = true;
					break;
				}
				for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
					int nextX = x+deltaX[deltaIndex];
					int nextY = y+deltaY[deltaIndex];
					if (0<=nextX && nextX<size &&
						0<=nextY && nextY<size &&
						maze[nextY][nextX] != '1' &&
						!visited[nextY][nextX]) {
						pointQueue.offer(new int[] {nextY, nextX});
						visited[nextY][nextX] = true;
					}
				}
			}
			builder.append(targetSucceed ? 1 : 0).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static void initializeTest() {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		deltaX = new int[] {0, 1, 0, -1};
		deltaY = new int[] {-1, 0, 1, 0};
	}
	private static void initialize() throws IOException {
		builder.append("#").append(reader.readLine()).append(" ");
		size = 100;
		maze = new char[size][size];
		visited = new boolean[size][size];
		boolean sourceFound = false;
		for (int row=0; row<size; row++) {
			maze[row] = reader.readLine().toCharArray();
			for (int column=0; column<size && !sourceFound; column++) {
				if (maze[row][column] == '2') {
					startX = column;
					startY = row;
					sourceFound = true;
				}
			}
		}
		targetSucceed = false;
	}
}
