package testReview.testApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Test3_유민재 {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int cityCount;
	private static int busCount;
	private static int[][] cityBuses;
	private static boolean[][] recorded;
	private static int startCity;
	private static int destinateCity;
	private static int[] cityCost;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		Queue<Integer> queue = new PriorityQueue<>();
		queue.offer(startCity);
		cityCost[startCity] = 0;
		while (!queue.isEmpty()) {
			int fromCity = queue.poll();
			for (int toCity=1; toCity<=cityCount; toCity++) {
				// 버스로 갈 수 있는 도시에 대해서만 수행 - 아니면 recorded에서 걸림
				if (!recorded[fromCity][toCity]) continue;
				if (cityCost[toCity] > 
					cityCost[fromCity]+
					cityBuses[fromCity][toCity]) {
					cityCost[toCity] = cityCost[fromCity]+
							cityBuses[fromCity][toCity];
					queue.offer(toCity);
				}
			}
		}
		System.out.println(cityCost[destinateCity]);
		reader.close();
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		cityCount = Integer.parseInt(reader.readLine().trim());
		busCount = Integer.parseInt(reader.readLine().trim());
		cityBuses = new int[cityCount+1][cityCount+1];
		recorded = new boolean[cityCount+1][cityCount+1];
		for (int bus=0; bus<busCount; bus++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int fromCity = Integer.parseInt(tokenizer.nextToken());
			int toCity = Integer.parseInt(tokenizer.nextToken());
			int cost = Integer.parseInt(tokenizer.nextToken());
			// 이미 들어온 도시간 버스가 들어왔다면 더 작은 비용의 버스로 넣어줌
			if (recorded[fromCity][toCity]) {
				cityBuses[fromCity][toCity] = 
						Math.min(cityBuses[fromCity][toCity], cost);
			} else {
				cityBuses[fromCity][toCity] = cost;
				recorded[fromCity][toCity] = true;
			}
		}
		tokenizer = new StringTokenizer(reader.readLine().trim());
		startCity = Integer.parseInt(tokenizer.nextToken());
		destinateCity = Integer.parseInt(tokenizer.nextToken());
		cityCost = new int[cityCount+1];
		Arrays.fill(cityCost, Integer.MAX_VALUE);
	}

}
