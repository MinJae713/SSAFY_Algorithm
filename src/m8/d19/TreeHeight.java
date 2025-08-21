package m8.d19;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class TreeHeight {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int treeCount;
	private static int[] treeHeights;
	private static int days;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("Sample_input.txt"));
		int tcCount = initializeTest();
		for (int tc=1; tc<=tcCount; tc++) {
			initialize();
			// logic
			for (int treeIndex=0; treeIndex<treeCount-1;) {
				if (treeHeights[treeIndex] < treeHeights[treeCount-1])
					waterToTree(treeIndex);
				else treeIndex++;
			}
			builder.append("#").append(tc).
					append(" ").append(days).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	
	private static void waterToTree(int treeIndex) {
		int difference = treeHeights[treeCount-1]-treeHeights[treeIndex];
		int grow = (days%2)+1;
		// 차이가 2보다 크면 무조건 물을 줌
		// 차이가 2보다 작으면서 차이와 자랄 수 있는 키가 동일한 날은 물을 줌
		if (difference > 2 || 
				(difference <= 2 && difference == grow)) {
			treeHeights[treeIndex] += grow;
		} else {
			// treeIndex에 있는 애가 물을 줄 수 없는 경우
			for (int nextTree=treeIndex+1; nextTree<treeCount-1; nextTree++) {
				difference = treeHeights[treeCount-1]-treeHeights[nextTree];
				if (difference > 2 || 
						(difference <= 2 && difference == grow)) {
					// 다음 나무를 찾음
					treeHeights[nextTree] += grow;
					break;
				}
			}
		}
		days++;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int tcCount = Integer.parseInt(reader.readLine().trim());
		return tcCount;
	}
	private static void initialize() throws IOException {
		treeCount = Integer.parseInt(reader.readLine().trim());
		treeHeights = new int[treeCount];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<treeCount; index++)
			treeHeights[index] = Integer.parseInt(tokenizer.nextToken());
		days = 0;
		Arrays.sort(treeHeights);
	}
}
