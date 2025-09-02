package homeworks.m8.d7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제풀이]
 * 1. 테스트 케이스 입력
 * 2. 재료 수, 동시 포함 불가 수 입력
 * 	2.1. 재료 조합 수 크기의 boolean 배열 생성, true로 초기화
 * 3. 동시 포함 불가 수 만큼 반복
 * 	3.1. 동시 포함 불가 두 수 입력 => 비트 마스킹 값 계산
 * 	3.2. 재료 조합의 수 만큼 반복
 * 		3.2.1. 0~재료 조합 수 안의 값이 마스킹 값과 & 연산, 마스킹 값과 같은지 확인
 * 		3.2.2. 같다면 해당 위치 값 false로 지정
 * 4. true에 해당하는 위치의 수 계산, 계산 값 출력
 */
public class SelfMadeBurger {

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		// 1. 테스트 케이스 입력
		int testCase = Integer.parseInt(reader.readLine());
		for (int testCaseCount=1; testCaseCount<=testCase; testCaseCount++) {
			StringTokenizer dishNotContained = new StringTokenizer(reader.readLine());
			// 2. 재료 수, 동시 포함 불가 수 입력
			int dish = Integer.parseInt(dishNotContained.nextToken());
			int notContained = Integer.parseInt(dishNotContained.nextToken());
			// 2.1. 재료 조합 수 크기의 boolean 배열 생성, true로 초기화
			boolean[] combinationable = new boolean[2 << dish];
			Arrays.fill(combinationable, true);
			// 3. 동시 포함 불가 수 만큼 반복
			for (int index=0; index<notContained; index++) {
				// 3.1. 동시 포함 불가 두 수 입력 => 비트 마스킹 값 계산
				StringTokenizer notContainedOneTwo = new StringTokenizer(reader.readLine());
				int notContainedOne = Integer.parseInt(notContainedOneTwo.nextToken());
				int notContainedTwo = Integer.parseInt(notContainedOneTwo.nextToken());
				int masking = (1<<(notContainedOne-1)) + (1<<(notContainedTwo-1));
				// 3.2. 재료 조합의 수 만큼 반복
				for (int combination=0; combination<1<<dish; combination++) {
					// 3.2.1. 0~재료 조합 수 안의 값이 마스킹 값과 & 연산, 마스킹 값과 같은지 확인
					if ((combination & masking) == masking)
						// 3.2.2. 같다면 해당 위치 값 false로 지정
						combinationable[combination] = false;
				}
					
			}
			// 4. true에 해당하는 위치의 수 계산, 계산 값 출력
			int total = 0;
			for (int combination=0; combination<1<<dish; combination++)
				if (combinationable[combination]) total++;
			builder.append("#").append(testCaseCount).append(" ").append(total).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

}
