package classSolution.problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Supplies {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static int[][] delta;
	private static int bound;
	private static int[][] map;
	private static boolean[][] visited;
	private static int[][] minimumTime;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			minimumTime[0][0] = 0;
			for (int index=0; index<bound*bound; index++) {
				// minimumTime이 가장 작은 지점 탐색
				int minimumWeight = Integer.MAX_VALUE;
				int currentRow = -1;
				int currentColumn = -1;
				for (int row=0; row<bound; row++)
					for (int column=0; column<bound; column++) {
						if (visited[row][column]) continue;
						else if (minimumTime[row][column] < minimumWeight) {
							minimumWeight = minimumTime[row][column];
							currentRow = row;
							currentColumn = column;
						}
					}
				visited[currentRow][currentColumn] = true;
				if (currentRow == bound-1 && currentColumn == bound-1)
					break;
				// 해당 지점에서 인접한 지점의 minimumTime 값 수정
				for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
					int nextRow = currentRow+delta[deltaIndex][0];
					int nextColumn = currentColumn+delta[deltaIndex][1];
					if (!checkNext(nextRow, nextColumn)) continue;
					else if (minimumTime[nextRow][nextColumn] > 
						minimumTime[currentRow][currentColumn]+
						map[nextRow][nextColumn]) {
						minimumTime[nextRow][nextColumn] = 
								minimumTime[currentRow][currentColumn]+
								map[nextRow][nextColumn];
					}
				}
			}
			builder.append("#").append(testCase).append(" ").
					append(minimumTime[bound-1][bound-1]).append("\n");
		}
		System.out.print(builder);
		reader.close();
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
			char[] oneLine = reader.readLine().trim().toCharArray();
			for (int column=0; column<bound; column++)
				map[row][column] = oneLine[column]-'0';
		}
		minimumTime = new int[bound][bound];
		for (int row=0; row<bound; row++)
			Arrays.fill(minimumTime[row], Integer.MAX_VALUE);
		visited = new boolean[bound][bound];
	}

}
