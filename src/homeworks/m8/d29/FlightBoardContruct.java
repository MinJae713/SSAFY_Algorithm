package homeworks.m8.d29;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 활주로 각 행마다 반복
 * 	1.1. 첫 열, 끝 열은 고정하여 각 행마다 활주로 건설 가능 여부 확인 (possibleConstruct)
 * 	1.2. possibleConstruct(): 한 줄을 받아서 그 줄에 활주로를 건설 할 수 있는지 확인
 * 		1.2.1. 경사로 건설 여부(tiffContructed) 초기화
 * 		1.2.2. 각 위치의 양 옆 확인(nextIndex) - 현재 위치가 마지막 위치보다 작다면 오른쪽, 처음보다 크다면 왼쪽 확인
 * 		1.2.3. 현재 위치와 자신의 옆 위치의 높이 다르다면 아래 수행
 * 		1.2.4. 현재 위치와 자신의 옆 위치 높이 차이가 1보다 크다면? - false 반환(활주로 건설 불가)
 * 		1.2.5. 그 이외의 경우라면(현재 위치와 자신의 옆 위치 높이 차이가 1)
 * 			1.2.5.1. 자신으로부터 경사로 길이까지 떨어진 위치가 영역 너비 이상이면? false 반환
 * 			1.2.5.2. 자신의 옆 위치의 높이 저장(nextHeight)
 * 			1.2.5.3. 자신의 옆 위치~자신에서 경사로 길이만큼 떨어진 위치 만큼 반복 (checkingIndex)
 * 				1.2.5.3.1. 이미 경사로가 설치된 위치라면? false 반환
 * 				1.2.5.3.2. checkingIndex 위치의 높이가 nextHeight와 다르다면? false 반환 (단차가 있어서 건설 불가)
 * 				1.2.5.3.3. 같다면? 경사로 설치 여부 true 지정
 *			1.2.6. 반복 종료 후 -> true 반환 
 * 	1.3. 건설 가능한 활주로 수 만큼 합산 (totalContruct)
 * 2. 배열 대각선 대칭 교환
 * 	2.1. 행은 0~마지막-2위치(row), 열은 1+row~마지막-1위치(column) 마다 반복
 * 	2.2. (행, 열)위치와 (열, 행)위치값 변경
 * 3. 1번과 동일 과정 수행
 */
public class FlightBoardContruct {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int bound;
	private static int tiffLength;
	private static int[][] flightBoard;
	private static int totalConstruct;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int row=0; row<bound; row++)
				totalConstruct += possibleConstruct(flightBoard[row]) ? 1 : 0;
			symmetrySwap();
			for (int row=0; row<bound; row++)
				totalConstruct += possibleConstruct(flightBoard[row]) ? 1 : 0;
			builder.append("#").append(testCase).append(" ").
						append(totalConstruct).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static boolean possibleConstruct(int[] oneLine) {
		boolean[] tiffContructed = new boolean[bound];
		for (int index=0; index<bound; index++) {
			// 여기서 한 줄이 활주로를 놓을 수 있는가, 아닌가가 결정됨
			int difference = 0;
			int nextHeight = 0;
			if (index > 0 && oneLine[index] != oneLine[index-1]) {
				// 왼쪽 확인
				difference = oneLine[index]-oneLine[index-1];
				if (difference > 1) return false;
				else if (difference == 1) {
					if (index-tiffLength < 0) 
						return false; // 경사로가 영역 벗어남
					nextHeight = oneLine[index-1];
					for (int checkingIndex=index-1; 
							checkingIndex>=index-tiffLength; 
							checkingIndex--) {
						if (tiffContructed[checkingIndex] || 
								oneLine[checkingIndex] != nextHeight) 
							return false;
						tiffContructed[checkingIndex] = true;
					}
				}
			}
			if (index < bound-1 && oneLine[index] != oneLine[index+1]) {
				// 오른쪽 확인
				difference = oneLine[index]-oneLine[index+1];
				if (difference > 1) return false;
				else if (difference == 1) {
					// 높이 차이가 무조건 1인 상황 (오른쪽이 더 낮음)
					if (index+tiffLength >= bound) 
						return false; // 경사로가 영역 벗어남
					nextHeight = oneLine[index+1];
					for (int checkingIndex=index+1; 
							checkingIndex<=index+tiffLength; 
							checkingIndex++) {
						// 오른쪽일 때는 경사로 설치 여부 보지 않음 
						// 0부터 시작하기에 경사로가 설치가 될 일이 읎다
						if (oneLine[checkingIndex] != nextHeight) 
							return false;
						tiffContructed[checkingIndex] = true;
					}
				}
				// 오른쪽이 높은 경우는 여기서 보지 않는다! - 
				// 이유는 차피 다음 위치 기준으로 보면 왼쪽이 낮은 경우이고, 
				// 이에 따라 다음 위치에서 확인될 것이기 때문
			}
		}
		return true;
	}
	private static void symmetrySwap() {
		for (int row=0; row<bound-1; row++)
			for (int column=1+row; column<bound; column++) {
				int temp = flightBoard[row][column];
				flightBoard[row][column] = flightBoard[column][row];
				flightBoard[column][row] = temp;
			}
	}
	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		bound = Integer.parseInt(tokenizer.nextToken());
		tiffLength = Integer.parseInt(tokenizer.nextToken());
		flightBoard = new int[bound][bound];
		totalConstruct = 0;
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++)
				flightBoard[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
	}
}
