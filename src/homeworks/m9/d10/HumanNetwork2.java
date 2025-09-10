package homeworks.m9.d10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - heightMemo: 사람과 사람 사이에 몇 개의 키 관계가 있는지 기록
 * - heigher: 해당 위치 사람보다 키가 큰 사람의 수 기록
 * - lower: 해당 위치 사람보다 키가 작은 사람 수 기록
 * 1. 사람 수 만큼 반복 (connect)
 * 	1.1. 사람 수 만큼 반복 (from)
 * 		1.1.1. 사람 수 만큼 반복 (to)
 * 		1.1.2. from과 to가 같다면 continue
 * 		1.1.3. heightMemo의 from, to 해당 위치 입력(noConnect)
 * 		1.1.4. heightMemo의 from, connect 해당 위치 입력(connect1)
 * 		1.1.5. heightMemo의 connect, to 해당 위치 입력(connect2)
 * 2. 사람 수 만큼 반복 (from)
 * 	2.1. 사람 수 만큼 반복 (to)
 * 	2.2. from과 to가 동일하거나, 
 * 		 heightMemo의 from, to 해당 위치가 초기값이면 continue
 * 	2.3. heigher의 from 위치 1 증가
 * 	2.4. lower의 to 위치 1 증가
 * 3. 사람 수 만큼 반복 (human)
 * 	3.1. 해당 위치 heigher, lower값+1 계산 (knowableCount)
 * 	3.2. knowableCount가 총 사람 수와 동일하다면 
 * 		  키 순서를 파악할 수 있는 사람 수 1 증가 (totalKnowableCount)
 */
public class HumanNetwork2 {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int humanCount;
	private static int compareCount;
	private static int[][] heightMemo;
	private static int[] heigher;
	private static int[] lower;
	private static int totalKnowableCount;
	private static final int INF = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			setHeightMemo();
			calculateHeigherLower();
			sumTotalKnowableCount();
			builder.append("#").append(testCase).append(" ").
					append(totalKnowableCount).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void setHeightMemo() {
		for (int connect=1; connect<=humanCount; connect++)
			for (int from=1; from<=humanCount; from++)
				for (int to=1; to<=humanCount; to++) {
					if (from == to) continue;
					int noConnect = heightMemo[from][to];
					int connect1 = heightMemo[from][connect];
					int connect2 = heightMemo[connect][to];
					if (connect1 == INF || connect2 == INF) continue;
					heightMemo[from][to] = Math.min(noConnect, 
							connect1+connect2);
				}
	}
	
	private static void calculateHeigherLower() {
		for (int from=1; from<=humanCount; from++)
			for (int to=1; to<=humanCount; to++) {
				if (from == to || 
					heightMemo[from][to] == INF) continue;
				heigher[from]++;
				lower[to]++;
			}
	}
	
	private static void sumTotalKnowableCount() {
		for (int human=1; human<=humanCount; human++) {
			int knowableCount = heigher[human]+lower[human]+1;
			totalKnowableCount += knowableCount == humanCount ? 1 : 0;
		}
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		humanCount = Integer.parseInt(reader.readLine().trim());
		compareCount = Integer.parseInt(reader.readLine().trim());
		heightMemo = new int[humanCount+1][humanCount+1];
		heigher = new int[humanCount+1];
		lower = new int[humanCount+1];
		totalKnowableCount = 0;
		for (int human=1; human<=humanCount; human++) {
			Arrays.fill(heightMemo[human], INF);
			heightMemo[human][human] = 0;
		}
		for (int index=0; index<compareCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			heightMemo[from][to] = 1;
		}
	}

}
