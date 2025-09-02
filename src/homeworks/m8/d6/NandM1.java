package homeworks.m8.d6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * <변수 및 함수 초기화>
 * 	1. N 및 M 입력
 * 	2. numbers: 수열 내 숫자를 담는 M 크기의 배열
 * 	3. contained: 1~N 범위 내 숫자들이 numbers에 포함되었는지 여부를 나타내는 배열
 * 		3.1. N까지 표현해야 하므로, N+1 크기로 생성
 * 	4. setNumbers(index): numbers의 index 위치부터 M-1 위치 내에서 순열을 만드는 함수
 * <로직 구현>
 * 	1. setNumbers 함수 호출 -> 파라미터는 0으로 지정
 * 		1.1. 처음 위치에서 M-1 위치 내에서 순열 생성
 * 	2. setNumbers 로직 구현
 * 		2.1. index 값이 M이 된 경우 -> 완성된 배열 요소 반환 및 메소드 실행 종료
 * 		2.2. 그 이외의 경우, 1~N 범위 내에서 반복하면서 아래 내용 실행
 * 			2.2.1. number: 1~N 범위 내 값 저장
 * 			2.2.2. 저장된 number 값이 numbers 배열에 포함되어 있는지 확인
 * 			2.2.3. 포함 된 경우는 continue
 * 			2.2.4. 미포함 된 경우
 * 				2.2.4.1. numbers의 index 위치에 number 값 저장
 * 				2.2.4.2. contained의 number 위치 값 true 저장 (number 값은 포함됨)
 * 				2.2.4.3. setNumbers 메소드 재귀호출 -> 파라미터는 index+1
 * 				2.2.4.4. contained의 number 위치 값 false 저장 (number 값은 미포함됨)
 */
public class NandM1 {
	public static void main(String[] args) throws IOException {
		builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		// <변수 초기화>
		// 1. N 및 M 입력
		StringTokenizer NM = new StringTokenizer(reader.readLine());
		N = Integer.parseInt(NM.nextToken());
		M = Integer.parseInt(NM.nextToken());
		numbers = new int[M];
		contained = new boolean[N+1];
		// <로직 구현>
		// 1. setNumbers 함수 호출 -> 파라미터는 0으로 지정
		// 1.1. 처음 위치에서 M-1 위치 내에서 순열 생성
		setNumbers(0);
		System.out.print(builder);
		reader.close();
	}
	
	static StringBuilder builder;
	static int N;
	static int M;
	// 2. numbers: 수열 내 숫자를 담는 M 크기의 배열
	static int[] numbers;
	// 3. contained: 1~N 범위 내 숫자들이 numbers에 포함되었는지 여부를 나타내는 배열
	// 3.1. N까지 표현해야 하므로, N+1 크기로 생성
	static boolean[] contained;
	// 4. setNumbers(index): numbers의 index 위치부터 M-1 위치 내에서 순열을 만드는 함수
	static void setNumbers(int index) {
		// 2. setNumbers 로직 구현
		// 2.1. index 값이 M이 된 경우 -> 완성된 배열 요소 반환 및 메소드 실행 종료
		if (index == M) {
			for (int m=0; m<M; m++) 
				builder.append(numbers[m]).append(" ");
			builder.append("\n");
			return;
		}
		// 2.2. 그 이외의 경우, 1~N 범위 내에서 반복하면서 아래 내용 실행
		for (int number=1; number<=N; number++) {
			// 2.2.1. number: 1~N 범위 내 값 저장
			// 2.2.2. 저장된 number 값이 numbers 배열에 포함되어 있는지 확인
			// 2.2.3. 포함 된 경우는 continue
			if (contained[number]) continue;
			// 2.2.4. 미포함 된 경우
			// 2.2.4.1. numbers의 index 위치에 number 값 저장
			numbers[index] = number;
			// 2.2.4.2. contained의 number 위치 값 true 저장 (number 값은 포함됨)
			contained[number] = true;
			// 2.2.4.3. setNumbers 메소드 재귀호출 -> 파라미터는 index+1
			setNumbers(index+1);
			// 2.2.4.4. contained의 number 위치 값 false 저장 (number 값은 미포함됨)
			contained[number] = false;
		}
	}
}
