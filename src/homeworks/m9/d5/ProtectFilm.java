package homeworks.m9.d5;

import java.io.BufferedReader;
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
 * 	2.1. 파라미터는 약물 투입 횟수(drugCount), 투입 행 번호(row)
 * 	2.2. row가 행 길이와 같은 경우 - 해당 필름 상태가 합격인지 아닌지 확인
 * 		2.2.1. 합격이라면 drugCount와 최소 약물 투입 횟수 비교
 * 		2.2.2. 함수 실행 종료
 * 	2.3. row 위치 필름 상태 입력(origin)
 * 	2.4. drugCount는 그대로, row+1을 파라미터로 checkPassAvailable() 재귀 호출
 * 	2.5. row 위치 필름 0으로 입력
 * 	2.6. drugCount+1, row+1을 파라미터로 checkPassAvailable() 재귀 호출
 * 	2.7. row 위치 필름 2^열 길이-1로 입력
 * 	2.8. drugCount+1, row+1을 파라미터로 checkPassAvailable() 재귀 호출
 * 	2.9. row 위치 필름 origin으로 입력
 * 3. 필름 상태 검사
 * 	3.1. 합격 기준 개수가 1이면 무조건 true
 * 	3.2. 각 열마다 반복 (column)
 * 		3.2.1. 해당 열 검사 여부 false로 초기화
 * 	3.3. 첫 행~합격 기준 개수 번째 행의 0, 1 개수 계산 (zeroOneFlag)
 * 	3.4. 첫 번째 행~행 길이-합격 기준 개수 위치까지 반복 (row)
 * 		3.4.1. 해당 열 검사 여부가 false인 동안 수행
 * 		3.4.2. zeroOneFlag의 0 혹은 1의 개수가 
 * 			      합격 기준 개수라면 해당 열 검사 여부 true 지정
 * 		3.4.3. 열 검사 여부가 true거나 row+합격 기준 개수의 행 길이와 같다면 break
 * 		3.4.4. row 위치 zeroOneFlag 값 1 감소
 * 		3.4.5. row+합격 기준 개수 위치 zeroOneFlag 값 1 증가
 * 	3.5. 해당 열 검사 여부가 false라면 false 반환
 * 	3.6. 모든 열을 검사한 경우 -> true 반환
 * 
 * - 백트래킹 할 여지가 없을까...?? 시간이 겨우 통과한 느낌이라...
 */
public class ProtectFilm {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int thick;
	private static int width;
	private static int passCount;
	private static int[] films;
	private static int minDrugCount;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			checkPassAvailable(0, 0);
			builder.append("#").append(testCase).append(" ").
					append(minDrugCount).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	
	private static void checkPassAvailable(int drugCount, int row) {
		if (row == thick) {
			if (checkFilm()) 
				minDrugCount = Integer.min(minDrugCount, drugCount);
			return;
		}
		int origin = films[row];
		checkPassAvailable(drugCount, row+1);
		films[row] = 0;
		checkPassAvailable(drugCount+1, row+1);
		films[row] = (1<<width)-1;
		checkPassAvailable(drugCount+1, row+1);
		films[row] = origin;
	}

	private static boolean checkFilm() {
		if (passCount == 1) return true;
		for (int column=0; column<width; column++) {
			boolean checkColumn = false;
			int columnFlag = 1<<column;
			int[] zeroOneFlag = new int[2];
			for (int row=0; row<passCount; row++)
				zeroOneFlag[(films[row] & columnFlag) >> column]++;
			for (int row=0; row<=thick-passCount && !checkColumn; row++) {
				checkColumn = zeroOneFlag[0] == passCount || zeroOneFlag[1] == passCount;
				if (checkColumn || row+passCount == thick) break;
				zeroOneFlag[(films[row] & columnFlag) >> column]--;
				zeroOneFlag[(films[row+passCount] & columnFlag) >> column]++;
			}
			if (!checkColumn) return false;
		}
		return true;
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
		minDrugCount = Integer.MAX_VALUE;
	}
}
