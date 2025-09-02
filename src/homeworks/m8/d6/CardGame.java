package homeworks.m8.d6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * <변수 및 함수 정의>
 * 1. builder: 테스트 케이스 별 실행 결과 저장
 * 2. gyuyeongCards: 규영이가 라운드 별로 내는 카드, 라운드 순서 별로 저장됨
 * 3. inyeongCards: 인영이가 라운드 별로 내는 카드, makePermuteFrom을 통해 라운드별 인영의 카드 값이 지정됨
 * 4. cardStatus: 카드가 나왔는지 여부 지정, 1~18번 번호별로 지정
 * 	4.1. 규영이가 뽑은 카드는 true로 지정, 인영이가 뽑은 카드는 true로 지정 후 false로 바뀜
 * 5. makePermuteFrom(index) 인영이가 라운드 별로 낼 수 있는 카드의 조합 생성
 * <로직>
 * 1. 테스트 케이스 입력 및 builder 초기화
 * 2. 테스트 케이스별 반복 및 win, loose, gyuyeongCards, inyeongCards, cardStatus 변수 초기화
 * 	2.1. 각각 규영이가 이긴, 진 횟수
 * 3. 규영이가 라운드 별로 내는 카드 번호 저장 및 각 카드 별 나온 여부 저장
 * 4. makePermuteFrom 함수 호출, index는 0
 * 	4.1. index값이 9라면 게임 결과 계산
 * 		4.1.1. gyuyeongScore, inyeongScore: 규영이와 인영이 점수 합산
 * 		4.1.2. 각 라운드 별 규영이 카드, 인영이 카드 점수 비교
 * 			4.1.2.1. 규영이 카드+인영이 카드 점수를 규영이가 높다면 gyuyeongScore,인영이가 높다면 inyeongScore에 합산
 * 		4.1.3. 합산 이후 두 점수 비교 -> gyuyeongScore가 높다면 win 변수, inyeongScore가 높다면 loose 변수 값 +1 증가
 * 	4.2. index가 9보다 작다면 라운드별 인영의 카드 점수 지정
 * 		4.2.1. 1~18까지 반복, 반복되서 나오는 수가 cardStatus에서 true라면 다음 반복 수행
 * 			4.2.1.1. true가 아니라면 해당 값을 inyeongCards의 index 위치에 지정
 * 			4.2.1.2. cardStatus의 해당 값 true
 * 			4.2.1.3. makePermuteFrom 함수의 index를 1 증가시켜서 재귀 호출
 * 			4.2.1.4. cardStatus의 해당 값 false
 * 5. win, loose 값을 builder에 추가
 * 6. builder 결과 출력
 */
public class CardGame {
	public static void main(String[] args) throws IOException {
		// <로직>
		// 1. 테스트 케이스 입력 및 builder 초기화
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine());
		// 2. 테스트 케이스별 반복 및 win, loose, gyuyeongCards, inyeongCards, cardStatus 변수 초기화
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			// 2.1. 각각 규영이가 이긴, 진 횟수
			builder.append("#").append(testCase).append(" ");
			win = 0;
			loose = 0;
			gyuyeongCards = new int[9];
			inyeongCards = new int[9];
			cardStatus = new boolean[19];
			// 3. 규영이가 라운드 별로 내는 카드 번호 저장 및 각 카드 별 나온 여부 저장
			StringTokenizer gyuyeongCardsLine = new StringTokenizer(reader.readLine());
			for (int cardIndex=0; cardIndex<9; cardIndex++) {
				gyuyeongCards[cardIndex] = Integer.parseInt(gyuyeongCardsLine.nextToken());
				cardStatus[gyuyeongCards[cardIndex]] = true;
			}
			// 4. makePermuteFrom 함수 호출, index는 0
			makePermuteFrom(0);
			// 5. win, loose 값을 builder에 추가
			builder.append(win).append(" ").append(loose).append("\n");
		}
		// 6. builder 결과 출력
		System.out.println(builder);
		reader.close();
	}
	// <변수 및 함수 정의>
	// 1. builder: 테스트 케이스 별 실행 결과 저장
	static StringBuilder builder;
	// 2. gyuyeongCards: 규영이가 라운드 별로 내는 카드, 라운드 순서 별로 저장됨
	static int[] gyuyeongCards;
	// 3. inyeongCards: 인영이가 라운드 별로 내는 카드, makePermuteFrom을 통해 라운드별 인영의 카드 값이 지정됨
	static int[] inyeongCards;
	// 4. cardStatus: 카드가 나왔는지 여부 지정, 1~18번 번호별로 지정
	// 4.1. 규영이가 뽑은 카드는 true로 지정, 인영이가 뽑은 카드는 true로 지정 후 false로 바뀜
	static boolean[] cardStatus;
	// 5. makePermuteFrom(index) 인영이가 라운드 별로 낼 수 있는 카드의 조합 생성
	static int win, loose;
	static void makePermuteFrom(int index) {
		if (index == 9) {
			// 4.1. index값이 9라면 게임 결과 계산
			// 4.1.1. gyuyeongScore, inyeongScore: 규영이와 인영이 점수 합산
			int gyuyeongScore = 0;
			int inyeongScore = 0;
			for (int scoreIndex=0; scoreIndex<9; scoreIndex++) {
				// 4.1.2. 각 라운드 별 규영이 카드, 인영이 카드 점수 차이 계산
				int gyuyeongRoundScore = gyuyeongCards[scoreIndex];
				int inyeongRoundScore = inyeongCards[scoreIndex];
				int roundTotal = gyuyeongRoundScore+inyeongRoundScore;
				// 4.1.2.1. 규영이 카드+인영이 카드 점수를 규영이가 높다면 gyuyeongScore,인영이가 높다면 inyeongScore에 합산
				if (gyuyeongRoundScore>inyeongRoundScore)
					gyuyeongScore += roundTotal;
				else if (gyuyeongRoundScore<inyeongRoundScore)
					inyeongScore += roundTotal;
			}
			// 4.1.3. 합산 이후 두 점수 비교 -> gyuyeongScore가 높다면 win 변수, inyeongScore가 높다면 loose 변수 값 +1 증가
			if (gyuyeongScore>inyeongScore) win++;
			else if (gyuyeongScore<inyeongScore) loose++;
		} else {
			// 4.2. index가 9보다 작다면 라운드별 인영의 카드 점수 지정
			// 4.2.1. 1~18까지 반복, 반복되서 나오는 수가 cardStatus에서 true라면 다음 반복 수행
			for (int number=1; number<=18; number++) {
				if (cardStatus[number]) continue;
				// 4.2.1.1. true가 아니라면 해당 값을 inyeongCards의 index 위치에 지정
				inyeongCards[index] = number;
				// 4.2.1.2. cardStatus의 해당 값 true
				cardStatus[number] = true;
				// 4.2.1.3. makePermuteFrom 함수의 index를 1 증가시켜서 재귀 호출
				makePermuteFrom(index+1);
				// 4.2.1.4. cardStatus의 해당 값 false
				cardStatus[number] = false;
			}
		}
	}
}
