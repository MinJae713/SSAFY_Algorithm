package homeworks.m8.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class DishBit {
	private static BufferedReader reader;
	private static int dishCount;
	private static StringTokenizer tokenizer;
	private static long[] sin;
	private static long[] bitter;
	private static long minimum;

	public static void main(String[] args) throws IOException {
		initialize();
		calculateMinimum();
		System.out.println(minimum);
		reader.close();
	}
	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		dishCount = Integer.parseInt(reader.readLine().trim());
		sin = new long[dishCount];
		bitter = new long[dishCount];
		minimum = Long.MAX_VALUE;
		for (int index=0; index<dishCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			sin[index] = Long.parseLong(tokenizer.nextToken());
			bitter[index] = Long.parseLong(tokenizer.nextToken());
		}
	}
	private static void calculateMinimum() {
		for (int contained=1; contained < 1<<dishCount; contained++) {
			// contained => 각 재료의 포함 여부 나타냄
			// 포함된 재료들의 신, 쓴맛 계산
			// 1101 => 1, 2, 4번 재료 포함
			int masking = 0;
			long sinTotal = 1;
			long bitterTotal = 0;
			for (int dishIndex=0; dishIndex<dishCount; dishIndex++) {
				masking = 1 << dishIndex; // 검출할 위치의 값 지정
				if ((contained & masking) == masking) {
					sinTotal *= sin[dishIndex];
					bitterTotal += bitter[dishIndex];
				}
			}
			long total = sinTotal-bitterTotal;
			total *= total < 0 ? -1 : 1;
			minimum = Math.min(minimum, total);
		}
	}
}
