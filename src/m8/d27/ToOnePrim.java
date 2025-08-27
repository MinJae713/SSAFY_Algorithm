package m8.d27;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 0번째 섬 선택
 * 	1.1. 우선순위 큐(queue)에 해당 섬의 번호(number)와 0 offer
 * 2. 큐에 요소가 있는 동안 반복
 * 	2.1. 큐에서 요소 poll (island)
 * 	2.2. 해당 island를 이미 방문했다면 다음 반복 수행
 * 	2.3. 해당 island 방문 표시 및 환경 부담금 총합(totalCharge)에 합산
 * 	2.4. 모든 섬에 대해 반복(adjacentIsland), 이미 방문한 섬이면 다음 반복 수행
 * 		2.4.1. adjacentIsland 및 island와 adjacentIsland간의 
 * 			      환경 부담금을 파라미터로 TurnelToIsland 객체 생성, 큐에 추가
 * 3. totalCharge 값 반올림
 */
public class ToOnePrim {
	static class TurnelToIsland implements Comparable<TurnelToIsland>{
		int number;
		double charge;
		public TurnelToIsland(int number, double charge) {
			super();
			this.number = number;
			this.charge = charge;
		}
		@Override
		public int compareTo(TurnelToIsland other) {
			return Double.compare(charge, other.charge);
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static int islandCount;
	private static double[][] chargeRates; // 각 섬의 환경 부담금
	private static boolean[] visited;
	private static double totalCharge;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("re_sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			int start = 0;
			Queue<TurnelToIsland> queue = new PriorityQueue<>();
			queue.offer(new TurnelToIsland(start, 0));
			while (!queue.isEmpty()) {
				// 환경 부담금이 가장 낮은 섬이 나옴
				TurnelToIsland island = queue.poll();
				if (visited[island.number]) continue;
				visited[island.number] = true;
				totalCharge += island.charge;
				// 나온 섬의 인접 섬 확인 -> 인접 섬 큐에 넣음
				for (int adjacentIsland=0; 
						adjacentIsland<islandCount; 
						adjacentIsland++) {
					if (visited[adjacentIsland]) continue;
					queue.offer(new TurnelToIsland(adjacentIsland, 
							chargeRates[island.number][adjacentIsland]));
				}
			}
			builder.append("#").append(testCase).append(" ").
					append(Math.round(totalCharge)).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void initialize() throws IOException {
		islandCount = Integer.parseInt(reader.readLine().trim());
		chargeRates = new double[islandCount][islandCount];
		int[][] islandLocation = new int[islandCount][]; // 각 섬 위치
		visited = new boolean[islandCount];
		StringTokenizer locationX = new StringTokenizer(reader.readLine().trim());
		StringTokenizer locationY = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<islandCount; index++)
			islandLocation[index] = new int[] {
				Integer.parseInt(locationX.nextToken()),
				Integer.parseInt(locationY.nextToken()),
			};
		double rate = Double.parseDouble(reader.readLine().trim());
		totalCharge = 0;
		for (int islandA=0; islandA<islandCount; islandA++) 
			for (int islandB=islandA+1; islandB<islandCount; islandB++) {
				int[] islandAPosition = islandLocation[islandA];
				int[] islandBPosition = islandLocation[islandB];
				double charge = (Math.pow(islandAPosition[0]-islandBPosition[0], 2)+
						Math.pow(islandAPosition[1]-islandBPosition[1], 2))*rate;
				chargeRates[islandA][islandB] = charge;
				chargeRates[islandB][islandA] = charge;
			}
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	
}
