package swea;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class FlyCatch {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int fieldBound;
	private static int catchBound;
	private static int[][] field, totalSum;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("resources/input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			for (int row=0; row<fieldBound; row++) {
				for (int column=0; column<fieldBound; column++)
					System.out.printf("%-3d", field[row][column]);
				System.out.println();
			}
			System.out.println();
			for (int row=0; row<fieldBound; row++) {
				for (int column=0; column<fieldBound; column++)
					System.out.printf("%-4d", totalSum[row][column]);
				System.out.println();
			}	
		}
		
		reader.close();
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
		field = new int[fieldBound][fieldBound];
		for (int row=0; row<fieldBound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<fieldBound; column++) {
				field[row][column] = Integer.parseInt(tokenizer.nextToken());
				if (row == 0 && column == 0)
					totalSum[row][column] = field[row][column];
				else if (row == 0)
					totalSum[row][column] = field[row][column]+totalSum[row][column-1];
				else if (column == 0)
					totalSum[row][column] = field[row][column]+totalSum[row-1][column];
				else
					totalSum[row][column] = field[row][column]+totalSum[row-1][column]+totalSum[row][column-1]-totalSum[row-1][column-1];
			}
		}
	}

}
