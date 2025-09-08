package homeworks.m9.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - minCostMemo: 앱의 수량과 비활성화 해야 할 메모리 양이 주어졌을 때 최소 비용 저장
 * 1. getMinCost(): minCostMemo 값 입력
 * 	1.1. 파라미터는 앱의 수(count), 비활성화 메모리의 양(memory)
 * 2. count 위치 앱 비활성화 시 최소 비용(notAvailable) 값은 count 위치 비용, 
 * 	    활성화 시 최소 비용(available) 값은 최대값으로 초기화
 * 3. count 위치 앱을 비활성화 시키는 경우
 * 	3.1. memory가 count 위치의 메모리의 양 이상이고,
 * 	  	 minCostMemo의 count-1, memory-count 위치 메모리 양에 해당하는 값이 초기값이면?
 * 		3.1.1. count-1, memory-count 위치의 메모리 양을 파라미터로 getMinCost() 재귀호출
 * 		3.1.2. 위 호출 결과를 minCostMemo 위치에 입력
 * 4. count 위치 앱을 비활성화 하지 않는 경우
 * 	4.1. 0~count 위치의 메모리 총합이 memory와 같거나 크고,
 * 		 minCostMemo의 count-1, memory 위치가 초기값이면?
 * 	4.2. count-1, memory를 파라미터로 getMinCost() 재귀호출
 * 5. 앱 비활성화 시 최소 비용(notAvailable)은 필요 메모리 양이 count 위치 메모리 양 보다 크거나 같다면
 * 	  minCostMemo의 count-1, memory-count 위치의 메모리 양+현재 위치 비용을, 아니라면 0 입력
 * 6. 앱 활성화 시 최소 비용(available)은 minCostMemo의 count-1, memory 위치 값 입력
 * 7. notContained와 contained 값 비교 -> 더 작은 값 반환
 */
public class App {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int count;
	private static int needMemory;
	private static int[] appBytes;
	private static int[] appCosts;
	private static int[][] minCostMemo;
	private static int[] totalMemory;
	private static final int INF = Integer.MAX_VALUE;
	
	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		System.out.println(getMinCost(count, needMemory));
		reader.close();
	}

	private static int getMinCost(int count, int memory) {
		if (memory <= 0) return 0;
		else if (memory > totalMemory[count]) return INF;
		if (memory <= totalMemory[count] && 
				minCostMemo[count-1][memory] == INF)
			minCostMemo[count-1][memory] = getMinCost(count-1, memory);
		if (memory >= appBytes[count] && 
				minCostMemo[count-1][memory-appBytes[count]] == INF) {
			minCostMemo[count-1][memory-appBytes[count]] = 
					getMinCost(count-1, memory-appBytes[count]);
		}
		int notAvailable = appCosts[count]+
				(memory>=appBytes[count] ? 
						minCostMemo[count-1][memory-appBytes[count]] : 0);
		int available = minCostMemo[count-1][memory];
		return Math.min(notAvailable, available);
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		count = Integer.parseInt(tokenizer.nextToken());
		needMemory = Integer.parseInt(tokenizer.nextToken());
		appBytes = new int[count+1];
		appCosts = new int[count+1];
		minCostMemo = new int[count+1][needMemory+1];
		totalMemory = new int[count+1];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		appBytes[0] = Integer.MAX_VALUE;
		for (int index=1; index<=count; index++) {
			appBytes[index] = Integer.parseInt(tokenizer.nextToken());
			if (index == 1) totalMemory[index] = appBytes[index];
			else totalMemory[index] = totalMemory[index-1]+appBytes[index];
		}
		appCosts[0] = Integer.MAX_VALUE;
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=1; index<=count; index++)
			appCosts[index] = Integer.parseInt(tokenizer.nextToken());
		for (int index=0; index<=count; index++)
			Arrays.fill(minCostMemo[index], INF);
	}

	/**
	 * 좋은 질문이야. 너가 작성한 코드는 **재귀 + 메모이제이션** 방식으로 잘 구현되어 있지만, 이 문제에서는 \*\*메모리 사용량이 너무 커져서 "메모리 초과"\*\*가 나는 상황이야. 이유와 해결책을 아래에 자세히 설명할게.

		---
		
		## 💥 문제 요약 (백준 7579번 - 앱)
		
		* 앱을 비활성화해서 필요한 메모리를 확보하고,
		* 이때 드는 **최소 비용**을 구하는 문제.
		* **입력 조건**
		
		  * 앱 개수 $N \leq 100$
		  * 필요한 메모리 $M \leq 10^7$
		  * 각 앱의 메모리 최대 10,000
		  * 각 앱의 비용 최대 100
		
		---
		
		## 🔥 왜 메모리 초과가 나는가?
		
		너는 현재 이 배열을 선언했지:
		
		```java
		minCostMemo = new int[count+1][needMemory+1];
		```
		
		즉, 크기는:
		
		* `count + 1` (최대 101)
		* `needMemory + 1` (최대 **10,000,001**)
		
		따라서 **약 101 × 10,000,001 = 약 1,010,000,101개의 int 값**,
		\=> 메모리 사용량 약 **4GB** → 메모리 초과 발생
		
		---
		
		## 💡 해결 방법: "비용 기준 DP"로 전환하기
		
		> 📌 **핵심 아이디어**:
		> **필요한 메모리**를 인덱스로 쓰는 게 아니라, **비용**을 인덱스로 사용하는 거야.
		
		### 📊 변경된 DP 방식
		
		* `dp[cost] = 확보할 수 있는 최대 메모리`
		* 비용을 기준으로 0부터 시작해서, 점점 비용을 올려가며 최대 메모리를 계산해.
		* 필요한 메모리 M 이상 확보할 수 있는 최소 비용을 찾아내는 방식!
		
		---
		
		## ✅ 개선된 코드 예시
		
		```java
		import java.io.*;
		import java.util.*;
		
		public class Main {
		    public static void main(String[] args) throws IOException {
		        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
		        int N = Integer.parseInt(tokenizer.nextToken());
		        int M = Integer.parseInt(tokenizer.nextToken());
		
		        int[] memory = new int[N];
		        int[] cost = new int[N];
		        tokenizer = new StringTokenizer(reader.readLine());
		        for (int i = 0; i < N; i++) {
		            memory[i] = Integer.parseInt(tokenizer.nextToken());
		        }
		
		        tokenizer = new StringTokenizer(reader.readLine());
		        int totalCost = 0;
		        for (int i = 0; i < N; i++) {
		            cost[i] = Integer.parseInt(tokenizer.nextToken());
		            totalCost += cost[i];
		        }
		
		        int[] dp = new int[totalCost + 1]; // dp[cost] = max memory
		        for (int i = 0; i < N; i++) {
		            for (int j = totalCost; j >= cost[i]; j--) {
		                dp[j] = Math.max(dp[j], dp[j - cost[i]] + memory[i]);
		            }
		        }
		
		        for (int i = 0; i <= totalCost; i++) {
		            if (dp[i] >= M) {
		                System.out.println(i);
		                break;
		            }
		        }
		    }
		}
		```
		
		---
		
		## 📈 메모리 사용량 비교
		
		| 접근 방식 | 인덱스 기준           | 배열 크기        | 메모리 사용량      |
		| ----- | ---------------- | ------------ | ------------ |
		| 기존 방식 | memory (최대 10^7) | `101 x 10^7` | ❌ **약 4GB**  |
		| 개선 방식 | cost (최대 10,000) | `1 x 10001`  | ✅ **약 40KB** |
		
		---
		
		## ✅ 결론
		
		* 이 문제는 **배낭 문제(Knapsack)** 유형으로, 비용을 기준으로 DP를 구성해야 메모리 초과를 피할 수 있어.
		* 시간/공간 복잡도를 동시에 만족시킬 수 있는 방식은 `cost` 기준으로 푸는 것이 가장 적합해.
		
		---
		
		필요하다면 기존 재귀 기반 코드도 `cost` 기준으로 재작성해줄 수 있어.
		어떻게 할지 더 도와줄까?

	 */
}
