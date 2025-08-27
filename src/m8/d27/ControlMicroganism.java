package m8.d27;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 1시간~격리 시간(checkTime) 동안 반복
 * 2. 미생물 그룹들(groups) 이동
 * 	2.1. 이동 후 이동 위치에 그룹 번호 추가
 * 3. 각 미생물 그룹의 이동 결과 처리
 * 	3.1. 미생물 그룹이 영역 마지막 위치에 있다면?
 * 		3.1.1. 미생물 수 감소 및 방향 변경
 * 	3.2. 영역 내에 있다면? -> 겹치는 애들 있는지 봐야하는디? (아직 미정)
 * 		3.2.1. 겹치는 미생물 그룹 위치 확인
 * 		3.2.2. 수가 가장 많은 미생물 그룹에 나머지 미생물 그룹의 미생물 수 합산
 * 		3.2.3. 나머지 미생물은 groups에서 제거
 * 4. 남아있는 미생물 수 합산
 */
public class ControlMicroganism {
	static class MicroganismGroup {
		int x;
		int y;
		int count;
		int direction;
		public MicroganismGroup(int x, int y, int count, int direction) {
			super();
			this.x = x;
			this.y = y;
			this.count = count;
			this.direction = direction;
		}
		void move() {
			x += delta[direction][0];
			y += delta[direction][1];
		}
		void die() {
			count /= 2;
			if (direction <= 2) // 1 2
				direction = 3-direction;
			else direction = 7-direction; // 3 4
		}
		void summision(int otherCount) {
			count += otherCount;
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int bound;
	private static int checkTime;
	private static int groupCount;
	private static List<MicroganismGroup> groups;
	private static int totalMicroganism;
	
	public static void main(String[] args) throws IOException {
		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int time=1; time<=checkTime; time++) {
				for (int number=0; number<groups.size(); number++) {
					MicroganismGroup group = groups.get(number);
					group.move();
				}
				for (int number=0; number<groups.size(); number++) {
					MicroganismGroup group = groups.get(number);
					if (inArea(group.x, group.y)) {
						// 테두리 영역 안에 있는 경우 -> 동일 위치에 있는 그룹을 확인해야 함
					} else group.die();
				}
			}
			for (MicroganismGroup group : groups)
				totalMicroganism += group.count;
			builder.append("#").append(testCase).append(" ").
					append(totalMicroganism).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static boolean inArea(int x, int y) {
		return 1<=x && x<bound-1 &&
				1<=y && y<bound-1;
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
		groups = new ArrayList<>();
		for (int index=0; index<groupCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int y = Integer.parseInt(tokenizer.nextToken());
			int x = Integer.parseInt(tokenizer.nextToken());
			int count = Integer.parseInt(tokenizer.nextToken());
			int direction = Integer.parseInt(tokenizer.nextToken());
			groups.add(new MicroganismGroup(x, y, count, direction));
		}
		totalMicroganism = 0;
	}
}
