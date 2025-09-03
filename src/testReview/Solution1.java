package testReview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Solution1 {
	static class Apple implements Comparable<Apple>{
		int x;
		int y;
		int number;
		public Apple(int x, int y, int number) {
			super();
			this.x = x;
			this.y = y;
			this.number = number;
		}
		@Override
		public int compareTo(Apple other) {
			return Integer.compare(number, other.number);
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int bound;
	private static int[][] map;
	private static Queue<Apple> applePositions;
	private static int x;
	private static int y;
	private static int direction;
	private static int minimumTurning;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			while (!applePositions.isEmpty()) {
				Apple apple = applePositions.poll();
				int nextDirection = 0;
				int diffX = apple.x - x;
				int diffY = apple.y - y;
				if (diffX > 0 && diffY < 0) {
					// 1사분면
					if (direction == 1)			nextDirection = 2;
					else if (direction == 2) 	nextDirection = 1;
					else if (direction == 3) 	nextDirection = 2;
					else if (direction == 4) 	nextDirection = 2;
				} else if (diffX < 0 && diffY < 0) {
					// 2사분면
					if (direction == 1)			nextDirection = 4;
					else if (direction == 2)	nextDirection = 1;
					else if (direction == 3) 	nextDirection = 1;
					else if (direction == 4)	nextDirection = 1;
				} else if (diffX < 0 && diffY > 0) {
					// 3사분면
					if (direction == 1)			nextDirection = 4;
					else if (direction == 2)	nextDirection = 4;
					else if (direction == 3)	nextDirection = 4;
					else if (direction == 4)	nextDirection = 3;
				} else if (diffX > 0 && diffY > 0) {
					// 4사분면
					if (direction == 1)			nextDirection = 3;
					else if (direction == 2)	nextDirection = 3;
					else if (direction == 3)	nextDirection = 2;
					else if (direction == 4)	nextDirection = 3;
				}
				minimumTurning += getTurningCount(direction, nextDirection);
				direction = nextDirection;
				x = apple.x;
				y = apple.y;
			}
			builder.append("#").append(testCase).append(" ").
					append(minimumTurning).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int getTurningCount(int direction, int nextDirection) {
		int turningCount = nextDirection-direction;
		if (turningCount < 0) turningCount += 4;
		return turningCount;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		bound = Integer.parseInt(reader.readLine().trim());
		map = new int[bound][bound];
		applePositions = new PriorityQueue<>();
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++) {
				map[row][column] = Integer.parseInt(tokenizer.nextToken());
				if (map[row][column] > 0)
					applePositions.offer(new Apple(column, row, map[row][column]));
			}
		}
		Apple first = applePositions.poll();
		x = first.x;
		y = first.y;
		direction = 3;
		minimumTurning = 1;
		// 첫 번째는 먹은 것으로 봄
	}

}
