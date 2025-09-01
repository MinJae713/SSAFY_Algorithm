package testReview;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Test2_유민재 {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int bound;
	private static int[][] garden;
	private static int good;
	private static int bad;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("resources/Test2.txt"));
		initialize();
		divideGarden(0, 0, bound);
		builder.append(good).append("\n").append(bad);
		System.out.println(builder);
		reader.close();
	}

	private static void divideGarden(int startX, 
			int startY, int length) {
		int[] areaStatus = getAreaStatus(startX, startY, length);
		// 모두 손상 타일
		if (areaStatus[0] == 0) bad++;
		// 모두 정상 타일
		else if (areaStatus[1] == 0) good++;
		else {
			divideGarden(startX, 			startY, 			length/2);
			divideGarden(startX+length/2, 	startY, 			length/2);
			divideGarden(startX, 			startY+length/2, 	length/2);
			divideGarden(startX+length/2, 	startY+length/2, 	length/2);
		}
	}

	private static int[] getAreaStatus(int startX, int startY, int length) {
		int[] areaStatus = new int[2]; // [0]: good, [1]: bad
		for (int row=startY; row<startY+length; row++)
			for (int column=startX; column<startX+length; column++)
				areaStatus[garden[row][column]]++;
		return areaStatus;
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		bound = Integer.parseInt(reader.readLine().trim());
		garden = new int[bound][bound];
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++)
				garden[row][column] = Integer.parseInt(tokenizer.nextToken());
		}
		good = bad = 0;
	}

}
