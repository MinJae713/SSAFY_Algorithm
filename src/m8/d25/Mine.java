package m8.d25;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * [문제 풀이]
 * 1. 표의 모든 위치에 대해 반복(row, column)
 * 	1.1. (row, column) 위치가 지뢰거나, 이미 방문한 위치면 다음 반복 수행
 * 	1.2. (row, column) 위치 주변에 지뢰가 있다면? 
 * 		1.2.1. 주변에 지뢰가 있는 위치 담는 배열에 입력(searchedMines)
 * 	1.3. 지뢰가 없다면?
 *	 		1.3.1. 표 각각 위치 주변에 지뢰가 없는 영역 탐색(searchNoneMineArea)
 * 		1.3.2. 파라미터는 현재 행 및 열 (position)
 * 2. 현재 행 및 열 큐(queue)에 enque
 * 3. 현재 행 방문 여부 true
 * 4. queue에 요소가 있는 동안 반복
 * 	4.1. queue에서 요소 deque (current)
 * 	4.2. current위치의 8방향에 지뢰가 있는지 확인 (searchMine)
 * 		4.2.1. 8방향의 지뢰 갯수 계산
 * 	4.3. 지뢰가 최소 하나 있다면 다음 반복 실행
 * 	4.4. current의 8방향에 대해 반복(nextColumn, nextRow)
 * 		4.4.1. 다음 방향이 범위 내에 없거나 이미 방문했다면 다음 반복 수행
 * 		4.4.2. 다음 방향 큐에 enque
 * 		4.4.3. 다음 방향 방문 여부 true
 * 5. 클릭 횟수(clickCount) 1 증가
 * 6. 주변에 지뢰가 있는 지점들 처리(searchMineArea)
 * 	6.1. searchedMines의 각 지점들에 대해 반복
 * 	6.2. 아직 방문하지 않은 지점들 수 만큼 clickCount 증가
 */
public class Mine {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static int[][] delta;
	private static int bound;
	private static char[][] mineField;
	private static boolean[][] visited;
	private static int clickCount;
	private static List<int[]> searchedMines;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int row=0; row<bound; row++)
				for (int column=0; column<bound; column++) {
					if (mineField[row][column] == '*' || 
							visited[row][column])
						continue;
					if (searchMine(row, column) > 0)
						searchedMines.add(new int[] {row, column});
					else searchNoneMineArea(new int[] {row, column});
				}
			searchMineArea();
			builder.append("#").append(testCase).
					append(" ").append(clickCount).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void searchNoneMineArea(int[] position) {
		// 주변에 지뢰가 없는 위치만 수행됨
		Queue<int[]> queue = new ArrayDeque<>();
		queue.offer(position);
		visited[position[0]][position[1]] = true;
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			// 주변 지뢰가 없는 위치에서 접근할 수 있기에 enque 되었지만,
			// deque되어 주변 지뢰가 있는지 보니까 하나 이상 있는 경우임
			// 주변에 지뢰가 있지만 방문 여부는 enque 과정에서 true로 지정됨 (1)
			if (searchMine(current[0], current[1]) > 0) continue;
			// 지뢰가 없는 경우 -> 주변에 접근할 수 있는 방향들 방문 표시 및 enque
			for (int deltaIndex=0; deltaIndex<8; deltaIndex++) {
				int nextRow = current[0] + delta[deltaIndex][0];
				int nextColumn = current[1] + delta[deltaIndex][1];
				if (!inBound(nextRow, nextColumn)) continue;
				else if (visited[nextRow][nextColumn]) continue;
				queue.offer(new int[] {nextRow, nextColumn});
				visited[nextRow][nextColumn] = true;
			}
		}
		clickCount++; 
		// 주변에 지뢰가 없음으로 인해 
		// 한꺼번에 많이 표시될 수 있는 부분에 대한 처리
		// 클릭 한 번으로 가능함
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
	
	private static void searchMineArea() {
		// 주변에 지뢰가 최소 하나 이상 있는 지점들 처리
		for (int[] point : searchedMines)  {
			// 지뢰가 있지만 방문 여부가 true인 지점?
			// 주변 지뢰가 없는 지점을 통해 방문 처리가 이뤄진 지점임
			// (1) -> 이미 true 처리가 됨에 따라 해당 지점은 이미 클릭이 된 것
			clickCount += visited[point[0]][point[1]] ? 0 : 1;
			visited[point[0]][point[1]] = true; // 방문 여부 체크
		}
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
		searchedMines = new ArrayList<int[]>();
	}
}
