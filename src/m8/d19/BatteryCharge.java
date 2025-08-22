package m8.d19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 시간이 0~time이 될 때 까지 반복
 * 2. A, B 각각 현 위치에서 선택할 수 있는 BC 확인
 * 	2.1. BC의 갯수 만큼 반복하면서 |BC위치x-A 혹은 B 위치x|+|BC위치y-A 혹은 B 위치y|
 * 			이 값이 해당 BC의 충전 범위 내에 있는지 확인
 * 	2.2. 충전 범위 내에 있다면 해당 BC의 인덱스 값을 각각 A, B 선택 가능 BC 리스트에 추가
 * 3. A, B 각각에서 BC를 선택해서 해당 BC의 성능 만큼 충전
 * 	3.1. A가 어느 BC에도 포함되어 있지 않다면? B가 선택할 수 있는 BC 중
 * 		  성능이 가장 높은 BC의 성능 값을 충전 총합에 합산
 * 	3.2. B의 경우에도 동일 적용 -> A 기준 가장 높은 성능 값 합산
 * 	3.3. A, B가 최소 하나 이상의 BC에 포함되어 있다면?
 * 		3.3.1. A, B에서 선택할 수 있는 BC의 성능 조합 생성
 * 		3.3.2. 두 성능을 더한 후, 만들어진 조합에 따른 성능 합산의 최대값과 비교
 * 		3.3.3. 단, A, B에서 동일한 BC를 선택했다면 두 성능 합산 후 합산 값을 2로 나눔
 * 4. 마지막 time 값이 아닌 경우, A, B의 다음 위치(x, y) 입력
 * 5. A, B의 BC 리스트 초기화
 */
public class BatteryCharge {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[] deltaX;
	private static int[] deltaY;
	private static int time;
	private static int bcCount;
	private static int[] movingA;
	private static int[] movingB;
	private static int xA;
	private static int yA;
	private static int xB;
	private static int yB;
	private static int[] bcLocationX;
	private static int[] bcLocationY;
	private static int[] coverage;
	private static int[] performance;
	private static List<Integer> bcBoundA;
	private static List<Integer> bcBoundB;
	private static int totalCharge;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			bcCharge();
			builder.append("#").append(testCase).
					append(" ").append(totalCharge).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static void bcCharge() {
		for (int timeIndex=0; timeIndex<=time; timeIndex++) {
			setBCBound();
			calculateMaxPerformance();
			if (timeIndex == time) continue;
			nextMoving(timeIndex);
			clearBCSBound();
		}
	}
	private static void setBCBound() {
		for (int bcIndex=0; bcIndex<bcCount; bcIndex++) {
			if (Math.abs(bcLocationX[bcIndex]-xA)+
					Math.abs(bcLocationY[bcIndex]-yA)<=
					coverage[bcIndex])
				bcBoundA.add(bcIndex);
			if (Math.abs(bcLocationX[bcIndex]-xB)+
					Math.abs(bcLocationY[bcIndex]-yB)<=
					coverage[bcIndex])
				bcBoundB.add(bcIndex);
		}
	}
	private static void calculateMaxPerformance() {
		int maxPerformance = 0;
		if (bcBoundA.size() == 0 && bcBoundB.size() == 0);
		else if (bcBoundB.size() == 0) 
			for (int bcA : bcBoundA)
				maxPerformance = Math.max(maxPerformance, performance[bcA]);
		else if (bcBoundA.size() == 0) 
			for (int bcB : bcBoundB)
				maxPerformance = Math.max(maxPerformance, performance[bcB]);
		else {
			for (int bcA : bcBoundA) {
				for (int bcB : bcBoundB) {
					int totalPerformance = performance[bcA]+performance[bcB];
					if (bcA == bcB) totalPerformance /= 2;
					maxPerformance = Math.max(maxPerformance, totalPerformance);
				}
			}
		}
		totalCharge += maxPerformance;
	}
	private static void nextMoving(int timeIndex) {
		xA += deltaX[movingA[timeIndex]];
		yA += deltaY[movingA[timeIndex]];
		xB += deltaX[movingB[timeIndex]];
		yB += deltaY[movingB[timeIndex]];
	}
	private static void clearBCSBound() {
		bcBoundA.clear();
		bcBoundB.clear();
	}
	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		deltaX = new int[] {0, 0, 1, 0, -1};
		deltaY = new int[] {0, -1, 0, 1, 0};
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		time = Integer.parseInt(tokenizer.nextToken());
		bcCount = Integer.parseInt(tokenizer.nextToken());
		movingA = new int[time];
		movingB = new int[time];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int timeIndex=0; timeIndex<time; timeIndex++)
			movingA[timeIndex] = Integer.parseInt(tokenizer.nextToken());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int timeIndex=0; timeIndex<time; timeIndex++)
			movingB[timeIndex] = Integer.parseInt(tokenizer.nextToken());
		xA = yA = 1;
		xB = yB = 10;
		bcLocationX = new int[bcCount];
		bcLocationY = new int[bcCount];
		coverage = new int[bcCount];
		performance = new int[bcCount];
		for (int bcIndex=0; bcIndex<bcCount; bcIndex++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			bcLocationX[bcIndex] = Integer.parseInt(tokenizer.nextToken());
			bcLocationY[bcIndex] = Integer.parseInt(tokenizer.nextToken());
			coverage[bcIndex] = Integer.parseInt(tokenizer.nextToken());
			performance[bcIndex] = Integer.parseInt(tokenizer.nextToken());
		}
		bcBoundA = new ArrayList<Integer>();
		bcBoundB = new ArrayList<Integer>();
		totalCharge = 0;
	}
}
