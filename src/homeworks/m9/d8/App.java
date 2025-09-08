package homeworks.m9.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - minCostMemo: 앱의 수량과 비활성화 해야 할 메모리 양이 주어졌을 때 최소 비용 저장
 * 1. getMinCost(): minCostMemo 값 입력
 * 	1.1. 파라미터는 앱의 수(count), 비활성화 메모리의 양(memory)
 * 2. count 위치 앱 비활성화 시 최소 비용(notAvailable) 값은 count 위치 비용, 
 * 	    활성화 시 최소 비용(available) 값은 최대값으로 초기화
 * 3. count 위치 앱을 비활성화 시키는 경우
 * 	3.1. memory가 count 위치의 메모리의 양 이상이고,
 * 	  	 minCostMemo의 count-1, memory-count 위치 메모리 양에 해당하는 값이 초기값이면?
 * 		3.1.1. count-1, memory-count 위치의 메모리 양을 파라미터로 getMinCost() 재귀호출
 * 		3.1.2. 위 호출 결과를 minCostMemo 위치에 입력
 * 4. count 위치 앱을 비활성화 하지 않는 경우
 * 	4.1. 0~count 위치의 메모리 총합이 memory와 같거나 크고,
 * 		 minCostMemo의 count-1, memory 위치가 초기값이면?
 * 	4.2. count-1, memory를 파라미터로 getMinCost() 재귀호출
 * 5. 앱 비활성화 시 최소 비용(notAvailable)은 필요 메모리 양이 count 위치 메모리 양 보다 크거나 같다면
 * 	  minCostMemo의 count-1, memory-count 위치의 메모리 양+현재 위치 비용을, 아니라면 0 입력
 * 6. 앱 활성화 시 최소 비용(available)은 minCostMemo의 count-1, memory 위치 값 입력
 * 7. notContained와 contained 값 비교 -> 더 작은 값 반환
 */
public class App {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int count;
	private static int needMemory;
	private static int[] appBytes;
	private static int[] appCosts;
	private static int[][] minCostMemo;
	private static int[] totalMemory;
	private static final int INF = Integer.MAX_VALUE;
	
	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		System.out.println(getMinCost(count, needMemory));
		reader.close();
	}

	private static int getMinCost(int count, int memory) {
		if (memory <= 0) return 0;
		else if (memory > totalMemory[count]) return INF;
		if (memory <= totalMemory[count] && 
				minCostMemo[count-1][memory] == INF)
			minCostMemo[count-1][memory] = getMinCost(count-1, memory);
		if (memory >= appBytes[count] && 
				minCostMemo[count-1][memory-appBytes[count]] == INF) {
			minCostMemo[count-1][memory-appBytes[count]] = 
					getMinCost(count-1, memory-appBytes[count]);
		}
		int notAvailable = appCosts[count]+
				(memory>=appBytes[count] ? 
						minCostMemo[count-1][memory-appBytes[count]] : 0);
		int available = minCostMemo[count-1][memory];
		return Math.min(notAvailable, available);
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		count = Integer.parseInt(tokenizer.nextToken());
		needMemory = Integer.parseInt(tokenizer.nextToken());
		appBytes = new int[count+1];
		appCosts = new int[count+1];
		minCostMemo = new int[count+1][needMemory+1];
		totalMemory = new int[count+1];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		appBytes[0] = Integer.MAX_VALUE;
		for (int index=1; index<=count; index++) {
			appBytes[index] = Integer.parseInt(tokenizer.nextToken());
			if (index == 1) totalMemory[index] = appBytes[index];
			else totalMemory[index] = totalMemory[index-1]+appBytes[index];
		}
		appCosts[0] = Integer.MAX_VALUE;
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=1; index<=count; index++)
			appCosts[index] = Integer.parseInt(tokenizer.nextToken());
		for (int index=0; index<=count; index++)
			Arrays.fill(minCostMemo[index], INF);
	}
}
