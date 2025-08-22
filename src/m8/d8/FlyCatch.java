package m8.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * <변수 및 함수>
 * 1. totalFlies: (0, 0)부터 특정 위치까지 파리의 갯수 합산
 * 	1.1. (3, 2)의 값이 100 이라면 그 100은 (0, 0)~(3, 2) 범위 내 파리 수 합산
 * 2. fieldWidth: 영역의 너비
 * 3. catchWidth: 파리채의 너비
 * 4. maxCatched: 잡은 파리의 최대 수
 * 5. initialize(): 변수 값 초기화
 * 6. setTotalFlies(): totalFlies 배열 초기화
 * 7. catchFlies(): 영역 별로 잡을 수 있는 파리의 수 중, 최댓값 계산
 * <로직>
 * 1. 테스트 케이스 입력
 * 2. 영역, 파리채의 너비 입력
 * 3. totalFlies 배열 생성, maxCatched 초기화
 * 4. totalFlies 배열 초기화
 * 	4.1. 특정 위치 파리 수 입력
 * 	4.2. 그 위치가 (0, 0)이면? 누적 파리 수 = 해당 위치 파리 수
 * 	4.3. x가 0이라면? 누적 파리 수 = 해당 위치 파리 수+y-1위치의 누적 파리수
 * 	4.4. y가 0이라면? 누적 파리 수 = 해당 위치 파리 수+x-1위치의 누적 파리수
 * 	4.5. 둘다 0보다 크다면? 누적 파리 수 = 해당 위치 파리 수+x-1위치의 누적 파리수+y-1위치의 누적 파리수-(x-1, y-1)위치의 누적 파리수
 * 5. catchWidth~fieldWidth 범위 내 구간 파리수 계산 - 최대 파리수 출력
 */
public class FlyCatch {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] totalFlies;
	private static int fieldWidth;
	private static int catchWidth;
	private static int maxCatched;
	
	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine());
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			setTotalFlies();
//			print();
			catchFlies();
			builder.append("#").append(testCase).append(" ").append(maxCatched).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
//	private static void print() {
//		for (int i=0; i<fieldWidth; i++) {
//			for (int j=0; j<fieldWidth; j++)
//				System.out.printf("%-5d", totalFlies[i][j]);
//			System.out.println();
//		}
//		System.out.printf("영역: %d, 파리채: %d\n", fieldWidth, catchWidth);
//	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		fieldWidth = Integer.parseInt(tokenizer.nextToken());
		catchWidth = Integer.parseInt(tokenizer.nextToken());
		maxCatched = Integer.MIN_VALUE;
		totalFlies = new int[fieldWidth][fieldWidth];
	}
	private static void setTotalFlies() throws IOException {
		for (int row=0; row<fieldWidth; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<fieldWidth; column++) {
				totalFlies[row][column] = Integer.parseInt(tokenizer.nextToken());
				if (row == 0 && column == 0);
				else if (row == 0) totalFlies[row][column] += totalFlies[row][column-1];
				else if (column == 0) totalFlies[row][column] += totalFlies[row-1][column];
				else totalFlies[row][column] += totalFlies[row][column-1]+
						totalFlies[row-1][column]-totalFlies[row-1][column-1];
			}
		}
	}
	private static void catchFlies() {
		for (int y=catchWidth-1; y<fieldWidth; y++)
			for (int x=catchWidth-1; x<fieldWidth; x++) {
				int startY = y-catchWidth+1;
				int startX = x-catchWidth+1;
				int total = totalFlies[y][x];
				if (startX == 0 && startY == 0);
				else if (startX == 0) total -= totalFlies[startY-1][x];
				else if (startY == 0) total -= totalFlies[y][startX-1];
				else {
					total -= totalFlies[startY-1][x]+totalFlies[y][startX-1];
					total += totalFlies[startY-1][startX-1];
				}
				maxCatched = Math.max(maxCatched, total);
			}
	}
}
