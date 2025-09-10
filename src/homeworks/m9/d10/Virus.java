package homeworks.m9.d10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - connectedMemo: 
 *   ㄴ 컴퓨터와 컴퓨터가 몇 단계에 걸쳐서 연결되어 있는지 저장
 * 1. 컴퓨터의 개수 만큼 반복(connect)
 * 	1.1. 컴퓨터의 개수 만큼 반복(from)
 * 		1.1.1. 컴퓨터의 개수 만큼 반복(to)
 * 		1.1.2. from과 to가 동일하다면 continue
 * 		1.1.3. connectedMemo의 from, to 해당 위치 값 입력(noConnect)
 * 		1.1.4. connectedMemo의 from, to 해당 위치 값 입력(connect1)
 * 		1.1.5. connectedMemo의 from, to 해당 위치 값 입력(connect2)
 *		1.1.6. connect1가 초기값이거나, connect2가 초기값이면 continue
 *		1.1.7. noConnect와 connect1+connect2 중 더 작은 값을
 *			   connectedMemo의 from, to 해당 위치에 입력
 * 2. 2번~마지막 컴퓨터에 대해 반복, connectMemo 
 * 	  1번 행 해당 각 위치가 초기값이 아닌 위치 개수 계산
 */
public class Virus {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int computerCount;
	private static int connectCount;
	private static int[][] connectMemo;
	private static int affectCount;
	private static final int INF = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		setConnectMemo();
		for (int computer=2; computer<=computerCount; computer++)
			affectCount += connectMemo[1][computer] == INF ? 0 : 1;
		System.out.println(affectCount);
		reader.close();
	}

	private static void setConnectMemo() {
		for (int connect=1; connect<=computerCount; connect++)
			for (int from=1; from<=computerCount; from++)
				for (int to=1; to<=computerCount; to++) {
					if (from == to) continue;
					int noConnect = connectMemo[from][to];
					int connect1 = connectMemo[from][connect];
					int connect2 = connectMemo[connect][to];
					if (connect1 == INF || connect2 == INF) continue;
					connectMemo[from][to] = Math.min(noConnect, connect1+connect2);
				}
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		computerCount = Integer.parseInt(reader.readLine().trim());
		connectMemo = new int[computerCount+1][computerCount+1];
		connectCount = Integer.parseInt(reader.readLine().trim());
		for (int computer=1; computer<=computerCount; computer++) {
			Arrays.fill(connectMemo[computer], INF);
			connectMemo[computer][computer] = 0;
		}
		for (int index=0; index<connectCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			connectMemo[from][to] = 1;
			connectMemo[to][from] = 1;
		}
		affectCount = 0;
	}

}
