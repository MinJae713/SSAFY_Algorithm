package homeworks.m9.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - maxPriceMemo: 물건의 개수와 물건의 부피가 주어질 때 그에 해당하는 최대 가치를 저장하는 배열
 * 1. getMaxPrice(): maxPriceMemo에 값을 넣는 메소드
 * 	1.1. 파라미터는 물건의 수(count), 제한 부피(limit)
 * 2. count가 0이면? 0 반환
 * 3. maxPriceMemo의 count-1위치 limit위치 값이 초기값이면?
 * 	3.1. count-1, limit를 파라미터로 getMaxPrice 재귀 호출
 * 	3.2. 호출한 결과를 maxPriceMemo의 count-1위치 limit위치에 입력
 * 4. limit가 count-1위치 물건의 부피 이상이고,
 * 	  maxPriceMemo의 count-1위치 limit-(count-1위치 물건의 부피 위치)가 초기값이면?
 * 	4.1. count-1, limit-(count-1위치 물건의 부피 위치)를 파라미터로 getMaxPrice 재귀 호출
 * 	4.2. 호출한 결과를 maxPriceMemo의 count-1위치 limit-(count-1위치 물건의 부피 위치)에 입력
 * 5. limit가 count-1위치 물건의 부피보다 작다면 maxPriceMemo의 count-1위치 limit위치 값 반환
 * 6. 아니라면 maxPriceMemo의 count-1위치 limit위치 값과 
 * 	  maxPriceMemo의 count-1위치 limit-(count-1위치 물건의 부피 위치)위치 값+
 * 	  count-1 위치의 물건 가치한 값 중 더 큰 값 반환
 */
public class Knapsack {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int count;
	private static int limit;
	private static int[] weights;
	private static int[] prices;
	private static int[][] maxPriceMemo;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initalizeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initalize();
			// logic
			builder.append("#").append(testCase).append(" ").
					append(getMaxPrice(count, limit)).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int getMaxPrice(int count, int limit) {
		if (count == 0) return maxPriceMemo[count][limit];
		if (maxPriceMemo[count-1][limit] == 0)
			maxPriceMemo[count-1][limit] = getMaxPrice(count-1, limit);
		if (limit>=weights[count] && maxPriceMemo[count-1][limit-weights[count]] == 0)
			maxPriceMemo[count-1][limit-weights[count]] = 
				getMaxPrice(count-1, limit-weights[count]);
		int notContain = maxPriceMemo[count-1][limit];
		int contain = 0;
		if (limit>=weights[count]) 
			contain = maxPriceMemo[count-1][limit-weights[count]]+prices[count];
		return Math.max(notContain, contain);
	}

	private static int initalizeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}

	private static void initalize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		count = Integer.parseInt(tokenizer.nextToken());
		limit = Integer.parseInt(tokenizer.nextToken());
		weights = new int[count+1];
		prices = new int[count+1];
		for (int index=1; index<=count; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			weights[index] = Integer.parseInt(tokenizer.nextToken());
			prices[index] = Integer.parseInt(tokenizer.nextToken());
		}
		maxPriceMemo = new int[count+1][limit+1];
	}

}
