package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 건물 범위^2 크기로 시작점부터 해당 위치까지의 최소 연료 
 * 	소모비용을 저장하는 배열(minimumCost) 생성
 * 	1.1. 각 위치 가장 큰 값으로 지정
 * 2. 첫 위치 연료 소모비용 0 지정
 * 3. 범위^2 만큼 반복
 * 4. minimumCost에서 최소 연료 소모비용이 가장 작은 행(row), 열(column) 반환
 * 	4.1. 이미 방문한 위치라면 continue
 * 5. 해당 행 및 열이 마지막 위치라면? 반복 종료(break)
 * 6. 해당 행 및 열 방문 처리(visited)
 * 7. 해당 위치의 4방향에 대해 반복(nextColumn, Row)
 * 8. 다음 위치가 범위 밖이거나, 이미 방문했다면 continue
 * 9. 현재 위치와 다음 위치간 연료 소모 비용 계산 (fuelCost)
 * 10. 다음 위치의 minimumCost보다 현재 위치(row, column) 
 * 	minimumCost+fuelCost가 더 작다면?
 * 11. 다음 위치의 minimumCost를 현재 위치 minimumCost+fuelCost로 변경
 */
public class AIRobotDijkstra {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int bound;
	private static int[][] map;
	private static int[][] minimumCost; // 시작 위치(0, 0)에서 현재 위치까지 최소 연료 소모 비용 저장
	private static boolean[][] visited;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			minimumCost[0][0] = 0;
			for (int count=0; count<bound*bound; count++) {
				// 지도의 각 위치에서 minimumCost가 최소인 위치 반환
				int row = -1, column = -1;
				int minimum = Integer.MAX_VALUE;
				for (int rowIndex=0; rowIndex<bound; rowIndex++)
					for (int columnIndex=0; columnIndex<bound; columnIndex++) {
						if (visited[rowIndex][columnIndex]) continue;
						else if (minimumCost[rowIndex][columnIndex] < minimum) {
							minimum = minimumCost[rowIndex][columnIndex];
							row = rowIndex;
							column = columnIndex;
						}
					}
				visited[row][column] = true; // 최소 위치 방문 처리
				if (row == bound-1 && column == bound-1) break;
				// 인접 위치 중, 방문하지 않은 위치의 최소 연료 소모 비용 값을 더 작은 값으로 입력
				for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
					int nextRow = row+delta[deltaIndex][0];
					int nextColumn = column+delta[deltaIndex][1];
					if (!checkNext(nextRow, nextColumn)) continue;
					int fuelCost = getFuelCost(map[row][column], 
														map[nextRow][nextColumn]);
					if (minimumCost[nextRow][nextColumn] > 
							minimumCost[row][column]+fuelCost)
						minimumCost[nextRow][nextColumn] = 
							minimumCost[row][column]+fuelCost;
				}
			}
			
			builder.append("#").append(testCase).append(" ").
						append(minimumCost[bound-1][bound-1]).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int getFuelCost(int currentHeight, int nextHeight) {
		if (currentHeight < nextHeight)
			return Math.abs(nextHeight-currentHeight)*2;
		else if (currentHeight > nextHeight)
			return 0;
		else return 1;
	}

	private static boolean checkNext(int row, int column) {
		return 0<=row && row<bound && 
				0<=column && column<bound && 
				!visited[row][column];
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		bound = Integer.parseInt(reader.readLine().trim());
		map = new int[bound][bound];
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++)
				map[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
		minimumCost = new int[bound][bound];
		for (int row=0; row<bound; row++)
			Arrays.fill(minimumCost[row], Integer.MAX_VALUE);
		visited = new boolean[bound][bound];
	}

}
