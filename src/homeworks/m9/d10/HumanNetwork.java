package homeworks.m9.d10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - connectMemo: 사람과 사람간 최단 거리 저장
 * 1. 사람 수 만큼 반복(connect)
 * 	1.1. 사람 수 만큼 반복(from)
 * 		1.1.1. 사람 수 만큼 반복(to)
 * 		1.1.2. from과 to가 같다면 continue
 * 		1.1.3. connectMemo의 from, to 해당 위치 값 입력 (noConnect)
 *		1.1.4. connectMemo의 from, connect 해당 위치 값 입력 (connect1)
 *		1.1.5. connectMemo의 connect, to 해당 위치 값 입력 (connect2)
 *		1.1.6. connect1이 초기값이거나, connect2가 초기값이면? continue
 *		1.1.7. connectMemo의 from, to 해당 위치에 noConnect와
 *			   connect1+connect2 중 더 작은 값 입력
 * 2. 사람 수 만큼 반복(from)
 * 	2.1. 각 사람 별 최단 경로 합산 변수 초기화(closeness)
 * 	2.2. 사람 수 만큼 반복 (to) 
 * 		2.2.1. connectMemo의 from, to 해당 위치 값 합산 -> closeness에 입력
 * 	2.3. 각 from 해당 사람 중, closeness가 가장 작은 값 구함 (minCloseness)
 */
public class HumanNetwork {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int humanCount;
	private static int[][] connectMemo;
	private static int minCloseness;
	private static final int INF = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			setConnectMemo();
			getMinCloseness();
			builder.append("#").append(testCase).append(" ").
					append(minCloseness).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void setConnectMemo() {
		for (int connect=0; connect<humanCount; connect++)
			for (int from=0; from<humanCount; from++)
				for (int to=0; to<humanCount; to++) {
					if (from == to) continue;
					int noConnect = connectMemo[from][to];
					int connect1 = connectMemo[from][connect];
					int connect2 = connectMemo[connect][to];
					if (connect1 == INF || connect2 == INF) continue;
					connectMemo[from][to] = 
							Math.min(noConnect, connect1+connect2);
				}
	}
	
	private static void getMinCloseness() {
		for (int from=0; from<humanCount; from++) {
			int closeness = 0;
			for (int to=0; to<humanCount; to++)
				closeness += connectMemo[from][to] == INF ? 
						0 : connectMemo[from][to];
			minCloseness = Math.min(minCloseness, closeness);
		}
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		humanCount = Integer.parseInt(tokenizer.nextToken());
		connectMemo = new int[humanCount][humanCount];
		for (int fromHuman=0; fromHuman<humanCount; fromHuman++) {
			for (int toHuman=0; toHuman<humanCount; toHuman++) {
				int connect = Integer.parseInt(tokenizer.nextToken());
				connectMemo[fromHuman][toHuman] = connect == 0 ? INF : connect;
			}
			connectMemo[fromHuman][fromHuman] = 0;
		}
		minCloseness = Integer.MAX_VALUE;
	}

}
