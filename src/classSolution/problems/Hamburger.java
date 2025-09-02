package classSolution.problems;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * SWEA 5215 햄버거다이어트
 * @author SSAFY
 * 
 * 1. 테스트 케이스 개수 입력
 * 2. 각 테스트 케이스마다,
 * 	2.1. 재료 갯수, 제한 칼로리 입력
 * 	2.1. 재료 갯수 만큼 각 재료 점수, 칼로리 입력
 * 
 * 3. 조합 구현
 * 	3.1. 재료에 대한 조합을 구했다면?
 * 		3.1.1. 점수, 칼로리 합산
 * 		3.1.2. 제한 칼로리 넘어가는지 확인하고,
 * 			3.1.2.1. 칼로리를 넘어가지 않으면, 점수가 더 높으면 갱신
 * 
 * 4. 가장 높은 점수 출력
 */
public class Hamburger {
	static BufferedReader br;
	static StringTokenizer st;
	static StringBuilder sb;
	
	static int foodCount, limitCalories;
	static int[][] foodInfoArray;
	static int[][] selectedFoodArray;
	
	static final int SCORE = 0, CALORIE = 1;
	
	private static void inputTestCase() throws IOException {
		// 2.1. 재료 갯수, 제한 칼로리 입력
		st = new StringTokenizer(br.readLine().trim());
		foodCount = Integer.parseInt(st.nextToken());
		limitCalories = Integer.parseInt(st.nextToken());
		
		// 2.1. 재료 갯수 만큼 각 재료 점수, 칼로리 입력
		foodInfoArray = new int[foodCount][foodCount];
		for (int foodIndex=0; foodIndex<foodCount; foodIndex++) {
			st = new StringTokenizer(br.readLine().trim());
			
			// 점수, 칼로리
			foodInfoArray[foodIndex][SCORE] = Integer.parseInt(st.nextToken());
			foodInfoArray[foodIndex][CALORIE] = Integer.parseInt(st.nextToken());
		}
	}
	
	static int bestScore;
	public static void combination(int elementIndex, int selectIndex) {
		// 1. 기저 조건 (종료 조건)
		if (selectIndex == selectedFoodArray.length) {
			int tmpSumScore = 0;
			int tmpSumCalorie = 0;
			for (int index=0; index<selectedFoodArray.length; index++) {
				tmpSumScore += selectedFoodArray[index][SCORE];
				tmpSumCalorie += selectedFoodArray[index][CALORIE];
			}
			if (tmpSumCalorie <= limitCalories)
				bestScore = Math.max(bestScore, tmpSumScore);
			
			return;
		}
		// 음식을 모두 확인했을 시
		if (elementIndex == foodCount) return;
		
		// 2. 아직 원소를 다 안봤거나, 아직 다 선택하지 않았거나.
		
		// 선택, 복구
		selectedFoodArray[selectIndex][SCORE] = foodInfoArray[elementIndex][SCORE];
		selectedFoodArray[selectIndex][CALORIE] = foodInfoArray[elementIndex][CALORIE];
		combination(elementIndex+1, selectIndex+1);
		
		selectedFoodArray[selectIndex][SCORE] = -1;
		selectedFoodArray[selectIndex][CALORIE] = -1;
		combination(elementIndex+1, selectIndex);
		
	}
	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("sample_input.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		
		// 1. 테스트 케이스 개수 입력
		int testCase = Integer.parseInt(br.readLine().trim());
		for (int tc=1; tc<=testCase; tc++) {
			// 2. 각 테스트 케이스마다,
			inputTestCase();
			
			// 3. 조합 구현
			for (int combCount=1; combCount<=foodCount; combCount++) {
				selectedFoodArray = new int[combCount][2];
				combination(0, 0);
			}
			
			// 4. 가장 높은 점수 출력
			sb.append("#").append(tc).append(" ").append(bestScore).append("\n");
		}
		System.out.println(sb);
	}
}
