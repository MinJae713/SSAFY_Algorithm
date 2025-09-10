package homeworks.m9.d10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - historyMemo: 사건과 사건 사이에 전후 관계를 고려하여 몇 단계가 이어져있는지 입력
 * 	  ㄴ 이어져있지 않다면 초기값으로 입력됨
 * 	  ㄴ historyMemo[from][to]: from 사건 이후에 몇 개의 전후 관계를 지나 to 사건이 나오는지 기록
 * 	      ㄴ from에서 to의 전후 관계가 파악된다고 해서 to에서 from으로 전후 관계가 무조건 파악되지는 않음
 * 1. 모든 사건에 대해 반복(connect)
 * 	1.1. 모든 사건에 대해 반복(from)
 * 		1.1.1. 모든 사건에 대해 반복(to)
 * 		1.1.2. from 사건과 to 사건이 동일하다면 continue
 * 		1.1.3. historyMemo의 from, to 해당 위치 값 입력 (noConnect)
 * 		1.1.4. historyMemo의 from, connect 해당 위치 입력 (connect1)
 * 		1.1.5. historyMemo의 connect, to 해당 위치 입력 (connect2)
 * 		1.1.6. connect1이 초기값이거나, connect2가 초기값이면 continue
 * 		1.1.7. noConnect와 connect1+connect2 중 더 작은 값을
 * 			   historyMemo의 from, to 해당 위치에 입력
 * 2. 전후 관계를 확인할 각 사건에 대해 반복
 * 	2.1. historyMemo의 앞 사건, 뒤 사건 해당 값이 초기값이 아니면? -1을 결과에 입력
 * 	2.2. historyMemo의 뒤 사건, 앞 사건 해당 값이 초기값이 아니면? 1을 결과에 입력
 * 	2.3. 둘 다 아니면? 0을 결과에 입력
 */
public class History {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int historyCount;
	private static int relationCount;
	private static int[][] historyMemo;
	private static int pairCount;
	private static Queue<int[]> pair;
	private static final int INF = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		setHistoryMemo();
		checkHistoryRelation();
		System.out.print(builder);
		reader.close();
	}

	private static void checkHistoryRelation() {
		while (!pair.isEmpty()) {
			int[] histories = pair.poll();
			int fromHistory = histories[0];
			int toHistory = histories[1];
			if (historyMemo[fromHistory][toHistory] != INF)
				builder.append(-1).append("\n");
			else if (historyMemo[toHistory][fromHistory] != INF)
				builder.append(1).append("\n");
			else builder.append(0).append("\n");
		}
	}

	private static void setHistoryMemo() {
		for (int connect=1; connect<=historyCount; connect++)
			for (int from=1; from<=historyCount; from++)
				for (int to=1; to<=historyCount; to++) {
					if (from == to) continue;
					int noConnect = historyMemo[from][to];
					int connect1 = historyMemo[from][connect];
					int connect2 = historyMemo[connect][to];
					if (connect1 == INF || connect2 == INF) continue;
					historyMemo[from][to] = Math.min(noConnect, connect1+connect2);
				}
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		tokenizer = new StringTokenizer(reader.readLine().trim());
		historyCount = Integer.parseInt(tokenizer.nextToken());
		relationCount = Integer.parseInt(tokenizer.nextToken());
		historyMemo = new int[historyCount+1][historyCount+1];
		for (int history=1; history<=historyCount; history++) {
			Arrays.fill(historyMemo[history], INF);
			historyMemo[history][history] = 0;
		}
		for (int index=0; index<relationCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int fromHistory = Integer.parseInt(tokenizer.nextToken());
			int toHistory = Integer.parseInt(tokenizer.nextToken());
			historyMemo[fromHistory][toHistory] = 1;
		}
		pairCount = Integer.parseInt(reader.readLine().trim());
		pair = new ArrayDeque<int[]>();
		for (int index=0; index<pairCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			pair.offer(new int[] {
				Integer.parseInt(tokenizer.nextToken()),
				Integer.parseInt(tokenizer.nextToken())
			});
		}
	}

}
