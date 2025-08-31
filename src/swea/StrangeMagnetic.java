package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 각 열마다 반복, 첫 행이 1이거나, 두 번째 행에 0이면 다음 반복 수행
 * 	1.1. moveMagnetic(): 자석 아래쪽으로 이동 및 더 끌릴 수 있으면 재귀 호출
 * 		1.1.1. 파라미터는 끌려오는 자석 그룹중 맨 아래 자석의 행 위치(groupRow)및 
 * 				 그룹의 크기(groupSize), 그룹의 인력 크기(groupPower), 열 위치
 * 	1.2. groupRow 아래로 처음으로 나오는 자석의 행 위치 확인(toRow)
 * 		1.2.1. toRow의 크기를 늘릴 때, 거리(distance)도 같이 증가
 * 	1.3. 그룹의 마지막 위치~그룹의 처음 위치의 자석을 distance-1 만큼 이동(board)
 * 		1.3.1. 각 기존 위치는 0으로, 이동 후 위치는 1로 지정
 * 	1.4. toRow가 영역 크기와 동일하다면 함수 실행 종료
 * 	1.5. groupPower에 groupPower*1.9^이동 거리 입력
 * 	1.6. toRow부터 아래에 있는 자석이 인력이 얼마인지(targetPower)계산
 * 	1.7. toRow 1 감소(target 그룹의 마지막 자석을 가리키게 됨)
 * 	1.8. groupPower와 targetPower 비교, groupPower가 더 크다면 moveMagnetic 재귀호출
 * 		1.8.1. 파라미터는 targetRow-1, groupSize+targetPower, 
 * 				 groupPower+targetPower, column
 * 2. 맨 마지막 행의 자석 갯수 계산
 * 3. 자석 보드 배열을 대각선 대칭으로 요소 교환
 * 4. 1, 2번 로직 동일 수행
 */
public class StrangeMagnetic {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int bound;
	private static int[][] board;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			builder.append("#").append(testCase).append(" ");
			builder.append(movingMagnetics()).append(" ");
			symmetrySwap();
			builder.append(movingMagnetics()).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int movingMagnetics() {
		int count = 0;
		for (int column=0; column<bound; column++) {
			// 자석 이동 로직 - 한 열의 자석 이동 구현
			if (board[0][column]==1 && board[1][column]==0)
				moveMagnetic(0, 1, 1, column);
			if (board[bound-1][column]>0) count++;
		}
		return count;
	}

	private static void moveMagnetic(int groupRow, 
			int groupSize, double groupPower, int column) {
		// 자석 이동
		int toRow = groupRow+1;
		int distance = 0; 
		while (toRow<bound && board[toRow][column] == 0) {
			toRow++;
			distance++;
		}
		if (distance == 0) return; // 이동하지 않음 - 할 것 없음
		for (int row=groupRow; row>groupRow-groupSize; row--) {
			board[row][column] = 0;
			board[row+distance][column] = 1;
		}
		if (toRow == bound) return;
		// 자석 끌려가는지 확인
		// toRow가 bound보다 작다면 그룹 아래에 자석이 발견되었다는 것
		groupPower *= Math.pow(1.9, distance);
		int targetRow = toRow+1;
		int targetPower = 1;
		while (targetRow<bound && board[targetRow][column] == 1) {
			targetRow++; // 그룹의 끝(처음 0이 나온 위치 or bound)이 됨
			targetPower++;
		}
		targetRow--;
		if (groupPower < targetPower) return;
		moveMagnetic(targetRow, groupSize+targetPower, 
				groupPower+targetPower, column);
	}

	private static void symmetrySwap() {
		for (int row=0; row<bound-1; row++)
			for (int column=1+row; column<bound; column++) {
				int temp = board[row][column];
				board[row][column] = board[column][row];
				board[column][row] = temp;
			}
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		bound = Integer.parseInt(reader.readLine().trim());
		board = new int[bound][bound];
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++) 
				board[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
	}
}
