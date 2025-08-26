package m8.d26;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 모든 연결들에 대해 반복
 * 	1.1. 선택된 연결 수(selectedCount)가 컴퓨터 수-1이 되면 반복 종료
 * 2. 연결 양쪽의 컴퓨터에 대해 unionComputer 연산
 * 	2.1. 파라미터는 양쪽 컴퓨터(computerA, B)
 * 	2.2. 양쪽 컴퓨터를 포함하는 집합의 대표 반환(find)
 * 		2.2.1. 파라미터는 컴퓨터 번호(computer)
 * 		2.2.2. computer와 해당 컴퓨터의 부모 컴퓨터가 같다면?
 * 			2.2.2.1. computer 반환
 * 		2.2.3. computer의 부모를 파라미터로 find 재귀 호출
 * 		2.2.4. 호출 결과를 computer 부모로 입력
 * 	2.3. 양쪽 컴퓨터의 대표가 같다면 false 반환
 * 	2.4. 두 대표 값 중 더 작은 값의 부모에 더 큰 대표값 입력
 * 	2.5. true 반환
 * 3. 연산 결과 false가 나왔다면 다음 반복 실행
 * 4. selectedCount 1 증가
 * 5. 연결 최소 비용 총합(minimumPrice)에 해당 연결 비용 합산
 */
public class NetworkConnect {
	static class Connect implements Comparable<Connect>{
		int computerA;
		int computerB;
		int price;
		public Connect(int computerA, int computerB, int price) {
			super();
			this.computerA = computerA;
			this.computerB = computerB;
			this.price = price;
		}
		@Override
		public int compareTo(Connect other) {
			return Integer.compare(price, other.price);
		}
	}
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int computerCount;
	private static int[] computerParents;
	private static int connectCount;
	private static Connect[] connects;
	private static int minimumPrice;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		int selectedCount = 0;
		for (int connectIndex=0; connectIndex<connectCount && selectedCount<computerCount-1; connectIndex++) {
			Connect connect = connects[connectIndex];
			boolean unioned = unionComputer(connect.computerA, connect.computerB);
			if (!unioned) continue;
			selectedCount++;
			minimumPrice += connect.price;
		}
		System.out.println(minimumPrice);
		reader.close();
	}

	private static boolean unionComputer(int computerA, int computerB) {
		int aRoot = find(computerA);
		int bRoot = find(computerB);
		if (aRoot == bRoot) return false;
		else if (aRoot < bRoot) computerParents[aRoot] = bRoot;
		else computerParents[bRoot] = aRoot;
		return true;
	}

	private static int find(int computer) {
		if (computer == computerParents[computer]) return computer;
		return computerParents[computer] = find(computerParents[computer]);
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		computerCount = Integer.parseInt(reader.readLine().trim());
		computerParents = new int[computerCount+1];
		for (int computer=1; computer<=computerCount; computer++)
			computerParents[computer] = computer;
		connectCount = Integer.parseInt(reader.readLine().trim());
		connects = new Connect[connectCount];
		for (int connect=0; connect<connectCount; connect++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int computerA = Integer.parseInt(tokenizer.nextToken());
			int computerB = Integer.parseInt(tokenizer.nextToken());
			int price = Integer.parseInt(tokenizer.nextToken());
			connects[connect] = new Connect(computerA, computerB, price);
		}
		Arrays.sort(connects);
		minimumPrice = 0;
	}

}
