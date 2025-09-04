package homeworks.m9.d4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - minimumDistanceMemo[a][contained]: 
 *   ㄴ 각 고객의 포함 여부가 주어질 때(contained) a에서 집까지 가는데 걸리는 최소 비용
 * 1. getMinimumDistance(): 한 고객에서 몇 개의 다른 고객을 거쳐서 
 * 						      자신의 집까지 가는데 걸리는 최소 비용 계산
 * 	1.1. 파라미터는 현재 고객 번호(currentUser), 고객 포함 비트(containedBit)
 * 	1.2. 현재 고객 포함 여부 1로 지정 (containedBit |= (1<<currentUser))
 * 	1.3. 남은 고객이 해당 고객 밖에 안남았다면? (containedBit & (~(1<<currentUser)) == 0)
 * 		1.3.1. currentUser-마지막 위치 거리 반환
 * 	1.3. 모든 고객에 대해 반복(nextUser)
 * 	1.4. 다음 고객을 방문하지 않았다면? continue
 * 	1.5. minimumDistanceMemo의 nextUser 위치 containedBit 위치 값이 초기값이라면?
 * 		1.5.1. nextUser, containedBit를 파라미터로 getMinimumDistance 재귀 호출
 * 		1.5.2. 호출 결과를 minimumDistanceMemo의 
 * 			   nextUser 위치 containedBit 위치에 입력
 * 		1.5.3. 위 입력 값과 currentUser-nextUser 위치 거리 합산(totalDistance)
 * 		1.5.4. 합산한 결과 중, 가장 작은 값 반환
 * 2. 모든 고객 번호에 대해 반복(nextUser)
 * 	2.1. 각 고객에 대해 getMinimumDistance() 호출
 * 	2.2. 위 실행 결과와 시작 위치-nextUser 위치 거리 합산 
 * 	2.3. 위 값과 시작 지점에서 최소 거리 값 비교 -> 더 작은 값으로 입력
 */
public class FinePathDP {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int userCount;
	private static int[] companyPosition; // (x, y)
	private static int[] homePosition;
	private static int[][] userPositions;
	private static int[][] minimumDistanceMemo;
	private static int minimumDistance;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int user=0; user<userCount; user++) {
				minimumDistanceMemo[user][0] = getMinimumDistance(user, 0)+
						getDistance(companyPosition, userPositions[user]);
				minimumDistance = Math.min(minimumDistance, minimumDistanceMemo[user][0]);
			}
			builder.append("#").append(testCase).append(" ").
					append(minimumDistance).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int getMinimumDistance(int currentUser, int containedBit) {
		containedBit |= (1<<currentUser); // 고객 포함 여부 지정
		// 모든 고객을 방문했을 때
		if (containedBit == (1<<userCount)-1)
			return getDistance(userPositions[currentUser], homePosition);
		int totalDistance = Integer.MAX_VALUE;
		for (int nextUser=0; nextUser<userCount; nextUser++) {
			int nextUserBit = 1<<nextUser;
			// 다음 고객이 포함되어 있는가
			if ((containedBit & nextUserBit) == nextUserBit) continue;
			if (minimumDistanceMemo[nextUser][containedBit] == Integer.MAX_VALUE)
				minimumDistanceMemo[nextUser][containedBit] = 
					getMinimumDistance(nextUser, containedBit);
			totalDistance = Math.min(totalDistance, 
					getDistance(userPositions[currentUser], userPositions[nextUser])+
					minimumDistanceMemo[nextUser][containedBit]);
		}
		return totalDistance;
	}

	private static int getDistance(int[] from, int[] to) {
		return Math.abs(to[0]-from[0])+Math.abs(to[1]-from[1]);
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		userCount = Integer.parseInt(reader.readLine().trim());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		companyPosition = new int[] {
			Integer.parseInt(tokenizer.nextToken()),
			Integer.parseInt(tokenizer.nextToken())
		};
		homePosition = new int[] {
			Integer.parseInt(tokenizer.nextToken()),
			Integer.parseInt(tokenizer.nextToken())
		};
		userPositions = new int[userCount][2];
		for (int index=0; index<userCount; index++)
			userPositions[index] = new int[] {
				Integer.parseInt(tokenizer.nextToken()),
				Integer.parseInt(tokenizer.nextToken())
			};
		minimumDistanceMemo = new int[userCount][1<<userCount];
		for (int user=0; user<userCount; user++)
			Arrays.fill(minimumDistanceMemo[user], Integer.MAX_VALUE);
		minimumDistance = Integer.MAX_VALUE;
	}

}
