package m8.d25;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * [문제 풀이]
 * 1. 필드의 각 지점에 대해 반복
 * 	1.1. 현재 지점이 *이라면 다음 반복 수행
 * 2. 각 지점의 8방향 중 접근할 수 있는 방향의 값이 모두 .인지 확인
 * 3. 모두 .이라면 접근할 수 있는 두 지점에 대해 union
 * 	3.1. 파라미터는 현재 지점 및 합칠 대상 지점
 * 	3.2. 각 지점이 속한 집합의 대표에 해당하는 지점 확인(find)
 * 		3.2.1. 파라미터는 확인할 위치 해당 행 및 열(row, column)
 * 		3.2.2. 그 행과 열의 부모에 해당하는 행과 열이 row, column과 동일하면?
 * 			3.2.2.1. 해당 행 및 열 값을 int[] 형으로 반환
 * 		3.2.3. 다음 행 및 열 값을 파라미터로 find 재귀 호출, 
 * 			      호출 결과를 row, column의 부모로 입력
 * 	3.3. 현재 지점이 속한 집합의 대표(currentRoot)와 합칠 대상(targetRoot)쪽의 대표가 동일하다면?
 * 		3.3.1. 함수 실행 종료
 * 	3.4. currentRoot의 부모 Row, Column에 targetRoot Row, Column 입력
 * 4. 필드의 각 지점에 대해 반복
 * 	4.1. 현재 지점이 *이라면 다음 반복 수행
 * 5. 각 지점에 대해 find 호출 -> 결과로 나온 row, column 위치 해당 isRoot 값이 처음 방문했는지 확인
 * 	5.1. 방문하지 않았다면? 해당 위치 true 지정, 클릭 수 1 증가
 *
 */
public class Mine {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static int[][] delta;
	private static int bound;
	private static char[][] mineField;
	private static int[][] parentPositionRow;
	private static int[][] parentPositionColumn;
	private static boolean[][] isRoot;
	private static int clickCount;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			unionAccessablePosition();
//			for (int row=0; row<bound; row++) {
//				for (int column=0; column<bound; column++) 
//					System.out.printf("(%d, %d) ", 
//							parentPositionColumn[row][column], 
//							parentPositionRow[row][column]);
//				System.out.println();
//			}
//			System.out.println();
			countClick();
			builder.append("#").append(testCase).
					append(" ").append(clickCount).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void countClick() {
		for (int row=0; row<bound; row++)
			for (int column=0; column<bound; column++) {
				if (mineField[row][column] == '*') continue;
				int[] root = find(row, column);
				if (!isRoot[root[1]][root[0]]) {
//					System.out.printf("(%d, %d)\n", column, row);
					isRoot[root[1]][root[0]] = true;
					clickCount++;
				}
			}
//		System.out.println();
	}

	private static void unionAccessablePosition() {
		for (int row=0; row<bound; row++)
			for (int column=0; column<bound; column++) {
				if (mineField[row][column] == '*') continue;
				boolean hasMine = false;
				int[][] accessAble = new int[8][2];
				int accessAbleSize = 0;
				for (int deltaIndex=0; deltaIndex<8 && !hasMine; deltaIndex++) {
					int nextColumn = column+delta[deltaIndex][0];
					int nextRow = row+delta[deltaIndex][1];
					if (!(0<=nextColumn && nextColumn<bound &&
							0<=nextRow && nextRow<bound)) continue;
					hasMine = mineField[nextRow][nextColumn] == '*';
					if (!hasMine) 
						accessAble[accessAbleSize++] = new int[] {nextColumn, nextRow};
				}
				if (hasMine) continue;
				System.out.printf("지뢰 없음: (%d, %d)\n", column, row);
				for (int accessAbleIndex=0; accessAbleIndex<accessAbleSize; accessAbleIndex++) {
					int accessAbleColumn = accessAble[accessAbleIndex][0];
					int accessAbleRow = accessAble[accessAbleIndex][1];
					union(row, column, accessAbleRow, accessAbleColumn);
				}
			}
	}

	private static void union(int row, int column, int accessAbleRow, int accessAbleColumn) {
		int[] currentRoot = find(row, column);
		int[] targetRoot = find(accessAbleRow, accessAbleColumn);
		if (currentRoot[0] == targetRoot[0] && currentRoot[1] == targetRoot[1]) return;
		parentPositionColumn[currentRoot[1]][currentRoot[0]] = targetRoot[0]; // 열 입력
		parentPositionRow[currentRoot[1]][currentRoot[0]] = targetRoot[1]; // 행 입력
	}

	private static int[] find(int row, int column) {
		if (row == parentPositionRow[row][column] && 
			column == parentPositionColumn[row][column])
			return new int[] {column, row};
		int parentRow = parentPositionRow[row][column];
		int parentColumn = parentPositionColumn[row][column];
		int[] parentFinded = find(parentRow, parentColumn);
		parentPositionColumn[row][column] = parentFinded[0];
		parentPositionRow[row][column] = parentFinded[1];
		return parentFinded;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{0, -1}, {1, -1}, {1, 0}, {1, 1}, 
			{0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		bound = Integer.parseInt(reader.readLine().trim());
		mineField = new char[bound][];
		for (int row=0; row<bound; row++)
			mineField[row] = reader.readLine().trim().toCharArray();
		parentPositionRow = new int[bound][bound];
		parentPositionColumn = new int[bound][bound];
		for (int row=0; row<bound; row++)
			for (int column=0; column<bound; column++) {
				parentPositionRow[row][column] = row;
				parentPositionColumn[row][column] = column;
			}
		isRoot = new boolean[bound][bound];
		clickCount = 0;
	}
}
