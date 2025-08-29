package m8.d29;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 수연이 이동 방향 별 반복(deltaIndex)
 * 	1.1. 이동 위치가 범위 밖이거나, X이거나, *이라면 다음 반복 수행
 * 	1.2. 수연이 기존 위치 '.'지정, 현재 위치 방문 표시 (visited), 다음 위치로 이동 (nextPosition())
 * 		1.2.1. 파라미터는 다음 위치(nextX, nextY), 이동 방향(delta), 
 * 			      영역(field), 시간(time), 수연이 방문 위치(visited)
 * 		1.2.2. 도달 성공 횟수 반환
 * 	1.3. 도달 성공 횟수 합산
 * 2. 현재 위치가 D라면? -> 시간 최솟값 지정, 1 반환
 * 3. 현재 위치 S로 지정, 현재 위치 방문 표시 (visited)
 * 4. * 확장
 * 	4.1. * 현재 확장 여부 확인 배열 초기화 (currentExpanded)
 * 	4.2. 영역의 모든 위치에 대해 반복(devilX, devilY)
 * 	4.3. 현재 위치 값이 *이 아니거나, 현재 확장된 위치라면 다음 반복 수행
 * 	4.4. *이라면 주변 4방향 확인 (nextDevilX, nextDevilY)
 * 		4.4.1. 방향 값이 영역 밖이거나, D거나, X거나, *이라면 다음 반복 수행
 * 		4.4.2. 방향 값이 S라면 0 반환 (수연이에 닿은 경우)
 * 			4.4.2.1. 확장된 위치에 수연이가 있던 경우 -> 수연이 이동 불가(가지치기)
 * 		4.4.3. 그 이외의 경우라면 해당 위치 * 및 확장 표시
 * 5. 수연이 이동 방향 별 반복(deltaIndex)
 * 	5.1. 다음 위치가 영역 밖이거나, X이거나, 이미 방문한 위치거나, *이거나 현재 온 방향이면 다음 반복 수행
 * 	5.2. 현재 위치 .으로 지정
 * 	5.3. 다음 위치, 이동 방향, 영역을 파라미터로 nextPosition 재귀호출
 * 		5.3.1. 영역은 기존 영역이 아닌 복사본을 파라미터로 전달
 * 		5.3.2. 다시 돌아왔을 때 기존 복사본을 사용하기 위함임
 * 	5.4. 도달 성공 횟수(arrived) 합산
 * 6. 도달 성공 횟수 반환
 * 7. 도달 성공 횟수가 0보다 크면 최소 시간, 0이라면 GAME OVER 출력
 */
public class OhMyGirlGod {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int rowCount;
	private static int columnCount;
	private static char[][] originField;
	private static int[] startPosition;
	private static int[] girlGodPosition;
	private static int totalAchieved;
	private static int minimumTime;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
//			if (testCase == 3) continue;
			for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
				int nextY = startPosition[0]+delta[deltaIndex][0];
				int nextX = startPosition[1]+delta[deltaIndex][1];
				if (!accessAble(nextX, nextY, originField)) continue;
				char[][] copiedField = copyField(originField);
				boolean[][] visited = new boolean[rowCount][columnCount];
				copiedField[startPosition[0]][startPosition[1]] = '.';
				visited[startPosition[0]][startPosition[1]] = true;
//				System.out.printf("(%d, %d)\n", nextX, nextY);
				totalAchieved += nextPosition(nextY, nextX, 
						copiedField, 1, visited);
//				forLog(copiedField);
			}
//			System.out.println("=========");
			builder.append("#").append(testCase).append(" ").
					append(totalAchieved > 0 ? minimumTime : "GAME OVER").
					append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static void forLog(char[][] field) {
		for (int row=0; row<rowCount; row++) {
			for (int column=0; column<columnCount; column++)
				System.out.printf("%c", field[row][column]);
			System.out.println();
		}
	}
	private static int nextPosition(int currentY, int currentX, 
			char[][] field, int time, boolean[][] visited) {
		if (currentY == girlGodPosition[0] && 
			currentX == girlGodPosition[1]) {
			minimumTime = Math.min(minimumTime, time);
			return 1;
		}
		field[currentY][currentX] = 'S';
		visited[currentY][currentX] = true;
		// * 확장하다가 확장 영역에 수연이가 있는 경우 -> 수연이는 움직일 수 없음
		if (!expandDevil(field)) return 0;
		// 수연이 이동
		int arrived = 0;
		for (int nextDelta=0; nextDelta<4; nextDelta++) {
			int nextY = currentY+delta[nextDelta][0];
			int nextX = currentX+delta[nextDelta][1];
			if (!accessAble(nextX, nextY, field) || visited[nextY][nextX]) continue;
//			System.out.println("[이동 전] - T: "+time);
//			forLog(field);
			field[currentY][currentX] = '.';
			arrived += nextPosition(nextY, nextX, 
					copyField(field), time+1, copyVisited(visited));
//			System.out.println("[이동 후] - T: "+time);
//			forLog(field);
		}
		return arrived;
	}
	private static boolean expandDevil(char[][] field) {
		// * 확장
		boolean[][] currentExpanded = new boolean[rowCount][columnCount];
		for (int devilY=0; devilY<rowCount; devilY++)
			for (int devilX=0; devilX<columnCount; devilX++) {
				if (field[devilY][devilX] != '*' || 
					currentExpanded[devilY][devilX]) continue;
//				System.out.printf("조건 만족 - (%d, %d)\n", devilX, devilY);
				for (int devilDelta=0; devilDelta<4; devilDelta++) {
					int nextDevilY = devilY+delta[devilDelta][0];
					int nextDevilX = devilX+delta[devilDelta][1];
					if (!accessAble(nextDevilX, nextDevilY, field) || 
							field[nextDevilY][nextDevilX] == 'D') continue;
					else if (field[nextDevilY][nextDevilX] == 'S') return false;
					else {
						field[nextDevilY][nextDevilX] = '*';
						currentExpanded[nextDevilY][nextDevilX] = true;
					}
				}
			}
		return true;
	}
	private static boolean accessAble(int x, int y, char[][] field) {
		return 0<=x && x<columnCount && 0<=y && y<rowCount &&
				field[y][x] != '*' && field[y][x] != 'X';
	}
	private static char[][] copyField(char[][] field) {
		char[][] copied = new char[rowCount][columnCount];
		for (int row=0; row<rowCount; row++)
			for (int column=0; column<columnCount; column++)
				copied[row][column] = field[row][column];
		return copied;
	}
	private static boolean[][] copyVisited(boolean[][] visited) {
		boolean[][] copied = new boolean[rowCount][columnCount];
		for (int row=0; row<rowCount; row++)
			for (int column=0; column<columnCount; column++)
				copied[row][column] = visited[row][column];
		return copied;
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		rowCount = Integer.parseInt(tokenizer.nextToken());
		columnCount = Integer.parseInt(tokenizer.nextToken());
		originField = new char[rowCount][columnCount];
		for (int rowIndex=0; rowIndex<rowCount; rowIndex++) {
			originField[rowIndex] = reader.readLine().trim().toCharArray();
			for (int columnIndex=0; columnIndex<columnCount; columnIndex++) {
				if (originField[rowIndex][columnIndex] == 'D')
					girlGodPosition = new int[] {rowIndex, columnIndex};
				else if (originField[rowIndex][columnIndex] == 'S')
					startPosition = new int[] {rowIndex, columnIndex};
			}
		}
		totalAchieved = 0;
		minimumTime = Integer.MAX_VALUE;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		return Integer.parseInt(reader.readLine().trim());
	}

}
