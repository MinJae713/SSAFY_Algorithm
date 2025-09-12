package homeworks.m9.d12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 전체 집 수*집 각각 비용-전체 범위 따른 운영 비용이 0 이상이면 전체 집 수가 결과가 됨
 * 	1.1. 아니라면 아래 실행
 * 2. 서비스 영역 마다 반복 (serviceSize)
 * 3. 운영비용 계산(cost): serviceSize*serviceSize+
 * 						(serviceSize-1)*(serviceSize-1)
 * 4. 거리 계산(distance): serviceSize-1
 * 5. 각 위치 마다 반복 (position)
 * 6. 각 집마다 반복 (home)
 * 7. 각 집과 position의 거리가 distance 이하인 개수 계산 (homeCount)
 * 8. 집의 개수*집 각각 비용-운영 비용이 0보다 작다면 continue
 * 9. 최대 집 수 계산
 */
public class HomeSecurityService {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int bound;
	private static int homePay;
	private static List<int[]> homePositions;
	private static int maxCount;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			int totalHomeIncome = homePositions.size()*homePay;
			int totalBoundCost = bound*bound+(bound-1)*(bound-1);
			// 모든 영역에 서비스를 해도 손해가 나지 않는 경우 -> 전체 집에 서비스 가능
			if (totalHomeIncome >= totalBoundCost)
				maxCount = homePositions.size();
			else 
				for (int serviceSize=1; serviceSize<=bound; serviceSize++) {
					int cost = serviceSize*serviceSize+
								(serviceSize-1)*(serviceSize-1);
					int distance = serviceSize-1;
					for (int row=0; row<bound; row++)
						for (int column=0; column<bound; column++) {
							int homeCount = 0;
							for (int[] position : homePositions)
								homeCount += getDistance(column, row, 
										position[0], position[1]) <= 
										distance ? 1 : 0;
							if (homeCount*homePay-cost < 0) continue;
							// 손해가 나면 고려하지 않음
							maxCount = Math.max(maxCount, homeCount);
						}
			}
			builder.append("#").append(testCase).append(" ").
					append(maxCount).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	
	private static int getDistance(int fromX, 
			int fromY, int toX, int toY) {
		return Math.abs(fromX-toX)+Math.abs(fromY-toY);
	}
	
	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}
	
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		bound = Integer.parseInt(tokenizer.nextToken());
		homePay = Integer.parseInt(tokenizer.nextToken());
		maxCount = 0;
		homePositions = new ArrayList<>();
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++) {
				int home = Integer.parseInt(tokenizer.nextToken());
				if (home == 1) homePositions.add(new int[] {column, row});
			}
		}
	}
}
