package homeworks.m9.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - maxByteMemo: 비용 별로 최대로 확보할 수 있는 메모리 용량 저장
 *   ㄴ 크기는 모든 앱의 비용 총합+1
 *   ㄴ 각 비용마다 최대 용량을 구성할 때 그 용량에 어떤 앱이 들어가 있는가, 아닌가로 나뉨
 *   ㄴ 들어가 있는 경우의 값: 들어간 앱의 비용만큼 뺀 경우의 maxByteMemo 값+들어간 앱의 용량
 *   	  ㄴ maxByteMemo[현재 비용-포함 앱 비용]+포함 앱 용량
 *   ㄴ 들어가지 않은 경우의 값: 비용은 그대로, 들어간 앱 제외 나머지 앱에 대한 maxByteMemo 값
 *   	  ㄴ maxByteMemo[현재 비용]
 *   ㄴ 이 둘 중 더 큰 값이 들어감
 * 1. 각 앱 마다 반복(app)
 * 	1.1. 반복으로 확인된 각 앱의 비용 누적 (totalCost)
 * 	1.2. 해당 앱 비용 총합~app의 비용까지 반복(cost)
 * 		1.2.1. app이 포함된 경우와 app이 포함되지 않은 경우 중, 더 큰 값 입력
 * 		1.2.2. 포함된 경우: maxByteMemo[cost-app의 cost]+app의 용량
 * 		1.2.3. 포함되지 않은 경우: 기존의 maxByteMemo[cost] 그대로
 * 		1.2.4. 해당 app 비용까지만 보는 이유
 * 			1.2.4.1. 그 이전은 비용 때문에 해당 app이 포함될 수 없음 -> 무조건 미포함 상태 그대로 감
 * 		1.2.5. 저거 반복이 역순인 이유?
 * 			1.2.5.1. maxByteMemo가 1차원 배열이고, 기존 위치가 계속 업데이트 되는 식으로 구현됨
 * 			1.2.5.2. 그 말인 즉, 순방향으로 올라간다면? 이전 값을 써야 하는데 자칫 잘못해서 바뀐 값을 갖다가 쓰는 일이 생김
 * 				1.2.5.2.1. ex) 3위치vs6위치 비교해서 6위치 값이 바뀌었고, 이후 6위치vs9위치 비교해서 9위치 값을 바꿔야 한다면?
 * 				1.2.5.2.2.     6과 9 위치 비교할 때는 바뀌기 전, 즉 3과 6을 비교하는 시점의 6위치 값을 쓰고 싶으나,
 * 									3과 6 비교 결과로 이미 6 위치가 바뀌었기 때문에 기존의 6위치 값을 쓰지 못함
 * 				1.2.5.2.3. 그래ㅐ서? 이 문제를 위해 반복을 역순으로 돌리면서 9위치를 먼저 바꾸고, 6위치를 나중에 바꾸는 식으로 구현함
 * 			1.2.5.3. 이는 앱이 포함된 경우가 현재 위치보다 앞에 있는 값을 확인하기 때문에 비롯된 문제임
 * 				1.2.5.3.1. 만일 현재 위치와 현재보다 뒤에 있는 값을 비교한다면? 
 * 							  뒤에 있는 값은 다시 기존 값을 써야 하니까 이럴 때는 순방향으로 가야함
 * 				1.2.5.3.2. 로직을 어캐 짜는가에 따라 다르니까 잘 봐야 할 것임
 * 2. 각 비용 마다 반복(cost)
 * 	2.1. maxByteMemo 값이 필요 용량(needMemory) 
 * 		  이상이라면 해당 cost return
 * 3. -1 반환 -> maxByteMemo 모든 위치가 필요 용량보다 작은 경우
 * 	3.1. 문제 제약조건에 따르면 다다를 수 없는 위치임
 * 
 * - 3일은 고민한 문제임, 문제 요구대로 최소 비용을 배낭 아이디어로 풀려다가 답이 안나와서 GPT 한번 돌려봄
 * - 돌려보니 최소 비용으로 생각하지 말고, 우선 각 비용당 최대 용량을 구하는 식으로 생각하라 해서 그 부분을 이해하는게 한 이틀 걸림
 * - 각 비용마다 나올 수 있는 최대 용량을 구하고, 필요한 용량을 처음으로 만족하는 맨 처음 비용을 구하는 과정을 이해하고 나니,
 *   그러면 이를 위해 memo에 어떻게 값을 채워넣을까 고민하게 됨
 * - 배낭 아이디어는 무조건 재귀의 결과로 memo에 값을 채워넣는 방식이라고 생각했기에, 
 *   memo에 채워넣는 방식을 고민할 때는 배낭 아이디어를 아얘 생각하지 못했음
 *   ㄴ 다만 비용이 주어질 때 각 앱을 비활성화할 수 있는지 여부에 따라 최대 용량이 어떻게 결정되겠구나 
 *   	  하는 생각은 실제로 최대 용량 memo 테이블을 그려보면서 아이디어는 얻음
 * - memo 테이블 각 위치에 최대 용량이 얼마가 들어가는지 감으로는 알겠는데, 
 *   그 값이 왜 그런 방식으로 들어가는지 원리를 알 수 없어서 GPT를 다시 돌림
 *   
 * - 찾아보니까 드디어 깨달은 것 -> 이 또한 배낭의 원리였구나...
 *   ㄴ 각 앱을 포함하는가, 포함하지 않는가에 따라 그 위치에 들어가는 용량의 최대값이 결정되는 것이었음
 * - 다만 그 원리를 재귀를 사용하지 않고, 1번째 앱, 2번째 앱... 순서대로 테이블에 값을 바꾸는 식으로 구현하는 것일 뿐임
 * - 원래 내가 알던 배낭? 2차원 배열에 행은 각 물건의 포함 개수, 열에는 물건의 제한 크기가 들어가서, 각 행, 열 위치 값은 최대 가치가 들어갔음
 *   ㄴ 배열을 채우기 위해 재귀를 써서 포함이 되면 주어진 제한 크기에 해당 물건의 크기를 빼서 이전의 최대 가치를 구하고, 
 *     그 값에 해당 물건의 가치를 더한 값과 기존 그 물건을 포함하지 않았을 때 최대 가치를 구한 값, 이렇게 두 값을 비교함
 *   ㄴ 이때는 재귀의 수를 줄이기 위해 그냥 메모이제이션이라는 기법을 쓰는구나 정도로만 이해할 뿐이었음
 * - 하지만 이 경우는 배낭의 원리를 그대로 쓰면서도 어떻게 재귀를 쓰지 않고 배낭 원리를 
 *   이용해 주어진 비용에 대한 최대 용량을 각각 구할 수 있을까에 대한 문제였음
 *   ㄴ 물론 생각하기 나름으로 재귀를 쓸 수도 있었겠지만, 이 문제에서는 최소한 재귀가 떠오르지 않았음
 *   
 * - 우선 어떤 물건을 포함하는가, 포함하지 않는가로 나누어, 어떤 경우를 최대 가치를 결정하기 위한 값으로 쓸 것인가 선택하는 아이디어는 동일함.
 * - 다만 큰 차이는 재귀일 때는 분할 정복의 원리라, n개의 물건에 대한 최대 가치를 구하기 위해 n-1, n-2개에 대해 먼저 구했음
 * - 하지만 재귀를 안쓰고 걍 메모이제이션만 쓴다면? 애초에 시작부터 물건의 총 개수로 시작하는게 아니라, 물건 1개일 때, 2개일 때
 *   순서대로 각 비용에서 만들 수 있는 최대 가치를 구하게 됨
 *   ㄴ 물건 1개일 때 각 비용의 최대 가치를 구하면, 물건 2개일 때 최대 가치를 구할 수 있고, 이를 통해 물건 3개일 때 최대 가치가 나오는 식.
 *   ㄴ 그니까 분할을 하는 것은 아니고, 걍 아래서부터 정복하고 정복하고 정복하면서 총 물건의 개수가 될 때 까지 올라오는 느낌일세
 *   
 * - 이 과정에서 물건 1개일 때 최대 가치를 통해 물건 2개일 때 최대 가치를 구하고, 또 그것을 통해 물건 3개일 때 최대 가치를 어떻게 구하게 되는가?
 *   ㄴ 여기에서 원래 재귀쓸 때 적용했던 배낭 문제의 아이디어가 적용되는 것임
 *   ㄴ 물건 2개일 때 제한이 x인 상황에서 최대 가치를 구한다면? 물건 1개인 경우 제한이 x인 상황의 최대 가치는 2번째 물건이 "포함되지 않은" 경우로서,
 *      물건이 1개인 경우 제한이 x-2번째 물건의 제한인 상황의 최대 가치+2번째 물건의 가치 -> 이 둘의 합은 2번째 물건이 "포함된" 경우로서 고려됨
 *   ㄴ 여기서 각각 물건이 1개인 경우 최대 가치는 이미 물건이 1개일 때 앞선 반복으로 구해진 값 그대로 쓰는 것 -> 정복된 결과를 갖고 또 다른 정복을 이루는 것임
 *   
 * - 또 커다란 차이가 있다면? 재귀를 쓸 때는 memo가 2차원이었지만, 여기서는 1차원임
 *   ㄴ 이는 앞선 과정으로 기록된 각 비용에 따른 최대 용량을 그대로 갖다 써서 현재 과정의 최대 용량을 지정하는 것
 *   ㄴ 물론 재귀를 할 때도 memo를 1차원으로 만들 수도 있었겠지만, 기본적으로 2차원을 쓸 때는 맨 처음 설정한 초기값에서 배열 값이 1번만 바뀌었음
 *   ㄴ 그래서 만일 memo의 어떤 위치가 초기값이다? 그러면 재귀를 통해 그 위치의 값을 어떤 최대 가치에 해당하는 값으로 바꾸었고, 
 *   	  바뀐 이후 그 위치에 대해서는 값이 바뀌지 않음 (초기값이 아니기 때문)
 * 
 * - 재귀를 안쓰면 memo의 각 위치 값이 바뀔 수 있음! -> 기존에 어떤 위치에 있던 값을 사용하여 그 위치의 값을 바꾸게 된다면 굳이 2차원을 쓸 필요가 없어짐
 * - 문제를 겨우 풀면서 DP의 아이디어는 꼭 재귀에만 적용되지 않음을 알았디
 * - 이전의 시행으로 저장된 메모이제이션 값을 통해 다시 그 이후의 메모이제이션이 이뤄지는 것 또한 DP인 것 -> 이는 재귀에만 국한된 얘기가 아님
 * - 글구 배낭 문제는 꼭 재귀로만 푸는 문제가 아니구나 하는 점도 알았고... 뭐든 공식처럼 외우기 보다 생각을 좀 열어서 다른 관점으로 접근하는 연습이 필요할 듯
 * - 이상 9월 11일 새벽 1시의 유민재였습니다
 */
public class App {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int count;
	private static int needMemory;
	private static int[] appBytes;
	private static int[] appCosts;
	private static int totalAppCost;
	private static int[] maxByteMemo;
	
	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		System.out.println(getMaxByte());
		reader.close();
	}

	private static int getMaxByte() {
		for (int app=1; app<=count; app++) {
			for (int cost=totalAppCost; cost>=appCosts[app]; cost--) {
				int contained = maxByteMemo[cost-appCosts[app]]+appBytes[app];
				maxByteMemo[cost] = Math.max(maxByteMemo[cost], contained);
				// maxByteMemo[cost]를 그대로 쓰는 것은 app이 
				// 포함되지 않은 경우, 최대 용량이 그대로 가기 때문임
				// 역방향인 이유 - 바뀐 값을 그대로 쓰지 않기 위해서임
			}
		}
		for (int cost=0; cost<=totalAppCost; cost++)
			if (maxByteMemo[cost] >= needMemory)
				return cost;
		return -1;
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		count = Integer.parseInt(tokenizer.nextToken());
		needMemory = Integer.parseInt(tokenizer.nextToken());
		appBytes = new int[count+1];
		appCosts = new int[count+1];
		appBytes[0] = Integer.MAX_VALUE;
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=1; index<=count; index++)
			appBytes[index] = Integer.parseInt(tokenizer.nextToken());
		appCosts[0] = Integer.MAX_VALUE;
		totalAppCost = 0;
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=1; index<=count; index++)
			totalAppCost += appCosts[index] = Integer.parseInt(tokenizer.nextToken());
		maxByteMemo = new int[totalAppCost+1];
	}
}
