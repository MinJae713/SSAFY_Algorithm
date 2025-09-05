package homeworks.m9.d5;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 가로 크기-1~0까지 반복 (AIndex)
 * 	1.1. 입력 받은 셀 정보가 1이면 현재 두께 위치 
 * 		  필름 값(films)의 AIndex 번째 비트에 1 추가
 * 2. checkPassAvailable(): 약물 추가 상태에 따른 
 * 							성능 검사 통과에 필요한 약물 투입 횟수 반환
 * 	2.1. 파라미터는 약품 넣은 횟수(drugCount), 약품 투여 횟수의 최솟값 반환
 * 	2.2. 필름 검사 -> 약품 투여 후 재귀 과정으로 이뤄짐
 * 3. 필름 상태 검사 (상태 검사에 실패하면 true가 반환됨)
 * 	3.1. 연속되지 않은 열(nonNumericColumn) 발견 여부 false로 지정
 * 	3.2. 첫번째 열에서 마지막 열 까지 반복(columnBit)
 * 		3.2.1. columnBit가 1<<width보다 작고,
 * 			     연속되지 않은 열이 false인 동안 수행
 * 		3.2.3. columnBit를 한 칸씩 앞으로 옮김 (column<<=1)
 * 	3.3. 연속 타입 셀 개수(numericCell) 0으로 초기화
 * 	3.4. 현재 셀 타입은 0번째 행의 column위치 셀 타입으로 지정
 * 		3.4.1. 0번째 위치 값 & columnBit
 * 	3.5. 두번째 행에서 마지막 행 까지 반복(row)
 * 		3.5.1. numermicCell이 합격 기준(passCount)보다 작은 경우 반복
 * 		3.5.2. row번째 위치 columnBit값이 현재 셀 타입과 동일하다면?
 * 			3.5.2.1. numericCell 1 증가
 * 		3.5.3. 다르다면? 
 * 			3.5.3.1. 해당 위치 columnBit 값으로 현재 셀 타입 변경, 
 * 			3.5.3.2. numericCell 0으로 초기화
 * 	3.6. numericCell이 합격 기준(passCount)보다 작다면? 
 * 		3.6.1. 연속되지 않은 열 발견 여부 true 지정
 * 	3.7. nonNumericColumn 값 반환
 * 4. 필름 상태 검사가 true라면 약품 투여
 * 	4.1. 모든 행에 대해 반복(row)
 * 	4.2. 현재 행의 원래 셀 상태(originStatus) 저장
 * 	4.3. 현재 행 약물 투여 여부(drugInputed) true 지정
 * 	4.4. 현재 행 값 ~(1<<열 개수)으로 지정 (A 약물 투여)
 * 	4.5. drugCount+1을 파라미터로 checkPassAvailable() 재귀 호출
 * 		4.5.1. 실행 결과를 currentDrugCount에 입력
 * 	4.6. 현재 행 값 0으로 지정 (B 약물 투여)
 * 	4.7. drugCount+1을 파라미터로 checkPassAvailable() 재귀 호출
 * 		4.7.1. 위 실행 결과와 currentDrugCount 값 비교, 더 작은 값 입력
 * 	4.8. 현재 행 값에 originStatus 입력
 * 	4.9. 현재 행 drugInputed false 지정
 */
public class ProtectFilm {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int thick;
	private static int width;
	private static int passCount;
	private static int[] films;
	private static boolean[] drugInputed;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			builder.append("#").append(testCase).append(" ").
					append(checkPassAvailable(0)).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int checkPassAvailable(int drugCount) {
		if (!checkFilm()) return drugCount;
		int currentDrugCount = drugCount;
		for (int row=0; row<thick; row++) {
			if (drugInputed[row]) continue;
			int originStatus = films[row];
			drugInputed[row] = true;
			films[row] = ~(1<<width);
			currentDrugCount = checkPassAvailable(drugCount+1);
			films[row] = 0;
			currentDrugCount = Integer.min(currentDrugCount, 
					checkPassAvailable(drugCount+1));
			films[row] = originStatus;
			drugInputed[row] = false;
		}
		return currentDrugCount;
	}

	private static boolean checkFilm() {
		boolean nonNumericColumn = false;
		forLog();
		for (int columnBit=1; columnBit<(1<<width) && 
				!nonNumericColumn; columnBit<<=1) {
//			System.out.printf("[%d]\n", columnBit);
			int numericCell = 1;
			int currentTypeFlag = films[0] & columnBit;
			for (int row=1; row<thick && 
					numericCell<passCount; row++) {
				int currentType = films[row] & columnBit;
//				System.out.printf("%d vs %d\n", currentTypeFlag, currentType);
				if (currentTypeFlag == currentType)
					numericCell++;
				else {
					currentTypeFlag = currentType;
					numericCell = 1;
				}
//				System.out.println(numericCell);
//				System.out.printf("%d, %d\n", row, thick);
			}
			nonNumericColumn = numericCell<passCount;
		}
//		System.out.println(nonNumericColumn);
		return nonNumericColumn;
	}
	
	private static void forLog() {
		for (int row=0; row<thick; row++) {
			for (int column=width-1; column>=0; column--)
				System.out.printf("%-3d", films[row] & (1<<column));
			System.out.println();
		}
		System.out.println();
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		thick = Integer.parseInt(tokenizer.nextToken());
		width = Integer.parseInt(tokenizer.nextToken());
		passCount = Integer.parseInt(tokenizer.nextToken());
		films = new int[thick];
		for (int row=0; row<thick; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=width-1; column>=0; column--)
				films[row] |= Integer.parseInt(tokenizer.nextToken()) << column;
		}
		drugInputed = new boolean[thick];
	}

}
