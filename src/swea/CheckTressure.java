package swea;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 */
public class CheckTressure {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int bound;
	private static int initPosition;
	private static int[] rooms;
	private static int tressureCount;
	private static int[] tressurePosition;
	private static boolean endOfLeft;
	private static boolean endOfRight;

	public static void main(String[] args) throws IOException {
		// 모르겠다
		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			int distance = 0;
			builder.append("#").append(testCase).
						append(" ").append(distance).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		 builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		bound = Integer.parseInt(tokenizer.nextToken());
		initPosition = Integer.parseInt(tokenizer.nextToken())-1;
		rooms = new int[bound];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<bound; index++) {
			rooms[index] = Integer.parseInt(tokenizer.nextToken());
			tressureCount += rooms[index];
		}
		tressurePosition = new int[tressureCount];
		int index=0;
		for (int count=0; count<bound; count++)
			if (rooms[count] == 1) tressurePosition[index++] = count;
		endOfLeft = false;
		endOfRight = false;
	}
}
