package homeworks.m9.d11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - Person: 각 위치에 있는 사람 표현
 *   ㄴ 속성: 사람 위치(x, y), 계단까지 걸리는 시간(toStairTime), 
 *   		현재 상태(status), 경과 시간(time), 계단 내려가는 시간(stairTime)
 *   ㄴ 생성자는 위치만 받음
 *   ㄴ initialize(): toStairTime을 파라미터로 받음, 
 *     ㄴ toStairTime, status, time, stairTime 초기화
 *     
 * 1. 사람마다 어느 계단으로 내려갈지 선택(부분 집합)
 * 2. 집합 생성 후, 각 사람을 stairGroup에 입력
 * 	2.1. 입력 시 각 계단 위치와 각 사람 본인의 위치간 거리 계산 -> 각 사람 초기화
 * 	2.2. 두 그룹 정렬 (계단까지 걸리는 시간 오름차순)
 * 	2.3. movingSimulation() 메소드 호출
 * 3. movingSimulation(): 각 사람의 이동 과정 시뮬레이션
 * 4. 이동중인 사람 수(count) 초기화
 * 5. 이동중인 사람 수가 0이 아닌 동안 반복
 * 6. 두 그룹과 그 안의 사람에 대해 반복(stairIndex, person)
 * 	6.1. 각 사람의 경과 시간 증가
 * 	6.2. 상태가 종료(END)라면 continue
 * 	6.3. 상태가 이동 중(MOVING)이고, 경과 시간이 toStairTime과 동일하다면?
 * 		6.3.1. 상태는 WAIT으로 변경
 * 	6.4. 상태가 WAIT이고, 경과 시간이 toStairTime+1 이상이며, 
 * 		  계단 이용 사람 수(stairPersonCount)가 3보다 작다면?
 * 		6.4.1. 상태는 DOWN으로 변경, stairTime 1 증가, 계단 이용 사람 수 1 증가
 * 	6.5. 상태가 DOWN이라면?
 * 		6.5.1. stairTime이 계단의 높이보다 작다면? stairTime 1증가
 * 		6.5.2. 계단 높이와 동일하다면? 
 * 			6.5.2.1. 계단 이용 사람 수 1 감소, 이동중인 사람 수 1 감소, status는 END로 변경
 * 	6.6. 매 반복 시 소요 시간(spendingTime) 1씩 증가
 * 7. spendingTime과 최소 소요 시간 비교 -> 더 작은 값 입력
 */
public class LunchTime {
	static class Person implements Comparable<Person>{
		int x, y;
		int toStairTime, status, time, stairTime;
		public Person(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		private void initialize(int toStairTime) {
			this.toStairTime = toStairTime;
			status = MOVING;
			time = 0;
			stairTime = 0;
		}
		@Override
		public int compareTo(Person other) {
			return Integer.compare(toStairTime, other.toStairTime);
		}
		@Override
		public String toString() {
			return "Person [x=" + x + ", y=" + y + ", toStairTime=" + toStairTime + ", status=" + status + ", time="
					+ time + ", stairTime=" + stairTime + "]";
		}
	}
	
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static final int MOVING = 1, WAIT = 2, DOWN = 3, END = 4;
	private static int bound;
	private static List<Person> persons;
	private static int personSize;
	private static int[][] stairPosition;
	private static int[] stairGroup;
	@SuppressWarnings("rawtypes")
	private static ArrayList[] stairPersons;
	private static int minSpendingTime;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			selectStair(0);
			
			builder.append("#").append(testCase).append(" ").
					append(minSpendingTime).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}
	
	@SuppressWarnings("unchecked")
	private static void selectStair(int personIndex) {
		if (personIndex == personSize) {
			// simulation 전 전처리
			stairPersons = new ArrayList[] {
				new ArrayList<Person>(),
				new ArrayList<Person>()
			};
			for (int index=0; index<personSize; index++) {
				int stairIndex = stairGroup[index];
				Person person = persons.get(index);
				int toStairTime = getDistance(person.y, 
						person.x, stairPosition[stairIndex]);
				person.initialize(toStairTime);
				stairPersons[stairIndex].add(person);
			}
			Collections.sort(stairPersons[0]);
			Collections.sort(stairPersons[1]);
			movingSimulation();
			return;
		}
		stairGroup[personIndex] = 0;
		selectStair(personIndex+1);
		stairGroup[personIndex] = 1;
		selectStair(personIndex+1);
	}

	private static void movingSimulation() {
		int spendingTime = 0;
		int[] stairPersonCount = new int[2]; // 계단 이용 사람 수
		for (int count=personSize; count>0; spendingTime++) { // 시간 마다 반복
			for (int stairIndex=0; stairIndex<2; stairIndex++) // 계단
				for (int personIndex=0; personIndex<stairPersons[stairIndex].size(); personIndex++) { // 계단에 있는 사람
					Person person = (Person)stairPersons[stairIndex].get(personIndex);
					person.time++;
					switch (person.status) {
					case END:
						break;
					case MOVING:
						if (person.time == person.toStairTime)
							person.status = WAIT;
						break;
					case WAIT:
						if (person.time >= person.toStairTime+1 && 
							stairPersonCount[stairIndex] < 3) {
							person.status = DOWN;
							person.stairTime++;
							stairPersonCount[stairIndex]++;
						}
						break;
					case DOWN:
						if (person.stairTime < stairPosition[stairIndex][2]) // 더 내려가야 됨
							person.stairTime++;
						else {
							person.status = END;
							stairPersonCount[stairIndex]--;
							count--;
						}
						break;
					}
				}
		}
		minSpendingTime = Math.min(minSpendingTime, spendingTime);
	}

	private static int getDistance(int personY, int personX, int[] stair) {
		return Math.abs(personY-stair[0])+Math.abs(personX-stair[1]);
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		bound = Integer.parseInt(reader.readLine().trim());
		persons = new ArrayList<>();
		stairPosition = new int[2][];
		int stairIndex = 0;
		for (int row=0; row<bound; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<bound; column++) {
				int flag = Integer.parseInt(tokenizer.nextToken());
				if (flag == 1) // 사람
					persons.add(new Person(column, row));
				else if (flag > 1)  // 계단
					stairPosition[stairIndex++] = new int[] {row, column, flag};
			}
		}
		personSize = persons.size();
		stairGroup = new int[personSize];
		minSpendingTime = Integer.MAX_VALUE;
	}
}
