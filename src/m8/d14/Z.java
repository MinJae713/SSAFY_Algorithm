package m8.d14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 입력 pow 값 해당 배열 길이 출력
 * 2. startRow, Column은 0, 위에서 구한 길이(length), 시작 숫자(startNumber)는 0으로 하여 getTargetNumber 메소드 호출
 * 	2.1. startRow, startColumn이 각각 targetRow, targetColumn과 동일하다면?
 * 		2.1.1. result에는 파라미터로 받은 startNumber 값 입력
 * 	2.2. targetRow가 startRow~startRow+length/2 안에, targetColumn이 startColumn~startColumn+length/2 안에 있다면?
 * 		2.2.1. 파라미터는 startRow, startColumn, length/2, startNumber로 하여 getTargetNumber 재귀호출
 * 	2.3. targetRow가 startRow+length/2~startRow+length 안에, targetColumn이 startColumn~startColumn+length/2 안에 있다면?
 * 		2.3.1. 파라미터는 startRow+length/2, startColumn, length/2, startNumber+(length*length)/4로 하여 getTargetNumber 재귀호출
 * 	2.4. targetRow가 startRow~startRow+length/2 안에, targetColumn이 startColumn+length/2~startColumn+length 안에 있다면?
 * 		2.4.1. 파라미터는 startRow, startColumn+length/2, length/2, startNumber+(length*length)/2로 하여 getTargetNumber 재귀호출
 * 	2.4. targetRow가 startRow+length/2~startRow+length 안에, targetColumn이 startColumn+length/2~startColumn+length 안에 있다면?
 * 		2.4.1. 파라미터는 startRow+length/2, startColumn+length/2, length/2, startNumber+(3*length*length)/4로 하여 getTargetNumber 재귀호출
 */
public class Z {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int pow;
	private static int targetRow;
	private static int targetColumn;
	private static double result;

	public static void main(String[] args) throws IOException {
		initialize();
		double length = Math.pow(2, pow);
		getTargetNumber(0, 0, length, 0);
		System.out.println((int)result);
		reader.close();
	}
	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		pow = Integer.parseInt(tokenizer.nextToken());
		targetRow = Integer.parseInt(tokenizer.nextToken());
		targetColumn = Integer.parseInt(tokenizer.nextToken());
	}
	private static void getTargetNumber(double startRow, double startColumn, double length, double startNumber) {
		if (targetRow == startRow && targetColumn == startColumn)
			result = startNumber;
		else if (startRow <= targetRow && targetRow < startRow+length/2 &&
					startColumn <= targetColumn && targetColumn < startColumn+length/2)
			getTargetNumber(startRow, startColumn, length/2, startNumber);
		else if (startRow <= targetRow && targetRow < startRow+length/2 &&
					startColumn+length/2 <= targetColumn && targetColumn < startColumn+length)
			getTargetNumber(startRow, startColumn+length/2, length/2, startNumber+(length*length)/4);
		else if (startRow+length/2 <= targetRow && targetRow < startRow+length &&
					startColumn <= targetColumn && targetColumn < startColumn+length/2)
			getTargetNumber(startRow+length/2, startColumn, length/2, startNumber+(length*length)/2);
		else if (startRow+length/2 <= targetRow && targetRow < startRow+length &&
					startColumn+length/2 <= targetColumn && targetColumn < startColumn+length)
			getTargetNumber(startRow+length/2, startColumn+length/2, length/2, startNumber+(3*length*length)/4);
	}
}
