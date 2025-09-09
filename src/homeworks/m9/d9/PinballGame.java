package homeworks.m9.d9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 입력 받을 때? 
 * 	1.1. 0 위치는 큐에 입력
 * 		1.1.1. x, y, 4방향 입력 -> 방향마다 입력이기에 4번 들어가야 함
 * 	1.2. 6~10 위치는 윔홀 위치 임시 저장(wormHoleMemo)에 기록 후 페어(wormHolePair)로 입력
 * 		1.2.1. wormHoleMemo: 각 위치에 웜홀 좌표를 정수로 변환한 값이 들어옴
 * 			1.2.1.1. 크기는 6~10으로 5이며, 각 위치는 6이 0부터 시작, 마지막인 10은 4에 해당
 * 		1.2.2. wormHolePair: 웜홀 짝이 되는 두 위치 값이 입력됨, 각 값은 위치 좌표를 정수로 변환한 값으로 입력
 * 			1.2.2.1. 크기는 게임판의 너비+1, 만일 a위치와 b위치가 웜홀로 연결되면?
 * 			1.2.2.2. wormHolePair[a]=b이고, wormHolePair[b]=a가 됨
 * 		1.2.3. 입력 받은 위치 값에 -6한 위치의 wormHoleMemo 값을 봄(wormHoleIndex)
 * 			1.2.3.1. 해당 위치가 초기값이면? wormHoleIndex 위치에 입력 받은 위치의 정수 형태 값 입력
 * 			1.2.3.2. 해당 위치가 초기값이 아니면? 입력 받은 위치의 정수 형태 값과 기존 wormHoleIndex 위치의 값 반환
 * 				1.2.3.2.1. 받은 값을 각각 wormHolePair에 입력 
 * 				1.2.3.2.2. wormHolePair[a]=b이고, wormHolePair[b]=a 형태로 각각 두 번 입력해야 함
 * 2. 큐에 요소가 있는 동안 반복
 * 	2.1. 점수 값 초기화(score)
 * 	2.2. 큐에서 요소 poll, 
 * 	2.3. 시작 위치(startPosition), 
 * 		  현재 위치 및 방향 (currentPosition, Direction) 초기화
 * 	2.4. 현재 위치가 종료 조건이 아닌 경우에 반복 (gameOver()) (do-while)
 * 		2.4.1. gameOver(): 현재 위치가 startPosition이거나, -1이면 true 반환
 * 			2.4.1.1. 현재 위치 값이 -1인지 보기 위해 현재 위치가 범위 내에 있는지 우선 확인(inBound())
 * 		2.4.2. 현재 이동 방향에 따른 다음 위치 구함(nextX, Y)
 * 		2.4.3. 현재 위치 값에 따라 다른 연산 수행
 * 		2.4.4. 현재 위치가 범위 밖이라면? (!inBound())
 * 			2.4.4.1. 현재 방향은 기존 현재 방향에서 2를 뺀 절대값으로 지정
 * 				2.4.4.1.1. 단, 현재 방향이 1이라면 3으로 지정
 * 			2.4.4.2. score는 1 증가
 * 			2.4.4.3. 다음 위치를 돌려놓지 않음(이미 나간 위치에서 delta 적용해서 어차피 들어오게 되어있음)
 * 		2.4.5. 현 위치 번호가 0 이하라면? 아무것도 안함
 * 		2.4.6. 현 위치의 번호가 5 이하라면? (블록)
 * 			2.4.6.1. 현 위치 번호 및 방향에 따른 다음 방향(afterBlockCollsion)을 현재 이동 방향으로 입력
 * 			2.4.6.2. score는 1 증가
 *		2.4.7. 현 위치 번호가 10 이하라면? (웜홀)
 *			2.4.7.1. 현 위치 번호를 wormHolePair에 입력하여 다음 이동 위치의 번호 반환
 *			2.4.7.2. 반환한 위치 번호를 좌표 형태로 변환 및 nextX, nextY에 입력
 *		2.4.8. 다음 위치를 현재 위치로 입력
 *	2.5. 게임 종료 이후 -> 현재 점수(score)와 최대 점수(maxScore)와 비교, 더 큰 값 입력
 */
public class PinballGame {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int[][] afterBlockCollsion;
	private static int size;
	private static int[][] gameMap;
	private static Queue<int[]> startQueue;
	private static int[] startPosition;
	private static int currentY;
	private static int currentX;
	private static int currentDirection;
	private static int[] wormHoleMemo;
	private static int[] wormHolePair;
	private static int maxScore;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			playPinball();
			builder.append("#").append(testCase).append(" ").
					append(maxScore).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void playPinball() {
		while (!startQueue.isEmpty()) {
			int score = 0;
			startPosition = startQueue.poll();
			currentY = startPosition[0];
			currentX = startPosition[1];
			currentDirection = startPosition[2];
			do {
				int nextY = currentY+delta[currentDirection][0];
				int nextX = currentX+delta[currentDirection][1];
				if (!inBound(nextY, nextX)) {
					currentDirection = currentDirection == 1 ? 3 : 
						Math.abs(currentDirection-2);
					score++;
				} else if (gameMap[nextY][nextX] <= 0);
				else if (gameMap[nextY][nextX] <= 5) {
					currentDirection = 
							afterBlockCollsion[gameMap[nextY][nextX]]
									[currentDirection];
					score++;
				} else if (gameMap[nextY][nextX] <= 10) {
					int currentNumber = positionToNumber(nextY, nextX);
					int nextNumber = wormHolePair[currentNumber];
					int[] nextPosition = numberToPosition(nextNumber);
					nextY = nextPosition[0];
					nextX = nextPosition[1];
				}
				currentY = nextY;
				currentX = nextX;
			} while (!gameOver());
			maxScore = Math.max(maxScore, score);
		}
	}

	private static boolean inBound(int nextY, int nextX) {
		return 0<=nextY && nextY<size &&
				0<=nextX && nextX<size;
	}

	private static boolean gameOver() {
		return (currentY == startPosition[0] && 
				currentX == startPosition[1]) || 
				(inBound(currentY, currentX) && 
				gameMap[currentY][currentX] == -1);
	}

	private static int positionToNumber(int row, int column) {
		return row*size+column;
	}
	
	private static int[] numberToPosition(int number) {
		int row = number/size;
		int column = number-size*row;
		return new int[] {row, column};
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		afterBlockCollsion = new int[][] {
			{-1, -1, -1, -1}, 
			{2, 3, 1, 0}, 
			{1, 3, 0, 2}, 
			{3, 2, 0, 1}, 
			{2, 0, 3, 1},
			{2, 3, 0, 1}
		};
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		size = Integer.parseInt(reader.readLine().trim());
		gameMap = new int[size][size];
		startQueue = new ArrayDeque<int[]>();
		wormHoleMemo = new int[6];
		Arrays.fill(wormHoleMemo, -1);
		wormHolePair = new int[size*size];
		maxScore = 0;
		for (int row=0; row<size; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<size; column++) {
				gameMap[row][column] = Integer.parseInt(tokenizer.nextToken());
				if (gameMap[row][column] == 0)
					for (int deltaIndex=0; deltaIndex<4; deltaIndex++)
						startQueue.offer(new int[] {row, column, deltaIndex});
				else if (6<=gameMap[row][column] && gameMap[row][column]<=10) {
					int wormHoleIndex = gameMap[row][column]-6;
					if (wormHoleMemo[wormHoleIndex] == -1)
						wormHoleMemo[wormHoleIndex] = positionToNumber(row, column);
					else {
						int fromNumber = positionToNumber(row, column);
						int toNumber = wormHoleMemo[wormHoleIndex];
						wormHolePair[fromNumber] = toNumber;
						wormHolePair[toNumber] = fromNumber;
					}
				}
			}
		}
	}
}
