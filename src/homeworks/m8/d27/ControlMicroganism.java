package homeworks.m8.d27;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 1시간~격리 시간(checkTime) 동안 반복
 * 2. 미생물 그룹들(groups) 이동
 * 3. 이동 후 위치가 테두리 안쪽에 있는 경우
 * 	3.1. 이동 후 위치에 그룹 번호가 없다면?
 * 		3.1.1. 이동 위치에 그룹 번호 지정
 * 	3.2. 그룹 번호가 있다면?
 * 		3.2.1. 기존에 그 위치에 있던 그룹의 미생물 수, 이동하는 그룹의 미생물 수 비교
 * 		3.2.2. 수가 많은 미생물 그룹에 적은 미생물 그룹의 미생물 수 합산
 * 			3.2.2.1. 초기 수량에 추가하는 것이 아닌 합산 수량(total)에 합산
 * 			3.2.2.2. ex) 수량이 5와 3인 그룹을 비교한 이후, 기존 수량이 5와 6인 그룹을 비교할 때
 * 						 기존 수량이 6인 그룹이 남아있도록 해야 함 -> 초기 수량에 합산하면 이 경우, 5가 남음
 * 		3.2.3. 적은 수의 미생물 그룹은 삭제
 * 		3.2.4. 이동하는 그룹이 기존 그룹보다 큰 경우, 해당 이동 위치에 이동 그룹 번호 지정
 * 4. 위치가 테두리에 있는 경우
 * 	4.1. 미생물이 한개라면 삭제
 * 	4.2. 그 이상이라면 미생물 수 감소 및 방향 변경
 * 5. 한 시간이 지난 이후 -> 각 그룹의 미생물 합산 수량을 초기 수량으로 입력
 * 6. 남아있는 미생물 수 각각의 초기 수량 합산
 */
public class ControlMicroganism {
	static class MicroGroup {
		int x;
		int y;
		int count;
		int total;
		int direction;
		public MicroGroup(int x, int y, int count, int direction) {
			super();
			this.x = x;
			this.y = y;
			total = this.count = count;
			this.direction = direction;
		}
		void move() {
			x += delta[direction][0];
			y += delta[direction][1];
		}
		void die() {
			total = count /= 2;
			if (direction <= 2) 
				direction = 3-direction;
			else direction = 7-direction; 
		}
		public void initializeCount() {
			count = total;
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int bound;
	private static int checkTime;
	private static int groupCount;
	private static MicroGroup[] groups;
	private static int[][] groupPosition;
	private static int totalMicroganism;
	
	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int time=1; time<=checkTime; time++) moveInTime(time);
			for (MicroGroup group : groups)
				if (group != null) totalMicroganism += group.count;
			builder.append("#").append(testCase).append(" ").
					append(totalMicroganism).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void moveInTime(int time) {
		// 그룹들 이동
		for (int number=0; number<groupCount; number++) {
			MicroGroup current = groups[number];
			if (current == null) continue;
			current.move();
			if (inArea(current)) {
				// 테두리 안쪽 배치
				// 차지하는 그룹이 없는 경우
				if (groupPosition[current.y][current.x] == -1)
					groupPosition[current.y][current.x] = number;
				else { // 차지하는 그룹이 있어서 그룹간 비교가 필요한 경우
					int existingNumber = groupPosition[current.y][current.x];
					MicroGroup exist = groups[existingNumber];
					if (exist.count > current.count) {
						exist.total += current.count;
						groups[number] = null;
					} else {
						current.total += exist.total;
						groups[existingNumber] = null;
						groupPosition[current.y][current.x] = number;
					}
				}
			} else {
				groupPosition[current.y][current.x] = number;
				// 테두리 밖쪽 배치 -> 미생물 수 조정 
				current.die();
				if (current.count == 0) 
					groups[number] = null;
			}
		}
		for (int number=0; number<groupCount; number++)  {
			MicroGroup group = groups[number];
			if (group == null) continue;
			group.initializeCount();
			groupPosition[group.y][group.x] = -1;
		}
	}

	private static boolean inArea(MicroGroup current) {
		return 1<=current.x && current.x<bound-1 &&
				1<=current.y && current.y<bound-1;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{0, 0}, {0, -1}, {0, 1}, {-1, 0}, {1, 0}};
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		bound = Integer.parseInt(tokenizer.nextToken());
		checkTime = Integer.parseInt(tokenizer.nextToken());
		groupCount = Integer.parseInt(tokenizer.nextToken());
		groups = new MicroGroup[groupCount];
		groupPosition = new int[bound][bound];
		for (int index=0; index<bound; index++)
			Arrays.fill(groupPosition[index], -1);
		for (int index=0; index<groupCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int y = Integer.parseInt(tokenizer.nextToken());
			int x = Integer.parseInt(tokenizer.nextToken());
			int count = Integer.parseInt(tokenizer.nextToken());
			int direction = Integer.parseInt(tokenizer.nextToken());
			groups[index] = new MicroGroup(x, y, count, direction);
		}
		totalMicroganism = 0;
	}
}
