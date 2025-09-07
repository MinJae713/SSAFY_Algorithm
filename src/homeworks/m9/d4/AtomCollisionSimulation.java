package homeworks.m9.d4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 모든 원자들에 대해 두개씩 조합 생성
 * 2. 생성 후, 만들어진 두 원자가 충돌할 수 있는지 확인
 * 	2.1. 충돌할 수 있는 원자는 충돌 큐(PriorityQueue)에 입력(Collision)
 * 		2.1.1. Collision: 두 원자의 번호(atom1, atom2), 충돌 시간(time)
 * 	2.2. x가 동일하고, y가 더 큰 원자의 방향이 1이며, y가 더 작은 원자 방향이 0이면?
 * 		2.2.1. 두 원자 번호와 y의 차이/2를 파라미터로 Collision 생성, 큐에 offer
 * 	2.3. y가 동일하고, x가 더 큰 원자의 방향이 2이며, x가 더 작은 원자 방향이 3이면?
 * 		2.3.1. 두 원자 번호와 x의 차이/2를 파라미터로 Collision 생성, 큐에 offer
 * 	2.4. 그 이외의 경우라면?
 * 		2.4.1. 두 원자의 x차이, y차이 계산 (두번째 원자-첫번째 원자)
 * 		2.4.2. 위 두 값의 절대값이 같지 않다면 continue
 * 		2.4.3. x차이가 양수이고, y차이가 양수라면?
 * 			2.4.3.1. 두번째, 첫번째 원자 이동 방향이 각각 1,3이거나, 2,0이면?
 * 			2.4.3.2. 두 원자 번호와 x차이 절대값을 파라미터로 Collision 생성, 큐에 offer
 * 		2.4.4. x차이가 음수이고, y차이가 음수라면?
 * 			2.4.4.1. 두번째, 첫번째 원자 이동 방향이 각각 3,1이거나, 0,2이면?
 * 			2.4.4.2. 두 원자 번호와 x차이 절대값을 파라미터로 Collision 생성, 큐에 offer
 * 		2.4.5. x차이가 양수이고, y차이가 음수라면?
 * 			2.4.5.1. 두번째 첫번째 원자 이동 방향이 각각 2,1이거나, 0,3이면?
 * 			2.4.5.2. 두 원자 번호와 x차이 절대값을 파라미터로 Collision 생성, 큐에 offer
 * 		2.4.6. x차이가 음수이고, y차이가 양수라면?
 * 			2.4.6.1. 두번째 첫번째 원자 이동 방향이 각각 1,2이거나, 3,0이면?
 * 			2.4.6.2. 두 원자 번호와 x차이 절대값을 파라미터로 Collision 생성, 큐에 offer
 * 3. 큐에 요소가 있는 동안 반복
 * 	3.1. 큐에서 요소 poll (atom1, atom2)
 * 	3.2. 큐에서 뺀 두 원자가 둘 다 터지지 않은 원자라면?
 * 		3.2.1. 두 원자의 터진 시간 입력, 각 원자의 에너지 값 합산
 * 	3.3. 둘 중 하나만 터졌다면?
 * 		3.3.1. 이미 터진쪽의 터진 시간 확인
 * 		3.3.2. 현재 Collision의 터진 시간과 같다면?
 * 			3.3.2.1. 안 터진 쪽 터진 시간 입력, 해당 원자의 에너지 값 합산
 * 			3.3.2.2. 앞 전의 큐를 통해 터진 원자와 동시에 만난 원자임
 * 		3.3.3. 터진 시간과 다르다면? - 터지지 않음
 * 			3.3.3.1. 같이 만나서 터질 위치에는 있으나, 반대가 이미 터져서 그냥 지나감
 */
public class AtomCollisionSimulation {
	static class Atom {
		float x;
		float y;
		int direction;
		int energy;
		public Atom(float x, float y, int direction, int energy) {
			super();
			this.x = x;
			this.y = y;
			this.direction = direction;
			this.energy = energy;
		}
	}
	
	static class Collision implements Comparable<Collision>{
		int atom1;
		int atom2;
		float time;
		public Collision(int atom1, int atom2, float time) {
			super();
			this.atom1 = atom1;
			this.atom2 = atom2;
			this.time = time;
		}
		@Override
		public int compareTo(Collision other) {
			return Float.compare(time, other.time);
		}
	}

	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int atomCount;
	private static Atom[] atoms;
	private static Queue<Collision> queue;
	private static int totalEnergy;
	private static float[] boomed;
	
	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			for (int atom1Index=0; atom1Index<atomCount-1; atom1Index++)
				for (int atom2Index=atom1Index+1; atom2Index<atomCount; atom2Index++)
					setAbleCollision(atom1Index, atom2Index);
			while (!queue.isEmpty()) {
				Collision collision = queue.poll();
				int atom1 = collision.atom1;
				int atom2 = collision.atom2;
				if (boomed[atom1]==0 && boomed[atom2]==0) {
					// 둘 다 터지지 않음
					totalEnergy += atoms[atom1].energy+atoms[atom2].energy;
					boomed[atom1] = boomed[atom2] = collision.time;
				} else if (boomed[atom1]==0) {
					// 두 번째는 터짐
					float time = collision.time;
					if (boomed[atom2] == time) {
						totalEnergy += atoms[atom1].energy;
						boomed[atom1] = time;
					}
				} else if (boomed[atom2]==0) {
					// 첫 번째는 터짐
					float time = collision.time;
					if (boomed[atom1] == time) {
						totalEnergy += atoms[atom2].energy;
						boomed[atom2] = time;
					}
				}
			}
			builder.append("#").append(testCase).append(" ").
						append(totalEnergy).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void setAbleCollision(int atom1Index, int atom2Index) {
		Atom atom1 = atoms[atom1Index];
		Atom atom2 = atoms[atom2Index];
		if (atom1.x == atom2.x) {
			if (atom1.y > atom2.y && 
					atom1.direction == 1 && 
					atom2.direction == 0) 
				queue.offer(new Collision(atom1Index, 
						atom2Index, (atom1.y-atom2.y)/2));
			else if (atom1.y < atom2.y && 
					atom2.direction == 1 && 
					atom1.direction == 0)
				queue.offer(new Collision(atom2Index, 
						atom1Index, (atom2.y-atom1.y)/2));
		} else if (atom1.y == atom2.y) {
			if (atom1.x > atom2.x && 
					atom1.direction == 2 &&
					atom2.direction == 3)
				queue.offer(new Collision(atom1Index, 
						atom2Index, (atom1.x-atom2.x)/2));
			else if (atom1.x < atom2.x && 
					atom2.direction == 2 &&
					atom1.direction == 3)
				queue.offer(new Collision(atom2Index, 
						atom1Index, (atom2.x-atom1.x)/2));
		} else {
			float differentY = atom2.y-atom1.y;
			float differentX = atom2.x-atom1.x;
			if (Math.abs(differentX) != Math.abs(differentY)) return;
			else if (differentX>0 && differentY>0 &&
					((atom2.direction == 1 && atom1.direction == 3) ||
					(atom2.direction == 2 && atom1.direction == 0)))
				queue.offer(new Collision(atom1Index, atom2Index, Math.abs(differentX)));
			else if (differentX<0 && differentY<0 &&
					((atom2.direction == 3 && atom1.direction == 1) ||
					(atom2.direction == 0 && atom1.direction == 2)))
				queue.offer(new Collision(atom1Index, atom2Index, Math.abs(differentX)));
			else if (differentX>0 && differentY<0 &&
					((atom2.direction == 2 && atom1.direction == 1) ||
					(atom2.direction == 0 && atom1.direction == 3)))
				queue.offer(new Collision(atom1Index, atom2Index, Math.abs(differentX)));
			else if (differentX<0 && differentY>0 &&
					((atom2.direction == 1 && atom1.direction == 2) ||
					(atom2.direction == 3 && atom1.direction == 0)))
				queue.offer(new Collision(atom1Index, atom2Index, Math.abs(differentX)));
		}
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		atomCount = Integer.parseInt(reader.readLine().trim());
		atoms = new Atom[atomCount];
		boomed = new float[atomCount];
		for (int index=0; index<atomCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			float x = Float.parseFloat(tokenizer.nextToken());
			float y = Float.parseFloat(tokenizer.nextToken());
			int direction = Integer.parseInt(tokenizer.nextToken());
			int energy = Integer.parseInt(tokenizer.nextToken());
			atoms[index] = new Atom(x, y, direction, energy);
		}
		queue = new PriorityQueue<Collision>();
		totalEnergy = 0;
	}
}
