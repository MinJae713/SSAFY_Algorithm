package m8.d7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Braket {

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder builder = new StringBuilder();
		int testCaseCount = 10;
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			int big = 0;
			int middle = 0;
			int small = 0;
			int better = 0;
			int braketCount = Integer.parseInt(reader.readLine().trim());
			String braketLine = reader.readLine().trim();
			for (int index=0; index<braketCount; index++) {
				char braket = braketLine.charAt(index);
				switch (braket) {
				case '<':
					better++;
					break;
				case '(':
					small++;
					break;
				case '{':
					middle++;
					break;
				case '[':
					big++;
					break;
				case ']':
					big--;
					break;
				case '}':
					middle--;
					break;
				case ')':
					small--;
					break;
				case '>':
					better--;
					break;
				}
			}
			boolean result = big == 0 && middle == 0 && small == 0 && better == 0;
			builder.append("#").append(testCase).append(" ").append(result ? 1 : 0).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

}
