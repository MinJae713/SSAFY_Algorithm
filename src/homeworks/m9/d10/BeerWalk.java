package homeworks.m9.d10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - minDistanceMemo: 집, 송도, 편의점들 간 최단 거리 저장
 * 	 ㄴ 집이 0번, 송도가 마지막, 그 사이 편의점들에 각 번호가 부여됨
 * - visited: 각 지점의 방문 여부 표시
 * - positions: 각 지점의 위치 저장
 * 1. 지점 입력 시 각 지점 간 거리를 minDistanceMemo에 입력
 * 	1.1. 이 거리는 두 지점 간 직접 거리임
 * 2. 각 장소들에 대해 반복 (connect)
 * 	2.1. 각 장소들에 대해 반복 (from)
 * 		2.1.1. 각 장소들에 대해 반복 (to)
 * 		2.1.2. from과 to가 같다면 continue
 * 		2.1.3. minDistanceMemo의 from, to 해당 위치 입력(noConnect)
 * 		2.1.4. minDistanceMemo의 from, connect 해당 위치 입력(connect1)
 * 		2.1.5. minDistanceMemo의 connect, to 해당 위치 입력(connect2)
 * 3. checkGoAble(): 집에서 송도까지 갈 수 있는지 여부를 문자열로 반환
 * 	3.1. 파라미터는 현재 위치 번호(current), 초기 호출 시 집(0)에서 시작
 * 4. current 위치에서 송도까지 거리 계산 (distance)
 * 5. distance가 1000 이하라면? happy 반환
 * 6. 1000보다 크다면?
 * 	6.1. 모든 편의점에 대해 반복 (store)
 * 		6.1.1. 이미 방문한 편의점이면 continue
 * 		6.1.2. 현재 위치에서 해당 위치까지 거리가 1000보다 크다면 continue
 * 		6.1.3. 현재 위치에서 해당 위치까지 거리가 가장 큰 위치(nextStore) 값 지정
 * 	6.2. nextStore가 값이 지정되지 않았다면? sad 반환
 * 	6.3. 값이 지정되었다면? nextStore를 파라미터로 checkGoAble 재귀 호출
 *
 */
public class BeerWalk {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int storeCount;
	private static int[][] minDistanceMemo;
	private static boolean[] visited;
	private static int[][] positions;

	public static void main(String[] args) throws IOException {
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			setMinDistanceMemo();
			builder.append(checkGoAble(0)).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void setMinDistanceMemo() {
		for (int connect=0; connect<storeCount+2; connect++)
			for (int from=0; from<storeCount+2; from++)
				for (int to=0; to<storeCount+2; to++) {
					if (from == to) continue;
					int noConnect = minDistanceMemo[from][to];
					int connect1 = minDistanceMemo[from][connect];
					int connect2 = minDistanceMemo[connect][to];
					minDistanceMemo[from][to] = 
							Math.min(noConnect, connect1+connect2);
				}
	}
	
	private static String checkGoAble(int current) {
		visited[current] = true;
		int distance = minDistanceMemo[current][storeCount+1];
		if (distance <= 1000) return "happy";
		else {
			int nextStore = -1;
			int nextStoreDistance = 0;
			for (int store=1; store<=storeCount; store++) {
				if (visited[store]) continue;
				else if (minDistanceMemo[current][store] > 1000) continue;
				if (nextStoreDistance < minDistanceMemo[current][store]) {
					nextStoreDistance = minDistanceMemo[current][store];
					nextStore = store;
				}
			}
			if (nextStore == -1) return "sad";
			else return checkGoAble(nextStore);
		}
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		storeCount = Integer.parseInt(reader.readLine().trim());
		minDistanceMemo = new int[storeCount+2][storeCount+2];
		visited = new boolean[storeCount+2];
		positions = new int[storeCount+2][];
		for (int index=0; index<storeCount+2; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int x = Integer.parseInt(tokenizer.nextToken());
			int y = Integer.parseInt(tokenizer.nextToken());
			positions[index] = new int[] {x, y};
		}
		for (int from=0; from<storeCount+2; from++)
			for (int to=0; to<storeCount+2; to++) {
				if (from == to) continue;
				int distance = getDistance(from, to);
				minDistanceMemo[from][to] = distance;
				minDistanceMemo[to][from] = distance;
			}
	}
	
	private static int getDistance(int from, int to) {
		int[] fromPosition = positions[from];
		int[] toPosition = positions[to];
		return Math.abs(fromPosition[0]-toPosition[0])+
				Math.abs(fromPosition[1]-toPosition[1]);
	}

}
