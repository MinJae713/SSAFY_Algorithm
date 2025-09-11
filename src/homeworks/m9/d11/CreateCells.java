package homeworks.m9.d11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - Cell: 줄기 세포의 속성 및 동작 표현
 * 	  ㄴ 속성: x, y(위치) power(번식력), time(경과 시간)
 *   ㄴ create(): 활성 상태(time == power+1)일 때, 번식 동작
 *     ㄴ 인접 4방향이 번식할 수 있는 위치라면 해당 위치에 
 *     	  동일 번식력, 상태 및 경과 시간은 0으로 지정하여 Cell 생성
 *     ㄴ 다음 위치에 번식할 수 있는지 확인(createAble())
 *     	  ㄴ 해당 위치에 세포가 없다면 true
 *     		ㄴ 새로 생성된 경우 -> 세포 수 1 증가
 *     	  ㄴ 해당 위치 세포가 현재 시점 생성되었고, 그 세포의 power가 더 작으면 true
 *       ㄴ 위 경우 아니면 모두 false
 *     ㄴ 번식 시 해당 위치에 지정 및 Cell 리스트에 추가, 
 *     	  및 활성 세포 수(count) 1 증가
 *     ㄴ 실행 이후 경과 시간 증가
 * 1. 입력 시 그리드(grid)의 크기는 (배양 시간(checkTime)*2+세로 크기)+
 *    (배양 시간(checkTime)*2+가로 크기)로 지정
 * 2. 각 세포의 초기 위치는 checkTime, checkTime 위치부터 지정
 * 3. 입력 받은 값이 0 보다 크다면?
 * 	3.1. 세포 리스트(cellList)에 Cell 객체 생성 및 추가
 * 	3.2. 위 Cell 객체를 해당 위치에 입력
 * 	3.3. 활성 세포 수(count) 1 증가
 * 4. 1~checkTime 까지 반복
 * 	4.1. 각 cell들 경과 시간 증가
 * 	4.2. cellList의 각 Cell들에 대해 반복
 * 	4.3. 중간에 cell이 늘어날 수 있으니, 반복 시작 시 초기 셀 개수 만큼 반복
 * 	4.4. 경과 시간이 해당 셀 power+1이라면? create() 메소드 호출
 * 	4.5. 경과 시간이 해당 셀 power+power라면? 활성 세포 수(count) 1감소
 * 		4.5.1. 4.4.와 다른 조건문으로 수행 (power가 1이면 번식 후 바로 죽음)
 */
public class CreateCells {
	static class Cell {
		int x;
		int y;
		int power;
		int time;
		public Cell(int x, int y, int power, int time) {
			super();
			this.x = x;
			this.y = y;
			this.power = power;
			this.time = time;
		}
		void create() {
			for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
				int nextY = y+delta[deltaIndex][0];
				int nextX = x+delta[deltaIndex][1];
				if (createAble(nextY, nextX)) {
					Cell cell = new Cell(nextX, nextY, power, 0);
					grid[nextY][nextX] = cell;
					cellList.add(cell);
				}
			}
		}
		boolean createAble(int nextY, int nextX) {
			if (grid[nextY][nextX] == null) {
				count++;
				return true;
			}
			else if (grid[nextY][nextX].time == 0 && 
					grid[nextY][nextX].power < power)
				return true;
			else return false;
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int height;
	private static int width;
	private static int checkTime;
	private static Cell[][] grid;
	private static List<Cell> cellList;
	private static int count;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int time=1; time<=checkTime; time++) {
				int initialSize = cellList.size();
				for (Cell cell : cellList) cell.time++;
				for (int cellIndex=0; cellIndex<initialSize; cellIndex++) {
					Cell cell = cellList.get(cellIndex);
					if (cell.time == cell.power+1) cell.create();
					if (cell.time == 2*cell.power) count--;
				}
			}
			builder.append("#").append(testCase).append(" ").
					append(count).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		height = Integer.parseInt(tokenizer.nextToken());
		width = Integer.parseInt(tokenizer.nextToken());
		checkTime = Integer.parseInt(tokenizer.nextToken());
		grid = new Cell[2*checkTime+height][2*checkTime+width];
		cellList = new ArrayList<Cell>();
		count = 0;
		for (int row=0; row<height; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<width; column++) {
				int power = Integer.parseInt(tokenizer.nextToken());
				if (power == 0) continue;
				int cellY = checkTime+row;
				int cellX = checkTime+column;
				Cell cell = new Cell(cellX, cellY, power, 0);
				grid[cellY][cellX] = cell;
				cellList.add(cell);
				count++;
			}
		}
	}
}
