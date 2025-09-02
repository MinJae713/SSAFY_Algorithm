package homeworks.m8.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * <변수 및 함수>
 * 1. builder: 테스트 케이스 별 결과 저장
 * 2. reader: 값 입력시 사용
 * 3. tokenizer: reader로 읽은 값 추출시 사용
 * 4. dishCount: 재료의 수
 * 5. limit: 제한 칼로리
 * 6. scores: 재료별 맛의 점수
 * 7. calories: 재료별 칼로리
 * 8. contaied: 재료별 포함 여부
 * 9. maxScore: 음식의 최대 가치
 * 11. initialize(): 변수 값 초기화
 * 12. setDishInformation(): 음식 정보 입력
 * 13. makeSubset(index): 재료로 구성할 수 있는 조합 생성
 * 
 * <로직>
 * 1. 테스트 케이스 갯수 입력
 * 2. dishCount, limit 입력
 * 3. scores, calories, contained, maxScore 초기화
 * 4. scores, calories 값 입력
 * 5. makeSubSet 메소드 호출, 파라미터는 0
 * 	5.1. index 값이 dishCount와 같다면?
 * 		5.1.1. 부분집합에 포함되어 있는 재료들의 점수, 칼로리 합산
 * 		5.1.2. 칼로리 총합이 limit 이하인 경우, 재료 점수 합산 값을 maxPrice와 비교, 
 * 					더 큰 값으로 maxPrice 값 변경
 * 	5.2. index 값이 dishCount보다 작다면?
 * 		5.2.1. contained의 index 위치에 true 값 지정
 * 		5.2.2. index+1을 파라미터로 makeSubset 메소드 재귀 호출
 * 		5.2.3. contained의 index 위치에 false 값 지정
 * 		5.2.4. index+1을 파라미터로 makeSubset 메소드 재귀 호출
 * 6. maxPrice 값 builder에 추가
 * 7. 결과 출력
 */
public class Hamburger {
	private static StringBuilder builder;
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int dishCount;
	private static int limit;
	private static int[] scores;
	private static int[] calories;
	private static boolean[] contained;
	private static int maxScore;
	
	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		builder = new StringBuilder();
		reader = new BufferedReader(new InputStreamReader(System.in));
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			setDishInformation();
			makeSubset(0);
			builder.append("#").append(testCase).append(" ").append(maxScore).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		dishCount = Integer.parseInt(tokenizer.nextToken());
		limit = Integer.parseInt(tokenizer.nextToken());
		scores = new int[dishCount];
		calories = new int[dishCount];
		contained = new boolean[dishCount];
		maxScore = Integer.MIN_VALUE;
	}
	private static void setDishInformation() throws IOException {
		for (int dishIndex=0; dishIndex<dishCount; dishIndex++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			scores[dishIndex] = Integer.parseInt(tokenizer.nextToken());
			calories[dishIndex] = Integer.parseInt(tokenizer.nextToken());
		}
	}
	private static void makeSubset(int index) {
		if (index == dishCount) {
			int totalScore = 0;
			int totalCalorie = 0;
			for (int dishIndex=0; dishIndex<dishCount; dishIndex++)
				if (contained[dishIndex]) {
					totalScore += scores[dishIndex];
					totalCalorie += calories[dishIndex];
				}
			if (totalCalorie <= limit)
				maxScore = Math.max(maxScore, totalScore);
			return;
		}
		contained[index] = true;
		makeSubset(index+1);
		contained[index] = false;
		makeSubset(index+1);
	}

}
