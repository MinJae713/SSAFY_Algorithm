package homeworks.m9.d16;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * <필요 수식 정리>
 * 1. a^p%p=a%p -> a*a^(p-2)%p=a%p
 * 2. (a!b!)%p = (a!%p * b!%p)%p
 * 3. nCr%p = (n!/(r!(n-r)!))%p = (n!%p/(r!(n-r)!)%p)%p
 * 	3.1. = (n!%p/(r!%p*(n-r)!%p)%p)%p --> 2번 식
 * 4. a!%p = ((a-1)!*a)%p = (a-1)!%p*a%p = (a-1)!%p*a
 * 	4.1. 단, a는 p보다 작은 경우라면 위 식 성립
 * 5. (n!%p/{(r!%p*(n-r)!%p)%p})%p -> (n!%p*{(r!%p*(n-r)!%p)%p}^p-2)%p
 * 	5.1. 페르마의 소정리 -> (a^p-2)%p = (a^-1)%p
 * 	5.2. r!%p*(n-r)!%p -> 이 식이 a로 치환된 것
 * 
 * <logic>
 * 1. nCr의 n의 해당하는 수: 1~n까지의 각 팩토리얼을 
 * 	   구한 값에서 1234567891을 나눈 나머지 입력 (factorialMemo)
 * 2. factorialMemo의 n번째 위치 값 입력 (upNumber)
 * 	2.1. n!%p에 해당
 * 3. factorialMemo의 r번째 위치 값 입력 (downRNumber)
 * 	3.1. r!%p에 해당
 * 4. factorialMemo의 n-r번째 위치 값 입력 (downNMinusRNumber)
 * 	4.1. (n-r)!%p에 해당
 * 5. downRNumber와 downNMinusRNumber 곱함 (multiple)
 * 	5.1. r!%p*(n-r)!%p
 * 6. multiple에 1234567891-2를 제곱한 값 계산 (powResult)
 * 	6.1. getPow(): 두 수를 받아서 제곱 연산
 * 	6.2. number: 제곱할 수, pow: 얼마나 제곱할 지
 * 	6.3. pow가 1이면 number 반환
 * 	6.4. number와 pow/2를 파라미터로 getPow() 재귀호출
 * 		6.4.1. 호출 결과는 result에 입력
 * 	6.5. result에 result 곱셈
 * 	6.6. pow가 홀수라면? result에 number 곱셈
 * 7. powResult 값과 upNumber 곱셈, 곱셈 결과를 1234567891로 나눈 나머지 구함
 * 
 * -> 이해를 포기했습니다... 나머지에 나머지에 나머지 연산이 이해가 안되서
 * 	블로그랑 GPT 보면서 겨우 했네요..... 코드는 제가 쳤으나 제가 코딩한게 아닌 느낌...
 * -> 이 문제를 진짜 이해하고 푸신 분 있다면.... 존경합니다 ㄹㅇ
 */
public class Combination {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int number;
	private static int round;
	private static final int MOD = 1234567891;
	private static long[] factorialMemo;

	public static void main(String[] args) throws IOException {
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			long upNumber = factorialMemo[number];
			long downRNumber = factorialMemo[round];
			long downNMinusRNumber = factorialMemo[number-round];
			long multiple = (downRNumber*downNMinusRNumber)%MOD;
			long powResult = getPow(multiple, MOD-2);
			
			builder.append("#").append(testCase).append(" ").
					append((upNumber*powResult)%MOD).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static long getPow(long number, int pow) {
		if (pow == 1) return number;
		long next = getPow(number, pow/2);
		long result = (next*next)%MOD;
		if (pow%2 == 1)
			return (result*number)%MOD;
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
		factorialMemo = new long[number+1];
		factorialMemo[1] = 1;
		for (int index=2; index<=number; index++)
			factorialMemo[index] = (factorialMemo[index-1]*index)%MOD;
	}
}
