package m8.d25;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * [문제 풀이]
 * 1. 표의 모든 위치에 대해 반복
 * 	1.1. 표 각각 위치 주변에 지뢰가 없는 영역 탐색(searchNoneMineArea)
 * 	1.2. 파라미터는 현재 행 및 열 (position)
 * 	1.3. 이전 수행으로 인해 이미 방문여부가 true가 된 위치는 수행되지 않음
 * 2. 현재 행 및 열 큐(queue)에 enque
 * 3. 현재 행 방문 여부 true
 * 4. queue에 요소가 있는 동안 반복
 * 	4.1. queue에서 요소 deque (current)
 * 	4.2. 이미 방문한 위치라면 다음 반복 수행
 * 	4.3. current위치의 8방향에 지뢰가 있는지 확인 (searchMine)
 * 		4.3.1. 8방향의 지뢰 갯수 계산
 * 	4.4. 지뢰가 최소 하나 있다면 다음 반복 실행
 * 	4.5. current의 8방향에 대해 반복(nextColumn, nextRow)
 * 		4.5.1. 다음 방향이 접근할 수 없거나 이미 방문했다면 다음 반복 수행
 * 		4.5.2. 다음 방향 큐에 enque
 * 		4.5.3. 다음 방향 방문 여부 true
 * 5. 클릭 횟수(clickCount) 1 증가
 */
public class MineAgain {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static int[][] delta;
	private static int bound;
	private static char[][] mineField;
	private static boolean[][] visited;
	private static int clickCount;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int row=0; row<bound; row++)
				for (int column=0; column<bound; column++) {
					if (visited[row][column] || mineField[row][column] == '*')
						continue;
					searchNoneMineArea(new int[] {row, column});
				}
//			System.out.println();
			builder.append("#").append(testCase).
					append(" ").append(clickCount).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void searchNoneMineArea(int[] position) {
		if (searchMine(position[0], position[1]) > 0) return;
		Queue<int[]> queue = new ArrayDeque<>();
		queue.offer(position);
		visited[position[0]][position[1]] = true;
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			if (searchMine(current[0], current[1]) > 0) continue;
			System.out.println(current[0]+", "+current[1]);
			// 지뢰가 없는 경우
			for (int deltaIndex=0; deltaIndex<8; deltaIndex++) {
				int nextRow = current[0] + delta[deltaIndex][0];
				int nextColumn = current[1] + delta[deltaIndex][1];
				if (!inBound(nextRow, nextColumn)) continue;
				else if (visited[nextRow][nextColumn]) continue;
				queue.offer(new int[] {nextRow, nextColumn});
				visited[nextRow][nextColumn] = true;
			}
		}
		clickCount++; // 무작정 클릭하지 못하게 해야 함
	}

	private static int searchMine(int currentRow, int currentColumn) {
		int mineCount = 0;
		for (int deltaIndex=0; deltaIndex<8; deltaIndex++) {
			int nextRow = currentRow + delta[deltaIndex][0];
			int nextColumn = currentColumn + delta[deltaIndex][1];
			if (!inBound(nextRow, nextColumn)) continue;
			mineCount += mineField[nextRow][nextColumn] == '*' ? 1 : 0;
		}
		return mineCount;
	}

	private static boolean inBound(int row, int column) {
		return 0<=row && row<bound &&
				0<=column && column<bound;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {
			{-1, 0}, {-1, 1},
			{0, 1}, {1, 1},
			{1, 0}, {1, -1},
			{0, -1}, {-1, -1}
		};
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		bound = Integer.parseInt(reader.readLine().trim());
		mineField = new char[bound][bound];
		visited = new boolean[bound][bound];
		for (int row=0; row<bound; row++)
			mineField[row] = reader.readLine().trim().toCharArray();
		clickCount = 0;
	}
}
