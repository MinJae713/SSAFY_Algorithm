package homeworks.m9.d15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 지도의 각 위치마다 반복
 * 	1.1. 호출 전, 섬의 번호는 1 증가
 * 	1.2. 해당 위치가 0이거나, 이미 방문한 위치면 continue
 * 	1.3. 해당 위치에 대해 checkSameIsland() 호출
 * 	1.4. 파라미터는 해당 열 및 행, 섬의 번호(1부터 시작)
 * 2. checkSameIsland(): 같은 섬 영역 탐색
 * 	2.1. 파라미터는 섬의 위치(x, y), 섬의 번호(number)
 * 	2.2. x, y 위치 및 섬의 번호 큐(queue)에 offer, 해당 위치 방문 표시, 
 * 		  해당 위치 번호를 섬의 번호로 입력
 * 	2.3. 큐에 요소가 있는 동안 반복 (deltaIndex)
 * 		2.3.1. 큐에서 요소 poll (current)
 * 		2.3.2. current 위치의 4방향에 대해 반복 (next)
 * 		2.3.3. next 위치가 범위 밖이거나, 이미 방문한 위치면 continue
 * 		2.3.4. next 위치가 0이면? 
 * 			2.3.4.1. current 위치 및 deltaIndex를 
 * 					  다리 체크 queue(bridgeCheckQueue)에 offer
 * 		2.3.5. 위 둘 다 아니면? next 위치 방문 표시 및 해당 위치 섬의 번호로 입력
 * 		2.3.6. next 위치 큐에 offer
 * 3. 섬의 번호 크기의 2차원 배열 생성 (betweenDistance)
 * 	3.1. 각 요소 값 최대값으로 입력
 * 	3.2. 같은 번호 위치는 0으로 지정
 * 4. bridgeCheckQueue에 요소가 있는 동안 반복
 * 	4.1. 큐에서 요소 poll (current)
 * 		4.1.1. 방향은 currentDelta로 지정
 * 	4.2. currentDelta 방향으로 다음 위치 값 확인 (next),
 * 		  다음 위치가 범위 내에 있고, 0인 동안 반복 (do-while)
 * 		4.2.1. 다음 위치가 범위 내에 있고, 0이라면 섬 간 거리 (distance) 1 증가
 * 	4.3. next 위치가 범위를 벗어났다면 continue
 * 	4.4. distance가 1이면 continue
 * 	4.5. betweenDistance의 current 위치 번호, 찾은 섬 위치 번호 
 * 		  해당 값을 distance로 지정
 * 		4.5.1. 해당 위치가 초기값이 아니라면? 
 * 			      기존 위치 값과 distance 중 작은 값 입력
 * 5. 각 섬간 가중치가 최소인 다리들 선택 (Prim)
 * 	5.1. 1번 섬 간선 가중치 (bridgeMinWeight) 0으로 지정
 * 	5.2. 1번 섬 가중치 우선순위 큐(islandWeightQueue)에 입력
 * 		5.2.1. ToIslandBridge 클래스 인스턴스 생성 (island, distance)
 * 	5.3. 큐에 요소가 있는 동안 반복
 * 		5.3.1. 큐에서 요소 poll (currentIsland)
 * 		5.3.2. 처음 방문한 섬이라면? 
 * 			5.3.2.1. currentIsland 해당 bridgeMinWeight값을 
 * 			      	총 다리 길이(totalDistance)에 합산
 * 			5.3.2.2. 섬 방문여부 true 지정
 * 			5.3.2.3. 방문 섬 개수(visitedCount) 1 증가
 * 		5.3.3. 모든 위치 섬들에 대해 반복 (nextIsland)
 * 			5.3.3.1. 해당 위치 섬간 거리가 0이거나, 초기값이면 continue
 * 			5.3.3.2. nextIsland 위치 bridgeMinWeight보다
 * 					 currentIsland-nextIsland 해당 가중치가 더 작다면?
 * 			5.3.3.3. nextIsland 위치 bridgeMinWeight를
 * 					 currentIsland-nextIsland 해당 가중치로 입력
 * 			5.3.3.4. islandWeightQueue에 nextIsland와 새로 지정한 
 * 						bridgeMinWeight값을 파라미터로 ToIslandBridge 객체 생성 및 offer
 *
 */
public class IslandBridge {
	static class ToIslandBridge 
			implements Comparable<ToIslandBridge>{
		int island;
		int distance;
		public ToIslandBridge(int island, int distance) {
			super();
			this.island = island;
			this.distance = distance;
		}
		@Override
		public int compareTo(ToIslandBridge other) {
			return Integer.compare(distance, other.distance);
		}
	}
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int rowCount;
	private static int columnCount;
	private static int[][] map;
	private static boolean[][] visitedPoint;
	private static Queue<int[]> bridgeCheckQueue; 
	// 각 섬들의 테두리 위치 및 확인 방향 입력
	private static int islandCount;
	private static int[][] betweenDistance;
	private static int[] bridgeMinWeight;
	// 섬간 거리 및 가중치 기록 - islandCount 결정 이후 초기화 됨
	private static int totalDistance;
	private static int visitedCount;
	private static boolean[] visitedIsland;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		for (int row=0; row<rowCount; row++)
			for (int column=0; column<columnCount; column++)  {
				if (map[row][column] == 0 || 
						visitedPoint[row][column]) continue;
				checkSameIsland(column, row, ++islandCount);
			}
		initializeBridge();
		setMinimumBridgeDistance();
		checkMinimumSpanningIslands(1);
		
		System.out.println(visitedCount == islandCount ? totalDistance : -1);
		reader.close();
	}

	private static void checkMinimumSpanningIslands(int startIsland) {
		bridgeMinWeight[startIsland] = 0;
		Queue<ToIslandBridge> islandWeightQueue = new PriorityQueue<ToIslandBridge>();
		islandWeightQueue.offer(new ToIslandBridge(startIsland, bridgeMinWeight[startIsland]));
		while (!islandWeightQueue.isEmpty()) {
			ToIslandBridge currentIsland = islandWeightQueue.poll();
			if (!visitedIsland[currentIsland.island]) {
				visitedIsland[currentIsland.island] = true;
				totalDistance += currentIsland.distance;
				visitedCount++;
			}
			for (int nextIsland=1; nextIsland<=islandCount; nextIsland++) {
				if (betweenDistance[currentIsland.island][nextIsland] == 0 || 
						betweenDistance[currentIsland.island][nextIsland] == Integer.MAX_VALUE) 
					continue; // 인접이 아닌 경우
				else if (bridgeMinWeight[nextIsland] > 
						betweenDistance[currentIsland.island][nextIsland]) {
					bridgeMinWeight[nextIsland] = 
							betweenDistance[currentIsland.island][nextIsland];
					islandWeightQueue.offer(new ToIslandBridge(
							nextIsland, bridgeMinWeight[nextIsland]));
				}
			}
		}
	}

	private static void checkSameIsland(int x, int y, int number) {
		Queue<int[]> queue = new ArrayDeque<>();
		queue.offer(new int[] {y, x});
		visitedPoint[y][x] = true;
		map[y][x] = number;
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
				int nextY = current[0]+delta[deltaIndex][0];
				int nextX = current[1]+delta[deltaIndex][1];
				// 범위 밖 or 방문한 섬
				if (!checkNext(nextY, nextX)) continue;
				else if (map[nextY][nextX] == 0) // 바다 - 다리를 놓아봐야 함
					bridgeCheckQueue.offer(new int[] {
							current[0], current[1], deltaIndex});
				else {
					// 같은 섬 영역
					visitedPoint[nextY][nextX] = true;
					map[nextY][nextX] = number;
					queue.offer(new int[] {nextY, nextX});
				}
			}
		}
	}
	
	private static void initializeBridge() {
		betweenDistance = new int[islandCount+1][islandCount+1];
		for (int row=1; row<=islandCount; row++) {
			Arrays.fill(betweenDistance[row], Integer.MAX_VALUE);
			betweenDistance[row][row] = 0;
		}
		bridgeMinWeight = new int[islandCount+1];
		Arrays.fill(bridgeMinWeight, Integer.MAX_VALUE);
		visitedIsland = new boolean[islandCount+1];
	}
	
	private static void setMinimumBridgeDistance() {
		while (!bridgeCheckQueue.isEmpty()) {
			int[] current = bridgeCheckQueue.poll();
			int currentDelta = current[2];
			int distance = 0;
			int nextY = current[0];
			int nextX = current[1];
			do {
				nextY += delta[currentDelta][0];
				nextX += delta[currentDelta][1];
				distance += inBound(nextY, nextX) && 
						map[nextY][nextX] == 0 ? 1 : 0;
			} while (inBound(nextY, nextX) && map[nextY][nextX] == 0);
			if (!inBound(nextY, nextX) || distance == 1) continue;
			// 여기는 섬을 찾은 경우
			int fromIsland = map[current[0]][current[1]];
			int toIsland = map[nextY][nextX];
			distance = Math.min(distance, 
					betweenDistance[fromIsland][toIsland]);
			betweenDistance[fromIsland][toIsland] = distance;
			betweenDistance[toIsland][fromIsland] = distance;
		}
	}

	private static boolean checkNext(int nextY, int nextX) {
		return inBound(nextY, nextX) && !visitedPoint[nextY][nextX];
	}

	private static boolean inBound(int nextY, int nextX) {
		return 0<=nextY && nextY<rowCount && 
				0<=nextX && nextX<columnCount;
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		tokenizer = new StringTokenizer(reader.readLine().trim());
		rowCount = Integer.parseInt(tokenizer.nextToken());
		columnCount = Integer.parseInt(tokenizer.nextToken());
		map = new int[rowCount][columnCount];
		visitedPoint = new boolean[rowCount][columnCount];
		bridgeCheckQueue = new ArrayDeque<int[]>();
		islandCount = 0;
		totalDistance = 0;
		visitedCount = 0;
		for (int row=0; row<rowCount; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<columnCount; column++)
				map[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
	}
}
