package homeworks.m9.d2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 각 위치 별 꿀의 양 입력 받을 때 -> 각 위치~벌통 범위 내에서 채취할 수 있는 최대 수익 계산
 * 	1.1. 선택된 범위 내에서 벌통을 선택하는 부분 집합 생성
 * 	1.2. 부분 집합 생성 후, 선택한 벌통 내 벌꿀 양 합산이 
 * 		  최대 양(limiitAmount)을 넘어간다면 continue
 * 	1.3. 부분 집합 내 벌통의 벌꿀 양 각각 제곱한 값 합산
 * 	1.4. 최대 수익을 위치별 최대 수익 배열(maxIncomeOfPosition)에 입력
 * 2. 0~마지막 벌통(범위^2-1)- 중 2개의 벌통을 선택하여 벌통 조합 생성
 * 	2.1. 첫 번째 벌통 범위: 0~범위^2-벌통 범위
 * 	2.2. 첫 번째 벌통 위치(honeyBox1)의 열 위치+벌통 범위가 범위(bound)를 넘어가면 continue
 * 	2.3. 두 번째 벌통 범위: 첫 번째 벌통 위치+벌통 범위(honeyBound)~범위^2-벌통 범위
 * 	2.4. 두 번째로 선택한 벌통 위치의 열 위치+벌통 범위가 범위(bound)를 넘어가면 continue
 * 3. 선택한 두 범위 값 합산, 합산한 값들 중 최대값 저장(maxIncome)
 */
public class HoneyMyHoney {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int bound;
	private static int honeyBound;
	private static int limiitAmount;
	private static int[][] honeyMap;
	private static int[][] maxIncomeOfPosition;
	private static int maxIncome;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			setMaxIncomeOfPosition();
			for (int honeyBox1=0; honeyBox1<=bound*bound-honeyBound; honeyBox1++) {
				int[] onePosition = getPositionOfNumber(honeyBox1);
				if (onePosition[1]+honeyBound-1 >= bound) continue;
				for (int honeyBox2=honeyBox1+honeyBound; honeyBox2<=bound*bound-honeyBound; honeyBox2++) {
					int[] twoPosition = getPositionOfNumber(honeyBox2);
					if (twoPosition[1]+honeyBound-1 >= bound) continue;
					maxIncome = Math.max(maxIncome, 
							maxIncomeOfPosition[onePosition[0]][onePosition[1]]+
							maxIncomeOfPosition[twoPosition[0]][twoPosition[1]]);
				}
			}
			builder.append("#").append(testCase).append(" ").
					append(maxIncome).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	
	private static int[] getPositionOfNumber(int number) {
		int row = number/bound;
		int column = number-row*bound;
		return new int[] {row, column};
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		bound = Integer.parseInt(tokenizer.nextToken());
		honeyBound = Integer.parseInt(tokenizer.nextToken());
		limiitAmount = Integer.parseInt(tokenizer.nextToken());
		honeyMap = new int[bound][bound];
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++) 
				honeyMap[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
		maxIncome = 0;
	}

	private static void setMaxIncomeOfPosition() {
		maxIncomeOfPosition = new int[bound][bound-honeyBound+1];
		for (int row=0; row<bound; row++) 
			for (int column=0; column<=bound-honeyBound; column++)
				maxIncomeOfPosition[row][column] = setOnePositionMax(row, column, new boolean[honeyBound], 0);
	}

	private static int setOnePositionMax(int row, int column, 
			boolean[] contained, int count) {
		int onePositionMax = 0;
		if (count == honeyBound) {
			int totalSum = 0;
			int powTotalSum = 0;
			for (int index=0; index<honeyBound; index++) {
				if (!contained[index]) continue;
				totalSum += honeyMap[row][column+index];
				powTotalSum += honeyMap[row][column+index]*
						honeyMap[row][column+index];
			}
			if (totalSum > limiitAmount) return -1;
			return powTotalSum;
		}
		contained[count] = true;
		onePositionMax = setOnePositionMax(row, column, contained, count+1);
		contained[count] = false;
		onePositionMax = Math.max(onePositionMax, 
				setOnePositionMax(row, column, contained, count+1));
		return onePositionMax;
	}
}
