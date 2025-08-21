package m8.d20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 1~맛있는 정도가 가장 높은 칸의 값 까지 반복 (day)
 * 2. day와 맛있는 정도(tasty)가 동일한 칸(ate)은 true 지정
 * 3. 덩어리 갯수(totalPiece) 확인, 가장 많은 덩어리 수(maxTotalPiece) 구함
 * 	3.1. 칸 방문 배열 생성(visited)
 * 	3.2. 이미 먹은 칸이거나, 먹은 칸이 아니어도 방문한 칸이면 다음 반복으로 실행
 * 	3.3. 현재 칸을 인접 칸 큐 (ateQueue)에 입력
 * 		3.3.1. 인접 칸 큐는 행, 열 값 지정
 * 	3.4. 현재 칸 방문 여부 true
 * 	3.5. ateQueue에 요소가 있는 동안 반복
 * 		3.5.1. ateQueue에서 요소 하나 deque
 * 		3.5.2. 해당 위치의 4 방향에 대해 반복
 * 			3.5.2.1. 범위 내에 있고, 그 방향에 ate 값이 false이며, 방문하지 않았다면?
 * 			3.5.2.2. 해당 위치 enque, visited는 true 표시
 * 	3.6. 큐 반복이 끝났다면 totalPiece 값 1 증가
 * 	3.7. totalPiece와 maxTotalPiece 비교
 */
public class Cheese {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[] deltaColumn;
	private static int[] deltaRow;
	private static int size;
	private static int[][] tasty;
	private static boolean[][] ate;
	private static int maxTasty;
	private static int maxTotalPiece;
	
	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int day=1; day<=maxTasty; day++) {
				eatCheese(day);
				calculateTotalPiece();
			}
			builder.append("#").append(testCase).
					append(" ").append(maxTotalPiece).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static void eatCheese(int day) {
		for (int row=0; row<size; row++)
			for (int column=0; column<size; column++)
				if (tasty[row][column] == day)
					ate[row][column] = true;
	}
	private static void calculateTotalPiece() {
		int totalPiece = 0;
		boolean[][] visited = new boolean[size][size];
		for (int row=0; row<size; row++) {
			for (int column=0; column<size; column++) {
				if (ate[row][column] || visited[row][column])
					continue;
				Queue<int[]> ateQueue = new ArrayDeque<int[]>();
				ateQueue.offer(new int[] {row, column});
				visited[row][column] = true;
				while (!ateQueue.isEmpty()) {
					int[] area = ateQueue.poll();
					for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
						int nextRow = area[0]+deltaRow[deltaIndex];
						int nextColumn = area[1]+deltaColumn[deltaIndex];
						if (checkSamePiece(visited, nextRow, nextColumn)) {
							ateQueue.offer(new int[] {nextRow, nextColumn});
							visited[nextRow][nextColumn] = true;
						}
					}
				}
				totalPiece++;
			}
		}
		maxTotalPiece = Math.max(maxTotalPiece, totalPiece);
	}
	private static boolean checkSamePiece(
			boolean[][] visited, int nextRow, int nextColumn) {
		return 0<=nextRow && nextRow<size &&
			0<=nextColumn && nextColumn<size &&
			!ate[nextRow][nextColumn] && 
			!visited[nextRow][nextColumn];
	}
	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		deltaColumn = new int[] {0, 1, 0, -1};
		deltaRow = new int[] {-1, 0, 1, 0};
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		size = Integer.parseInt(reader.readLine().trim());
		tasty = new int[size][size];
		ate = new boolean[size][size];
		maxTasty = Integer.MIN_VALUE;
		maxTotalPiece = 1;
		for (int row=0; row<size; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<size; column++) {
				tasty[row][column] = Integer.parseInt(tokenizer.nextToken());
				maxTasty = Math.max(maxTasty, tasty[row][column]);
			}
		}
	}
}
