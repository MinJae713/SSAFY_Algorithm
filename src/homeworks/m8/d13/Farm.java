package homeworks.m8.d13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * [문제 풀이]
 * 1. 농장 정보 입력
 * 2. 처음 행 부터 중간 행까지 반복
 * 	2.1. 각 열의 중간 위치-해당 행~중간 위치+해당 행 범위 안의 값 합산
 * 	2.2. 합산하는 값의 행은 해당 행 및 (농장 크기-해당 행)
 * 3. 중간 행 값 합산
 * 4. 합산 결과 출력
 */
public class Farm {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static int size;
	private static int totalIncome;
	private static int[][] farm;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			setTotalIncome();
			builder.append("#").append(testCase).append(" ").append(totalIncome).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}
	private static void initialize() throws IOException {
		size = Integer.parseInt(reader.readLine().trim());
		totalIncome = 0;
		farm = new int[size][size];
		for (int row=0; row<size; row++) {
			String oneRow = reader.readLine().trim();
			for (int column=0; column<size; column++)
				farm[row][column] = oneRow.charAt(column)-'0';
		}
	}
	private static void setTotalIncome() {
		int middle = size/2;
		for (int row=0; row<middle; row++) 
			for (int column=middle-row; column<=middle+row; column++) 
				totalIncome += farm[row][column] + farm[size-1-row][column];
		for (int column=0; column<size; column++)
			totalIncome += farm[middle][column];
	}
}
