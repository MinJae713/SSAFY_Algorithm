package homeworks.m9.d2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 처음 위치~처음 위치+연속 접시 수 안의 요소들 만큼 반복
 * 	1.1. 해당 위치의 포함된 초밥 가지수(sushiCounts)가 0이면 초밥 종류 수 1 증가
 * 	1.2. 해당 위치 초밥 가지수 값 1 증가
 * 2. 첫 번째 초밥~마지막 초밥까지 반복(sushiIndex)
 * 	2.1. sushiIndex+연속 접시 수 위치의 초밥이 sushiCounts에 없다면? 초밥 종류 수 1증가
 * 	2.2. sushiIndex+연속 접시 수 위치 초밥 가지수(inBoundSushiCount) 값 1 증가
 * 	2.3. sushiIndex 위치의 초밥이 sushiCounts 값에서 1이라면? 초밥 종류 수 1감소
 * 	2.4. sushiIndex 위치 초밥 가지수 값 1 감소
 * 	2.5. sushiCounts에 쿠폰 번호에 해당하는 초밥이 0이라면 초밥 종류 수+1을,
 * 		  초밥이 1이라면 초밥 종류 수와 최대 초밥 종류수와 비교 -> 큰 값으로 입력
 */
public class Sushi {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int plateCount;
	private static int sushiTypeCount;
	private static int numericPlateCount;
	private static int coupon;
	private static int[] cycleSushis;
	private static int[] sushiCounts;
	private static int inBoundSushiCount;
	private static int maxSushiTypeCount;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		for (int sushiIndex=0; sushiIndex<numericPlateCount; sushiIndex++)
			addSushi(sushiIndex);
		for (int sushiIndex=0; sushiIndex<plateCount; sushiIndex++) {
			addSushi(getLastSushiIndex(sushiIndex));
			removeSushi(sushiIndex);
			maxSushiTypeCount = Math.max(maxSushiTypeCount, 
					inBoundSushiCount+(sushiCounts[coupon] == 0 ? 1 : 0));
		}
		
		System.out.println(maxSushiTypeCount);
		reader.close();
	}
	
	private static void removeSushi(int sushiIndex) {
		int sushi = cycleSushis[sushiIndex];
		if (sushiCounts[sushi] == 1)
			inBoundSushiCount--;
		sushiCounts[sushi]--;
	}

	private static int getLastSushiIndex(int sushiIndex) {
		sushiIndex += numericPlateCount;
		sushiIndex -= sushiIndex<plateCount ? 0 : plateCount;
		return sushiIndex;
	}

	private static void addSushi(int sushiIndex) {
		int sushi = cycleSushis[sushiIndex];
		if (sushiCounts[sushi] == 0)
			inBoundSushiCount++;
		sushiCounts[sushi]++;
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		plateCount = Integer.parseInt(tokenizer.nextToken());
		sushiTypeCount = Integer.parseInt(tokenizer.nextToken());
		numericPlateCount = Integer.parseInt(tokenizer.nextToken());
		coupon = Integer.parseInt(tokenizer.nextToken());
		cycleSushis = new int[plateCount];
		for (int index=0; index<plateCount; index++)
			cycleSushis[index] = Integer.parseInt(reader.readLine().trim());
		sushiCounts = new int[sushiTypeCount+1];
		inBoundSushiCount = 0;
		maxSushiTypeCount = 0;
	}

}
