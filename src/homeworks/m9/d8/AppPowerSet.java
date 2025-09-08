package homeworks.m9.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class AppPowerSet {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int appCount;
	private static int limitMemory;
	private static int[] memoryBytes;
	private static int[] costs;
	private static boolean[] contained;
	private static int result;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		makeAppSet(1);
		System.out.println(result);
		reader.close();
	}

	private static void makeAppSet(int count) {
		if (count > appCount) {
			int memoryTotal = 0;
			int costTotal = 0;
			for (int index=1; index<=appCount; index++) {
				if (!contained[index]) continue;
				memoryTotal += memoryBytes[index];
				costTotal += costs[index];
			}
			if (memoryTotal < limitMemory) return;
			result = Math.min(result, costTotal);
			return;
		}
		contained[count] = true;
		makeAppSet(count+1);
		contained[count] = false;
		makeAppSet(count+1);
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		appCount = Integer.parseInt(tokenizer.nextToken());
		limitMemory = Integer.parseInt(tokenizer.nextToken());
		memoryBytes = new int[appCount+1];
		costs = new int[appCount+1];
		contained = new boolean[appCount+1];
		result = Integer.MAX_VALUE;
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=1; index<=appCount; index++)
			memoryBytes[index] = Integer.parseInt(tokenizer.nextToken());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=1; index<=appCount; index++)
			costs[index] = Integer.parseInt(tokenizer.nextToken());
	}

}
