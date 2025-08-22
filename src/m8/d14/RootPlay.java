package m8.d14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * [문제 풀이]
 * 1. number가 2보다 큰 동안 반복
 * 2. number의 제곱근 구함 -> 제곱근 값을 long 형으로 형변환(nextNumber)
 * 	2.1. 형 변환한 값 제곱 값이 number와 동일한지 확인
 * 	2.2. 동일하다면 해당 값을 number로 입력
 * 3. 동일하지 않다면? number 1 증가
 */
public class RootPlay {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static long number;
	private static int count;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			makeTwo();
			builder.append("#").append(testCase)
					.append(" ").append(count).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static void makeTwo() {
		int depth = 0;
		while (number > 2) {
			long nextNumber = (long)Math.sqrt(number);
			if (nextNumber*nextNumber == number) { 
				number = nextNumber;
				depth++;
			} else {
				nextNumber++;
				nextNumber *= nextNumber;
				depth += nextNumber-number;
				number = nextNumber;
			}
		}
		count = depth;
	}
	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}
	private static void initialize() throws IOException {
		number = Long.parseLong(reader.readLine().trim());
		count = 0;
	}
}
