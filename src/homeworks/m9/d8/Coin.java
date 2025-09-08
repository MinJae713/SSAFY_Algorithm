package homeworks.m9.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - countMemo: 입력 받은 금액을 만들 수 있는 가지수 저장
 * 1. getCountMemo(): 각 금액을 만드는 가지수 반환
 * 	1.1. 파라미터는 가지수를 확인할 금액(money)
 * 2. countMemo의 0번째 위치는 1로 지정
 * 	2.1. 어떤 동전이든 0개를 쓰면 0원을 만들 수 있음 -> 1가지로 셈
 * 3. 동전 종류의 수 만큼 반복(coin)
 * 	3.1. 0원부터 만들어야 하는 금액(targetMoney)까지 
 * 		  coin원으로 만들 수 있는 가지수를 셈
 * 4. coin 위치에서 targetMoney 위치까지 반복 (index)
 * 	4.1. countMemo의 index 위치 값은 기존 countMemo의 index 위치 값과
 * 		  countMemo의 index-coin 위치 값을 더한 값으로 입력
 * 	4.2. coin 위치부터 시작하는 이유는 1~coin 직전 위치는 
 * 		  coin을 사용한 가지수를 만들 수 없기 때문임
 * 	4.3. ex) 4으로 1,2,3원을 만들 수 없는 것 
 * 				(0원은 0번 써서 만들수는 있어서 1번으로침)
 * 	4.4. coin이 다음 index로 증가할 때의 coin~targetMoney 연산은 
 * 		  이전 coin '또는' 현재 coin을 사용하여 
 * 		  각 금액을 만들 수 있는 가지수를 구하는 것임
 * 	4.5. coin이 순서대로 2,3,5가 들어온다면, 처음에는 0~targetMoney 각 금액을
 * 		  '2원'으로 만들 수 있는 가지수를 구하게 되고, 이후 coin이 3원이 되면
 * 		  3원만이 아닌, '3원 또는 2원'을 사용하여 0~targetMoney 각 금액을 만드는
 * 		  가지수를 구함! -> '또는'의 의미라, 이 가지수는 3원만, 혹은 2원만, 
 * 		  혹은 3원과 2원을 함께 사용할 때 나오는 경우의 수임
 * 	4.6. 그러한 동작을 구현하기 위해, coin이 3원일 때 각 금액의 가지수를 구하는 로직은
 * 		  countMemo에 아무 값도 입력되지 않은 상태에서 시작하는게 아닌,
 * 		  앞전에 coin이 2원일 때 적용되었던 countMemo 상태에서 시작하게 되는거임
 * 		4.6.1. 그렇게 적용해야 기존에 2원일 때 입력되었던 countMemo의 상태에 이어서
 * 				 3원을 적용할 수 있고, 그 결과, 2원 또는 3원을 적용한 가지수가 입력될 수 있음
 * 		4.6.2. 6원을 만든다고 치면, 2원에 대한 가지수를 계산할 때 1이 나왔지만,
 * 				 이에 덧붙여서 3원을 적용하게 되면 그 가지수는 2가 됨 -> 이때 2는 2원 또는 3원을
 * 				 적용할 때 나올 수 있는 가지수에 해당함
 * 	4.7. 그럼 2원 적용 이후, 3원을 적용할 때는 어떤 식으로 덧붙여지는가? 를 봐야 하는데,
 * 		  이는 기존에 2원까지 적용했을 때 6원의 가지수와 6원에서 3원을 뺀 금액의 가지수를 더한 값임
 * 		4.7.1. 6원에서 3원을 빼는 이유? 그거슨 3원으로만 구성했을 때 가지수를 보기 위함임
 * 	4.8. 3원으로만 구성할 수 있는 가지수라면 0개 포함까지 생각해서 0,3,6,9...가 있음
 * 		4.8.1. 이 수들은 3원으로만 구성한 가지수 1이 포함되어 있는 수들임
 * 		4.8.2. 이 수들은 3원으로만 구성되는 가지수에 대해서는 모두 1이라는 같은 값을 갖게 됨
 * 	4.9. 이때 가지수는 공통적으로 꼭 1일 필요는 없고, 0일 수도 있음
 * 		4.9.1. 1,4,7,10... 등은 3만으로 구성할 수 없는 수들 - 3원으로만 구성되는 가지수에는 0이 됨
 * 	4.10. 이에 따라, 어떤 수가 3만으로 구성할 수 있는 수인가 아닌가는 그 수를 3으로 계속 빼서
 * 			3보다 작은 수로 만들었을 때, 그 수가 0이냐 아니냐에 따라 다르다고 볼 수 있고
 * 			만일 0이 된다면 3이 하나의 가지수로서 포함되는거고, 1이나 2면 가지수로 포함되지 않는 것
 * 	4.11. 이런 원리로 가지수를 구하는 수에 3을 계속 빼다보면 그 수에 3만 들어가는 가지수가 있는가,
 * 			없는가를 알 수 있는데, 그 가지수를 알기 위해 반복상 먼저 수행되는 -3 위치의 값을 보는거임
 * 		4.11.1. 차피 3원이라는 값에 대해서는 같은 결과를 가지기 때문임
 * 	4.12. 그렇다고 3원으로 뺀 값들이 가지수가 항상 같다고는 할 수 없음 - 이는 각 수의 가지수를 확인할 때
 * 			3만 갖고 확인하지는 않기 때문임 -> 3과 6은 3이라는 가지수에 대해서는 같지만, 위 예시처럼
 * 			2원을 적용했다고 하면? -> 3은 가지수가 1, 6은 가지수가 2라는 결과가 나오게 됨
 * 	4.13. 왜 이런 차이가 생기는가? 이는 3은 2로 구성될 수 없지만, 6은 2로 구성될 수 있기 때문이라는 점임
 * 	4.14. 이에 따라 6의 가지수를 구할 때는 반드시 6에서 3을 뺀 3원의 가지수를 구하고, 그 값에 2원까지 적용했을
 * 			때를 나타내는 기존 6원의 가지수를 더하게 되는것!
 * 	4.15. 그래서?? D[i] = D[i]+D[i-현재 금액] 이라는 점화식이 그래서 나오는 것입니다....
 * 		4.15.1. 우항의 D[i]는 앞 과정에서 나온 금액의 가지수, D[i-현재 금액]은 현재 금액을 
 * 				  적용했을 때 나오는 앞 수의 가지수라고 생각하십슈....
 * 	4.16. 나름 연구를 하긴 했는디 맞을지는 모르것습니다.... (100프로 이해가 되진 않아서...)
 * 	4.17. 보충 설명이 필요할 거 같으면 얘기 주시면 감사합니다....!! 언제든 환영입니다
 * 참고: https://d-cron.tistory.com/23
 */
public class Coin {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int moneyCount;
	private static int[] moneyTypes;
	private static int targetMoney;
	private static int[] countMemo;

	public static void main(String[] args) throws IOException {
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			builder.append(getCountMemo()).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int getCountMemo() {
		for (int coinIndex=0; coinIndex<moneyCount; coinIndex++) {
			int coin = moneyTypes[coinIndex];
			for (int index=coin; index<=targetMoney; index++)
				countMemo[index] = countMemo[index]+countMemo[index-coin];
			// coin원 까지 포함한 가지수 = 기존 coin이 없었을 때 가지수+현재 coin 만큼 뒤의 가지수
		}
		// coinIndex가 증가하면? countMemo에는 이전 coin들에 대한 가지수, 
		// 현재 coinIndex 위치 coin에 대한 가지수가 함께 포함됨
		return countMemo[targetMoney];
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		moneyCount = Integer.parseInt(reader.readLine().trim());
		moneyTypes = new int[moneyCount];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<moneyCount; index++)
			moneyTypes[index] = Integer.parseInt(tokenizer.nextToken());
		targetMoney = Integer.parseInt(reader.readLine().trim());
		countMemo = new int[targetMoney+1];
		countMemo[0] = 1;
	}

}
