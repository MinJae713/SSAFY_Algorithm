package homeworks.m9.d5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * [문제 풀이] => DFS로 성공!
 * 1. 각 위치의 높이 받을 때 현재 최대 높이보다 현재 높이가 더 크거나 같다면?
 * 	1.1. 더 크다면? 위치 리스트(maxHeightPositions) 초기화
 * 	1.2. maxHeightPositions에 해당 위치 추가
 * 2. maxHeightPositions 요소에 대해 반복
 * 3. getNextPath() 호출
 * 	3.1. 파라미터는 각 요소의 위치(currentX, Y), 깎은 여부(cutted), 
 * 		  현 위치 높이(currentHeight), 등산로 길이(pathLength)
 * 	3.2. 호출 전에 각 요소 방문 여부 true 지정, 호출 이후 false 지정
 * 4. 인접 4방향에 대해 반복 (next)
 * 5. 다음 위치가 범위 밖에 있거나 이미 방문한 위치면 continue
 * 6. 다음 위치 높이가 현재 위치 높이(currentHeight)보다 작다면?
 * 	6.1. 다음 위치 방문 여부 true 지정
 * 	6.2. 다음 위치 및 깎았는지 여부(cutted), 다음 위치 높이, 
 * 		      등산로 길이+1을 파라미터로  getNextPath() 재귀 호출
 * 	6.3. 다음 위치 방문 여부 false 지정
 * 	6.4. 다음 위치 탐색 여부 true 지정
 * 7. 다음 위치 높이가 현재 위치 높이보다 크거나 같고, 현재 깎은 여부가 false라면?
 * 	7.1. 다음 위치와 현재 위치의 높이 차이 계산(difference)
 * 	7.2. difference가 최대 공사 가능 깊이(maxCut) 이상이라면? continue
 * 	7.3. 다음 위치 방문 여부 true 지정
 * 	7.4. 1+difference~maxCut 사이에서 반복(cutRange)
 * 		7.4.1. 다음 위치 및 true, 다음 위치 높이, 등산로 길이+1을 
 * 			      파라미터로  getNextPath() 재귀 호출
 * 	7.5. 다음 위치 방문 여부 false 지정
 * 	7.6. 다음 위치 탐색 여부 true 지정
 * 8. 다음 위치 탐색 여부가 false라면?
 * 	8.1. 현재 위치의 등산로 길이와 최대 등산로 길이 비교 -> 최대값으로 입력
 */
public class HikingPath {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int bound;
	private static int maxCut;
	private static int[][] mapHeights;
	private static List<int[]> maxHeightPositions;
	private static int maxPathLength;
	private static boolean[][] visited;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int[] maxPosition : maxHeightPositions) {
				int positionY = maxPosition[0];
				int positionX = maxPosition[1];
				visited[positionY][positionX] = true;
				getNextPath(positionY, positionX, false, 
						mapHeights[positionY][positionX], 1);
				visited[positionY][positionX] = false;
			}
			builder.append("#").append(testCase).append(" ").
					append(maxPathLength).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void getNextPath(int currentY, int currentX, 
			boolean cutted, int currentHeight, int pathLength) {
		boolean nextSearched = false;
		for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
			int nextY = currentY+delta[deltaIndex][0];
			int nextX = currentX+delta[deltaIndex][1];
			if (!checkNext(nextY, nextX)) continue;
			if (mapHeights[nextY][nextX] < currentHeight) {
				visited[nextY][nextX] = true;
				getNextPath(nextY, nextX, cutted, 
						mapHeights[nextY][nextX], pathLength+1);
				visited[nextY][nextX] = false;
				nextSearched = true;
			} else if (!cutted) {
				int difference = mapHeights[nextY][nextX]-currentHeight;
				if (difference >= maxCut) continue;
				visited[nextY][nextX] = true;
				for (int cutRange=1+difference; 
						cutRange<=maxCut; cutRange++)
					getNextPath(nextY, nextX, true, 
							mapHeights[nextY][nextX]-cutRange, 
							pathLength+1);
				visited[nextY][nextX] = false;
				nextSearched = true;
			}
		}
		if (!nextSearched)
			maxPathLength = Math.max(maxPathLength, pathLength);
	}

	private static boolean checkNext(int nextY, int nextX) {
		return 0<=nextY && nextY<bound && 
				0<=nextX && nextX<bound && 
				!visited[nextY][nextX];
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		bound = Integer.parseInt(tokenizer.nextToken());
		maxCut = Integer.parseInt(tokenizer.nextToken());
		mapHeights = new int[bound][bound];
		maxHeightPositions = new ArrayList<>();
		maxPathLength = 0;
		int maxHeight = 0;
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++) {
				mapHeights[row][column] = Integer.parseInt(tokenizer.nextToken());
				if (maxHeight <= mapHeights[row][column]) {
					if (maxHeight < mapHeights[row][column])
						maxHeightPositions = new ArrayList<>();
					maxHeight = mapHeights[row][column];
					maxHeightPositions.add(new int[] {row, column});
				}
			}
		}
		visited = new boolean[bound][bound];
	}

}
