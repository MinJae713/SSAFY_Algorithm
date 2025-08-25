package base;

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
 * 	3.1. 재료에 대한 부분집합을 구했다면?
 * 		3.1.1. 점수, 칼로리 합산
 * 		3.1.2. 제한 칼로리 넘어가는지 확인하고,
 * 			3.1.2.1. 칼로리를 넘어가지 않으면, 점수가 더 높으면 갱신
 * 
 * 4. 가장 높은 점수 출력
 */
public class HamburgerPowerSet {
	static BufferedReader br;
	static StringTokenizer st;
	static StringBuilder sb;
	
	static int foodCount, limitCalories;
	static int[][] foodInfoArray;
	static boolean[] isSelected;
	
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
	public static void powerSet(int selectIndex) {
		if (selectIndex == foodCount) {
			int tmpSumScore = 0;
			int tmpSumCalorie = 0;
			for (int index=0; index<foodCount; index++) {
				if (isSelected[index]) {
					tmpSumScore += foodInfoArray[index][SCORE];
					tmpSumCalorie += foodInfoArray[index][CALORIE];
				}
			}
			if (tmpSumCalorie <= limitCalories)
				bestScore = Math.max(bestScore, tmpSumScore);
			return;
		}
		isSelected[selectIndex] = true; // 재료 선택
		powerSet(selectIndex+1);
		isSelected[selectIndex] = false; // 재료 미선택
		powerSet(selectIndex+1);
	}
	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("sample_input.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		
		// 1. 테스트 케이스 개수 입력
		int testCase = Integer.parseInt(br.readLine().trim());
		for (int tc=1; tc<=testCase; tc++) {
			bestScore = Integer.MIN_VALUE;
			// 2. 각 테스트 케이스마다,
			inputTestCase();
			
			// 3. 조합 구현
			powerSet(0);
			
			// 4. 가장 높은 점수 출력
			sb.append("#").append(tc).append(" ").append(bestScore).append("\n");
		}
		System.out.println(sb);
	}
}
