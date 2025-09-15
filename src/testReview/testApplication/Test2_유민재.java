package testReview.testApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Test2_유민재 {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int size;
	private static int[] numbers;
	
	public static void main(String[] args) throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		tokenizer = new StringTokenizer(reader.readLine().trim());
		size = Integer.parseInt(tokenizer.nextToken());
		numbers = new int[size+1];
		for (int index=1; index<=size; index++)
			numbers[index] = index;
		int command = Integer.parseInt(tokenizer.nextToken());
		for (int index=0; index<command; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int fromIndex = Integer.parseInt(tokenizer.nextToken());
			int toIndex = Integer.parseInt(tokenizer.nextToken());
			int middle = (fromIndex+toIndex)/2;
			for (int start=fromIndex, increase=0; 
					start<=middle; start++, increase++) {
				int temp = numbers[fromIndex+increase];
				numbers[fromIndex+increase] = numbers[toIndex-increase];
				numbers[toIndex-increase] = temp;
			}
		}
		for (int index=1; index<=size; index++)
			builder.append(numbers[index]).append(" ");
		System.out.println(builder);
		reader.close();
	}

}
