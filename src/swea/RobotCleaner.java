package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 각 먼지 위치를 우선순위 큐에 입력 (x, y, 번호)
 * 	1.1. 먼지 번호가 낮은 순으로 큐에 들어감
 * 2. 큐에 요소가 없는 경우 반복
 * 3. 큐에서 요소 deque (dust)
 * 4. 현재 청소기 위치(currentX, currentY)와 먼지의 위치 비교
 * 5. 청소기 위치 중심 먼지가 몇 사분면에 있는지, 
 * 	청소기 방향이 어딘지(currentDirection)에 따라 회전수 계산
 * 	5.1. 아래와 같이 방향 변경
 * 	5.2. 1사분면(청소기x<먼지x, 청소기y>먼지y)
 * 		5.2.1. 방향이 1: 방향은 2, 회전은 1 증가
 * 		5.2.2. 방향이 2: 방향은 1, 회전은 3 증가
 * 		5.2.3. 방향이 3: 방향은 2, 회전은 3 증가
 * 		5.2.4. 방향이 4: 방향은 2, 회전은 2 증가
 * 	5.3. 2사분면(청소기x>먼지x, 청소기y>먼지y)
 * 		5.3.1. 방향이 1: 방향은 4, 회전은 3 증가
 * 		5.3.2. 방향이 2: 방향은 1, 회전은 3 증가
 * 		5.3.3. 방향이 3: 방향은 1, 회전은 2 증가
 * 		5.3.4. 방향이 4: 방향은 1, 회전은 1 증가
 * 	5.4. 3사분면(청소기x>먼지x, 청소기y<먼지y)
 * 		5.4.1. 방향이 1: 방향은 4, 회전은 3 증가
 * 		5.4.2. 방향이 2: 방향은 4, 회전은 2 증가
 * 		5.4.3. 방향이 3: 방향은 4, 회전은 1 증가
 * 		5.4.4. 방향이 4: 방향은 3, 회전은 3 증가
 * 	5.5. 4사분면(청소기x<먼지x, 청소기y<먼지y)
 * 		5.5.1. 방향이 1: 방향은 3, 회전은 2 증가
 * 		5.5.2. 방향이 2: 방향은 3, 회전은 1 증가
 * 		5.5.3. 방향이 3: 방향은 2, 회전은 3 증가
 * 		5.6.4. 방향이 4: 방향은 3, 회전은 3 증가
 * 	5.6. getTurningCount()
 * 		5.6.1. 현재 방향과 다음 방향을 입력 받아서 회전 수 계산 및 반환
 * 		5.6.2. 다음 방향과 현재 방향의 차이 계산, 음수가 나오면 4를 더해서 반환
 * 6. 청소기 위치를 먼지 위치로 입력
 */
public class RobotCleaner {
	static class Dust implements Comparable<Dust>{
		int x;
		int y;
		int number;
		public Dust(int x, int y, int number) {
			super();
			this.x = x;
			this.y = y;
			this.number = number;
		}
		@Override
		public int compareTo(Dust other) {
			return Integer.compare(number, other.number);
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int bound;
	private static int[][] map;
	private static int currentX;
	private static int currentY;
	private static int currentDirection;
	private static Queue<Dust> queue;
	private static int turnningCount;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			while (!queue.isEmpty()) {
				Dust dust = queue.poll();
				int distanceX = dust.x-currentX;
				int distanceY = dust.y-currentY;
				int nextDirection = 0;
				if (distanceX>0 && distanceY<0) {
					// 1사분면
					if (currentDirection == 1) 		nextDirection = 2;
					else if (currentDirection == 2) 	nextDirection = 1;
					else if (currentDirection == 3) 	nextDirection = 2;
					else if (currentDirection == 4) 	nextDirection = 2;
				} else if (distanceX<0 && distanceY<0) {
					// 2사분면
					if (currentDirection == 1) 		nextDirection = 4;
					else if (currentDirection == 2) 	nextDirection = 1;
					else if (currentDirection == 3) 	nextDirection = 1;
					else if (currentDirection == 4) 	nextDirection = 1;
				} else if (distanceX<0 && distanceY>0) {
					// 3사분면
					if (currentDirection == 1)			nextDirection = 4;
					else if (currentDirection == 2)  nextDirection = 4;
					else if (currentDirection == 3)  nextDirection = 4;
					else if (currentDirection == 4) 	nextDirection = 3;
				} else if (distanceX>0 && distanceY>0) {
					// 4사분면
					if (currentDirection == 1)			nextDirection = 3;
					else if (currentDirection == 2) 	nextDirection = 3;
					else if (currentDirection == 3) 	nextDirection = 2;
					else if (currentDirection == 4) 	nextDirection = 3;
				}
				turnningCount += getTurningCount(currentDirection, nextDirection);
				currentX = dust.x;
				currentY = dust.y;
				currentDirection = nextDirection;
			}
			builder.append("#").append(testCase).
						append(" ").append(turnningCount).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int getTurningCount(int currentDirection, int nextDirection) {
		int turnningCount = nextDirection-currentDirection;
		if (turnningCount < 0) turnningCount += 4;
		return turnningCount;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}

	private static void initialize() throws IOException {
		bound = Integer.parseInt(reader.readLine().trim());
		map = new int[bound][bound];
		currentX = currentY = 0;
		currentDirection = 2;
		queue = new PriorityQueue<Dust>();
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++) {
				map[row][column] = Integer.parseInt(tokenizer.nextToken());
				if (map[row][column] > 0) 
					queue.offer(new Dust(column, row, map[row][column]));
			}
		}
		turnningCount = 0;
	}

}
