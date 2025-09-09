package homeworks.m9.d9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - maxLengthMemo: 각 위치 별 최장 부분 증가 수열의 길이 저장
 * 	 ㄴ 초기 모든 위치 값 1로 초기화 
 * 1. 모든 위치에 대해 반복 (targetIndex)
 * 2. 처음 위치~targetIndex-1 위치까지 반복(fromIndex)
 * 	2.1. fromIndex 위치 값이 targetIndex 위치 값보다 크다면 continue
 * 	2.2. fromIndex에 들어오는 위치 중, maxLengthMemo 
 * 		  값이 가장 큰 위치 저장(maxLength)
 * 		2.2.1. 초기 maxLength는 1로 입력(자기 자신만 구성될 때)
 * 3. maxLength+1 값을 maxLengthMemo의 targetIndex 위치에 입력
 * 4. 현재 최장 길이(maxLengthResult)와 현재 지정된 
 * 	  maxLengthMemo 값과 비교, 더 큰 값 입력
 */
public class LIS {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int length;
	private static int[] elements;
	private static int[] maxLengthMemo;
	private static int maxLengthResult;
	
	public static void main(String[] args) throws IOException {
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int fromIndex=0; fromIndex<length; fromIndex++) {
				int maxLength = 0;
				for (int targetIndex=0; targetIndex<fromIndex; targetIndex++) {
					if (elements[targetIndex] > elements[fromIndex]) continue;
					maxLength = Math.max(maxLength, maxLengthMemo[targetIndex]);
				}
				// from 이전의 위치들 중 LIS의 길이가 가장 긴 위치의 LIS 값 선택
				// 단, 직전 위치가 from 위치보다 값이 더 작아야 함
				// 위에서 뽑한 가장 긴 위치의 LIS 값+1한 값을 from 위치 LIS 값으로 입력
				maxLengthMemo[fromIndex] = maxLength+1;
				maxLengthResult = Math.max(maxLengthResult, maxLengthMemo[fromIndex]);
			}
			builder.append("#").append(testCase).append(" ").
					append(maxLengthResult).append("\n");
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
		length = Integer.parseInt(reader.readLine().trim());
		elements = new int[length];
		maxLengthMemo = new int[length];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<length; index++)
			elements[index] = Integer.parseInt(tokenizer.nextToken());
		Arrays.fill(maxLengthMemo, 1);
		maxLengthResult = 0;
	}
}
