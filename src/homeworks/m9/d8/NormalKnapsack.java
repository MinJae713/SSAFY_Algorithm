package homeworks.m9.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - maxValueMemo: 물건의 개수와 물건의 무게가 주어질 때 그에 해당하는 최대 가치를 저장하는 배열
 * 1. getMaxValue(): maxValueMemo에 값을 넣는 메소드
 * 	1.1. 파라미터는 물건의 수(count), 제한 무게(limit)
 * 2. count가 0이면? 0 반환
 * 3. maxValueMemo의 count-1위치 limit위치 값이 초기값이면?
 * 	3.1. count-1, limit를 파라미터로 getMaxValue 재귀 호출
 * 	3.2. 호출한 결과를 maxValueMemo의 count-1위치 limit위치에 입력
 * 4. limit가 count-1위치 물건의 부피 이상이고,
 * 	  maxValueMemo의 count-1위치 limit-(count-1위치 물건의 부피 위치)가 초기값이면?
 * 	4.1. count-1, limit-(count-1위치 물건의 부피 위치)를 파라미터로 getMaxPrice 재귀 호출
 * 	4.2. 호출한 결과를 maxValueMemo의 count-1위치 limit-(count-1위치 물건의 부피 위치)에 입력
 * 5. 3, 4의 결과 입력 (각각 notContained, contained)
 * 	5.1. contained는 현재 limit가 포함하려는 물건의 무게보다 작다면 0으로 지정
 * 	5.2. 현재 limit보다 크다면 maxValueMemo의 count-1위치 limit-(count-1위치 물건의 부피 위치) 
 * 		  값과 count 위치의 물건 가치 합산한 결과를 입력
 * 6. notContained와 contained 중, 더 큰 값 반환
 */
public class NormalKnapsack {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int count;
	private static int limit;
	private static int[] weights;
	private static int[] values;
	private static int[][] maxValueMemo;

	public static void main(String[] args) throws IOException {
		initialize();
		System.out.println(getMaxValue(count, limit));
		reader.close();
	}

	private static int getMaxValue(int count, int limit) {
		if (count == 0) return maxValueMemo[count][limit];
		if (maxValueMemo[count-1][limit] == 0)
			maxValueMemo[count-1][limit] = getMaxValue(count-1, limit);
		if (limit >= weights[count] && 
				maxValueMemo[count-1][limit-weights[count]] == 0)
			maxValueMemo[count-1][limit-weights[count]] = 
				getMaxValue(count-1, limit-weights[count]);
		int notContained = maxValueMemo[count-1][limit];
		int contained = limit >= weights[count] ? 
				maxValueMemo[count-1][limit-weights[count]]+values[count] : 0;
		return Math.max(notContained, contained);
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		count = Integer.parseInt(tokenizer.nextToken());
		limit = Integer.parseInt(tokenizer.nextToken());
		weights = new int[count+1];
		values = new int[count+1];
		maxValueMemo = new int[count+1][limit+1];
		for (int index=1; index<=count; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			weights[index] = Integer.parseInt(tokenizer.nextToken());
			values[index] = Integer.parseInt(tokenizer.nextToken());
		}
	}

}
