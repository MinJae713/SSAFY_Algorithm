package homeworks.m8.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 재료 갯수 입력
 * 2. 신 맛 배열, 쓴 맛 배열 초기화
 * 3. 각 재료별 신 맛, 쓴 맛 입력
 * 4. 각 재료별 포함 여부 배열 생성
 * 5. 재료 조합의 최소값 담는 변수 생성
 * 5. 각 조합 별 부분집합 생성
 * 	5.1. 부분집합 구성 완료시? -> 요소가 최소 하나 이상 포함된 경우 실행
 * 		5.1.1. 각 재료의 신맛, 쓴맛 계산, 차이 구함
 * 		5.1.2. 차이가 최솟값인지 확인
 * 	5.2. 부분집합 구성 미완료시?
 * 		5.2.1. 해당 요소 포함 후 다음 요소 재귀 호출
 * 		5.2.2. 해당 요소 미표함 후 다음 요소 재귀 호출
 * 6. 최솟값 변수 출력
 */
public class Dish {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int dishCount;
	private static long[] sin;
	private static long[] bitter;
	private static boolean[] contained;
	private static long minimum;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		initialize();
		setSubset(0);
		System.out.println(minimum);
		reader.close();
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		dishCount = Integer.parseInt(reader.readLine().trim());
		sin = new long[dishCount];
		bitter = new long[dishCount];
		minimum = Long.MAX_VALUE;
		contained = new boolean[dishCount];
		for (int index=0; index<dishCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			sin[index] = Long.parseLong(tokenizer.nextToken());
			bitter[index] = Long.parseLong(tokenizer.nextToken());
		}
	}
	
	private static void setSubset(int index) {
		if (index == dishCount) {
			long sinTotal = 1;
			long bitterTotal = 0;
			for (int dishIndex=0; dishIndex<dishCount; dishIndex++) {
				if (contained[dishIndex]) {
					sinTotal *= sin[dishIndex];
					bitterTotal += bitter[dishIndex];
				}
			}
			if (sinTotal == 1 && bitterTotal == 0) return;
			long difference = sinTotal-bitterTotal;
			difference *= difference < 0 ? -1 : 1;
			minimum = Math.min(minimum, difference);
		} else {
			contained[index] = true;
			setSubset(index+1);
			contained[index] = false;
			setSubset(index+1);
		}
	}

}
