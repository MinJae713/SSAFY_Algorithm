package homeworks.m9.d9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - MovingStatus: 민식이의 움직임 상태 나타냄
 * 	  ㄴ 현재 위치, 이동 거리, 갖고 있는 열쇠 종류(비트)
 * - visited: 민식이가 방문한 위치는 true로 지정
 * 	  ㄴ 이미 방문한 위치여도 민식이가 갖는 열쇠의 상태에 
 * 	      따라 갈 수 있는 지 여부가 달라짐
 *   ㄴ 따라서 3차원 배열로 구현함
 * 1. 큐에 현재 위치 입력 (MovingStatus)
 * 2. 현재 위치 방문 여부  true 지정
 * 	2.1. 비트 마스크 값은 0으로 지정
 * 3. 큐에 요소가 있는 동안 반복
 * 4. 큐에서 요소 poll
 * 5. 현재 위치 값이 1이라면?
 * 	5.1. 현재까지 이동 거리와 최소 이동 거리 비교 -> 더 작은 값 입력
 * 6. 현재 위치 값이 a~f 사이라면?
 * 	6.1. 현재 위치에서 a를 뺀 값 입력 (keyNumber)
 * 	6.2. 현재 열쇠 종류 비트에 keyNumber 해당 비트 추가
 * 		6.2.1. 1<<keyNumber와 | 연산
 * 	6.3. 4방향에 대해 반복 (next)
 * 		6.3.1. 다음 방향이 범위 밖에 있거나, 
 * 			      방문 여부가 true거나, #이라면 continue
 * 			6.3.1.1. 방문 여부는 다음 위치와 현재 열쇠 종류 비트를 입력한 결과 반환
 * 			6.3.1.2. 이미 방문한 위치여도 갖고 있는 열쇠 종류가 다르면 방문할 수 있음
 * 		6.3.2. 다음 방향이 A~F 사이라면?
 * 			6.3.2.1. 해당 위치 값에서 A 뺀 값 입력 (doorNumber)
 * 			6.3.2.2. keyNumber에 (1<<doorNumber)와 & 연산한 결과가 0이라면 continue
 * 		6.3.3. 다음 위치 및 이동 거리+1, 현재 열쇠 종류 비트를 파라미터로
 * 			   MovingStatus 생성 및 큐에 입력
 * 		6.3.4. visited의 다음 위치, 현재 열쇠 종류 비트 해당 위치 값 true 지정
 * 
 * keyPoint) 열쇠 하나하나 포함여부를 비트로 표현했다는 점, 
 * 			  방문 체크는 열쇠 포함 비트에 따라 같은 위치여도 다르게 했다는 점이 있슈
 * ㄴ 처음에 문제 접근할 때 순환이 안되게 방문 처리를 어캐하지?? 싶었는데
 *   문제를 보다보니까 같은 위치를 다시 오는 경우는 그 때 갖고 있는 키 종류가 다른 경우라는 것을 깨닫음
 * ㄴ 2,1 위치에 a,c,d라는 키를 갖고 방문했다면, 해당 위치를 a,c,d를 갖고 다시 오는 경우는
 * 	 최소한 이 문제에서는 없도록 구현해야 순환이 안생긴다는 것을 알게 됨
 * ㄴ 앞으로 BFS 문제를 풀 때 이전에 방문한 위치를 다시 방문하는 경우가 있어서 visited가 고민이다? 
 * ㄴ 그러면 그 문제에서 동일한 위치를 올 때 뭐가 달라지는지를 찾을 수 있어야 함
 * ㄴ 이 문제에서는 갖고 있는 키 종류가 다르기 때문에 갖고 있는 키 종류 하나하나를 비트 마스크로 적용하여
 *   같은 위치여도 키 종류에 따라 방문 여부를 달리 지정했고, 그에 따라 visited가 3차원으로 표현된 것임
 * ㄴ 3차원 배열이 이상하다고 생각하지 말고, 경우에 따라 적용할 수 있다는 열린 생각을 항상 가집시다!
 */
public class MoonIsFuull__Go {
	static class MovingStatus {
		int x;
		int y;
		int distance;
		int keyBit;
		public MovingStatus(int x, int y, int distance, int keyBit) {
			super();
			this.x = x;
			this.y = y;
			this.distance = distance;
			this.keyBit = keyBit;
		}
	}
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int rowCount;
	private static int columnCount;
	private static char[][] maze;
	private static int startY;
	private static int startX;
	private static int keyTypeCount;
	private static boolean[][][] visited;
	private static int minDistance;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		Queue<MovingStatus> queue = new ArrayDeque<MovingStatus>();
		queue.offer(new MovingStatus(startX, startY, 0, 0));
		visited[startY][startX][0] = true;
		while (!queue.isEmpty()) {
			MovingStatus current = queue.poll();
			int keyBit = current.keyBit;
			if (maze[current.y][current.x] == '1') {
				minDistance = Math.min(minDistance, current.distance);
				continue;
			}
			if ('a'<=maze[current.y][current.x] && 
					maze[current.y][current.x]<='f') {
				// 열쇠를 찾았으면 소유한 열쇠 종류에 추가
				int keyNumber = maze[current.y][current.x]-'a';
				keyBit |= 1<<keyNumber;
			}
			for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
				int nextY = current.y+delta[deltaIndex][0];
				int nextX = current.x+delta[deltaIndex][1];
				if (!checkNext(nextY, nextX, keyBit)) continue;
				else if ('A'<=maze[nextY][nextX] && 
						maze[nextY][nextX]<='F') {
					int doorNumber = maze[nextY][nextX]-'A';
					if ((keyBit & (1<<doorNumber)) == 0) continue;
					// 문에 해당하는 열쇠가 없으면 이동할 수 없음
				}
				queue.offer(new MovingStatus(nextX, nextY, 
						current.distance+1, keyBit));
				visited[nextY][nextX][keyBit] = true;
			}
		}
		System.out.println(minDistance == Integer.MAX_VALUE ? -1 : minDistance);
		reader.close();
	}

	private static boolean checkNext(int nextY, int nextX, int keyBit) {
		return 0<=nextY && nextY<rowCount && 
				0<=nextX && nextX<columnCount && 
				!visited[nextY][nextX][keyBit] && 
				maze[nextY][nextX] != '#';
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		rowCount = Integer.parseInt(tokenizer.nextToken());
		columnCount = Integer.parseInt(tokenizer.nextToken());
		maze = new char[rowCount][];
		keyTypeCount = 6;
		visited = new boolean[rowCount][columnCount][1<<keyTypeCount];
		minDistance = Integer.MAX_VALUE;
		for (int row=0; row<rowCount; row++) {
			maze[row] = reader.readLine().trim().toCharArray();
			for (int column=0; column<columnCount; column++)
				if (maze[row][column] == '0') {
					startY = row;
					startX = column;
				}
		}
	}

}
