package homeworks.m9.d3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 1번 집에 R,G,B를 선택했을 때 나올 수 있는 비용의 최솟값 계산
 * 	1.1. getMinimumPrice(): n번 집부터 마지막 집까지 칠한 색에 대한 최소 비용 계산
 * 	1.2. 파라미터는 색을 칠할 집(homeIndex), 칠할 색상 번호(color)
 * 2. minimumPriceMemo[][]: homeIndex번째 집에 각각 color로 R,G,B 색상을 선택했을 때 
 * 								 마지막 집까지 칠한 색까지 포함하여 최소 비용 저장
 * 	2.1. R,G,B 각각 0,1,2번 위치 해당
 * 3. homeIndex가 마지막 위치라면? homeIndex번째 집의 color 색상에 대한 비용 반환
 * 4. homeIndex가 0일 때는 1,2, 1일 때는 0,2, 2일 때는 0,1 번째 색상 선택
 * 5. 위에서 색상 선택 후, homeIndex 다음 위치의 선택 색상에 대한 minimumPriceMemo 값 확인
 * 	5.1. 지정된 값이 초기값이면? homeIndex+1과 위 색상을 파라미터로 재귀 호출 및 호출 결과 입력
 * 6. result는 minimumPriceMemo의 homeIndex 다음 위치 두 색상에 대한 값 중 
 * 	더 작은 값+homeIndex 위치 color 해당 비용 값 반환
 */
public class RGB {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int homeCount;
	private static int[][]	colorPrices;
	private static int[][] minimumPriceMemo;
	private static final int INF = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		int minimumPrice = INF;
		for (int color=0; color<3; color++) {
			minimumPriceMemo[0][color] = getMinimumPrice(0, color);
			minimumPrice = Math.min(minimumPrice, minimumPriceMemo[0][color]);
		}
		System.out.println(minimumPrice);
		reader.close();
	}

	private static int getMinimumPrice(int homeIndex, int color) {
		if (minimumPriceMemo[homeIndex][color] != INF)
			return minimumPriceMemo[homeIndex][color];
		if (homeIndex == homeCount-1)
			return colorPrices[homeIndex][color];
		int selectOne = -1;
		int selectTwo = -1;
		if (color == 0) {
			selectOne = 1;
			selectTwo = 2;
		} else if (color == 1) {
			selectOne = 0;
			selectTwo = 2;
		} else if (color == 2) {
			selectOne = 0;
			selectTwo = 1;
		}
		if (minimumPriceMemo[homeIndex+1][selectOne] == INF)
			minimumPriceMemo[homeIndex+1][selectOne] = getMinimumPrice(homeIndex+1, selectOne);
		if (minimumPriceMemo[homeIndex+1][selectTwo] == INF)
			minimumPriceMemo[homeIndex+1][selectTwo] = getMinimumPrice(homeIndex+1, selectTwo);
		return Integer.min(minimumPriceMemo[homeIndex+1][selectOne], 
				minimumPriceMemo[homeIndex+1][selectTwo])+colorPrices[homeIndex][color];
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		homeCount = Integer.parseInt(reader.readLine().trim());
		colorPrices = new int[homeCount][3];
		for (int homeIndex=0; homeIndex<homeCount; homeIndex++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			colorPrices[homeIndex][0] = Integer.parseInt(tokenizer.nextToken()); // red
			colorPrices[homeIndex][1] = Integer.parseInt(tokenizer.nextToken()); // green
			colorPrices[homeIndex][2] = Integer.parseInt(tokenizer.nextToken()); // blue
		}
		minimumPriceMemo = new int[homeCount][3];
		for (int homeIndex=0; homeIndex<homeCount; homeIndex++)
			Arrays.fill(minimumPriceMemo[homeIndex], INF);
	}

}
