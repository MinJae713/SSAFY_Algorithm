package m9.d1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 입력 받을 때 
 * 	1.1. 받은 위치가 0이라면 0 위치의 수 증가
 * 	1.2. 받은 위치가 2라면 바이러스 위치 리스트(initialVirusPositions)에 추가
 * 2. 0~세로*가로-1의 숫자 중, 3개의 수 선택 number
 * 	2.1. 해당 위치(행(row):number/columnCount, 
 * 		  열(column):number-columnCount*행)가 0이 아니라면 continue
 * 3. 조합이 완성되면?
 * 4. 바이러스 방문 배열(virusVisited) 초기화
 * 5. 현재 0 위치의 수 입력 (saftyCount)
 * 6. 선택된 3개의 수에 해당하는 위치 1로 지정
 * 7. 0의 개수 3 감소 (saftyCount)
 * 8. 바이러스 위치 리스트의 위치들 위치 큐(virusQueue)에 추가
 * 	8.1. 추가 시 해당 위치들 방문 true 지정
 * 9. 큐에 요소가 있는 동안 반복
 * 	9.1. 큐에서 요소 deque(current)
 * 	9.2. 4방향에 대해 반복 (nextX, nextY)
 * 	9.3. 다음 위치가 범위 내에 있고, 방문하지 않았으며, 0이라면?
 * 	9.4. 다음 위치 방문 여부 true
 * 	9.5. saftyCount 1 감소
 *	9.6. 다음 위치 큐에 추가
 * 10. 현재 최대 안전 구역 영역의 수(maximumSaftyCount)와
 * 	  saftyCount 비교 -> 더 큰 값으로 입력
 * 11. 1로 지정된 위치 0으로 돌려놓음
 */
public class Lab {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int rowCount;
	private static int columnCount;
	private static int[][] map;
	private static int initialSaftyCount;
	private static List<int[]> initialVirusPositions;
	private static int[] numberContained;
	private static int maximumSaftyCount;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/input.txt"));
		initialize();
		// logic
		combinationPosition(0, 0);
		System.out.println(maximumSaftyCount);
		reader.close();
	}
	
	private static void combinationPosition(int start, int count) {
		if (count == 3) {
			int saftyCount = initialSaftyCount;
			boolean[][] virusVisited = new boolean[rowCount][columnCount];
			// 벽을 세움
			for (int number : numberContained) {
				int[] position = getPosition(number);
				int row = position[0];
				int column = position[1];
				map[row][column] = 1;
				saftyCount--;
			}
			// 바이러스 전파
			Queue<int[]> virusQueue = new ArrayDeque<int[]>();
			// 초기 바이러스 위치 지정
			for (int[] position : initialVirusPositions) {
				virusQueue.offer(position);
				virusVisited[position[0]][position[1]] = true;
			}
			while (!virusQueue.isEmpty()) {
				int[] current = virusQueue.poll();
				for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
					int nextY = current[0]+delta[deltaIndex][0];
					int nextX = current[1]+delta[deltaIndex][1];
					if (!checkNext(nextY, nextX, virusVisited))
						continue;
					virusVisited[nextY][nextX] = true;
					saftyCount--;
					virusQueue.offer(new int[] {nextY, nextX});
				}
			}
			maximumSaftyCount = Math.max(maximumSaftyCount, saftyCount);
			// 벽 제거(원복)
			for (int number : numberContained) {
				int[] position = getPosition(number);
				int row = position[0];
				int column = position[1];
				map[row][column] = 0;
			}
			return;
		}
		for (int number=start; number<rowCount*columnCount; number++) {
			int[] position = getPosition(number);
			int row = position[0];
			int column = position[1];
			if (map[row][column] != 0) continue;
			numberContained[count] = number;
			combinationPosition(number+1, count+1);
		}
	}

	private static boolean checkNext(int y, int x, boolean[][] visited) {
		return 0<=y && y<rowCount &&
				0<=x && x<columnCount &&
				!visited[y][x] && map[y][x] == 0;
	}
	
	private static int[] getPosition(int number) {
		int row = number/columnCount;
		int column = number-columnCount*row;
		return new int[] {row, column};
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		rowCount = Integer.parseInt(tokenizer.nextToken());
		columnCount = Integer.parseInt(tokenizer.nextToken());
		map = new int[rowCount][columnCount];
		initialSaftyCount = 0;
		initialVirusPositions = new ArrayList<int[]>();
		for (int row=0; row<rowCount; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<columnCount; column++) {
				map[row][column] = Integer.parseInt(tokenizer.nextToken());
				if (map[row][column] == 0) initialSaftyCount++;
				else if (map[row][column] == 2)
					initialVirusPositions.add(new int[] {row, column});
			}
		}
		numberContained = new int[3];
		maximumSaftyCount = 0;
	}

}
