package boj;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 수 입력(grid) -> N*N
 * 2. 구간 입력 (x1, y1), (x2, y2)
 * 3. 누적합 배열 생성(sumGrid) -> N*N
 * 4. setAreaSum(x, y) -> N-1 파라미터
 * 	4.1. x, y == 0 -> sumGrid(x, y) = grid(x, y)
 * 	4.2. x == 0 -> sumGrid(x, y) = grid(x, y)+sumGrid(x, y-1)
 * 	4.3. y == 0 -> sumGrid(x, y) = grid(x, y)+sumGrid(x-1, y)
 * 	4.4. else -> sumGrid(x, y) = grid(x, y)+setArea(x-1, y)+setArea(x, y-1)-setArea(x-1, y-1)
 * 	4.5. sumGrid 값이 0이라면 setAreaSum 재귀 호출
 * 5. M 입력 -> m 횟수 만큼 반복
 * 	5.1. x1, y1, x2, y2 입력
 * 	5.2. sumGrid(x2, y2)-sumGrid(x2, y1)-sumGrid(x1, y2)+sumGrid(x1, y1)
 * 	5.3. 위 계산 결과 builder에 추가
 */
public class AreaSum_11660 {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int N;
	private static int M;
	private static int[][] grid;
	private static int[][] sumGrid;
	
	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("input.txt"));
		initialize();
		setGrid();
		setAreaSum(N-1, N-1);
//		print(grid);
//		System.out.println();
//		print(sumGrid);
//		System.out.println();
		getRangeTotal();
		
		System.out.print(builder);
		reader.close();
	}
//	private static void print(int[][] grid) {
//		for (int[] row : grid) {
//			for (int cell : row)
//				System.out.printf("%-3d", cell);
//			System.out.println();
//		}
//	}
	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		tokenizer = new StringTokenizer(reader.readLine().trim());
		N = Integer.parseInt(tokenizer.nextToken());
		M = Integer.parseInt(tokenizer.nextToken());
		grid = new int[N][N];
		sumGrid = new int[N][N];
	}
	private static void setGrid() throws IOException {
		for (int y=0; y<N; y++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int x=0; x<N; x++)
				grid[y][x] = Integer.parseInt(tokenizer.nextToken());
		}
	}
	private static void setAreaSum(int x, int y) {
		if (x == 0 && y == 0)
			sumGrid[y][x] = grid[y][x];
		else if (x == 0) {
			if (sumGrid[y-1][x] == 0) setAreaSum(x, y-1);
			sumGrid[y][x] = grid[y][x]+sumGrid[y-1][x];
		}
		else if (y == 0) {
			if (sumGrid[y][x-1] == 0) setAreaSum(x-1, y);
			sumGrid[y][x] = grid[y][x]+sumGrid[y][x-1];
		}
		else {
			if (sumGrid[y-1][x-1] == 0) setAreaSum(x-1, y-1);
			if (sumGrid[y-1][x] == 0) setAreaSum(x, y-1);
			if (sumGrid[y][x-1] == 0) setAreaSum(x-1, y);
			sumGrid[y][x] = grid[y][x]+sumGrid[y][x-1]+sumGrid[y-1][x]-sumGrid[y-1][x-1];
		}
	}
	private static void getRangeTotal() throws IOException {
		for (int m=0; m<M; m++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int x1 = Integer.parseInt(tokenizer.nextToken())-1;
			int y1 = Integer.parseInt(tokenizer.nextToken())-1;
			int x2 = Integer.parseInt(tokenizer.nextToken())-1;
			int y2 = Integer.parseInt(tokenizer.nextToken())-1;
			int total = sumGrid[x2][y2];
			if (x1 == 0 && y1 == 0);
			else if (x1 == 0) total -= sumGrid[x2][y1-1];
			else if (y1 == 0) total -= sumGrid[x1-1][y2];
			else {
				total -= sumGrid[x2][y1-1]+sumGrid[x1-1][y2];
				total += sumGrid[x1-1][y1-1];
			}
			builder.append(total).append("\n");
		}
	}
}
