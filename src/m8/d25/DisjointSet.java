package m8.d25;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. find: 입력 받은 원소가 소속된 집합의 대표값 구함
 * 	1.1. 파라미터는 찾으려는 값(number)
 * 	1.2. 자신의 부모와 자신과 같은 값이라면? number 반환
 * 	1.3. 자신의 부모에 해당되는 값을 파라미터로 find 재귀호출 및
 * 		 호출 결과를 자신의 부모 값으로 지정
 * 		1.3.1. find 재귀 호출 결과, 그 number가 속한 집합의 대표값이 나오고,
 * 			      그 대표값이 자신의 부모가 되어, 경로가 압축됨
 * 2. union: 입력 받은 두 원소가 속한 집합의 합집합을 구함
 * 	2.1. 파라미터는 합집합 연산을 수행하려는 집합의 두 원소
 * 	2.2. 두 원소가 속한 집합의 대표값 반환(find)
 * 	2.3. 대표값이 동일하다면 함수 실행 종료
 * 	2.4. 한 대표값의 부모를 다른 한쪽의 대표값으로 변경
 */
public class DisjointSet {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int numberCount;
	private static int commandCount;
	private static int[] parents;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			setOperate(testCase);
		}
		System.out.print(builder);
		reader.close();
	}

	private static void setOperate(int testCase) throws IOException {
		builder.append("#").append(testCase).append(" ");
		for (int index=0; index<commandCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int command = Integer.parseInt(tokenizer.nextToken());
			int number1 = Integer.parseInt(tokenizer.nextToken());
			int number2 = Integer.parseInt(tokenizer.nextToken());
			switch (command) {
			case 0:
				union(number1, number2);
				break;
			case 1:
				builder.append(find(number1) == find(number2) ? 1 : 0);
				break;
			}
		}
		builder.append("\n");
	}

	private static void union(int number1, int number2) {
		int root1 = find(number1);
		int root2 = find(number2);
		if (root1 == root2) return;
		parents[root1] = root2;
	}

	private static int find(int number) {
		if (number == parents[number]) return number;
		return parents[number] = find(parents[number]);
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		numberCount = Integer.parseInt(tokenizer.nextToken());
		parents = new int[numberCount+1];
		for (int number=1; number<=numberCount; number++)
			parents[number] = number;
		commandCount = Integer.parseInt(tokenizer.nextToken());
	}
}
