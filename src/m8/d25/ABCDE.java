package m8.d25;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. N명의 사람에 대해 반복(number)
 * 	1.1. 5명을 찾았다면 반복 종료(hasFound)
 * 2. number 위치 사람을 시작으로 5명까지 들어갈 수 있는지 확인(getRelation)
 * 	2.1. 파라미터는 탐색 사람 번호(number), 들어온 깊이(depth)
 * 	2.2. 해당 번호 방문 여부 true 지정
 * 	2.3. 들어온 깊이가 5라면 true 반환
 * 	2.4. 해당 번호와 인접한 번호(relationPerson)들에 대해 반복
 * 		2.4.1. 이미 방문한 번호라면 다음 반복 실행
 * 		2.4.2. 방문하지 않았다면 해당 번호와 depth+1를 파라미터로 재귀 호출
 * 		2.4.3. 호출 이후, relationPerson 방문 여부 다시 돌려놓음
 * 			2.4.3.1. 다음 반복 시 다른 위치에서 들어갈 수 있게 하기 위함임
 * 	2.5. false 반환
 */
public class ABCDE {
	static class Person {
		int number;
		Person next;
		public Person(int number, Person next) {
			super();
			this.number = number;
			this.next = next;
		}
	}
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int peopleCount;
	private static Person[] relations;
	private static boolean[] visited;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		initialize();
		// logic
		boolean hasFound = false;
		for (int number=0; number<peopleCount && !hasFound; number++) {
			visited = new boolean[peopleCount];
			hasFound = getRelation(number, 1);
		}
		System.out.print(hasFound ? 1 : 0);
		reader.close();
	}

	private static boolean getRelation(int number, int depth) {
		visited[number] = true;
		if (depth == 5) return true;
		for (Person relationPerson=relations[number]; 
				relationPerson!=null; 
				relationPerson=relationPerson.next) {
			if (visited[relationPerson.number]) continue;
			if (getRelation(relationPerson.number, depth+1))
				return true;
			visited[relationPerson.number] = false;
		}
		return false;
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		peopleCount = Integer.parseInt(tokenizer.nextToken());
		relations = new Person[peopleCount];
		int relationCount = Integer.parseInt(tokenizer.nextToken());
		for (int index=0; index<relationCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			relations[from] = new Person(to, relations[from]);
			relations[to] = new Person(from, relations[to]);
		}
	}

}
