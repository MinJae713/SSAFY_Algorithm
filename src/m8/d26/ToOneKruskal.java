package m8.d26;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 각 섬에서 이을 수 있는 모든 터널의 경우의 수 저장(allTurnels)
 * 	1.1. 각 터널들이 객체 형태(Turnel)로 지정됨
 * 	1.2. from, to: 각 섬의 고유 번호, charge: 환경 부담금
 * 2. allTurnels의 터널들을 환경 부담금 기준 오름차순으로 정렬
 * 3. 각 섬들에 대한 서로소 집합 생성(parents)
 * 	3.1. parents의 각 값은 각 섬들의 부모에 해당하는 섬을 가리킴
 * 4. allTurnels의 각 터널들에 대해 반복(turnel)
 * 	4.1. 선택된 터널의 수(selectedCount)가 섬 갯수-1개가 되면 반복 종료
 * 	4.2. 터널 양쪽의 두 섬에 대해 unionIsland 실행
 * 		4.2.1. 파라미터는 두 섬 번호(from, to)
 * 		4.2.2. 두 섬을 포함하는 집합의 대표 번호 확인(find)
 * 			4.2.2.1. 파라미터는 확인하려는 섬의 번호(island)
 * 			4.2.2.2. island 값이 해당 값의 부모(islandParents)와 동일하다면?
 * 				4.2.2.2.1. island 값 반환
 * 			4.2.2.3. island의 부모 섬을 파라미터로 find 재귀 호출
 * 			4.2.2.4. 재귀 호출한 결과를 island의 부모 섬에 입력
 * 		4.2.3. 두 섬의 대표 번호가 동일하다면 false 반환
 * 		4.2.4. 두 섬의 대표 번호값 비교, 더 작은 쪽의 부모에 더 큰 쪽 번호 입력
 * 		4.2.5. true 반환
 *	4.3. unionIsland 실행 결과가 false라면 다음 반복 실행
 * 	4.4. selectedCount 1 증가
 * 	4.5. 최소 환경 부담금 합(minimumCharge)에 해당 터널의 환경 부담금 합산
 * 5. minimumCharge에서 소숫점 1의 자리까지 반올림한 값 계산
 */
public class ToOneKruskal {
	static class Turnel implements Comparable<Turnel>{
		int from;
		int to;
		double charge;
		public Turnel(int from, int to, double charge) {
			super();
			this.from = from;
			this.to = to;
			this.charge = charge;
		}
		@Override
		public int compareTo(Turnel other) {
			return Double.compare(charge, other.charge);
		}
		@Override
		public String toString() {
			return String.format("%d->%d, %f", from, to, charge);
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int islandCount;
	private static int[] xPoints;
	private static int[] yPoints;
	private static Double rate;
	private static int allTurnelCount;
	private static Turnel[] allTurnels;
	private static int[] islandParents;
	private static double minimumCharge;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("re_sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			int selectedCount = 0;
			for (int turnelIndex=0; turnelIndex<allTurnelCount && selectedCount<islandCount-1; turnelIndex++) {
				Turnel turnel = allTurnels[turnelIndex];
				boolean unioned = unionTurnel(turnel.from, turnel.to);
				if (!unioned) continue;
				selectedCount++;
				minimumCharge += turnel.charge;
			}
			builder.append("#").append(testCase).append(" ").
					append(Math.round(minimumCharge)).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static boolean unionTurnel(int from, int to) {
		int rootFrom = find(from);
		int rootTo = find(to);
		if (rootFrom == rootTo) return false;
		if (rootFrom < rootTo)
			islandParents[rootFrom] = rootTo;
		else islandParents[rootTo] = rootFrom;
		return true;
	}

	private static int find(int island) {
		if (island == islandParents[island]) return island;
		return islandParents[island] = find(islandParents[island]);
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		islandCount = Integer.parseInt(reader.readLine().trim());
		xPoints = new int[islandCount];
		yPoints = new int[islandCount];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<islandCount; index++)
			xPoints[index] = Integer.parseInt(tokenizer.nextToken());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<islandCount; index++)
			yPoints[index] = Integer.parseInt(tokenizer.nextToken());
		rate = Double.parseDouble(reader.readLine().trim());
		allTurnelCount = islandCount*(islandCount-1)/2;
		allTurnels = new Turnel[allTurnelCount];
		int turnelIndex = 0;
		for (int from=0; from<islandCount; from++)
			for (int to=from+1; to<islandCount; to++) {
				double charge = (Math.pow(xPoints[to]-xPoints[from], 2)+
						Math.pow(yPoints[to]-yPoints[from], 2))*rate;
				allTurnels[turnelIndex++] = new Turnel(from, to, charge);
			}
		Arrays.sort(allTurnels);
		islandParents = new int[islandCount];
		for (int island=0; island<islandCount; island++)
			islandParents[island] = island;
		minimumCharge = 0;
	}
}
