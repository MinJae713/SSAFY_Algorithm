package homeworks.m9.d10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 나무 높이 오름차순 정렬
 * 2. 처음~끝 나무 각각과 맨 마지막 나무의 차이 계산 (difference)
 * 3. difference의 차이를 갖는 나무는 짝수일, 홀수일 각각 몇일 물을 줘야 하는지 계산
 * 	3.1. ex) 차이가 8: 짝/홀 -> 4/0, 차이가 7: 3/1
 * 4. 모든 나무의 difference에 따른 짝수/홀수 일 합산
 * 5. 짝수일이 홀수일 보다 크거나 같다면?
 * 	5.1. 짝수일과 홀수일 차이가 1보다 큰 동안 반복
 * 		5.1.1. 짝수일 1 감소, 홀수일 2 증가
 * 6. 홀수일이 짝수일보다 크다면?
 * 	6.1. 짝수일은 0으로 지정, 홀수일은 2곱한 값-1한 값 입력
 * 	6.2. 의미상 홀수일에 들어간 값이 물을 주는 총합이 됨
 * 7. 짝수일과 홀수일 합산
 * 	7.1. 5번의 연산 결과 짝수일이 더 크다면 1 추가
 */
public class TreeHeight {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int treeCount;
	private static int[] treeHeights;
	private static int[] wateringDay;
	private static int wateringDayTotal;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			Arrays.sort(treeHeights);
			for (int tree=0; tree<treeCount-1; tree++) {
				int difference = treeHeights[treeCount-1]-treeHeights[tree];
				if (difference == 0) continue; // 물을 줄 필요 없는 나무
				int even = difference/2;
				wateringDay[0] += even;
				wateringDay[1] += difference-even*2;
			}
			if (wateringDay[0] >= wateringDay[1]) {
				while (Math.abs(wateringDay[0]-wateringDay[1]) > 1) {
					wateringDay[0]--;
					wateringDay[1] += 2;
				}
			} else {
				wateringDay[0] = 0;
				wateringDay[1] = 2*wateringDay[1]-1; // 이땐 물주는 총 일수가 여기 담김
				// 짝수일에 물을 주는 횟수(2번)은 홀수일 이틀로 나눌 수 있음
				// but 그렇다고 홀수일에 물 주는 것을 합쳐서 짝수일에 줄 수는 없음
				// ㄴ 그러면 하루에 두 나무에 물을 주겠다는거랑 같은 의미가 됨
				// 홀수일이 더 많은 경우는, 중간에 물을 안주는 날이 많을 수 밖에 없음
				// ㄴ 홀수일에 줄 수 있는 나무들은 오로지 홀수일에만 줄 수 있기 때문임
				// ㄴ 1만 필요한데 그것을 짝수일에 줄 수는 없으니...
				// 이에 따라, 이 경우는 짝수일이 몇일인가에 관계 없이 
				// 홀수일 만으로도 총 물주는 일수가 결정됨
			}
			wateringDayTotal = wateringDay[0]+wateringDay[1];
			wateringDayTotal += wateringDay[0] > wateringDay[1] ? 1 : 0;
			builder.append("#").append(testCase).append(" ").
						append(wateringDayTotal).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		treeCount = Integer.parseInt(reader.readLine().trim());
		treeHeights = new int[treeCount];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<treeCount; index++)
			treeHeights[index] = Integer.parseInt(tokenizer.nextToken());
		wateringDay = new int[2];
		wateringDayTotal = 0;
	}
}
