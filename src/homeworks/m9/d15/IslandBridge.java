package homeworks.m9.d15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
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
 * 	4.2. currentDelta 방향으로 다음 위치 값 확인,
 * 		  다음 위치가 범위 내에 있고, 0인 동안 반복 (do-while)
 * 		4.2.1. 다음 위치가 0이라면 섬 간 거리 (distance) 1 증가
 * 	4.3. 다음 위치를 못찾았다면 continue
 * 	4.4. distance가 1이면 continue
 * 	4.5. betweenDistance의 current 위치 번호, 찾은 섬 위치 번호 
 * 		  해당 값을 distance로 지정
 * 		4.5.1. 해당 위치가 초기값이 아니라면? 
 * 			      기존 위치 값과 distance 중 작은 값 입력
 * 5. 각 섬간 가중치가 최소인 다리들 선택 (Prim)
 * 	5.1. 1번 섬 간선 가중치 (bridgeMinWeight) 0으로 지정
 * 	5.2. 1번 섬 가중치 우선순위 큐(islandWeightQueue)에 입력
 * 	5.3. 큐에 요소가 있는 동안 반복
 * 		5.3.1. 큐에서 요소 poll (current)
 * 		5.3.2. current 해당 bridgeMinWeight값을 
 * 			      총 다리 길이(totalDistance)에 합산
 * 		5.3.3. 방문 섬 개수(count) 1 증가
 * 		5.3.4. 모든 위치 섬들에 대해 반복 (nextIsland)
 * 			5.3.4.1. 해당 위치 섬간 거리가 0이거나, 초기값이면 continue
 * 			5.3.4.2. nextIsland 위치 bridgeMinWeight보다
 * 					 current-nextIsland 해당 가중치가 더 작다면?
 * 			5.3.4.3. nextIsland 위치 bridgeMinWeight를
 * 					 current-nextIsland 해당 가중치로 입력
 * 			5.3.4.4. islandWeightQueue에 nextIsland offer
 *
 */
public class IslandBridge {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int rowCount;
	private static int columnCount;
	private static int[][] map;
	private static boolean[][] visited;
	private static Queue<int[]> bridgeCheckQueue; 
	// 각 섬들의 테두리 위치 및 확인 방향 입력
	private static int islandCount;
	private static int[][] betweenDistance;
	private static int[] bridgeMinWeight;
	// 섬간 거리 및 가중치 기록 - islandCount 결정 이후 초기화 됨
	private static int totalDistance;
	private static int visitedCount;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		for (int row=0; row<rowCount; row++) {
			for (int column=0; column<columnCount; column++)  {
				if (map[row][column] == 0 || 
						visited[row][column]) continue;
				checkSameIsland(column, row, ++islandCount);
			}
		}
		forLog();
		System.out.println(islandCount);
		
		System.out.println(visitedCount == 0 ? -1 : totalDistance);
		reader.close();
	}

	private static void checkSameIsland(int x, int y, int number) {
		Queue<int[]> queue = new ArrayDeque<>();
		queue.offer(new int[] {y, x});
		visited[y][x] = true;
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
					visited[nextY][nextX] = true;
					map[nextY][nextX] = number;
					queue.offer(new int[] {nextY, nextX});
				}
			}
		}
	}

	private static boolean checkNext(int nextY, int nextX) {
		return 0<=nextY && nextY<rowCount && 
				0<=nextX && nextX<columnCount &&
				!visited[nextY][nextX];
	}

	private static void forLog() {
		for (int row=0; row<rowCount; row++) {
			for (int column=0; column<columnCount; column++) 
				System.out.printf("%-2d", map[row][column]);
			System.out.println();
		}
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		tokenizer = new StringTokenizer(reader.readLine().trim());
		rowCount = Integer.parseInt(tokenizer.nextToken());
		columnCount = Integer.parseInt(tokenizer.nextToken());
		map = new int[rowCount][columnCount];
		visited = new boolean[rowCount][columnCount];
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
