package homeworks.m8.d13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 행 갯수 만큼 반복(row)
 * 2. row 행 첫 번째 열에서 시작해서 마지막 열 까지 가는 경로 탐색
 * 	2.1. 탐색 성공 시 connectCount 1 증가
 * 3. 함수 호출로 받은 열(column)이 마지막 위치라면?
 * 	3.1. 경로 탐색 성공 -> true 반환
 * 4. 방향 별로 탐색
 * 	4.1. 다음 위치가 범위 내에 있고, 방문하지 않았으며, 탐색할 수 있는 경로라면?
 * 		4.1.1. 다음 위치 경로 탐색 불가 표시, 다음 위치에 대해 재귀 호출
 * 		4.1.2. 재귀 호출한 결과가 true가 되었다면? true 반환
 * 		4.1.3. 재귀 결과가 false여도 다시 되돌려놓지 않음.. 왜?
 * 		4.1.4. 경로가 이어졌으면 실제로 파이프가 놓아져 있음을 의미
 * 		4.1.5. 이어지지 않았다면 어차피 다음 경로로 갈 수 없는 위치임을 의미
 * 		4.1.6. 그래서 이미 들어간 경로라면 다시 들어가지 않게끔 함 (경로 연결 성공 여부와 무관)
 * 		4.1.7. 이걸 못찾으면 다음 행에서 경로를 찾을 때 이미 탐색한 위치인데도 다시 경로를 찾을 수 있음
 * 			4.1.7.1. 중복된 결과가 나올 수 있고, 그래서 시간 초과가 나는 것입니다.. 참고하십쇼 여러분
 * 5. 모든 방향 탐색 결과가 true가 되지 않았다면? -> false 반환
 * ps) 어우... 어려웠습니다..
 */
public class Bread {
	private static BufferedReader reader;
	private static int rowCount;
	private static int columnCount;
	private static char[][] map;
	private static int connectCount;
	private static int[] deltaNext;

	public static void main(String[] args) throws IOException {
		initialize();
		for (int row=0; row<rowCount; row++)
			if (connectPipeLine(row, 0))
				connectCount++;
		System.out.println(connectCount);
		reader.close();
	}
	private static boolean connectPipeLine(int row, int column) {
		if (column == columnCount-1)
			return true;
		for (int deltaIndex=0; deltaIndex<3; deltaIndex++) {
			int nextRow = row+deltaNext[deltaIndex];
			int nextColumn = column+1;
			if (0<=nextRow && nextRow<rowCount &&
					map[nextRow][nextColumn] == '.') {
				map[nextRow][nextColumn] = 'x';
				if (connectPipeLine(nextRow, nextColumn))
					return true; // 경로가 이어진 경우
//				map[nextRow][nextColumn] = '.';
				// 되돌려 놓지 않는 이유?
				// true가 반환되었다면 경로가 완성되서 다시 갈 수 없는 길
				// false가 반환되었다면 다음 경로 탐색에서 다시 들어왔을 때
				// 어차피 갈 수 없는 길임을 나타낼 수 있음
			}
		}
		return false;
	}
	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer tokenizer = new StringTokenizer(reader.readLine().trim());
		rowCount = Integer.parseInt(tokenizer.nextToken());
		columnCount = Integer.parseInt(tokenizer.nextToken());
		map = new char[rowCount][];
		for (int row=0; row<rowCount; row++) 
			map[row] = reader.readLine().toCharArray();
		connectCount = 0;
		deltaNext = new int[] {-1, 0, 1};
	}
}
