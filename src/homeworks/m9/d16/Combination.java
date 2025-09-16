package homeworks.m9.d16;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * <필요 수식 정리>
 * 1. a^p%p=a%p -> a^(p-2)%p=a^(-1)%p
 * 2. (a!b!)%p = a!%p * b!%p
 * 3. nCr%p = (n!/(r!(n-r)!))%p = (n!*(r!(n-r)!)^(-1))%p
 * 	3.1. = n!%p * (r!(n-r)!)^(-1)%p --> 2번 식
 * 	3.2. = n!%p * (r!(n-r)!)^(p-2)%p --> 1번 식
 * 4. a!%p = ((a-1)!*a)%p = (a-1)!%p*a%p = (a-1)!%p*a
 * 	4.1. 단, a는 p보다 작은 경우라면 위 식 성립
 * 5. (r!(n-r)!)^(p-2)%p = (r!^(p-2)*(n-r)!^(p-2))%p
 * 	5.1. r!^(p-2)%p * (n-r)!^(p-2)%p
 * 	5.2. (r!%p)^(p-2) * ((n-r)!%p)^(p-2)
 * 	5.3. (r!%p * (n-r)!%p)^(p-2)
 * 
 * <logic>
 * 1. nCr의 n의 해당하는 수: 1~n까지의 각 팩토리얼을 
 * 	   구한 값에서 1234567891을 나눈 나머지 입력 (factorialMemo)
 * 2. factorialMemo의 n번째 위치 값 입력 (upNumber)
 * 3. factorialMemo의 r번째 위치 값 입력 (downRNumber)
 * 4. factorialMemo의 n-r번째 위치 값 입력 (downNMinusRNumber)
 * 5. downRNumber과 downNMinusRNumber 곱셈 (multiple)
 * 6. multiple에서 1234567889를 제곱한 값 반환 (downResult)
 * 	6.1. getPow(): 한 수를 입력받은 만큼 제곱한 값 반환
 * 		6.1.1. number: 제곱할 수, pow: 얼마나 제곱할지에 대한 수
 * 	6.2. pow가 1이라면? number 반환
 * 	6.3. number, pow/2를 파라미터로 getPow() 재귀 호출
 * 		6.3.1. 호출 결과는 result에 입력
 * 	6.4. result에 result 곱함
 * 	6.5. pow가 홀수라면?
 * 		6.5.1. result에 number 곱함
 * 	6.6. result 반환
 * 7. upNumber와 downResult 곱함
 */
public class Combination {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int number;
	private static int round;
	private static final int MOD = 1234567891;
	private static int[] factorialMemo;

	public static void main(String[] args) throws IOException {
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			int upNumber = factorialMemo[number];
			int downRNumber = factorialMemo[round];
			int downNMinusRNumber = factorialMemo[number-round];
			int multiple = (downRNumber*downNMinusRNumber)%MOD;
			System.out.println(multiple);
			int downResult = getPow(multiple, MOD-2);
			builder.append("#").append(testCase).append(" ").
					append(upNumber*downResult).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int getPow(int number, int pow) {
		if (pow == 1) return number;
		int result = getPow(number, pow/2);
		System.out.printf("%d, %d -> %d\n", number, pow, result);
		result *= result;
		if (pow%2 == 1)
			result *= number;
		return result;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		number = Integer.parseInt(tokenizer.nextToken());
		round = Integer.parseInt(tokenizer.nextToken());
		factorialMemo = new int[number+1];
		factorialMemo[1] = 1;
		for (int index=2; index<=number; index++)
			factorialMemo[index] = (factorialMemo[index-1]*index)%MOD;
	}
}
