package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class FlyCatch {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int fieldBound;
	private static int catchBound;
	private static int[][] totalSum;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			builder.append("#").append(testCase).append(" ").
						append(getMaxCatchedCount()).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int getMaxCatchedCount() {
		int maxCatched = 0;
		for (int row=catchBound-1; row<fieldBound; row++)
			for (int column=catchBound-1; column<fieldBound; column++) {
				int areaSum = totalSum[row][column];
				if (row == catchBound-1 && column == catchBound-1);
				else if (row == catchBound-1)
					areaSum -= totalSum[row][column-catchBound];
				else if (column == catchBound-1)
					areaSum -= totalSum[row-catchBound][column];
				else areaSum -= totalSum[row][column-catchBound]+
									totalSum[row-catchBound][column]-
									totalSum[row-catchBound][column-catchBound];
				maxCatched = Math.max(maxCatched, areaSum);
			}
		return maxCatched;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		fieldBound = Integer.parseInt(tokenizer.nextToken());
		catchBound = Integer.parseInt(tokenizer.nextToken());
		totalSum = new int[fieldBound][fieldBound];
		for (int row=0; row<fieldBound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<fieldBound; column++) {
				int oneArea = Integer.parseInt(tokenizer.nextToken());
				if (row == 0 && column == 0)
					totalSum[row][column] = oneArea;
				else if (row == 0)
					totalSum[row][column] = oneArea+totalSum[row][column-1];
				else if (column == 0)
					totalSum[row][column] = oneArea+totalSum[row-1][column];
				else
					totalSum[row][column] = oneArea+totalSum[row-1][column]+
													totalSum[row][column-1]-totalSum[row-1][column-1];
			}
		}
	}
}
