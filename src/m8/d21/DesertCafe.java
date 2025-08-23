package m8.d21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 카페 범위에서 열은 행은 처음~마지막에서 세번째, 
 * 	열은 두번째~마지막에서 두번째 위치에 대해 반복
 * 	1.1. 방문한 카페 종류 배열(visitedCafe) 초기화
 * 	1.2. 현재 위치의 카페 종류 배열 값 true
 * 	1.3. 각 위치에서 출발하여 디저트 카페 방문 (visitDesertCafe)
 * 	1.4. 현재 위치의 카페 종류 배열 값 false
 * 2. 파라미터로는 시작 카페 위치(startRow, startColumn), 
 * 	현재 카페 위치(row, column), 방향(delta), 지나온 카페 수(cafeCount),
 * 3. 다음으로 이동할 수 있는 방향(nextDelta)에 대해 반복
 * 	3.1. 다음 방향이 시작 위치라면? 지나온 카페 수+1과 최댓값 비교,
 * 	3.2. 다음 방향이 범위 밖에 있거나, 이미 방문한 카페 종류라면 다음 반복 수행
 * 	3.3. 아니라면 다음 위치에 대해 재귀 호출
 * 		3.3.1. 현재 위치의 카페 종류 배열 값 true
 * 		3.3.2. 파라미터는 시작 카페 위치, 다음 카페 위치, 
 * 				 다음 이동 방향(nextDelta), 지나온 카페 수+1
 * 		3.3.3. 현재 위치의 카페 종류 배열 값 true
 * 4. 현재 위치의 카페 종류 배열 값 false
 */
public class DesertCafe {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] deltaList;
	private static int[][] nextAccess;
	private static int size;
	private static int[][] cafes;
	private static boolean[] visitedCafe;
	private static int maxCount;
	private static int startRow;
	private static int startColumn;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int row=0; row<size-2; row++) 
				for (int column=1; column<size-1; column++) {
					visitedCafe = new boolean[101];
					startRow = row;
					startColumn = column;
					visitedCafe[cafes[row][column]] = true;
					visitDesertCafe(row, column, 0, 0);
					visitedCafe[cafes[row][column]] = false;
				}
			builder.append("#").append(testCase).
						append(" ").append(maxCount).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static void visitDesertCafe(int row, int column, int delta, int cafeCount) {
		int[] nextDeltas = nextAccess[delta];
		for (int index=0; index<nextDeltas.length; index++) {
			int nextDelta = nextDeltas[index];
			int nextColumn = column+deltaList[nextDelta][0];
			int nextRow = row+deltaList[nextDelta][1];
			if (comback(nextDelta, nextRow, nextColumn)) {
				maxCount = Math.max(maxCount, cafeCount+1);
				return;
			} else if (!checkMovable(nextRow, nextColumn)) continue;
			visitedCafe[cafes[nextRow][nextColumn]] = true;
			visitDesertCafe(nextRow, nextColumn, nextDelta, cafeCount+1);
			visitedCafe[cafes[nextRow][nextColumn]] = false;
		}
	}
	private static boolean checkMovable(int row, int column) {
		if (row<0 || size<=row) return false;
		else if (column<0 || size<=column) return false;
		return !visitedCafe[cafes[row][column]];
	}
	private static boolean comback(int delta, int nextRow, int nextColumn) {
		return delta == 4 && nextRow == startRow  && nextColumn == startColumn;
	}
	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		deltaList = new int[][] {{0, 0}, {1, 1}, {-1, 1}, {-1, -1}, {1, -1}};
		nextAccess = new int[][] {{1}, {1, 2}, {2, 3}, {3, 4}, {4}};
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		size = Integer.parseInt(reader.readLine().trim());
		cafes = new int[size][size];
		for (int row=0; row<size; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<size; column++)
				cafes[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
		maxCount = -1;
	}
}
// ? 이게 왜 패스? -> visitedCafe 초기화 하는 부분이 문제였던거 같은데...
