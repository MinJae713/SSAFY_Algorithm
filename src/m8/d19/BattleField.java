package m8.d19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 각 command 마다 반복
 * 2. S
 * 	2.1. 각 방향에서 바라보는 방향에 벽돌(*) 혹은 강철 벽(#-)이 있는지 확인
 * 	2.2. 있으면 벽돌 위치 값을 평지(.)로 변경
 * 3. UDLR
 * 	3.1. 각 방향에 맞춰서 direction 값 변경
 * 	3.2. delta 배열로 다음 위치 구함
 * 	3.3. 다음 위치가 이동할 수 있는 위치인지 확인
 * 		3.3.1. 배열 범위 밖에 있거나, 범위 안에는 있지만 평지가 아닌 경우는 이동 불가
 * 	3.4. 이동할 수 있다면? 
 * 		3.4.1. x, y 해당 위치는 평지(.)로 변경, 
 * 		3.4.2. x, y 값은 다음 위치로 이동
 * 	3.5. x, y 해당 위치는 direction 값에 맞춰 변경
 */
public class BattleField {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[] deltaX;
	private static int[] deltaY;
	private static char[] directionSigns;
	private static char[][] field;
	private static int fieldWidth;
	private static int fieldHeight;
	private static int x;
	private static int y;
	private static int direction;
	private static int commandCount;
	private static char[] commands;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			inputCommand();
			setResult(testCase);
		}
		System.out.print(builder);
		reader.close();
	}

	private static void inputCommand() {
		for (int commandIndex=0; commandIndex<commandCount; commandIndex++) {
			if (commands[commandIndex] == 'S') {
				// shoot
				switch (directionSigns[direction]) {
				case '^':
					for (int yPosition=y-1; yPosition>=0; yPosition--) {
						if (field[yPosition][x] == '#') break;
						else if (field[yPosition][x] == '*') {
							field[yPosition][x] = '.';
							break;
						}
					}
					break;
				case '>':
					for (int xPosition=x+1; xPosition<fieldWidth; xPosition++) 
						if (field[y][xPosition] == '#') break;
						else if (field[y][xPosition] == '*') {
							field[y][xPosition] = '.';
							break;
						}
					break;
				case 'v':
					for (int yPosition=y+1; yPosition<fieldHeight; yPosition++)
						if (field[yPosition][x] == '#') break;
						else if (field[yPosition][x] == '*') {
							field[yPosition][x] = '.';
							break;
						}
					break;
				case '<':
					for (int xPosition=x-1; xPosition>=0; xPosition--) 
						if (field[y][xPosition] == '#') break;
						else if (field[y][xPosition] == '*') {
							field[y][xPosition] = '.';
							break;
						}
					break;
				}
			} else {
				// move
				switch (commands[commandIndex]) {
				case 'U':
					direction = 0;
					break;
				case 'R':
					direction = 1;
					break;
				case 'D':
					direction = 2;
					break;
				case 'L':
					direction = 3;
					break;
				}
				int nextX = x+deltaX[direction];
				int nextY = y+deltaY[direction];
				if (moveAble(nextX, nextY)) {
					field[y][x] = '.';
					x = nextX;
					y = nextY;
				}
				field[y][x] = directionSigns[direction];
			}
		}
	}

	private static boolean moveAble(int nextX, int nextY) {
		return 0 <= nextX && nextX < fieldWidth &&
				0 <= nextY && nextY < fieldHeight &&
				field[nextY][nextX] == '.';
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		deltaY = new int[] {-1, 0, 1, 0};
		deltaX = new int[] {0, 1, 0, -1};
		directionSigns = new char[] {'^', '>', 'v', '<'};
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		fieldHeight = Integer.parseInt(tokenizer.nextToken());
		fieldWidth = Integer.parseInt(tokenizer.nextToken());
		field = new char[fieldHeight][];
		boolean positionFinded = false;
		for (int height=0; height<fieldHeight; height++) {
			field[height] = reader.readLine().trim().toCharArray();
			for (int width=0; width<fieldWidth && !positionFinded; width++) {
				char fieldElement = field[height][width];
				if (fieldElement == '<' ||
					fieldElement == '>' || 
					fieldElement == 'v' || 
					fieldElement == '^') {
					x = width;
					y = height;
					switch (fieldElement) {
					case '^':
						direction = 0;
						break;
					case '>':
						direction = 1;
						break;
					case 'v':
						direction = 2;
						break;
					case '<':
						direction = 3;
						break;
					}
					positionFinded = true;
				}
			}
		}
		commandCount = Integer.parseInt(reader.readLine().trim());
		commands = reader.readLine().trim().toCharArray();
	}
	private static void setResult(int testCase) {
		builder.append("#").append(testCase).append(" ");
		for (int row=0; row<fieldHeight; row++) {
			for (int column=0; column<fieldWidth; column++)
				builder.append(field[row][column]);
			builder.append("\n");
		}
	}
}
