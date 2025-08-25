package m8.d25;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 서로 알고 있는 관계 입력, 알고 있는 관계에 대해 union 실행
 * 	1.1. 파라미터는 서로 알고있는 두 사람(from, to)
 * 	1.2. 두 사람이 속한 그룹의 대표 반환 (find)
 * 		1.2.1. 자신(person)과 자신의 부모(knowParent)가 동일하다면 person 반환
 * 		1.2.2. 자신의 부모를 파라미터로 find 재귀호출, 호출 결과를 자신의 부모로 입력
 * 	1.3. 대표가 동일하다면 함수 실행 종료
 *	1.4. 한쪽 대표의 부모로 다른 한쪽 대표 입력
 * 2. 모든 사람들에 대해 반복 수행(person)
 * 	2.1. person이 속한 그룹의 대표 반환 
 * 		2.1.1. 해당 대표를 처음 찾았다면(rootFounded) true로 변경 및 groupCount 1 증가
 */
public class VillagersUF {
	// 왜 그래프가 아니라 집합으로 풀어야 하는걸까?
	// 그래프 순회로 하면 문제가 안풀리는디.... 이유가 뭘까?
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int personCount;
	private static int knowCount;
	private static int[] knowParent;
	private static boolean[] rootFounded;
	private static int groupCount;

	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("s_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			makeSet();
			countRoots();
			builder.append("#").append(testCase).
					append(" ").append(groupCount).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	private static void makeSet() throws IOException {
		for (int index=0; index<knowCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			if (!tokenizer.hasMoreTokens()) continue; 
			int from = Integer.parseInt(tokenizer.nextToken());
			if (!tokenizer.hasMoreTokens()) continue; 
			int to = Integer.parseInt(tokenizer.nextToken());
			union(from, to);
		}
	}
	private static void union(int from, int to) {
		int rootFrom = find(from);
		int rootTo = find(to);
		if (rootFrom == rootTo) return;
		knowParent[rootFrom] = rootTo;
	}
	private static int find(int person) {
		if (person == knowParent[person]) return person;
		return knowParent[person] = find(knowParent[person]);
	}
	private static void countRoots() {
		for (int person=1; person<=personCount; person++) {
			if (rootFounded[find(person)]) continue;
			rootFounded[find(person)] = true;
			groupCount++;
		}
	}
	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		personCount = Integer.parseInt(tokenizer.nextToken());
		knowParent = new int[personCount+1];
		rootFounded = new boolean[personCount+1];
		for (int number=1; number<=personCount; number++)
			knowParent[number] = number;
		knowCount = Integer.parseInt(tokenizer.nextToken());
		groupCount = 0;
	}
}
