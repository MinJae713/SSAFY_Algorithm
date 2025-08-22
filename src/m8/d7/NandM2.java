package m8.d7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제풀이]
 * <변수 및 함수>
 * 1. N, M: N개의 수 중에서 M개를 선택해서 수열 생성
 * 2. numbers: 수열에 들어가는 값을 담는 배열
 * 3. contained: numbers에 포함된 수인지 표시하는 배열
 * 4. builder: 생성된 수열 정보를 문자열로 저장
 * 5. setPermuteFrom(index): numbers의 index~N-1 범위 내에 값 지정
 * <로직>
 * 1. N, M 입력
 * 2. numbers 배열 생성 (크기는 M)
 * 3. contained 배열 생성 (크기는 N+1)
 * 4. setPermuteFrom 메소드 호출, index는 0
 * 	4.1. index 값이 M이라면? numbers 정보를 builder에 추가
 * 	4.2. index 값이 M보다 작다면? 1~N 범위 내에서 반복 (number - 범위 내 값)
 * 		4.2.1. number가 numbers에 포함되어 있다면? 다음 반복 실행
 * 		4.2.2. index 값이 0보다 크고, numbers의 index-1 위치 값이 
 * 					number보다 크다면? 다음 반복 실행
 * 		4.2.3. 위 경우가 해당되지 않는 경우
 *	  			4.2.3.1. numbers의 index 위치에 값 지정
 * 			4.2.3.2. contained의 지정한 값 해당 위치 true로 지정
 * 			4.2.3.3. index+1을 파라미터로 하여 setPermuteFrom 메소드 재귀호출
 * 			4.2.3.4. contained의 지정한 값 해당 위치 false로 지정
 * 5. builder의 값 호출
 */
public class NandM2 {

	// <변수 및 함수>
	// 1. N, M: N개의 수 중에서 M개를 선택해서 수열 생성
	static int N, M;
	// 2. numbers: 수열에 들어가는 값을 담는 배열
	static int[] numbers;
	// 3. contained: numbers에 포함된 수인지 표시하는 배열
	static boolean[] contained;
	// 4. builder: 생성된 수열 정보를 문자열로 저장
	static StringBuilder builder;
	
	// 5. setPermuteFrom(index): numbers의 index~N-1 범위 내에 값 지정
	public static void setPermuteFrom(int index) {
		// 4.1. index 값이 M이라면? numbers 정보를 builder에 추가
		if (index == M) {
			for (int m=0; m<M; m++)
				builder.append(numbers[m]).append(" ");
			builder.append("\n");
			return;
		}
		// 4.2. index 값이 M보다 작다면? 1~N 범위 내에서 반복 (number - 범위 내 값)
		for (int number=1; number<=N; number++) {
			// 4.2.1. number가 numbers에 포함되어 있다면? 다음 반복 실행
			if (contained[number]) continue;
			// 4.2.2. index 값이 0보다 크고, numbers의 index-1 위치 값이 number보다 크다면? 다음 반복 실행
			else if (index>0 && numbers[index-1]>number)
				continue;
			else {
				// 4.2.3. 위 경우가 해당되지 않는 경우
				// 4.2.3.1. numbers의 index 위치에 값 지정
				numbers[index] = number;
				// 4.2.3.2. contained의 지정한 값 해당 위치 true로 지정
				contained[number] = true;
				// 4.2.3.3. index+1을 파라미터로 하여 setPermuteFrom 메소드 재귀호출
				setPermuteFrom(index+1);
				// 4.2.3.4. contained의 지정한 값 해당 위치 false로 지정
				contained[number] = false;
			}
		}
	}
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		// <로직>
		// 1. N, M 입력
		StringTokenizer NM = new StringTokenizer(reader.readLine());
		N = Integer.parseInt(NM.nextToken());
		M = Integer.parseInt(NM.nextToken());
		// 2. numbers 배열 생성 (크기는 M)
		numbers = new int[M];
		// 3. contained 배열 생성 (크기는 N+1)
		contained = new boolean[N+1];
		// 4. setPermuteFrom 메소드 호출, index는 0
		setPermuteFrom(0);
		// 5. builder의 값 호출
		System.out.println(builder);
		reader.close();
	}
}
