package m8.d7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Sum4 {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int numberCount;
	private static int sumisionCount;
	private static int[]	numberSumision;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		tokenizer = new StringTokenizer(reader.readLine().trim());
		numberCount = Integer.parseInt(tokenizer.nextToken());
		sumisionCount = Integer.parseInt(tokenizer.nextToken());
		numberSumision = new int[numberCount+1];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=1; index<=numberCount; index++)
			numberSumision[index] = Integer.parseInt(tokenizer.nextToken())+numberSumision[index-1];
		for (int index=0; index<sumisionCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			builder.append(numberSumision[to]-numberSumision[from-1]).append("\n");
		}
		System.out.println(builder);
		reader.close();
	}

}
