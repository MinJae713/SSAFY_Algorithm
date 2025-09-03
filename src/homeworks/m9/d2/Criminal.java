package homeworks.m9.d2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 범인의 초기 위치 및 소요 시간(1) 큐에 offer
 * 2. 방문 표시(visited) 및 위치 개수(count) 증가
 * 3. 큐에 요소가 있는 동안 반복
 * 4. 큐에서 요소 poll (currentX, Y, Time)
 * 5. 다음 시간이 제한 소요 시간(limitTime)보다 크다면 continue
 * 5. 해당 위치의 터널 유형 반환 (currentTurnelType)
 * 6. 그 터널에서 이동할 수 있는 방향에 대해 반복 (deltaType)
 * 7. 다음 위치 값 저장(nextX, nextY)
 * 8. 다음 위치가 범위 밖에 있거나, 이미 방문한 위치면 continue
 * 9. 다음 위치가 이어져 있는 터널이 아니면 continue
 * 10. 다음 위치 방문 여부 true
 * 11. count 1 증가
 * 12. 다음 위치 큐에 입력
 */
public class Criminal {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int[][] turnelDelta; // 1부터 시작
	private static boolean[][] deltaToConnectAble;
	private static int height;
	private static int width;
	private static int startY;
	private static int startX;
	private static int limitTime;
	private static int turnnelMap[][];
	private static boolean visited[][];
	private static int count;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			Queue<int[]> statusQueue = new ArrayDeque<int[]>();
			statusQueue.offer(new int[] {startY, startX, 1}); // 위치 및 소요 시간
			visited[startY][startX] = true;
			while (!statusQueue.isEmpty()) {
				int[] current = statusQueue.poll();
				int currentY = current[0];
				int currentX = current[1];
				int currentTime = current[2];
				if (currentTime+1 == limitTime) continue;
				int currentTurnelType = turnnelMap[currentY][currentX];
				for (int deltaType : turnelDelta[currentTurnelType]) {
					int nextY = currentY+delta[deltaType][0];
					int nextX = currentX+delta[deltaType][1];
					if (!checkNext(nextX, nextY)) continue;
					// 다음 위치가 이어져있는 터널인지 확인
					if (!deltaToConnectAble[deltaType][turnnelMap[nextY][nextX]]) 
						continue;
					// 여기까지 오면 이동할 수 있는 터널로 확인됨
					visited[nextY][nextX] = true;
					count++;
					statusQueue.offer(new int[] {nextY, nextX, currentTime+1});
				}
			}
			builder.append("#").append(testCase).append(" ").
					append(count).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static boolean checkNext(int x, int y) {
		return 0<=x && x<width &&
				0<=y && y<height &&
				!visited[y][x];
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		turnelDelta = new int[][] {
			{},
			{0, 1, 2, 3},
			{0, 2},
			{1, 3},
			{0, 1},
			{1, 2},
			{2, 3},
			{3, 0}
		};
		// 그쪽 방향으로 들어갈 때 들어갈 수 있는 터널은 true
		deltaToConnectAble = new boolean[][] {
			{false, true, true, false, false, true, true, false}, // 상
			{false, true, false, true, false, false, true, true}, // 우
			{false, true, true, false, true, false, false, true}, // 하
			{false, true, false, true, true, true, false, false} // 좌
		};
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		height = Integer.parseInt(tokenizer.nextToken());
		width = Integer.parseInt(tokenizer.nextToken());
		startY = Integer.parseInt(tokenizer.nextToken());
		startX = Integer.parseInt(tokenizer.nextToken());
		limitTime = Integer.parseInt(tokenizer.nextToken());
		turnnelMap = new int[height][width];
		visited = new boolean[height][width];
		count = 0;
	}

}
