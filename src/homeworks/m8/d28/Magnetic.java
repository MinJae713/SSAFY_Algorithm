package homeworks.m8.d28;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 회전 정보마다 반복
 * 2. 파라미터는 회전 자석 번호(number) 및 방향(direction), 체크 방향(checkFlag)으로 지정하여 자석 회전(wheelMagnetic())
 * 	2.1. checkFlag: -1(왼쪽) 0(자신) 1(오른쪽) => 왼쪽에서 왔다면 오른쪽으로는 가지 못하게 해야 함, 반대도 마찬가지
 * 3. 인접 자석 확인
 * 	3.1. 체크 방향이 왼쪽이 아니고, 자석 번호가 마지막 자석 번호보다 작은 경우, 자신의 오른쪽 자석 확인
 * 		3.1.1. 현재 자석(number)의 현재 위치(magneticPointer)+2한 위치의 값과
 * 			      다음 자석(number+1)의 현재 위치-2 값이 다르다면?
 * 		3.1.2. 다음 자석 번호와 현재 이동 방향에 -1을 곱한 값을 파라미터로 wheelMagnetic 재귀 호출
 * 	3.2. 체크 방향이 오른쪽이 아니고, 자석 번호가 0보다 큰 경우, 자신의 왼쪽 자석 확인
 * 		3.2.1. 현재 자석(number)의 현재 위치(magneticPointer)-2한 위치의 값과
 * 			      이전 자석(number-1)의 현재 위치+2 값이 다르다면?
 * 		3.2.2. 이전 자석 번호와 현재 이동 방향에 -1을 곱한 값을 파라미터로 wheelMagnetic 재귀 호출
 * 4. 현재 위치에서 direction 만큼 이동한 값을 현재 위치 값으로 입력 (afterTurningIndex)
 * 	4.1. 파라미터는 회전 자석 번호 및 이동 방향
 * 5. 각 자석의 현재 위치(magneticPointer)값에 2를 제곱한 값 합산
 * 	5.1 2^자석 번호의 값을 곱하며, N극이면 0, S극이면 1을 곱하여 합산
 *
 */
public class Magnetic {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int magnecticCount;
	private static int magenticNal;
	private static int[][] magnetic;
	private static int[] magneticPointer;
	private static int commandCount;
	private static int[][] commands;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int commandIndex=0; commandIndex<commandCount; commandIndex++) {
				int[] command = commands[commandIndex];
				wheelMagnetic(command[0], command[1], 0);
			}
			int total = 0;
			for (int index=0; index<4; index++)
				total += magnetic[index][magneticPointer[index]]*Math.pow(2, index);
			builder.append("#").append(testCase).
					append(" ").append(total).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void wheelMagnetic(int number, int direction, int checkFlag) {
		if (checkFlag != -1 && number < magnecticCount-1) {
			int currentMagnetType = magnetic[number][afterTurning(number, 2)];
			int nextMagnetType = magnetic[number+1][afterTurning(number+1, -2)];
			if (currentMagnetType != nextMagnetType) wheelMagnetic(number+1, direction*(-1), 1);
		}
		if (checkFlag != 1 && number > 0) {
			int currentMagnetType = magnetic[number][afterTurning(number, -2)];
			int previousMagnetType = magnetic[number-1][afterTurning(number-1, 2)];
			if (currentMagnetType != previousMagnetType) wheelMagnetic(number-1, direction*(-1), -1);
		}
		magneticPointer[number] = afterTurning(number, direction);
	}

	private static int afterTurning(int number, int direction) {
		int currentPointer = magneticPointer[number];
		currentPointer += direction;
		if (currentPointer < 0)
			currentPointer += magenticNal; // 마지막 위치
		else if (currentPointer >= magenticNal)
			currentPointer -= magenticNal; // 초기 위치
		return currentPointer;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		commandCount = Integer.parseInt(reader.readLine().trim());
		commands = new int[commandCount][];
		magnecticCount = 4;
		magenticNal = 8;
		magnetic = new int[magnecticCount][magenticNal];
		magneticPointer = new int[magnecticCount];
		for (int index=0; index<magnecticCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int nal=0; nal<magenticNal; nal++)
				magnetic[index][nal] = Integer.parseInt(tokenizer.nextToken());
		}
		for (int index=0; index<commandCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			commands[index] = new int[] {
				Integer.parseInt(tokenizer.nextToken())-1,
				Integer.parseInt(tokenizer.nextToken())*(-1)
			};
		}
	}
}
