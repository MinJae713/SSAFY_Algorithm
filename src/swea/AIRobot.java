package swea;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 초기 위치의 방문 여부(visited) true
 * 2. nextPosition(): 로봇의 다음 위치 이동 및 연료 소모 비용 계산
 * 	2.1. 파라미터는 현재 위치(currentRow, currentColumn), 
 * 		  누적 연료 소모 비용(fuel), 들어온 방향(currentDelta)
 * 3. 현재 위치가 마지막(bound-1, bound-1) 위치라면?
 * 	3.1. 누적 연료 소모 비용의 최솟값(minimumFuel) 계산 및 함수 실행 종료
 * 4. 현재 위치의 네 방향에 대해 반복
 * 	4.1. 다음 방향이 초기 위치가 아니고, 들어온 방향이라면 continue
 * 	4.2. 다음 방향이 들어갈 수 있는 방향인지 확인
 * 		4.2.1. 범위 내에 있으며, 방문하지 않은 경우
 * 	4.3. 다음 위치 높이 확인 및 연료 소모 비용 계산(nextFuel)
 * 		4.3.1. 현재 위치의 높이 < 다음 위치의 높이 : |다음 위치의 높이 - 현재 위치의 높이| * 2
 * 		4.3.2. 현재 위치의 높이 > 다음 위치의 높이: 0
 * 		4.3.3. 현재 위치의 높이 == 다음 위치의 높이: 1
 * 	4.4. nextFuel+fuel 한 값이 minimumFuel보다 크다면 다음 반복 실행
 * 	4.5. 다음 위치 방문 여부 true
 * 	4.6. 다음 위치, 현재 연료+nextFuel, 들어가는 방향을 파라미터로 nextPosition() 재귀호출
 * 	4.7. 다음 위치 방문 여부 false
 */
public class AIRobot {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int bound;
	private static int[][] heights;
	private static boolean[][] visited;
	private static int minimumFuel;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			visited[0][0] = true;
			nextPositionDFS(0, 0, 0, -1);
			builder.append("#").append(testCase).
					append(" ").append(minimumFuel).append("\n");
			visited[0][0] = false;
		}
		System.out.print(builder);
		reader.close();
	}

	private static void nextPositionDFS(int currentRow, 
			int currentColumn, int fuel, int currentDelta) {
		if (currentRow == bound-1 && currentColumn == bound-1) {
			minimumFuel = Math.min(minimumFuel, fuel);
			return;
		}
		for (int nextDelta=0; nextDelta<4; nextDelta++) {
			if (currentDelta > -1 && Math.abs(currentDelta-nextDelta) == 2)
				continue;
			int nextColumn = currentColumn+delta[nextDelta][0];
			int nextRow = currentRow+delta[nextDelta][1];
			if (!moveAble(nextColumn, nextRow)) continue;
			int currentHeight = heights[currentRow][currentColumn];
			int nextHeight = heights[nextRow][nextColumn];
			int nextFuel = 0;
			if (currentHeight < nextHeight)
				nextFuel = (nextHeight-currentHeight)*2;
			else if (currentHeight > nextHeight);
			else nextFuel++;
			if (fuel+nextFuel > minimumFuel) continue;
			visited[nextRow][nextColumn] = true;
			nextPositionDFS(nextRow, nextColumn, fuel+nextFuel, nextDelta);
			visited[nextRow][nextColumn] = false;
		}
	}
	
//	private static void nextPositionBFS() {
//		Queue<int[]> queue = new ArrayDeque<int[]>();
//		queue.offer(new int[] {0, 0, -1, 0});
//		while (!queue.isEmpty()) {
//			int[] current = queue.poll();
//			int currentColumn = current[0];
//			int currentRow = current[1];
//			int currentDelta = current[2];
//			int currentFuel = current[3];
//			if (currentRow == bound-1 && currentColumn == bound-1) {
//				minimumFuel = Math.min(minimumFuel, currentFuel);
//				return;
//			}
//			for (int nextDelta=0; nextDelta<4; nextDelta++) {
//				if (currentDelta > -1 && Math.abs(currentDelta-nextDelta) == 2)
//					continue;
//				int nextColumn = currentColumn+delta[nextDelta][0];
//				int nextRow = currentRow+delta[nextDelta][1];
//				if (!(0<=nextColumn && nextColumn<bound &&
//						0<=nextRow && nextRow<bound)) continue;
//				int currentHeight = heights[currentRow][currentColumn];
//				int nextHeight = heights[nextRow][nextColumn];
//				int nextFuel = 0;
//				if (currentHeight < nextHeight)
//					nextFuel = (nextHeight-currentHeight)*2;
//				else if (currentHeight > nextHeight);
//				else nextFuel++;
//				if (currentFuel+nextFuel>minimumFuel) continue;
//				queue.offer(new int[] {nextColumn, nextRow, nextDelta, 
//						currentFuel+nextFuel});
//			}
//		}
//	}

	private static boolean moveAble(int nextColumn, int nextRow) {
		return 0<=nextColumn && nextColumn<bound &&
					0<=nextRow && nextRow<bound &&
					!visited[nextRow][nextColumn];
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		bound = Integer.parseInt(reader.readLine().trim());
		heights = new int[bound][bound];
		visited = new boolean[bound][bound];
		minimumFuel = Integer.MAX_VALUE;
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++)
				heights[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
	}
}
