package homeworks.m8.d14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. connectLine(): 코어의 전선 연결 구현
 * 	1.1. 파라미터에는 코어 위치(index), 
 * 			연결한 코어 갯수(coreCount), 
 * 			연결한 선 길이(connectLength)
 * 2. 모든 전선에 대해 확인이 끝났다면? - coreCount와 maxCoreCount 비교
 * 	2.1. coreCount가 더 크면 maxCoreCount 입력 및 
 * 			connectLength를 maxConnectLength에 입력 
 * 	2.2. 두 값이 동일하면 connectLength와 maxConnectLength 비교 -> 더 작은 값 입력
 * 3. 각각 연결x, 북, 동, 남, 서쪽 방향 연결 가능 확인 및 연결 구현
 * 4. 연결하지 않는 경우
 * 	4.1. 다음 core 위치를 파라미터로 connectLine 재귀 호출
 * 5. 어느 방향이든 연결하는 경우
 * 	5.1. 그 방향으로 연결할 수 있는지 확인 (field 맨 끝 위치까지 1이 나오지 않으면!)
 * 	5.2. 연결할 수 있다면 그 방향 모두 1로 채움
 * 		5.2.1. 1로 채우면서 connectLine 1씩 증가
 * 	5.3. 다음 코어 위치, 연결 코어 갯수+1, connectLength를 파라미터로 connectLine 재귀 호출
 * 	5.4. 연결 이후 그 방향 모두 0으로 바꿈
 * 		5.4.1. 0으로 바꾸면서 connectLine 1씩 감소
 */
public class Maxinose {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int length;
	private static int[][] field;
	private static int maxCoreCount;
	private static int maxConnectLength;
	private static List<int[]> needCores;
	
	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			connectLine(0, maxCoreCount, 0);
			builder.append("#").append(testCase).
					append(" ").append(maxConnectLength).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void connectLine(int coreIndex, 
			int coreCount, int connectLength) {
		if (coreIndex == needCores.size()) {
			if (coreCount > maxCoreCount) {
				maxCoreCount = coreCount;
				maxConnectLength = connectLength;
			} else if (coreCount == maxCoreCount)
				maxConnectLength = Math.min(maxConnectLength, connectLength);
			return;
		}
		// 연결하지 않는 경우
		connectLine(coreIndex+1, coreCount, connectLength);
		int coreRow = needCores.get(coreIndex)[0];
		int coreColumn = needCores.get(coreIndex)[1];
		// 북쪽으로 연결
		boolean connectAble = false;
		for (int row=coreRow-1; row>=0; row--) {
			if (field[row][coreColumn] == 1)
				break;
			if (row == 0) connectAble = true;
		}
		if (connectAble) {
			for (int row=coreRow-1; row>=0; row--) {
				field[row][coreColumn] = 1;
				connectLength++;
			}
			connectLine(coreIndex+1, coreCount+1, connectLength);
			for (int row=coreRow-1; row>=0; row--) {
				field[row][coreColumn] = 0;
				connectLength--;
			}
		}
		// 동쪽으로 연결
		connectAble = false;
		for (int column=coreColumn+1; column<length; column++) {
			if (field[coreRow][column] == 1)
				break;
			if (column == length-1) connectAble = true;
		}
		if (connectAble) {
			for (int column=coreColumn+1; column<length; column++) {
				field[coreRow][column] = 1;
				connectLength++;
			}
			connectLine(coreIndex+1, coreCount+1, connectLength);
			for (int column=coreColumn+1; column<length; column++) {
				field[coreRow][column] = 0;
				connectLength--;
			}
		}
		// 남쪽으로 연결
		connectAble = false;
		for (int row=coreRow+1; row<length; row++) {
			if (field[row][coreColumn] == 1)
				break;
			if (row == length-1) connectAble = true;
		}
		if (connectAble) {
			for (int row=coreRow+1; row<length; row++) {
				field[row][coreColumn] = 1;
				connectLength++;
			}
			connectLine(coreIndex+1, coreCount+1, connectLength);
			for (int row=coreRow+1; row<length; row++) {
				field[row][coreColumn] = 0;
				connectLength--;
			}
		}
		// 서쪽으로 연결
		connectAble = false;
		for (int column=coreColumn-1; column>=0; column--) {
			if (field[coreRow][column] == 1)
				break;
			if (column == 0) connectAble = true;
		}
		if (connectAble) {
			for (int column=coreColumn-1; column>=0; column--) {
				field[coreRow][column] = 1;
				connectLength++;
			}
			connectLine(coreIndex+1, coreCount+1, connectLength);
			for (int column=coreColumn-1; column>=0; column--) {
				field[coreRow][column] = 0;
				connectLength--;
			}
		}
	}
	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		length = Integer.parseInt(reader.readLine().trim());
		field = new int[length][length];
		maxCoreCount = 0;
		maxConnectLength = 0;
		needCores = new ArrayList<int[]>();
		for (int row=0; row<length; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<length; column++) {
				field[row][column] = Integer.parseInt(tokenizer.nextToken());
				if (field[row][column] == 1) {
					if (row == 0 || row == length-1 ||
							column == 0 || column == length-1) 
						maxCoreCount++;
					else needCores.add(new int[] {row, column});
				}
			}
		}
	}
}
