package m8.d12;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제풀이]
 * <변수 및 함수>
 * 1. reader: 값 입력 시 사용
 * 2. builder: 결과 값 저장 시 사용
 * 3. tokenizer: 입력 받은 값 공백 기준으로 받아올 때 사용
 * 4. dishCount: 음식의 수
 * 5. limit: 제한 칼로리
 * 6. values: 음식 번호별 음식 가치
 * 7. calories: 음식 번호별 음식의 칼로리
 * 8. contained: 음식의 포함 여부(0/1 각각 포함/미포함)
 * 9. max: 음식 조합의 최대 가치
 *
 * 1. initializeBasicAndgetTestCaseCount(): reader, builder 값 초기화, 테스트 케이스 수 반환
 * 2. initialize(): 변수 입력 및 초기화
 * 2. initializeContained(count): contained 배열 초기화 - count 갯수 만큼 마지막부터 1 입력
 * 3. getPossiblePermutation(count): 음식 갯수 별 가능한 조합 생성
 * 4. nextPermutation(): 다음 조합 생성, 만들 수 있으면 true, 아니면 false 반환
 * 5. swap(a, b): contained 내 a, b 위치 요소 교환
 * 
 * <로직>
 * 1. 변수 초기화 및 값 입력
 * 2. 음식의 갯수(count)만큼 반복
 * 	2.1. contained 배열 초기화
 * 		2.1.1. 크기는 dishCount, 마지막 위치부터 count 길이만큼 1로 초기화
 * 	2.2. 음식 갯수를 파라미터로 조합 생성
 * 		2.2.1. 조합을 만들 수 있는 동안 반복
 * 			2.2.1.1. 만들어진 조합에 있는 음식들의 칼로리, 가치 총합 계산
 * 			2.2.1.2. 칼로리가 limit보다 넘어갔다면 다음 반복 수행
 * 			2.2.1.3. limit보다 작으면 max와 비교 -> max 값 변경
 * 		2.2.2. 만들 수 있는 조합이 있는지 확인?
 * 			2.2.2.1. contained 배열의 마지막 위치부터 현재 위치(top) 
 *                   값이 이전 위치 값보다 큰 위치 확인
 *          	2.2.2.1.1. top이 0이 되면 false 반환
 *          2.2.2.2. contained 배열의 마지막 위치부터 탐색해서
 *          		 처음으로 1이 나오는 위치 확인 (to)
 *          2.2.2.3. top-1 위치와 to 위치 swap
 *          2.2.2.4. top~배열 마지막 위치까지 오름차순 정렬
 *          2.2.2.5. true 반환
 * 3. max 값 출력
 */
public class Hamburger {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int dishCount;
	private static int limit;
	private static int[] values;
	private static int[] calories;
	private static int max;
	private static byte[] contained;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeBasicAndgetTestCaseCount();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			for (int count=1; count<=dishCount; count++) {
				initializeContained(count);
				getPossiblePermutation(count);
			}
			builder.append("#").append(testCase).append(" ").append(max).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int initializeBasicAndgetTestCaseCount() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}
	private static void initializeContained(int count) {
		contained = new byte[dishCount];
		for (int index=dishCount-1; index>=dishCount-count; index--)
			contained[index] = 1;
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		dishCount = Integer.parseInt(tokenizer.nextToken());
		limit = Integer.parseInt(tokenizer.nextToken());
		values = new int[dishCount];
		calories = new int[dishCount];
		for (int index=0; index<dishCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			values[index] = Integer.parseInt(tokenizer.nextToken());
			calories[index] = Integer.parseInt(tokenizer.nextToken());
		}
		max = Integer.MIN_VALUE;
	}
	private static void getPossiblePermutation(int count) {
		do {
			int totalCalorie = 0;
			int totalValue = 0;
			for (int index=0; index<dishCount; index++)
				if (contained[index] == 1) {
					totalCalorie += calories[index];
					totalValue += values[index];
				}
			if (totalCalorie > limit) continue;
			max = Math.max(max, totalValue);
		} while (nextPermutation());
	}

	private static boolean nextPermutation() {
		int top = dishCount-1;
		while (top > 0 && contained[top-1] >= contained[top]) top--;
		if (top == 0) return false;
		int to = dishCount-1;
		while (contained[top-1] >= contained[to]) to--;
		swap(top-1, to);
		to = dishCount-1;
		while (top < to) swap(top++, to--);
		return true;
	}

	private static void swap(int from, int to) {
		byte temp = contained[from];
		contained[from] = contained[to];
		contained[to] = temp;
	}
}
