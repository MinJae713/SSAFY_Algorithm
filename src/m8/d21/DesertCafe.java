package m8.d21;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 카페 범위에서 열은 행은 처음~마지막에서 세번째, 
 * 	열은 두번째~마지막에서 두번째 위치에 대해 반복
 * 	1.1. 방문한 카페 종류 배열(visitedCafe) 초기화
 * 	1.2. 각 위치에서 출발하여 디저트 카페 방문 (visitDesertCafe)
 * 2. 파라미터로는 시작 카페 위치(startRow, startColumn), 
 * 	현재 카페 위치(row, column), 방향(delta), 지나온 카페 수(cafeCount), 
 * 	남동 방향 이동 수(southEastCount)
 * 3. 현재 위치의 카페 종류 배열 값 true
 * 4. 들어온 방향 확인
 * 	4.1. 초기 위치(0)이면?
 * 		4.1.1. 남동방향 다음 위치값 반환(nextRow, Column)
 * 		4.1.2. 다음 방향이 범위 내에 있고, 이미 방문한 카페 종류가 아니라면?
 * 			4.1.2.1. 다음 위치와 남동 방향(1), 지나온 카페 수+1, 
 * 						남동 방향 이동 수+1을 파라미터로 재귀 호출
 * 	4.2. 남동 방향(1)이면?
 * 		4.2.1. 남동방향 다음 위치값 반환(nextRow, Column)
 * 		4.2.2. 다음 방향이 범위 내에 있고, 이미 방문한 카페 종류가 아니라면?
 * 			4.2.2.1. 다음 위치와 남동 방향(1), 지나온 카페 수+1, 
 * 						남동 방향 이동 수+1을 파라미터로 재귀 호출
 * 		4.2.3 남서방향 다음 위치값 반환(nextRow, Column)
 * 		4.2.4. 다음 방향이 범위 내에 있고, 이미 방문한 카페 종류가 아니라면?
 * 			4.2.4.1. 다음 위치와 남서 방향(2), 지나온 카페 수+1, 
 * 						남동 방향 이동 수는 그대로 하여 재귀 호출
 * 	4.3. 남서 방향(2)이면?
 * 		4.3.1. 남서방향 다음 위치값 반환(nextRow, Column)
 * 		4.3.2. 다음 방향이 범위 내에 있고, 이미 방문한 카페 종류가 아니라면?
 * 			4.3.2.1. 다음 위치와 남서 방향(2), 지나온 카페 수+1, 
 * 						남동 방향 이동 수는 그대로 하여 재귀 호출
 * 		4.3.3. 북서방향 다음 위치값 반환(nextRow, Column)
 * 		4.3.4. 다음 방향이 범위 내에 있고, 이미 방문한 카페 종류가 아니라면?
 * 			4.3.4.1. 다음 위치와 북서 방향(3), 지나온 카페 수+1,
 * 						남동 방향 이동 수-1을 파라미터로 재귀 호출
 * 	4.4. 북서 방향(3)이면?
 * 		4.4.1. 북서방향 다음 위치값 반환(nextRow, Column)
 * 		4.4.2. 남동 방향 이동 수가 0보다 크고, 다음 방향이 범위 내에 있으며, 이미 방문한 카페 종류가 아니면?
 * 			4.4.2.1. 다음 위치와 북서 방향(3), 지나온 카페 수+1,
 * 						남동 방향 이동 수-1을 파라미터로 재귀 호출 및 함수 종료
 * 			4.4.2.2. 남동 방향 이동 수가 0보다 크다는 것은 북서로 더 가야 한다는 것
 * 			4.4.2.3. 가야 할 북서 방향이 있는데 북동 방향으로 꺾으면 네모가 완성되지 않음
 * 		4.4.3. 북동방향 다음 위치값 반환(nextRow, Column)
 * 		4.4.4. 다음 방향이 범위 내에 있고, 이미 방문한 카페 종류가 아니라면?
 * 			4.4.4.1. 다음 위치와 북동 방향(4), 지나온 카페 수+1,
 * 						남동 방향 이동 수는 그대로 하여 재귀 호출
 * 		4.4.5. 이 경우는 남동 방향 이동 수가 0이 되고, 더 올라갈 북서 방향이 없어서 북동 방향으로 올라가는 경우 
 * 	4.5. 북동 방향(4)이면?
 * 		4.5.1. 현재 위치가 출발 위치와 동일하다면?
 * 			4.5.1.1. 지나온 카페 수와 최대 카페 수와 비교 -> 큰 값으로 입력, 함수 실행 종료
 * 		4.5.2. 북동방향 다음 위치값 반환(nextRow, Column)
 * 		4.5.3. 다음 방향이 범위 내에 있고, 이미 방문한 카페 종류가 아니라면?
 * 			4.5.3.1. 다음 위치와 북동 방향(4), 지나온 카페 수+1,
 * 						남동 방향 이동 수는 그대로 하여 재귀 호출
 */
public class DesertCafe {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[] deltaX;
	private static int[]	deltaY;
	private static int size;
	private static int[][] cafes;
	private static boolean[] visitedCafe;
	private static int maxCount;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int row=0; row<size-2; row++) 
				for (int column=1; column<size-1; column++) 
					visitDesertCafe(row, column, row, column, 0, 0, 0);
			builder.append("#").append(testCase).
						append(" ").append(maxCount).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void visitDesertCafe(int startRow, int startColumn, 
			int row, int column, int delta, int cafeCount, int southEastCount) {
//		System.out.printf("delta:%d, (%d, %d), s.e.c:%d\n", delta, column, row, southEastCount);
		visitedCafe[cafes[row][column]] = true;
		int nextRow = 0;
		int nextColumn = 0;
		switch (delta) {
		case 0:
			nextRow = row+deltaY[1];
			nextColumn = column+deltaX[1];
			if (checkMovable(nextRow, nextColumn))
				visitDesertCafe(startRow, startColumn, nextRow, 
						nextColumn, 1, cafeCount+1, southEastCount+1);
			break;
		case 1:
			nextRow = row+deltaY[1];
			nextColumn = column+deltaX[1];
			if (checkMovable(nextRow, nextColumn))
				visitDesertCafe(startRow, startColumn, nextRow, 
						nextColumn, 1, cafeCount+1, southEastCount+1);
			nextRow = row+deltaY[2];
			nextColumn = column+deltaX[2];
			if (checkMovable(nextRow, nextColumn))
				visitDesertCafe(startRow, startColumn, nextRow, 
						nextColumn, 2, cafeCount+1, southEastCount);
			break;
		case 2:
			nextRow = row+deltaY[2];
			nextColumn = column+deltaX[2];
			if (checkMovable(nextRow, nextColumn))
				visitDesertCafe(startRow, startColumn, nextRow, 
						nextColumn, 2, cafeCount+1, southEastCount);
			nextRow = row+deltaY[3];
			nextColumn = column+deltaX[3];
			if (checkMovable(nextRow, nextColumn))
				visitDesertCafe(startRow, startColumn, nextRow, 
						nextColumn, 3, cafeCount+1, southEastCount-1);
			break;
		case 3:
			nextRow = row+deltaY[3];
			nextColumn = column+deltaX[3];
			if (southEastCount>0 && checkMovable(nextRow, nextColumn)) {
				visitDesertCafe(startRow, startColumn, nextRow, 
						nextColumn, 3, cafeCount+1, southEastCount-1);
				return;
			}
			nextRow = row+deltaY[4];
			nextColumn = column+deltaX[4];
			if (checkMovable(nextRow, nextColumn) ||
					(nextRow == startRow && nextColumn == startColumn))
				visitDesertCafe(startRow, startColumn, nextRow, 
						nextColumn, 4, cafeCount+1, southEastCount);
			break;
		case 4:
			if (row == startRow && column == startColumn) {
				maxCount = Math.max(maxCount, cafeCount);
				return;
			}
			nextRow = row+deltaY[4];
			nextColumn = column+deltaX[4];
			if (checkMovable(nextRow, nextColumn))
				visitDesertCafe(startRow, startColumn, nextRow, 
						nextColumn, 4, cafeCount+1, southEastCount);				
			break;
		}
		visitedCafe[cafes[row][column]] = false;
	}

	private static boolean checkMovable(int row, int column) {
		return 0<=row && row<size &&
				0<=column && column<size &&
				!visitedCafe[cafes[row][column]];
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		deltaX = new int[] {0, 1, -1, -1, 1};
		deltaY = new int[] {0, 1, 1, -1, -1};
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
		visitedCafe = new boolean[101];
		maxCount = -1;
	}
}
