package homeworks.m9.d10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - lower: 한 학생 보다 작은 학생 번호 입력 (Set[])
 * 	  ㄴ 4가 1,3,5보다 크다면 heigher[4]엔 1,3,5가 들어감	
 * - heigher: 한 학생보다 큰 학생 번호 입력 (Set[])
 * 	  ㄴ 1이 5,4,2보다 작다면 heigher[1]엔 5,4,2가 들어감
 * 1. 각 학생 번호 마다 반복
 * 	1.1. 확인 학생 번호, 시작 학생 번호를 파라미터로 compareHeight() 호출
 * 2. compareHeight(): 각 학생보다 큰 학생과 작은 학생을 
 * 					   heigherThan, lowerThan에 입력
 * 	2.1. 파라미터는 확인 학생(target), 시작 학생(start)
 * 3. target과 start가 다르다면?
 * 	3.1. lower의 target 위치에 start 추가
 * 	3.2. heigher의 start 위치에 target 추가
 * 4. target보다 크다고 확인된 학생(인접 학생)에 대해 반복 (nextStudent)
 * 	4.1. next 학생을 이미 방문했다면 continue
 * 	4.2. next 학생 방문 여부 true
 * 	4.3. next 학생, target학생을 파라미터로 compareHeight() 재귀 호출
 *	4.4. next 학생 방문 여부 false
 * 5. 메소드 호출 이후, 각 학생 번호 마다 반복
 * 	5.1. 한 학생의 lower, heigher 크기 합산+1이 전체 학생 수라면 
 * 		  키 순서를 알 수 있는 학생 수 1증가
 * 
 * 메모리 터저부림...
 */
public class HeightOrderDFS {
	static class Student {
		int number;
		Student next;
		public Student(int number, Student next) {
			super();
			this.number = number;
			this.next = next;
		}
	}
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int studentCount;
	private static int compareCount;
	private static Student[] heigherStudents;
	@SuppressWarnings("rawtypes")
	private static Set[] lower;
	@SuppressWarnings("rawtypes")
	private static Set[] heigher;
	private static int knowableCount;
	private static boolean[] visited;
	
	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		for (int student=1; student<=studentCount; student++)
			compareHeight(student, student);
		for (int student=1; student<=studentCount; student++)
			knowableCount += heigher[student].size()+
							lower[student].size()+1 == 
							studentCount ? 1 : 0;
		System.out.println(knowableCount);
		reader.close();
	}

	@SuppressWarnings("unchecked")
	private static void compareHeight(int target, int start) {
		if (target != start) {
			lower[target].add(start);
			heigher[start].add(target);
		}
		for (Student nextStudent=heigherStudents[target]; 
				nextStudent!=null; nextStudent=nextStudent.next) {
			if (visited[nextStudent.number]) continue;
			visited[nextStudent.number] = true;
			compareHeight(nextStudent.number, start);
			visited[nextStudent.number] = false;
		}
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		studentCount = Integer.parseInt(tokenizer.nextToken());
		compareCount = Integer.parseInt(tokenizer.nextToken());
		heigherStudents = new Student[studentCount+1];
		lower = new Set[studentCount+1];
		heigher = new Set[studentCount+1];
		for (int student=1; student<=studentCount; student++) {
			lower[student] = new HashSet<Integer>();
			heigher[student] = new HashSet<Integer>();
		}
		for (int count=0; count<compareCount; count++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			heigherStudents[from] = new Student(to, heigherStudents[from]);
		}
		knowableCount = 0;
		visited = new boolean[studentCount+1];
	}

}
