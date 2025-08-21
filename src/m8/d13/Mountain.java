package m8.d13;

import java.io.IOException;
import java.util.Scanner;

/**
 * [문제 풀이]
 * 1. 두 번째 요소 - 첫 번째 요소 차이가
 * 	1.1. 양수면 upDown 상태 true, 음수면 false 
 * 	1.2. start, end에 0 입력
 * 2. 처음~마지막에서 2번째 요소에 대해 반복
 * 	2.1. 현재 위치 - 다음 위치 차이 계산 (difference)
 * 	2.2. upDown이 true면?
 * 		2.2.2. difference가 양수면? - end 1 증가
 * 		2.2.3. difference가 음수면? 
 * 			2.2.3.1. end-start 차이 계산 -> up에 입력
 * 			2.2.3.2. start에 end 입력
 * 			2.2.3.3. upDown에 false 입력
 * 	2.3. upDown이 false면?
 * 		2.3.1. difference가 음수면? - end 1 증가
 * 		2.3.2. difference가 양수면?
 * 			2.3.2.1. end-start 차이 계산 -> down에 입력
 * 			2.3.2.2. total에 up * down 값 추가
 * 			2.3.2.3. start에 end 입력
 * 			2.3.2.4. upDown에 true 입력
 * 3. updown이 false면? 
 * 	3.1. down에 end-start 차이 입력
 * 	3.2. total에 up * down 값 추가
 */
public class Mountain {
	private static Scanner scanner;
	private static StringBuilder builder;
	private static int mountainCount;
	private static int[] mountains;
	private static int total;
	
	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			calculateTotalMountains();
			builder.append("#").append(testCase).append(" ").append(total).append("\n");
		}
		System.out.print(builder);
		scanner.close();
	}

	private static int initializeTest() throws IOException {
		scanner = new Scanner(System.in);
		builder = new StringBuilder();
		return scanner.nextInt();
	}
	private static void initialize() throws IOException {
		mountainCount = scanner.nextInt();
		mountains = new int[mountainCount];
		total = 0;
		for (int index=0; index<mountainCount; index++)
			mountains[index] = scanner.nextInt();
	}
	private static void calculateTotalMountains() {
		boolean upDown = mountains[0] < mountains[1];
		int start = 0, end = 1;
		int up = 0, down = 0;
		if (upDown) up++;
		else down++;
		for (int index=1; index<mountainCount-1; index++) {
			int difference = mountains[index+1] - mountains[index];
			if (upDown) {
				if (difference > 0);
				else {
					up = end-start;
					start = end;
					upDown = false;
				}
			} else {
				if (difference < 0);
				else {
					down = end-start;
					total += up * down;
					start = end;
					upDown = true;
				}
			}
			end++;
		}
		if (!upDown) {
			down = end-start;
			total += up * down;
		}
	}
}
