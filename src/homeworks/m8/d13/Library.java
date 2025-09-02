package homeworks.m8.d13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 테스트 케이스 입력, 테스트 케이스 만큼 반복
 * 2. 점원 수, 선반 높이 입력
 * 3. 각 점원의 키 입력
 * 4. 각 점원별 포함 여부 나타내는 배열 생성
 * 5. 각 점원의 포함 여부 결정
 * 	5.1. 모든 점원에 대해 포함 여부가 정해졌다면?
 * 		5.1.1. 포함된 점원의 키 합산
 * 		5.1.2. 선반 높이와 차이 계산
 * 		5.1.3. 차이가 양수라면 최솟값인지 확인
 * 	5.2. 포함 여부가 정해지지 않은 점원이 있다면?
 * 		5.2.1. 해당 점원 포함 여부 true
 * 		5.2.2. 다음 점원에 포함 여부 결정(재귀)
 * 		5.2.3. 해당 점원 포함 여부 false
 * 		5.2.4. 다음 점원에 포함 여부 결정(재귀)
 * 6. 최솟값 출력
 */
public class Library {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int count;
	private static int sunban;
	private static int[] heights;
	private static boolean[] contained;
	private static int minimum;
	
	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			setContained(0);
			builder.append("#").append(testCase).append(" ").append(minimum).append("\n");
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
		tokenizer = new StringTokenizer(reader.readLine().trim());
		count = Integer.parseInt(tokenizer.nextToken());
		sunban = Integer.parseInt(tokenizer.nextToken());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		heights = new int[count];
		contained = new boolean[count];
		minimum = Integer.MAX_VALUE;
		for (int index=0; index<count; index++)
			heights[index] = Integer.parseInt(tokenizer.nextToken());
	}
	private static void setContained(int index) {
		if (index == count) {
			int total = 0;
			for (int heightsIndex=0; heightsIndex<count; heightsIndex++) 
				if (contained[heightsIndex])
					total += heights[heightsIndex];
			if (total >= sunban) minimum = Math.min(minimum, total-sunban);
			return;
		}
		contained[index] = true;
		setContained(index+1);
		contained[index] = false;
		setContained(index+1);
	}
}
