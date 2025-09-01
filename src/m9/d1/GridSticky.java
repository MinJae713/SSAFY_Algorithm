package m9.d1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 배열의 각 위치에 대해 반복
 * 2. 각 위치를 파라미터로 구할 수 있는 문자열 확인(makeNumber())
 * 	2.1. 파라미터는 현재 행 및 열, 문자열 이어붙이는 객체(numberBuilder), 현재 깊이(depth)
 * 3. 현재 깊이가 6이라면?
 * 	3.1. 문자열 집합(numberSet)에 생성한 문자열 추가 및 함수 실행 종료
 * 4. 현 위치 숫자를 builder에 추가
 * 5. 4방향에 대해 반복
 * 6. 다음 방향이 범위 밖이라면 continue
 * 7. 다음 행 및 열, builder, 현재 깊이를 파라미터로 makeNumber 재귀호출
 */
public class GridSticky {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int bound;
	private static byte[][] grid;
	private static Set<String> numberSet;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int row=0; row<bound; row++)
				for (int column=0; column<bound; column++)
					makeNumber(row, column, new byte[7], 0);
			builder.append("#").append(testCase).append(" ").
						append(numberSet.size()).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void makeNumber(int row, int column, 
			byte[] numbers, int depth) {
		if (depth == 7) {
			StringBuilder numberBuilder = new StringBuilder();
			for (byte number : numbers)
				numberBuilder.append(number);
			numberSet.add(numberBuilder.toString());
			return;
		}
		numbers[depth] = grid[row][column];
		for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
			int nextRow = row+delta[deltaIndex][0];
			int nextColumn = column+delta[deltaIndex][1];
			if (!checkNext(nextRow, nextColumn)) continue;
			makeNumber(nextRow, nextColumn, numbers, depth+1);
		}
	}

	private static boolean checkNext(int row, int column) {
		return 0<=row && row<bound && 0<=column && column<bound;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		bound = 4;
		grid = new byte[bound][bound];
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++)
				grid[row][column] = Byte.parseByte(tokenizer.nextToken());
			numberSet = new HashSet<String>();
		}
	}

}
