package homeworks.m8.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 연산의 수 입력, 연산 수 만큼 반복
 * 2. contained: 포함된 수를 int형으로 표현
 * 	2.1. 아무 수도 없으면 0, 모든 수가 있으면  2^21-1
 * 3. masking: 마스킹 값 
 * 4. add x: masking = 1 << (x-1), contained |= masking
 * 5. remove x: masking = ~(1 << (x-1)), contained &= masking
 * 6. check x: masking = 1 << (x-1), contained & masking 이 masking인지 확인
 * 	6.1. 0이면 포함x, 0보다 크면 포함됨
 * 	6.2. 결과는 builder에 추가
 * 7. toggle x: masking = 1 << (x-1), checked = contained & masking == masking
 * 	7.1. checked가 true? masking = ~(1 << (x-1)), contained &= masking
 * 	7.2. checked가 false? masking = 1 << (x-1), contained |= masking
 * 8. all: contained = 2097151
 * 9. empty: contained = 0
 */
public class SetBit {

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder builder = new StringBuilder();
		StringTokenizer tokenizer;
		int operateCount = Integer.parseInt(reader.readLine().trim());
		int contained = 0;
		for (int index=0; index<operateCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			String operate = tokenizer.nextToken();
			int x = 0;
			int masking = 0;
			switch (operate) {
			case "add":
				x = Integer.parseInt(tokenizer.nextToken());
				masking = 1 << (x-1);
				contained |= masking;
				break;
			case "remove":
				x = Integer.parseInt(tokenizer.nextToken());
				masking = ~(1 << (x-1));
				contained &= masking;
				break;
			case "check":
				x = Integer.parseInt(tokenizer.nextToken());
				masking = 1 << (x-1);
				builder.append((contained & masking) == masking ? 1 : 0).append("\n");
				break;
			case "toggle":
				x = Integer.parseInt(tokenizer.nextToken());
				masking = 1 << (x-1);
				if ((contained & masking) == masking) {
					masking = ~(1 << (x-1));
					contained &= masking;
				} else {
					masking = 1 << (x-1);
					contained |= masking;
				}
				break;
			case "all":
				contained = 2097151;
				break;
			case "empty":
				contained = 0;
				break;
			}
		}
		System.out.print(builder);
		reader.close();
	}

}
