package homeworks.m9.d3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * [문제 풀이]
 * 1. makeToOneRecursive(): 입력 받은 숫자를 1로 만드는데 들어가는 최소 연산 횟수 반환
 * 2. number가 1이라면 현재 연산 횟수 반환
 *	... 안되것다
 */
public class MakeToOneDP {
	private static BufferedReader reader;
	private static int[] countMemo;
	private static final int INF = Integer.MAX_VALUE;
	public static void main(String[] args) throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		int start = Integer.parseInt(reader.readLine().trim());
		countMemo = new int[start+1];
		Arrays.fill(countMemo, INF);
		for (int i=1; i<=start; i++)
			makeToOneRecursive(i, 0);
		System.out.println(countMemo[start]);
		reader.close();
	}
	private static int makeToOneRecursive(int number, int count) {
		if (number == 1) return count;
		if (countMemo[number] == INF || countMemo[number] > count) {
			if (countMemo[number-1] == INF)
				countMemo[number-1] = makeToOneRecursive(number-1, count+1);
			if (number%2 == 0 && countMemo[number/2] == INF)
				countMemo[number/2] = makeToOneRecursive(number/2, count+1);
			if (number%3 == 0 && countMemo[number/3] == INF)
				countMemo[number/3] = makeToOneRecursive(number/3, count+1);
			countMemo[number] = Math.min(countMemo[number-1], 
					Math.min(countMemo[number/2], countMemo[number/3]));
		}
		return countMemo[number];
	}

}
