package homeworks.m8.d7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Sum5 {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int size;
	private static int sumisionCount;
	private static int[][] areaSum;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		// 초기화
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		tokenizer = new StringTokenizer(reader.readLine().trim());
		size = Integer.parseInt(tokenizer.nextToken());
		sumisionCount = Integer.parseInt(tokenizer.nextToken());
		areaSum = new int[size][size];
		// 누적합 입력
		for (int row=0; row<size; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<size; column++) {
				areaSum[row][column] = Integer.parseInt(tokenizer.nextToken());
				if (row == 0 && column == 0);
				else if (row == 0) 
					areaSum[row][column] += areaSum[row][column-1];
				else if (column == 0) 
					areaSum[row][column] += areaSum[row-1][column];
				else areaSum[row][column] += areaSum[row][column-1]+
						areaSum[row-1][column]-areaSum[row-1][column-1];
			}
		}
		// 구간별 계산
		for (int index=0; index<sumisionCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int fromX = Integer.parseInt(tokenizer.nextToken())-1;
			int fromY = Integer.parseInt(tokenizer.nextToken())-1;
			int toX = Integer.parseInt(tokenizer.nextToken())-1;
			int toY = Integer.parseInt(tokenizer.nextToken())-1;
			int result = areaSum[toY][toX];
			if (fromX == 0 && fromY == 0);
			else if (fromX == 0)
				result -= areaSum[fromY-1][toX];
			else if (fromY == 0)
				result -= areaSum[toY][fromX-1];
			else result -= areaSum[fromY-1][toX]+
					areaSum[toY][fromX-1]-areaSum[fromY-1][fromX-1];
			builder.append(result).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
}
