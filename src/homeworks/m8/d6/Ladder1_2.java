package homeworks.m8.d6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제풀이]
 * <변수 정의>
 * 1. builder: 각 테스트 케이스 별 출력 정보 저장
 * 2. N: 사다리 높이, 너비
 * 3. ladder: 사다리 정보 저장(0, 1, 2)
 * 4. currentX, currentY: 현재 사다리 내 위치
 * 5. currentDirection: 현재 이동 방향 (0: 위, 1: 오른쪽, 2: 왼쪽)
 * <로직>
 * 1. 테스트 케이스 번호 입력
 * 2. ladder 정보 입력
 * 	2.1. 마지막 줄 정보 입력 시, 값이 2인 위치 저장(currentX, currentY)
 * 3. currentY가 0 이상인 동안 반복
 * 	3.1. currentDirection이 위쪽일 때
 * 		3.1.1. currentX가 1이상이고, (currentX-1, currentY) 위치의 값이 1이면?
 * 					currentDirection은 왼쪽, currentX 값 1 감소
 *	  		3.1.2. currentX가 N-1 미만이고, (currentX+1, currentY) 위치의 값이 1이면? 
 *						currentDirection은 오른쪽, currentX 값 1 증가
 *	  		3.1.3. 둘 다 해당되지 않으면? currentY 값 1 감소
 * 	3.2. currentDirection이 오른쪽일 때
 * 		3.2.1. currentY가 1이상이고, (currentX, currentY-1) 위치의 값이 1이면?
 * 					currentDirection은 위쪽, currentY 값 1 감소
 * 		3.2.2. currentX가 N-1 미만이고, (currentX+1, currentY) 위치의 값이 1이면?
 * 					currentX 값 1 증가
 * 	3.3. currentDirection이 왼쪽일 때
 * 		3.3.1. currentY가 1이상이고, (currentX, currentY-1) 위치의 값이 1이면?
 * 					currentDirection은 위쪽, currentY 값 1 감소
 * 		3.3.2. currentX가 1이상이고, (currentX-1, currentY) 위치의 값이 1이면?
 * 					currentX 값 1 감소
 * 4. currentX 값 builder에 추가
 * 5. builder 정보 출력
 */
public class Ladder1_2 {

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		// <변수 정의>
		// 1. builder: 각 테스트 케이스 별 출력 정보 저장
		StringBuilder builder = new StringBuilder();
		// N: 사다리 높이, 너비
		int N = 100;
		// 3. ladder: 사다리 정보 저장(0, 1, 2)
		int[][] ladder = new int[N][N];
		// 4. currentX, currentY: 현재 사다리 내 위치
		int currentX = 0;
		int currentY = 0;
		// 5. currentDirection: 현재 이동 방향 (0: 위, 1: 오른쪽, 2: 왼쪽)
		int currentDirection = 0;
		// <로직>
		for (int caseIndex=1; caseIndex<=10; caseIndex++) {
			// 1. 테스트 케이스 번호 입력
			int testCase = Integer.parseInt(reader.readLine());
			builder.append("#").append(testCase).append(" ");
			// 2. ladder 정보 입력
			for (int yIndex=0; yIndex<N; yIndex++) {
				StringTokenizer line = new StringTokenizer(reader.readLine());
				for (int xIndex=0; xIndex<N; xIndex++) {
					ladder[yIndex][xIndex] = Integer.parseInt(line.nextToken());
					// 2.1. 마지막 줄 정보 입력 시, 값이 2인 위치 저장(currentX, currentY)
					if (yIndex == N-1 && ladder[yIndex][xIndex] == 2) {
						currentX = xIndex;
						currentY = yIndex;
					}
				}
			}
			// 3. currentY가 0 이상인 동안 반복
			while (currentY >= 0) {
				// 3.1. currentDirection이 위쪽일 때
				if (currentDirection == 0) {
					// 3.1.1. currentX가 1이상이고, (currentX-1, currentY) 위치의 값이 1이면?
					if (currentX >= 1 && ladder[currentY][currentX-1] == 1) {
						// currentDirection은 왼쪽, currentX 값 1 감소
						currentDirection = 2;
						currentX--;
					// currentX가 N-1 미만이고, (currentX+1, currentY) 위치의 값이 1이면?
					} else if (currentX < N-1 && ladder[currentY][currentX+1] == 1) {
						// currentDirection은 오른쪽, currentX 값 1 증가
						currentDirection = 1;
						currentX++;
					// 3.1.3. 둘 다 해당되지 않으면? currentY 값 1 감소
					} else currentY--;
				// 3.2. currentDirection이 오른쪽일 때
				} else if (currentDirection == 1) {
					// 3.2.1. currentY가 1이상이고, (currentX, currentY-1) 위치의 값이 1이면?
					if (currentY >= 1 && ladder[currentY-1][currentX] == 1) {
						// currentDirection은 위쪽, currentY 값 1 감소
						currentDirection = 0;
						currentY--;
					// 3.2.2. currentX가 N-1 미만이고, (currentX+1, currentY) 위치의 값이 1이면?
					} else if (currentX < N-1 && ladder[currentY][currentX+1] == 1)
						currentX++; // currentX 값 1 증가
				// 3.3. currentDirection이 왼쪽일 때
				} else if (currentDirection == 2) {
					// 3.3.1. currentY가 1이상이고, (currentX, currentY-1) 위치의 값이 1이면?
					if (currentY >= 1 && ladder[currentY-1][currentX] == 1) {
						// currentDirection은 위쪽, currentY 값 1 감소
						currentDirection = 0;
						currentY--;
					// 3.3.2. currentX가 1이상이고, (currentX-1, currentY) 위치의 값이 1이면?
					} else if (currentX >= 1 && ladder[currentY][currentX-1] == 1)
						currentX--; // currentX 값 1 감소
				}
			}
			// 4. currentX 값 builder에 추가
			builder.append(currentX).append("\n");
		}
		// 5. builder 정보 출력
		System.out.println(builder);
		reader.close();
	}

}
