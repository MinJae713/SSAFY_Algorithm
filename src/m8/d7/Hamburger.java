package m8.d7;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * <변수 및 함수>
 * 1. N: 햄버거의 수
 * 2. L: 햄버거의 최대 칼로리
 * 3. prices: 햄버거별 가치
 * 4. calories: 햄버거별 칼로리
 * 5. maxPrice: 햄버거조합의 최대 가치
 * 6. computeCalorie: 햄버거의 칼로리 조합 저장 -> n번째 햄버거는 n 값이 저장되며, 조합의 수는 1부터 n까지 들어감
 * 7. computePrice: 햄버거의 가치 조합 저장
 * 8. setComputeFrom(index, start, computeMax): 
 * 		start 위치부터 N-1까지로 만들 수 있는 햄버거 조합 생성
 * 	8.1. index: computePrice, Calorie에 n번째 햄버거를 넣을 때 사용하는 인덱스
 * 	8.2. start: 햄버거 조합 구성 시 어디부터 선택할 지를 나타내는 인덱스
 * 	8.3. computeMax: 조합의 길이
 * <로직>
 * 1. 테스트 케이스 입력
 * 2. 햄버거의 수(N), 햄버거의 칼로리(L) 입력
 * 3. prices, hamburgerCompute, calories, maxPrice 초기화 및 값 입력
 * 4. 햄버거의 수 만큼 반복 (computeMax: 1~N)
 * 	4.1. computeCalorie, computePrice 생성 (크기는 computeMax)
 * 	4.2. setComputeFrom 함수 호출
 *	  	4.2.1. index 값이 computeMax와 같다면?
 *			4.2.1.1. computeCalorie, computePrice 합산
 *			4.2.1.2. computeCalorie 합산 결과가 L 이하인 경우, computePrice를 maxPrice와 비교, maxPrice 값 변경
 *	  	4.2.2. index 값이 computeMax보다 작다면? - start~N-1까지 반복
 *	  		4.2.2.1. computeCalorie, computePrice의 index 위치에 햄버거 인덱스 위치 값 저장
 *	  		4.2.2.2. setComputeFrom 재귀 호출 - (index+1, start+1, computeMax)
 * 5. 테스트 케이스, maxPrice 값 출력
 */
public class Hamburger {

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("sample_input.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder builder = new StringBuilder();
		// 1. 테스트 케이스 입력
		int testCaseCount = Integer.parseInt(reader.readLine());
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			// 2. 햄버거의 수(N), 햄버거의 칼로리(L) 입력
			StringTokenizer NL = new StringTokenizer(reader.readLine());
			N = Integer.parseInt(NL.nextToken());
			L = Integer.parseInt(NL.nextToken());
			// 3. prices, hamburgerCompute, calories, maxPrice 초기화 및 값 입력
			prices = new int[N];
			calories = new int[N];
			maxPrice = 0;
			for (int n=0; n<N; n++) {
				StringTokenizer priceCalorie = new StringTokenizer(reader.readLine());
				prices[n] = Integer.parseInt(priceCalorie.nextToken());
				calories[n] = Integer.parseInt(priceCalorie.nextToken());
			}
			// 4. 햄버거의 수 만큼 반복 (computeMax: 1~N)
			for (int computeMax=1; computeMax<=N; computeMax++) {
				// 4.1. computeCalorie, computePrice 생성 (크기는 computeMax)
				computePrice = new int[computeMax];
				computeCalorie = new int[computeMax];
				// 4.2. setComputeFrom 함수 호출
				setComputeFrom(0, 0, computeMax);
			}
			builder.append("#").append(testCase).append(" ").append(maxPrice).append("\n");
		}
		// 5. 테스트 케이스, maxPrice 값 출력
		System.out.print(builder);
		reader.close();
	}
	static int N;
	static int L;
	static int[] prices;
	static int[] calories;
	static int maxPrice;
	static int[] computePrice;
	static int[] computeCalorie;
	private static void setComputeFrom(int index, int start, int computeMax) {
		// 4.2.1. index 값이 computeMax와 같다면?
		if (index == computeMax) {
			// 4.2.1.1. computeCalorie, computePrice 합산
			int totalCalorie = 0;
			int totalPrice = 0;
			for (int n=0; n<computeMax; n++) {
				totalCalorie += computeCalorie[n];
				totalPrice += computePrice[n];
			}
			// 4.2.1.2. computeCalorie 합산 결과가 L 이하인 경우, computePrice를 maxPrice와 비교, maxPrice 값 변경
			if (totalCalorie <= L)
				maxPrice = Math.max(maxPrice, totalPrice);
			return;
		}
		// 4.2.2. index 값이 computeMax보다 작다면? - start~N-1까지 반복
		for (int hamburger=start; hamburger<N; hamburger++) {
			// 4.2.2.1. computeCalorie, computePrice의 index 위치에 햄버거 인덱스 위치 값 저장
			computeCalorie[index] = calories[hamburger];
			computePrice[index] = prices[hamburger];
			// 4.2.2.2. setComputeFrom 재귀 호출 - (index+1, start+1, computeMax)
			setComputeFrom(index+1, hamburger+1, computeMax);
		}
	}
}
