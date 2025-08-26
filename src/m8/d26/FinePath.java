package m8.d26;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. getMinimumPath(): position 위치에서 집까지의 거리 중, 최소 거리 값 반환
 * 	1.1. position: 현재 위치, visitedUsers: 고객 번호 별 방문 여부 표시, visitedCount: 방문 집 수
 * 2. visitedCount가 고객 수와 같다면?
 * 	2.1. position과 집의 거리 계산 및 반환
 * 3. 고객마다 반복(user), 이미 방문한 user라면 다음 반복 수행
 * 	3.1. 마지막 고객 여부(isLastUser) false 지정
 * 	3.2. user 해당 위치 visitedUsers true 지정
 * 	3.3. user와 visitedUsers, visitedCount+1을 파라미터로 getMinimumPath 재귀 호출
 * 		3.3.1. 실행 결과는 해당 위치에서 집까지 거리(userToHomeLength)에 입력 
 * 	3.4. user 해당 위치 visitedUsers false 지정
 * 	3.5. position에서 user까지 거리(positionToUserLength)+userToHomeLength의 최솟값 계산(minimumPositionToHomeLength)
 * 4. 위 최솟값 반환
 * 5. 회사에서 집까지 최소 거리의 소숫점 반올림
 */
public class FinePath {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int userCount;
	private static int[] companyPosition;
	private static int[] homePosition;
	private static int[][] userPositions;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			double result = getMinimumPath(companyPosition, new boolean[userCount], 0);
			builder.append("#").append(testCase).append(" ").
					append(Math.round(result)).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static double getMinimumPath(int[] position, 
			boolean[] visitedUser, int visitedCount) {
		if (visitedCount == userCount)
			return getDistance(position, homePosition);
		double minimumPositionToHomeLength = Double.MAX_VALUE;
		for (int user=0; user<userCount; user++) {
			if (visitedUser[user]) continue;
			visitedUser[user] = true;
			double userToHomeLength = getMinimumPath(
					userPositions[user], visitedUser, visitedCount+1);
			visitedUser[user] = false;
			double positionToUserLength = getDistance(position, userPositions[user]);
			minimumPositionToHomeLength = Math.min(
				minimumPositionToHomeLength, 
				positionToUserLength+userToHomeLength
			);
		}
		return minimumPositionToHomeLength;
	}
	
	private static double getDistance(int[] positionOne, int[] positionTwo) {
		return Math.abs(positionOne[0]-positionTwo[0])+
				Math.abs(positionOne[1]-positionTwo[1]);
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		userCount = Integer.parseInt(reader.readLine().trim());
		userPositions = new int[userCount][];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		companyPosition = new int[] {
			Integer.parseInt(tokenizer.nextToken()),
			Integer.parseInt(tokenizer.nextToken())
		};
		homePosition = new int[] {
			Integer.parseInt(tokenizer.nextToken()),
			Integer.parseInt(tokenizer.nextToken())
		};
		for (int userNumber=0; userNumber<userCount; userNumber++)
			userPositions[userNumber] = new int[] {
				Integer.parseInt(tokenizer.nextToken()),
				Integer.parseInt(tokenizer.nextToken())
			};
	}
}
