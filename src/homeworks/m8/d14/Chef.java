package homeworks.m8.d14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 전체 식재료(dish)의 절반(dish/2)으로 구성할 수 있는 조합(getNextCombinate()) 생성
 * 	1.1. 지정된 식재료 수가 dish/2가 되면?
 * 		1.1.1. A 식재료, B 식재료 포함 배열 생성
 * 		1.1.2. 식재료 갯수 만큼 반복 (index) -> 식재료 조합 배열 위치가 true면 A, false면 B 배열에 재료 위치 지정
 * 		1.1.3. A, B 요리의 식재로 시너지 합 계산, 둘의 차이 계산
 * 		1.1.4. 둘의 차이가 최솟값 보다 작다면 값 변경
 * 	1.2. 식재료 수가 dish/2보다 작다면?
 * 		1.2.1. 식재료 조합 시작 위치(start)부터 마지막 식재료 위치(dish-1)까지 반복(index)
 * 		1.2.2. index 위치 식재료 포함 여부 true 지정
 * 		1.2.3. 식재료 수+1, index 다음 위치를 파라미터로 getNextCombinate 재귀 호출
 * 		1.2.4. index 위치 식재료 포함 여부 false 지정
 */
public class Chef {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int dish;
	private static int[][] senerges;
	private static boolean[] contained;
	private static int[] containedA;
	private static int[] containedB;
	private static int minimum;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			getNextCombinate(0, 0);
			builder.append("#").append(testCase)
					.append(" ").append(minimum).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		dish = Integer.parseInt(reader.readLine().trim());
		senerges = new int[dish][dish];
		for (int row=0; row<dish; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<dish; column++)
				senerges[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
		contained = new boolean[dish];
		containedA = new int[dish/2];
		containedB = new int[dish/2];
		minimum = Integer.MAX_VALUE;
	}
	private static void getNextCombinate(int start, int dishCount) {
		if (dishCount == dish/2) {
			int indexA = 0, indexB = 0;
			for (int index=0; index<dish; index++) {
				if (contained[index]) containedA[indexA++] = index;
				else containedB[indexB++] = index;
			}
			int totalDifference = 
					getTotalSenerges(containedA)-
					getTotalSenerges(containedB);
			totalDifference *= totalDifference<0 ? -1 : 1;
			minimum = Math.min(minimum, totalDifference);
			return;
		}
		for (int index=start; index<dish; index++) {
			contained[index] = true;
			getNextCombinate(index+1, dishCount+1);
			contained[index] = false;
		}
	}

	private static int getTotalSenerges(int[] containedDishes) {
		int total = 0;
		for (int fromIndex=0; fromIndex<dish/2; fromIndex++) {
			for (int toIndex=0; toIndex<dish/2; toIndex++) {
				if (toIndex == fromIndex) continue;
				int from = containedDishes[fromIndex];
				int to = containedDishes[toIndex];
				total += senerges[from][to];
			}
		}
		return total;
	}
}
