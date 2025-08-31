package m8.d29;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 현재 위치 정보 큐에 입력(PositionStatus)
 * 	1.1. 수연이 위치(x, y), 수연이 이동 방향(direction), 
 * 		  방문 시간(time), 지도 상태(map)
 * 2. 수연이 위치 방문 true(visited)
 * 3. 큐에 요소가 있는 동안 반복
 * 	3.1. 요소 deque (currentStatus)
 * 		3.1.1. 지도는 복제본 반환 (copiedMap)
 * 	3.2. 현재 위치가 여신의 위치라면?
 * 		3.2.1. 도달 횟수(totalAchieved) 1 증가, 
 * 				 최소 시간(minimumTime) 비교
 * 	3.3. * 확장
 * 		3.3.1. 지도의 모든 요소 탐색 (devilX, Y)
 * 		3.3.2. 해당 위치가 *이 아니거나, 해당 시점에서 
 * 				 생성(nowGenerated)되었다면 다음 반복 수행
 * 		3.3.3. 해당 위치 4방향 탐색 (nextDevilX, Y)
 * 			3.3.3.1. 탐색 위치가 영역 밖에 있거나, X이거나, *이거나 D면 다음 반복 실행
 * 			3.3.3.2. 탐색 위치에 수연이가 있었다면? 반복 실행 종료 및 다음 큐 요소 탐색
 * 			3.3.3.3. 탐색 위치 *로 지정, 해당 시점 생성 여부 true
 * 	3.4. 수연이 이동 방향에 대해 반복 (nextX, nextY)
 * 		3.4.1. 해당 위치가 영역 밖에 있거나, X이거나, *이거나, 
 * 				 이미 방문한 위치거나, 현재 들어온 위치 반대 방향이면 다음 반복 실행
 * 		3.4.2. 탐색 위치 방문 true
 * 		3.4.3. 현재 위치 . 표시
 * 		3.4.4. 다음 위치 S 표시
 * 		3.4.5. 다음 이동 위치, 다음 이동 방향, 방문 시간+1, 지도 상태를 파라미터로
 * 				 현재 위치 정보 객체 생성, 큐에 입력
 * 4. 도달 횟수가 0보다 크다면 최소 시간을, 0이라면 GAME OVER 출력
 */
public class OhMyGirlGod {
	static class PositionStatus {
		int y;
		int x;
		int direction;
		int time;
		char[][] map;
		public PositionStatus(int y, int x, 
				int direction, int time, char[][] map) {
			super();
			this.y = y;
			this.x = x;
			this.direction = direction;
			this.time = time;
			this.map = map;
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int rowCount;
	private static int columnCount;
	private static char[][] originMap;
	private static int[] startPosition;
	private static int[] girlGodPosition;
	private static int totalAchieved;
	private static int minimumTime;
	private static boolean[][] visited;

	public static void main(String[] args) throws IOException{
//		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			Queue<PositionStatus> queue = initializeQueue();
			while (!queue.isEmpty()) proceedPositionStatus(queue);
			builder.append("#").append(testCase).append(" ").
					append(totalAchieved > 0 ? minimumTime : "GAME OVER").
					append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static Queue<PositionStatus> initializeQueue() {
		// logic
		Queue<PositionStatus> queue = new ArrayDeque<PositionStatus>();
		// 초기 수연이 이동방향 입력
		for (int nextDirection=0; nextDirection<4; nextDirection++) {
			int nextY = startPosition[0]+delta[nextDirection][0];
			int nextX = startPosition[1]+delta[nextDirection][1];
			char[][] copiedMap = copyMap(originMap);
			if (!accessAble(nextX, nextY, copiedMap)) continue;
			visited[nextY][nextX] = true;
			copiedMap[startPosition[0]][startPosition[1]] = '.';
			copiedMap[nextY][nextX] = 'S';
			queue.offer(new PositionStatus(nextY, nextX, 
					nextDirection, 1, copiedMap));
		}
		return queue;
	}
	private static void proceedPositionStatus(Queue<PositionStatus> queue) {
		PositionStatus currentStatus = queue.poll();
		int currentY = currentStatus.y;
		int currentX = currentStatus.x;
		int currentDirection = currentStatus.direction;
		int currentTime = currentStatus.time;
		char[][] copiedMap = currentStatus.map; // 무조건 복제된 지도가 나옴
		if (currentY == girlGodPosition[0] &&
				currentX == girlGodPosition[1]) {
			minimumTime = Math.min(minimumTime, currentTime);
			totalAchieved++;
			return;
		}
		// * 확장
		if (expandDevil(copiedMap)) return;
		// 수연이 이동
		for (int nextDirection=0; nextDirection<4; nextDirection++) {
			int nextY = currentY+delta[nextDirection][0];
			int nextX = currentX+delta[nextDirection][1];
			if (!accessAble(nextX, nextY, copiedMap) || 
					visited[nextY][nextX] || 
					Math.abs(currentDirection-nextDirection) == 2) continue;
			visited[nextY][nextX] = true;
			copiedMap[currentY][currentX] = '.';
			copiedMap[nextY][nextX] = 'S';
			queue.offer(new PositionStatus(nextY, nextX, 
					nextDirection, currentTime+1, copyMap(copiedMap)));
			copiedMap[nextY][nextX] = '.';
		}
	}
	private static boolean expandDevil(char[][] copiedMap) {
		boolean attacked = false;
		boolean[][] nowGenerated = new boolean[rowCount][columnCount];
		for (int devilY=0; devilY<rowCount && !attacked; devilY++)
			for (int devilX=0; devilX<columnCount && !attacked; devilX++) {
				if (copiedMap[devilY][devilX] != '*' || 
						nowGenerated[devilY][devilX]) continue;
				for (int deltaIndex=0; deltaIndex<4 && !attacked; deltaIndex++) {
					int nextDevilY = devilY+delta[deltaIndex][0];
					int nextDevilX = devilX+delta[deltaIndex][1];
					if (!accessAble(nextDevilX, nextDevilY, copiedMap) || 
							copiedMap[nextDevilY][nextDevilX] == 'D') continue;
					// 확장했더니 수연이 위치 -> 수연이가 타격을 받음 -> 아래서 이동 불가
					if (copiedMap[nextDevilY][nextDevilX] == 'S') attacked = true;
					copiedMap[nextDevilY][nextDevilX] = '*';
					nowGenerated[nextDevilY][nextDevilX] = true;
				}
			}
		return attacked;
	}
	private static boolean accessAble(int x, int y, char[][] field) {
		return 0<=x && x<columnCount && 0<=y && y<rowCount &&
				field[y][x] != '*' && field[y][x] != 'X';
	}
	private static char[][] copyMap(char[][] field) {
		char[][] copied = new char[rowCount][columnCount];
		for (int row=0; row<rowCount; row++)
			for (int column=0; column<columnCount; column++)
				copied[row][column] = field[row][column];
		return copied;
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		rowCount = Integer.parseInt(tokenizer.nextToken());
		columnCount = Integer.parseInt(tokenizer.nextToken());
		originMap = new char[rowCount][columnCount];
		for (int rowIndex=0; rowIndex<rowCount; rowIndex++) {
			originMap[rowIndex] = reader.readLine().trim().toCharArray();
			for (int columnIndex=0; columnIndex<columnCount; columnIndex++) {
				if (originMap[rowIndex][columnIndex] == 'D')
					girlGodPosition = new int[] {rowIndex, columnIndex};
				else if (originMap[rowIndex][columnIndex] == 'S')
					startPosition = new int[] {rowIndex, columnIndex};
			}
		}
		totalAchieved = 0;
		minimumTime = Integer.MAX_VALUE;
		visited = new boolean[rowCount][columnCount];
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		return Integer.parseInt(reader.readLine().trim());
	}
}
