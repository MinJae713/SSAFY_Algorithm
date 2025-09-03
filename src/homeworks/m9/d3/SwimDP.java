package homeworks.m9.d3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. getMonthTotalPrice(): 마지막 이용 달 부터 입력 받은 달(month)까지 수영장 요금 이용 요금 최소값 반환
 * 	1.1. 파라미터는 month - month달 부터 마지막 이용 달 까지 계산
 * 2. 입력 받은 달이 마지막 달을 넘어가면? 0 반환
 * 3. month달이 이용 일 수가 없고, month달의 최소 비용 기록(monthPricesMemo)이 초기값이면?
 * 	3.1. month+1을 파라미터로 getMonthTotalPrice() 재귀호출
 * 4. month달의 최소 비용 기록(monthPricesMemo)이 초기값이면 5 이후 실행
 * 5. month달의 한 달치 요금 계산
 * 	5.1. month달 이용일 수x1일권 가격 저장(monthPay)
 * 	5.2. monthPay가 1달권 보다 크다면 monthPay에 1달권 가격 입력
 * 6. month+1달 까지의 최소 비용 기록(monthPricesMemo)이 초기값이면?
 *	6.1. month+1을 파라미터로 getMonthTotalPrice() 재귀호출, 
 * 	6.2. 결과를 monthPricesMemo month+1 위치에 입력
 * 7. month+3달이 최소 비용 기록(monthPricesMemo)이 초기값이면?
 * 	7.1. month+3을 파라미터로 getMonthTotalPrice() 재귀호출, 
 * 	7.2. 결과를 monthPricesMemo month+3 위치에 입력
 * 8. monthPay+monthPricesMemo의 month+1 값(afterOneMonth)과 (oneMonthTotal)
 * 	  3달권+monthPricesMemo의 month+3 값(afterThreeMonth) 중 작은 값을 (threeMonthTotal)
 * 	  monthPricesMemo의 month 위치에 입력 및 반환
 * 	8.1. afterOneMonth: month+1이 마지막 이용달 이하라면 
 * 		 monthPricesMemo의 month+1 값, 아니면 0으로 지정
 * 	8.2. afterThreeMonth: month+3이 마지막 이용달 이하라면 
 * 		 monthPricesMemo의 month+3 값, 아니면 0으로 지정
 * 9. monthPricesMemo의 month 값 반환
 * 10. 위 메소드 결과와 1년치 요금 비교 -> 더 작은 값으로 출력
 */
public class SwimDP {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[] prices;
	private static int[] usePlan;
	private static int[] monthPricesMemo;
	private static int startUse;
	private static int endUse;
	private static final int INF = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			builder.append("#").append(testCase).append(" ").
					append(Math.min(getMonthTotalPrice(startUse), prices[3])).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int getMonthTotalPrice(int month) {
		if (month > endUse) return 0;
		else if (usePlan[month] == 0 && monthPricesMemo[month] == INF) {
			getMonthTotalPrice(month+1);
			monthPricesMemo[month] = monthPricesMemo[month+1];
		}
		else if (monthPricesMemo[month] == INF) {
			// 그 달의 최소 비용 금액이 지정되지 않은 경우 -> 금액을 지정해야 함
			int monthPay = usePlan[month]*prices[0]; // 1일권
			monthPay = Math.min(prices[1], monthPay); // 1달권과 비교
			if (month+1<=endUse && monthPricesMemo[month+1] == INF)
				monthPricesMemo[month+1] = getMonthTotalPrice(month+1);
			if (month+3<=endUse && monthPricesMemo[month+3] == INF)
				monthPricesMemo[month+3] = getMonthTotalPrice(month+1);
			int afterOneMonth = month+1 <= endUse ? monthPricesMemo[month+1] : 0;
			int afterThreeMonth = month+3 <= endUse ? monthPricesMemo[month+3] : 0;
			int oneMonthTotal = monthPay+afterOneMonth; // month달 1달치로 계산
			int threeMonthTotal = prices[2]+afterThreeMonth; // month달 3달치로 계산
			monthPricesMemo[month] = Math.min(oneMonthTotal, threeMonthTotal);
		}
		return monthPricesMemo[month];
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		prices = new int[4];
		usePlan = new int[13];
		monthPricesMemo = new int[13];
		Arrays.fill(monthPricesMemo, INF);
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<4; index++)
			prices[index] = Integer.parseInt(tokenizer.nextToken());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		boolean startChecked = false;
		for (int month=1; month<=12; month++) {
			usePlan[month] = Integer.parseInt(tokenizer.nextToken());
			if (usePlan[month] > 0) {
				if (!startChecked) {
					startUse = month;
					startChecked = true;
				}
				endUse = month;
			}
		}
	}

}
