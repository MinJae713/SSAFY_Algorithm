package m8.d14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 모든 방을 순회하면서 반복(roomX, roomY)
 * 2. 현재 위치에서 다른 방으로 들어가는 함수(moveRoom()) 호출
 * 	2.1. 파라미터는 현재 방 위치 및 얼마나 들어갔는지 (moveCount)
 * 	2.2. 어느 방에서 시작하는지에 대한 방 번호
 * 3. 현재 위치 방의 +1 값 저장 (nextNumber)
 * 4. 현재 방의 4방향 위치 마다 수행
 * 	4.1. 4방향 위치의 방이 범위 내 있는지 확인 (방이 존재하는가)
 * 		4.1.1. 없다면 다음 반복 수행
 * 	4.2. 해당 방향 위치의 방이 nextNumber와 동일한지 확인
 * 		4.2.1. 동일하다면 해당 방 위치에 대해 재귀 호출 (moveCount+1)
 * 5. 4방향 어떤 방도 방문하지 않았다면?
 * 	5.1. maxMove와 moveCount 비교
 * 	5.2. moveCount가 더 크다면? maxMove, maxMoveNumber 값 변경
 * 	5.3. 동일하다면? maxMoveNumber 비교, 더 작은 값으로 변경
 */
public class RoomMove {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static int[] deltaRow;
	private static int[] deltaColumn;
	private static StringTokenizer tokenizer;
	private static int size;
	private static int[][] roomNumbers;
	private static int maxMove;
	private static int maxMoveNumber;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			for (int roomY=0; roomY<size; roomY++)
				for (int roomX=0; roomX<size; roomX++) 
					moveRoom(roomX, roomY, 1, roomNumbers[roomY][roomX]);
			builder.append("#").append(testCase)
					.append(" ").append(maxMoveNumber)
					.append(" ").append(maxMove).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void moveRoom(int roomX, int roomY, 
			int moveCount, int startNumber) {
		int nextNumber = roomNumbers[roomY][roomX]+1;
		boolean moved = false;
		for (int delta=0; delta<4 && !moved; delta++) {
			int nextX = roomX+deltaColumn[delta];
			int nextY = roomY+deltaRow[delta];
			// Out of Bound
			if (nextX < 0 || size <= nextX) continue;
			if (nextY < 0 || size <= nextY) continue;
			if (nextNumber == roomNumbers[nextY][nextX]) {
				moved = true;
				moveRoom(nextX, nextY, moveCount+1, startNumber);
			}
		}
		if (!moved) {
			if (maxMove < moveCount) {
				maxMove = moveCount;
				maxMoveNumber = startNumber;
			} else if (maxMove == moveCount)
				maxMoveNumber = Math.min(maxMoveNumber, startNumber);
		}
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		deltaColumn = new int[] {1, 0, -1, 0};
		deltaRow = new int[] {0, 1, 0, -1};
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		size = Integer.parseInt(reader.readLine().trim());
		roomNumbers = new int[size][size];
		for (int row=0; row<size; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<size; column++)
				roomNumbers[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
		maxMove = 0;
		maxMoveNumber = 0;
	}
}
