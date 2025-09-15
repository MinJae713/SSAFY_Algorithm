package testReview.testBasic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

public class Test4_유민재 {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int rowCount;
	private static int columnCount;
	private static int maxLength;
	private static char[][] room;
	private static int[][] timeMemo;
	private static int startX;
	private static int startY;
	private static int endX;
	private static int endY;
	private static int minimumTime;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		Queue<int[]> queue = new ArrayDeque<int[]>();
		queue.offer(new int[] {startY, startX});
		timeMemo[startY][startX] = 0;
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			if (current[0] == endY && current[1] == endX) {
				minimumTime = Math.min(minimumTime, timeMemo[current[0]][current[1]]);
				continue;
			}
			for (int index=0; index<4; index++) {
				for (int length=1; length<=maxLength; length++) {
					int nextY = current[0]+delta[index][0]*length;
					int nextX = current[1]+delta[index][1]*length;
					if (!checkNext(nextY, nextX)) continue;
					int nextTime = timeMemo[current[0]][current[1]]+1;
					if (nextTime < timeMemo[nextY][nextX]) {
						timeMemo[nextY][nextX] = nextTime;
						queue.offer(new int[] {nextY, nextX});
					}
				}
			}
		}
		System.out.println(minimumTime == Integer.MAX_VALUE ? -1 : minimumTime);
		reader.close();
	}
	private static boolean checkNext(int y, int x) {
		return 0<=y && y<rowCount && 
				0<=x && x<columnCount && 
				room[y][x] != '#';
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		tokenizer = new StringTokenizer(reader.readLine().trim());
		rowCount = Integer.parseInt(tokenizer.nextToken());
		maxLength = Integer.parseInt(tokenizer.nextToken());
		columnCount = Integer.parseInt(tokenizer.nextToken());
		room = new char[rowCount][columnCount];
		for (int row=0; row<rowCount; row++)
			room[row] = reader.readLine().trim().toCharArray();
		timeMemo = new int[rowCount][columnCount];
		for (int row=0; row<rowCount; row++)
			Arrays.fill(timeMemo[row], Integer.MAX_VALUE);
		tokenizer = new StringTokenizer(reader.readLine().trim());
		startY = Integer.parseInt(tokenizer.nextToken())-1;
		startX = Integer.parseInt(tokenizer.nextToken())-1;
		endY = Integer.parseInt(tokenizer.nextToken())-1;
		endX = Integer.parseInt(tokenizer.nextToken())-1;
		minimumTime = Integer.MAX_VALUE;
	}

}
