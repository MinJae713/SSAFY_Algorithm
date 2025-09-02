package homeworks.m8.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExcitingSosu {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static int numberLength;
	private static int[] numberContained;
	
	public static void main(String[] args) throws IOException {
		initialize();
		findExcitingSosu(0);
		System.out.print(builder);
		reader.close();
	}

	private static void findExcitingSosu(int numberIndex) {
		if (numberIndex == numberLength) {
			// 포함된 소수 찾기
			int number = numberContained[0];
			// [3 5 7 9]
			// 3 -> 3*10+5=35 -> 35*10+7=357 -> 3579
			for (int index=1; index<numberLength; index++) {
				number *= 10;
				number += numberContained[index];
			}
			// 모든 자리가 소수인지 확인
			boolean sosufind = false;
			for (int length=(int)Math.pow(10, numberLength-1); length>=1 && !sosufind; length/=10) {
				int subNumber = number/length;
				for (int sosu=2; sosu*sosu<=subNumber && !sosufind; sosu++)
					if (subNumber % sosu == 0) sosufind = true; // 소수 찾음
			}
			if (!sosufind) builder.append(number).append("\n");
			return;
		}
		int startNumber = numberIndex == 0 ? 2 : 1;
		while (startNumber<=9) {
			// 첫째 자리수: 2, 3, 5, 7, 9
			// 그 이후: 1, 3, 5, 7, 9
			numberContained[numberIndex] = startNumber;
			findExcitingSosu(numberIndex+1);
			// 짝수는 미포함 (첫째 자리 2는 제외)
			startNumber += numberIndex == 0 && 
					startNumber == 2 ? 1 : 2;
		}
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		numberLength = Integer.parseInt(reader.readLine().trim());
		numberContained = new int[numberLength];
	}

}
