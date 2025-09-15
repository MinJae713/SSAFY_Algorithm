package testReview.testA;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Solution2 {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int N;
	private static int[] ballonScores;
	private static int maxScore;
	private static int[] permute;
	private static boolean[] contained;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("sample_input2.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			permuteBallons(0);
			builder.append("#").append(testCase).append(" ").
					append(maxScore).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void permuteBallons(int count) {
		if (count == N) {
			startGame(new boolean[N]);
			return;
		}
		for (int index=0; index<N; index++) {
			if (contained[index]) continue;
			permute[count] = index;
			contained[index] = true;
			permuteBallons(count+1);
			contained[index] = false;
		}
	}

	private static void startGame(boolean[] poped) {
		int totalScore = 0;
		for (int index=0; index<N; index++) {
			poped[index] = true;
			int left = index-1;
			while (left>=0 && poped[left]) left--;
			int right = index+1;
			while (right<N && poped[right]) right++;
			int score = left<0 && right>=N ? ballonScores[index] : 1; 
			// 둘 다 범위 벗어나면 현 위치 점수만 계산됨
			if (left >= 0) score *= ballonScores[left];
			if (right < N) score *= ballonScores[right];
			totalScore += score;
		}
		maxScore = Math.max(maxScore, totalScore);
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		N = Integer.parseInt(reader.readLine().trim());
		ballonScores = new int[N];
		maxScore = 0;
		permute = new int[N];
		contained = new boolean[N];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int i=0; i<N; i++)
			ballonScores[i] = Integer.parseInt(tokenizer.nextToken());
	}

}
