package homeworks.m9.d12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 전체 지렁이에서 절반의 지렁이를 뽑는 조합 생성
 * 	1.1. 조합 내 지렁이 번호는 fromGroup에 입력
 * 2. fromGroup과 fromGroup에 속하지 않은 지렁이 그룹 
 * 	좌표 합(각각 fromTotal, toTotal) 계산
 * 3. toTotal의 x-fromTotal의 x 구함 (differenceX)
 * 4. toTotal의 y-fromTotal의 y 구함 (differenceY)
 * 	4.1. 위 차이가 곧 지렁이의 움직임 벡터의 합이 됨
 * 	4.2. 일일이 두 지렁이끼리 짝을 지을 필요가 없음
 * 5. differenceX*differenceX+differenceY*differenceY을
 * 	벡터 합 최소 (minDifference)와 비교, 더 작은 값 입력
 */
public class WormMeeting {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int wormCount;
	private static long[][] wormPosition;
	private static boolean[] fromGroup;
	private static long minDifference;
	
	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			setWormGroup(0, 0);
			
			builder.append("#").append(testCase).append(" ").
						append(minDifference).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void setWormGroup(int startWorm, int wormIndex) {
		if (wormIndex == wormCount/2) {
			long[] fromTotal = new long[2];
			long[] toTotal = new long[2];
			for (int worm=0; worm<wormCount; worm++) {
				if (fromGroup[worm]) {
					fromTotal[0] += wormPosition[worm][0];
					fromTotal[1] += wormPosition[worm][1];
				} else {
					toTotal[0] += wormPosition[worm][0];
					toTotal[1] += wormPosition[worm][1];
				}
			}
			long differenceX = toTotal[0]-fromTotal[0];
			long differenceY = toTotal[1]-fromTotal[1];
			long vectorSize = differenceX*differenceX+
									differenceY*differenceY;
			minDifference = Math.min(minDifference, vectorSize);
			return;
		}
		for (int worm=startWorm; worm<wormCount; worm++) {
			fromGroup[worm] = true;
			setWormGroup(worm+1, wormIndex+1);
			fromGroup[worm] = false;
		}
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}

	private static void initialize() throws IOException {
		wormCount = Integer.parseInt(reader.readLine().trim());
		wormPosition = new long[wormCount][];
		fromGroup = new boolean[wormCount];
		minDifference = Long.MAX_VALUE;
		for (int worm=0; worm<wormCount; worm++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			wormPosition[worm] = new long[] {
				Long.parseLong(tokenizer.nextToken()),
				Long.parseLong(tokenizer.nextToken())
			};
		}
	}
}
