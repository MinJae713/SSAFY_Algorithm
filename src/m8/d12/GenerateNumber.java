package m8.d12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 테스트 케이스 입력, 케이스 수 만큼 반복
 * 2. 숫자의 수 입력, 숫자(numbers) 배열 및 연산자(operator) 배열 초기화
 * 3. 연산자 입력, operator 배열에 입력받은 정보 바탕으로 값 초기화
 * 	3.1. 0:+, -:1, *:2, /:3
 * 	3.2. 입력받은 각 연산자의 수 만큼 operator 배열 각 위치에 operator 해당 값 입력
 * 	3.3. ex) +가 2개라면 operator의 0, 1위치(index)에 0 입력, 입력할 때 마다 값 증가
 * 4. 숫자 입력
 * 5. operator 배열의 값들로 만들 수 있는 순열 생성 (permuteOperator)
 * 	5.1. 다음 만들 수 있는 순열이 있는 경우(nextPermutate) 반복
 * 		5.1.1. operator 배열 마지막 요소부터 1씩 감소하면서 
 * 				 현재 위치보다 다음 위치가 큰 위치 탐색 (top)
 * 		5.1.2. top이 0이면 false 반환
 * 		5.1.3. operator 배열 마지막 요소부터 1씩 감소하면서
 * 				 top 위치보다 큰 값들 중 가장 작은 값 탐색 (to)
 * 		5.1.4. top-1, to 위치 swap
 * 		5.1.5. top~배열 마지막 위치 정렬
 * 	5.2. 순열 생성 이후, operator 배열의 연산자 순서대로 numbers 값들 연산
 * 	5.3. 연산 결과 최댓값, 최솟값 지정
 * 6. 최댓값 - 최솟값 차이 계산, 계산 결과를 builder에 추가
 */
public class GenerateNumber {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int numberCount;
	private static int[] operator;
	private static int[] numbers;
	private static int max;
	private static int min;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			permuteOperator();
			builder.append("#").append(testCase)
					.append(" ").append(max-min).append("\n");
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
		numberCount = Integer.parseInt(reader.readLine().trim());
		numbers = new int[numberCount];
		operator = new int[numberCount-1];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		max = Integer.MIN_VALUE;
		min = Integer.MAX_VALUE;
		int operatorType = 0;
		for (int operatorIndex=0; operatorIndex<numberCount-1;) {
			int operatorCount = Integer.parseInt(tokenizer.nextToken());
			for (int index=0; index<operatorCount; index++)
				operator[operatorIndex++] = operatorType;
			operatorType++;
		}
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<numberCount; index++)
			numbers[index] = Integer.parseInt(tokenizer.nextToken());
	}
	private static void permuteOperator() {
		do {
			int total = numbers[0];
			for (int index=0; index<numberCount-1; index++) {
				switch (operator[index]) {
				case 0: {
					total += numbers[index+1];
					break;
				}
				case 1: {
					total -= numbers[index+1];
					break;
				}
				case 2: {
					total *= numbers[index+1];
					break;
				}
				case 3: {
					total /= numbers[index+1];
					break;
				}
				}
			}
			max = Math.max(max, total);
			min = Math.min(min, total);
		} while (nextPermutate());
	}
	private static boolean nextPermutate() {
		int top = numberCount-2;
		while (top > 0 && operator[top-1] >= operator[top])
			top--;
		if (top == 0) return false;
		int to = numberCount-2;
		while (operator[top-1] >= operator[to])
			to--;
		swap(top-1, to);
		to = numberCount-2;
		while (top < to) swap(top++, to--);
		return true;
	}
	private static void swap(int from, int to) {
		int temp = operator[from];
		operator[from] = operator[to];
		operator[to] = temp;
	}
}
