package homeworks.m9.d10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - minDistanceMemo: 집, 송도, 편의점들 간 거리 저장
 * 	 ㄴ 집이 0번, 송도가 마지막, 그 사이 편의점들에 각 번호가 부여됨
 * - visited: 각 지점의 방문 여부 표시
 * - positions: 각 지점의 위치 저장
 * 1. 지점 입력 시 각 지점 간 거리를 minDistanceMemo에 입력
 * 	1.1. 이 거리는 두 지점 간 직접 거리임
 * 2. checkGoAble(): 집에서 송도까지 갈 수 있는지 여부를 문자열로 반환
 * 3. 집 번호(0) 큐에 입력 및 방문 표시
 * 4. 큐에 요소가 있는 동안 반복
 * 	4.1. 큐에서 요소 poll(current)
 * 	4.2. 현재 위치에서 송도까지 거리가 1000 이하라면 happy 반환
 * 	4.3. 모든 편의점에 대해 반복(store)
 * 	4.4. 현재 편의점을 이미 방문했거나, 
 * 		  현재 위치에서 편의점까지 거리가 1000 이상이면 continue
 * 	4.5. 큐에 store poll 및 방문 표시
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
			builder.append(checkGoAble()).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	
	private static String checkGoAble() {
		Queue<Integer> queue = new ArrayDeque<Integer>();
		queue.offer(0);
		visited[0] = true;
		while (!queue.isEmpty()) {
			int current = queue.poll();
			if (minDistanceMemo[current][storeCount+1] <= 1000)
				return "happy";
			for (int store=1; store<=storeCount; store++) {
				if (visited[store] || 
					minDistanceMemo[current][store] > 1000) 
					continue;
				queue.offer(store);
				visited[store] = true;
			}
		}
		return "sad";
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
