package homeworks.m9.d15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 전체 카드의 절반+1에 해당하는 위치 찾음 (betweenIndex)
 * 2. 카드 개수 만큼 반복 (index)
 * 3. index가 betweenIndex보다 작다면?
 * 	3.1. 각 카드는 짝수 번째 인덱스에 입력
 * 4. index가 betweenIndex 이상이라면?
 * 	4.1. 각 카드는 홀수 번째 인덱스에 입력
 */
public class PerfectShuffle {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int cardSize;
	private static int betweenIndex;
	private static String[] deck;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			builder.append("#").append(testCase).append(" ");
			for (String card : deck)
				builder.append(card).append(" ");
			builder.append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}

	private static void initialize() throws IOException {
		cardSize = Integer.parseInt(reader.readLine().trim());
		betweenIndex = (cardSize-1)/2+1;
		deck = new String[cardSize];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		int evenIndex = 0, oddIndex = 1;
		for (int index=0; index<cardSize; index++) {
			if (index < betweenIndex) {
				deck[evenIndex] = tokenizer.nextToken();
				evenIndex += 2;
			}
			else {
				deck[oddIndex] = tokenizer.nextToken();
				oddIndex += 2;
			}
		}
	}
}
