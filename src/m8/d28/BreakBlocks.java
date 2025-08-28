package m8.d28;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 영역 너비에서 구슬 갯수(bizzCount) 만큼 조합 생성
 * 	1.1. 구슬은 조합은 중복 조합 -> 너비 위치가 동일할 수도 있음
 * 	1.2. 이전 조합 구성으로 모든 벽돌을 부쉈다면 다음 조합은 만들지 않음 -> 반복 종료
 * 2. 조합이 만들어지면 인덱스 0, 1, 2로 만들 수 있는 순열 생성
 * 	2.1. 순열 생성 -> 이전 조합 구성으로 모든 벽돌을 부쉈다면 다음 순열은 만들지 않음 -> 반복 종료
 * 	2.2. 순열이 만들어지면? 벽돌 정보(field) 초기화, 선택된 너비 위치(bizzColumn)마다 반복
 * 	2.3. 초기 높이(0)로부터 처음 벽돌이 나오는 위치 계산(bizzRow)
 * 	2.4. 계산을 했는데 그 열에서 벽돌이 나오는 위치를 못찾았다? -> 반복 종료
 * 		2.4.1. 그 시점까지 던진 구슬에 따라 깨진 벽돌의 수를 비교 (구슬 전부를 던진 것은 아닌거임)
 * 3. 너비 및 높이 위치, 벽돌 정보를 파라미터로 벽돌 깨기(breakBlocks)
 * 	3.1. 너비 및 높이 위치를 큐에 입력
 * 		3.1.1. 해당 위치 방문 표시
 * 	3.2. 큐에 요소가 있는 동안 반복
 * 		3.2.1. 큐에서 위치 반환(current)
 * 		3.2.2. 현재 위치값 저장(affectBound)
 * 		3.2.3. 현재 위치값은 0으로 입력
 * 		3.2.4. 부순 벽돌 수(breakCount) 1 증가
 * 		3.2.5. affectBound가 1이면 다음 반복 수행
 * 		3.2.5. 현재 위치값 및 영향 범위(북, 동, 남, 서)마다 반복
 * 			3.2.5.1. 해당 위치가 접근할 수 있는 범위가 아니거나, 
 * 					  현재 위치값이 0이라면 다음 반복 수행
 * 			3.2.5.2. 다음 위치를 큐에 입력 및 방문 표시
 * 			3.2.5.3. 이미 이전 시점에서 벽돌이 깨진 지점이라면 count가 증가되지 않도록 함
 * 	3.3. 너비의 각 위치마다 반복
 * 		3.3.1. 영역 마지막 높이로부터 처음 벽돌이 없는 위치 확인 (nonBlockIndex)
 * 			3.3.1.1. nonBlockIndex가 맨 처음 위치 이전(-1)이라면 다음 반복 수행
 * 			3.3.1.2. 그 열의 모든 높이가 벽돌로 차 있는 경우
 * 		3.3.2. nonBlockIndex에서 처음으로 벽돌이 나타나는 위치 확인 (blockIndex)
 * 			3.3.2.1. blockIndex에서 맨 처음 위치 이전(-1)이라면 다음 반복 수행
 * 			3.3.2.2. nonBlockIndex 위의 높이에 아무 벽돌도 없는 경우
 * 		3.3.3. blockIndex 위치가 초기 높이가 아닌 경우 반복
 * 			3.3.3.1. blockIndex 위치에 벽돌이 있다면  nonBlockIndex 위치와 
 * 					 blockIndex 위치 값 교환, nonBlockIndex 1 증가
 * 			3.3.3.2. blockIndex 1 증가
 * 	3.4. 부순 벽돌 수 반환
 * 4. 선택된 너비 위치에 따른 breakBlocks 실행 결과 합산, 
 * 	    최대 벽돌 깬 수(maxBreakBlocks) 비교
 * 5. 최대 벽돌 깬 수가 총 벽돌 깬 횟수인지를 반환
 * 6. 벽돌 갯수-최대 벽돌 깬 수 계산
 */
public class BreakBlocks {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int bizzCount;
	private static int width;
	private static int height;
	private static int[][] blockField;
	private static int[] selectedWidthPosition;
	private static int[] permutedIndexes;
	private static boolean[] indexVisited;
	private static int blockCount;
	private static int maxBreakBlocks;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			combinateBizz(0, 0);
			builder.append("#").append(testCase).append(" ").
					append(blockCount-maxBreakBlocks).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static boolean combinateBizz(int widthIndex, int count) {
		if (count == bizzCount) return permuteBizz(0);
		boolean allBreaked = false;
		for (int nextIndex=widthIndex; nextIndex<width && !allBreaked; nextIndex++) {
			selectedWidthPosition[count] = nextIndex;
			allBreaked = combinateBizz(nextIndex, count+1);
		}
		return allBreaked;
	}
	private static boolean permuteBizz(int count) {
		if (count == bizzCount) {
			int[][] field = copyField();
			int breakCount = 0;
			for (int bizzIndex=0; bizzIndex<bizzCount; bizzIndex++) {
				// 만든 조합별로 벽돌 깨기
				// index 순서를 경우에 따라 다시 조합해야 함 (순서)
				int bizzColumn = selectedWidthPosition[permutedIndexes[bizzIndex]];
				int bizzRow = 0;
				while (bizzRow<height && field[bizzRow][bizzColumn] == 0)
					bizzRow++;
				// 구슬을 떨어트렸는데 벽돌이 발견되지 않음 -> 그 경우는 애초에 만들 수 없는 조합
				if (bizzRow == height) break; 
				breakCount += breakBlocks(bizzColumn, bizzRow, field);
				// 블록 부순 이후 블록 정리
				resetBlockPositions(field);
			}
			maxBreakBlocks = Math.max(maxBreakBlocks, breakCount);
			return maxBreakBlocks == blockCount;
		}
		boolean allBreaked = false;
		for (int index=0; index<bizzCount && !allBreaked; index++) {
			if (indexVisited[index]) continue;
			permutedIndexes[count] = index;
			indexVisited[index] = true;
			allBreaked = permuteBizz(count+1);
			indexVisited[index] = false;
		}
		return allBreaked;
	}
	private static int[][] copyField() {
		int[][] field = new int[height][width];
		for (int row=0; row<height; row++)
			for (int column=0; column<width; column++)
				field[row][column] = blockField[row][column];
		return field;
	}
	private static int breakBlocks(int column, int row, int[][] field) {
		boolean[][] visited = new boolean[height][width];
		Queue<int[]> queue = new ArrayDeque<int[]>();
		queue.offer(new int[] {row, column});
		visited[row][column] = true;
		int breakCount = 0;
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			int affectBound = field[current[0]][current[1]];
			field[current[0]][current[1]] = 0;
			breakCount++;
			if (affectBound == 1) continue;
			for (int bound=1; bound<affectBound; bound++) 
				for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
					int nextRow = current[0]+delta[deltaIndex][0]*bound;
					int nextColumn = current[1]+delta[deltaIndex][1]*bound;
					if (!accessAble(nextRow, nextColumn, field) || 
							field[nextRow][nextColumn] == 0 || 
							visited[nextRow][nextColumn]) continue;
					queue.offer(new int[] {nextRow, nextColumn});
					visited[nextRow][nextColumn] = true;
				}
		}
		return breakCount;
	}
	private static boolean accessAble(int row, int column, int[][] field) {
		return 0<=row && row<height &&
				0<=column && column<width && 
				field[row][column] != 0;
	}
	private static void resetBlockPositions(int[][] field) {
		for (int column=0; column<width; column++) {
			int nonBlockIndex = height-1;
			while (nonBlockIndex >= 0 && field[nonBlockIndex][column] != 0)
				nonBlockIndex--;
			if (nonBlockIndex == -1) continue; // 그 열의 모든 높이에 블록이 있는 경우
			int blockIndex = nonBlockIndex;
			while (blockIndex >= 0 && field[blockIndex][column] == 0)
				blockIndex--;
			if (blockIndex == -1) continue; // nonBlock 위치부터 그 위로 모두 블록이 없는 경우
			for (; blockIndex >= 0; blockIndex--) {
				if (field[blockIndex][column] == 0) continue;
				int temp = field[blockIndex][column];
				field[blockIndex][column] = field[nonBlockIndex][column];
				field[nonBlockIndex][column] = temp;
				nonBlockIndex--;
			}
		}
	}
	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		return Integer.parseInt(reader.readLine().trim());
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		bizzCount = Integer.parseInt(tokenizer.nextToken());
		width = Integer.parseInt(tokenizer.nextToken());
		height = Integer.parseInt(tokenizer.nextToken());
		blockField = new int[height][width];
		selectedWidthPosition = new int[bizzCount];
		permutedIndexes = new int[bizzCount];
		indexVisited = new boolean[bizzCount];
		Arrays.fill(selectedWidthPosition, -1);
		blockCount = 0;
		for (int row=0; row<height; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<width; column++) {
				blockField[row][column] = Integer.parseInt(tokenizer.nextToken());
				blockCount += blockField[row][column] > 0 ? 1 : 0;
			}
		}
		maxBreakBlocks = 0;
	}
}
