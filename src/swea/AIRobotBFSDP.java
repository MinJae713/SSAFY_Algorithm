package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

public class AIRobotBFSDP {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int bound;
	private static int[][] map;
	private static int[][] costMemo;
	private static int minimumCost;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			Queue<int[]> queue = new ArrayDeque<int[]>();
			queue.offer(new int[] {0, 0});
			costMemo[0][0] = 0;
			while (!queue.isEmpty()) {
				int[] current = queue.poll();
				if (current[0] == bound-1 && current[1] == bound-1) {
					minimumCost = Math.min(minimumCost, costMemo[bound-1][bound-1]);
					continue;
				}
				for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
					int nextRow = current[0]+delta[deltaIndex][0];
					int nextColumn = current[1]+delta[deltaIndex][1];
					if (!checkNext(nextRow, nextColumn)) continue;
					int fuelCost = getFuelCost(map[current[0]][current[1]], 
														map[nextRow][nextColumn]);
					if (costMemo[nextRow][nextColumn] > 
							costMemo[current[0]][current[1]]+fuelCost) {
						costMemo[nextRow][nextColumn] = costMemo[current[0]][current[1]]+fuelCost;
						queue.offer(new int[] {nextRow, nextColumn});
					}
				}
			}
			builder.append("#").append(testCase).append(" ").
						append(minimumCost).append("\n");
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
				0<=column && column<bound;
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
		costMemo = new int[bound][bound];
		for (int row=0; row<bound; row++)
			Arrays.fill(costMemo[row], Integer.MAX_VALUE);
		minimumCost = Integer.MAX_VALUE;
	}

}
