package homeworks.m8.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 문자열 길이, 부분 문자열 길이 입력
 * 2. count 변수 생성
 * 3. A, C, G, T 최소  갯수 배열, 부분 문자열 내 갯수 배열 생성
 * 4. DNA 문자열 입력
 * 5. 시작, 끝 위치 입력 (끝 위치 = 부분 문자열-1)
 * 6. ACGT의 각 최소 갯수 최소 갯수 배열에 입력
 * 7. 시작 ~ 끝 위치 내 ACGT 갯수 계산 -> 부분 문자열 내 갯수 배열에 입력
 * 	7.1. A(0) C(1) G(2) T(3)
 * 8. 끝 위치가 문자열 길이가 될 때 까지 반복
 * 	8.1. 최소 갯수 배열 - 부분 문자열 내 갯수 배열 비교 => 각 위치가 최소 갯수 배열 각 위치 이상인지 확인
 * 		8.1.1. 전부 만족한다면 count 1 증가
 * 	8.2. 시작 위치 갯수 배열 값 1 감소, 시작 위치 1 증가
 * 	8.3. 끝 위치 1 증가, 끝 위치가 문자열 길이보다 작다면 해당 위치 문자에 해당하는 갯수 배열 값 1 증가
 * 9. count 출력
 */
public class DNA {

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		// 초기화
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer tokenizer = new StringTokenizer(reader.readLine().trim());
		int length = Integer.parseInt(tokenizer.nextToken());
		int subsetLength = Integer.parseInt(tokenizer.nextToken());
		int count = 0;
		int[] minimumCounts = new int[4]; // [A, C, G, T]
		int[] subsetCounts = new int[4]; // [A, C, G, T]
		String dna = reader.readLine().trim();
		int start = 0;
		int end = subsetLength-1;
		// ACGT 최소 갯수 입력
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<4; index++)
			minimumCounts[index] = Integer.parseInt(tokenizer.nextToken());
		// 초기 위치 ACGT 갯수 입력
		for (int index=start; index<=end; index++) {
			switch (dna.charAt(index)) {
			case 'A':
				subsetCounts[0]++;
				break;
			case 'C':
				subsetCounts[1]++;
				break;
			case 'G':
				subsetCounts[2]++;
				break;
			case 'T':
				subsetCounts[3]++;
				break;
			}
		}
		// 부분 문자열이 조건에 맞는지 확인
		while (end < length) {
			int index = 0;
			while (index<4) {
				if (minimumCounts[index] <= subsetCounts[index])
					index++;
				else break;
			}
			count += index == 4 ? 1 : 0;
			
			switch (dna.charAt(start)) {
			case 'A':
				subsetCounts[0]--;
				break;
			case 'C':
				subsetCounts[1]--;
				break;
			case 'G':
				subsetCounts[2]--;
				break;
			case 'T':
				subsetCounts[3]--;
				break;
			}
			start++;
			end++;
			if (end < length) {
				switch (dna.charAt(end)) {
				case 'A':
					subsetCounts[0]++;
					break;
				case 'C':
					subsetCounts[1]++;
					break;
				case 'G':
					subsetCounts[2]++;
					break;
				case 'T':
					subsetCounts[3]++;
					break;
				}
			}
		}
		System.out.println(count);
		reader.close();
	}

}
