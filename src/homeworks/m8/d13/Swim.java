package homeworks.m8.d13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 최소 가격을 1년간 이용 가격으로 초기화
 * 2. 처음으로 수영장을 이용하는 달을 startMonth, 마지막은 endMonth로 지정
 * 3. getMinimumFee(): 특정 달에서 마지막 달 까지 내야 하는 수영장 요금의 최솟값을 구함
 * 	3.1. 파라미터는 몇 월부터 시작할지에 대해서 '몇 월(month)', 
 * 	3.2. 반환은 그 달부터 마지막 달 까지 내는 요금의 최솟값
 * 4. 입력받은 달 수가 endMonth보다 크다면? -> 0 반환
 * 5. 입력받은 달이 이용 계획이 없다면? -> 다음 달을 파라미터로 getMinimumFee 재귀 호출
 * 6. 입력받은 달을 1일권으로 이용했을 때 금액 계산 (oneMonthFee)
 * 7. 위 계산 결과가 1달권을 이용할 때보다 크다면? -> 1달권 가격으로 oneMonthFee 입력
 * 8. 다음 달을 파라미터로 재귀호출한 결과+oneMonthFee 와
 * 	3달 후의 달을 파라미터로 재귀호출한 결과+3달 이용 요금 중, 더 작은 값 반환
 * 9. 반환된 값과 최소 가격과 비교 -> 더 작은 값으로 최소 가격 초기화
 */
public class Swim {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[] prices;
	private static int[] planOfMonth;
	private static int minimumPrice;
	private static int startMonth;
	private static int endMonth;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			minimumPrice = Math.min(minimumPrice, getMinimumFee(startMonth));
			builder.append("#").append(testCase).
						append(" ").append(minimumPrice).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int getMinimumFee(int month) {
		if (month > endMonth) return 0;
		else if (planOfMonth[month] == 0)
			return getMinimumFee(month+1);
		else {
			// 1일권 사용 시 이용 가격
			int oneMonthFee = planOfMonth[month] * prices[0];
			// 1달권 사용 시 가격과 비교
			oneMonthFee = Math.min(oneMonthFee, prices[1]);
			int withAMonth = oneMonthFee + getMinimumFee(month+1);
			int withThreeMonth = prices[2] + getMinimumFee(month+3);
			return Math.min(withAMonth, withThreeMonth);
		}
	}

	private static void initialize() throws IOException {
		prices = new int[4];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<4; index++)
			prices[index] = Integer.parseInt(tokenizer.nextToken());
		planOfMonth = new int[13];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		startMonth = 0;
		for (int month=1; month<=12; month++) {
			planOfMonth[month] = Integer.parseInt(tokenizer.nextToken());
			if (startMonth == 0 && planOfMonth[month] != 0)
				startMonth = month;
		}
		endMonth = 12;
		boolean checked = false;
		for (int month=12; month>=1 && !checked; month--) 
			if (planOfMonth[month] != 0) {
				endMonth = month;
				checked = true;
			}
		minimumPrice = prices[3];
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}

}
