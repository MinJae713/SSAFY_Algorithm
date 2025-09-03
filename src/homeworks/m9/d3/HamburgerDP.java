package homeworks.m9.d3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. getMaxValue(): 재료의 수(count)와 제한 칼로리(limit)를 받아서 
 * 				    만들 수 있는 조합의 최대 가치를 maxValue 배열에 지정 및 반환
 * 	1.1. 초기에는 입력 받은 칼로리와 입력 받은 재료의 수 입력
 * 2. 재료 수가 0보다 큰 경우 실행
 * 3. maxValue의 [count-1][limit] 위치 값이 초기값(-1)이면?
 * 	3.1. 각각 count-1, limit를 파라미터로 getMaxValue() 재귀 호출 및 결과 입력
 * 	3.2. count번째 음식을 포함하지 않는 경우임
 * 4. limit가 count번째 음식의 칼로리 이상이고, maxValue의 [count-1][limit-count번째 음식의 칼로리] 값이 초기값(-1)이면?
 * 	4.1. 각각 count-1, limit-count번째 음식의 칼로리를 파라미터로 getMaxValue() 재귀 호출 및 결과 입력
 * 	4.2. count번째 음식을 포함하는 경우임
 * 5. limit가 count번째 음식의 칼로리 이상이면 maxValue의 [count][limit] 위치에 maxValue의 [count-1][limit] 과
 * 	  maxValue의 [count-1][limit-count번째 음식의 칼로리]+count번째 음식의 가치 중 큰 값을,
 * 	   아니라면 maxValue의 [count-1][limit]를 입력
 * 6. maxValue의 [count][limit] 값 반환
 */
public class HamburgerDP {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int dishCount;
	private static int dishLimit;
	private static int[] value;
	private static int[] calorie;
	private static int[][] maxValue;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			builder.append("#").append(testCase).append(" ").
					append(getMaxValue(dishCount, dishLimit)).append("\n");
		}
		System.out.print(builder);
		reader.close();
		
	}

	private static int getMaxValue(int count, int limit) {
		if (count > 0) {
			if (maxValue[count-1][limit] == 0)
				maxValue[count-1][limit] = getMaxValue(count-1, limit);
			if (limit>=calorie[count] && maxValue[count-1][limit-calorie[count]] == 0)
				maxValue[count-1][limit-calorie[count]] = getMaxValue(count-1, limit-calorie[count]);
			maxValue[count][limit] = limit>=calorie[count] ? Math.max(maxValue[count-1][limit], 
					maxValue[count-1][limit-calorie[count]]+value[count]) : 
						maxValue[count-1][limit];
		}
		return maxValue[count][limit];
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		dishCount = Integer.parseInt(tokenizer.nextToken());
		value = new int[dishCount+1];
		calorie = new int[dishCount+1];
		dishLimit = Integer.parseInt(tokenizer.nextToken());
		for (int index=1; index<=dishCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			value[index] = Integer.parseInt(tokenizer.nextToken());
			calorie[index] = Integer.parseInt(tokenizer.nextToken());
		}
		maxValue = new int[dishCount+1][dishLimit+1];
	}

}
