package homeworks.m9.d2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 입력 받을 때, 각 나무 간 거리는 기존 값의 2배로 입력
 * 	1.1. 늑대의 움직임 고려, 2배로 움직일 때는 2로 나눠주기 위함
 * 2. 나무 그루터기별 여우의 최단 경로 입력
 * 	2.1. 여우의 초기 위치 우선순위 큐에 입력 (Fox)
 * 		2.1.1. 여우의 나무 위치 및 현재까지 이동 비용을 파라미터로 Fox 객체 생성 및 입력
 * 	2.2. 큐에 요소가 있는 동안 반복
 * 	2.3. 큐에서 요소 poll(currentPath) -> 현재까지 이동 비용이 가장 작은 요소가 나옴
 * 	2.4. currentPath의 나무 위치에서 인접한 나무들에 대해 반복(nextPath)
 * 		2.4.1. nextPath 위치 나무의 이동 비용이 현재 위치 이동 비용+
 * 			   (현재-다음)위치의 이동 경로 길이보다  크다면?
 * 		2.4.2. nextPath 위치 나무의 이동 비용은 현재 위치 이동 비용+
 * 			   (현재-다음)위치의 이동 경로 길이로 입력
 * 		2.4.3. nextPath 나무 위치 및 nextPath 위치 나무 이동 비용을 
 * 			      파라미터로 Fox 객체 생성, 큐에 입력
 * 3. 나무 그루터기별 늑대의 최단 경로 입력
 * 	3.1. 늑대는 각 위치에 도착할 때 2배 속도로 들어왔는지, 절반 속도로 들어왔는지를 나눠서 최단 경로를 구함
 * 		3.1.1. 여우와 다르게, 각 위치의 최단 경로를 받는 배열은 2차원임 (minimumPathWolf)
 * 		3.1.2. 0위치: 절반 속도로 들어왔을 때, 1위치: 2배 속도로 들어왔을 때
 * 	3.2. minimumPathWolf의 절반 속도로 들어왔을 때(0위치) 처음 위치는 0으로 지정
 * 	3.3. 늑대의 초기 위치 우선순위 큐에 입력(Wolf)
 * 		3.3.1. 여우의 나무 위치는 1, 현재까지 이동 비용은 0, 
 * 			   2배 속도(doubleSpeed)는 0으로 지정하여 Wolf 객체 생성 및 입력
 * 	3.4. 큐에 요소가 있는 동안 반복
 * 	3.5. 큐에서 요소 poll(currentPath)
 * 	3.6. 현재 지점까지의 최소 경로(minimumPathWolf)가 현재까지의 이동 경로보다 더 작다면? continue
 * 		3.6.1. 이미 지나온 경로라서, 아래에서 반복을 볼 필요가 없음
 * 	3.7. currentPath의 나무 위치에서 인접한 나무들에 대해 반복(nextPath)
 * 		3.7.1. 다음 이동 속도(nextDoubleSpeed)는 currentPath의 2배 속도가 0이면 1을, currentPath의 2배 속도가 1이면 0으로 지정
 * 		3.7.2. 다음 이동 경로 길이(nextDistance)는 nextDoubleSpeed가 1이면 currentPath-nextPath 이동 길이의 절반,
 * 				nextDoubleSpeed가 0이면 currentPath-nextPath 이동 길이의 두배로 입력
 * 		3.7.3. nextPath 위치 나무의 이동 비용이 현재 위치 이동 비용+nextDistance보다 크다면?
 * 			3.7.3.1. 이동 비용에서 2배 지정 여부는 nextDoubleSpeed로 입력
 * 		3.7.4. nextPath 위치 나무의 이동 비용(nextDoubleSpeed)은 
 * 			     현재 위치 이동 비용(currentPath의 doubleSpeed)+nextDistance로 입력
 * 		3.7.5. nextPath 나무 위치 및 next 위치 나무 이동 비용, nextDoubleSpeed을 파라미터로 Wolf 객체 생성 및 큐에 입력
 * 4. 각 나무 그루터기들의 최단 경로 값 비교
 * 	4.1. 여우가 먼저 도착하는 그루터기의 수 확인
 * 	4.2. 늑대의 경우, 각 그루터기를 2배 속도로 들어오는 경우와 절반 속도로 들어오는 경우를 비교하여 더 작은 값을 도출
 * 
 * etc) 늑대가 2배 속도 이동할 때랑, 절반 속도로 이동할 때랑 나눠서 최단 경로를 구한다는 아이디어가
 * 		생각하기 어려웠던 문제임 -> 나중에 시간나면 꼭 한번 다시 풀어보세요	
 *      ㄴ 근데...?? 이거만 생각하면 시간이 터집니다... 다익스트라 안에 continue를 하나 넣어줘야되는데... 그건 아직도 이해가 잘 안가네유
 */
public class MoonFoxPQ {
	static class Fox implements Comparable<Fox>{
		int number;
		int pathCost;
		public Fox(int number, int pathCost) {
			super();
			this.number = number;
			this.pathCost = pathCost;
		}
		@Override
		public int compareTo(Fox other) {
			return Integer.compare(pathCost, other.pathCost);
		}
		@Override
		public String toString() {
			return "Fox [number=" + number + ", pathCost=" + pathCost + "]";
		}
	}
	static class Wolf implements Comparable<Wolf> {
		int number;
		int pathCost;
		int doubleSpeed;
		public Wolf(int number, int pathCost, int doubleSpeed) {
			super();
			this.number = number;
			this.pathCost = pathCost;
			this.doubleSpeed = doubleSpeed;
		}
		@Override
		public int compareTo(Wolf other) {
			return Integer.compare(pathCost, other.pathCost);
		}
	}
	static class TreePath {
		int number;
		int distance;
		TreePath next;
		public TreePath(int number, int distance, TreePath next) {
			super();
			this.number = number;
			this.distance = distance;
			this.next = next;
		}
	}
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int treeCount;
	private static TreePath[] adjacentPaths;
	private static int[] minimumPathFox;
	private static int[][] minimumPathWolf;

	public static void main(String[] args) throws IOException {
		initialize();
		int start = 1;
		getMinimumPathFox(start);
		getMinimumPathWolf(start);
		int count = 0;
		for (int tree=1; tree<=treeCount; tree++)
			count += minimumPathFox[tree] < Math.min(minimumPathWolf[0][tree], 
					minimumPathWolf[1][tree]) ? 1 : 0;
		System.out.println(count);
		reader.close();
	}

	private static void getMinimumPathFox(int start) {
		Queue<Fox> queue = new PriorityQueue<Fox>();
		minimumPathFox[start] = 0;
		queue.offer(new Fox(start, minimumPathFox[start]));
		while (!queue.isEmpty()) {
			Fox currentPath = queue.poll();
			if (minimumPathFox[currentPath.number] < currentPath.pathCost) continue;
			for (TreePath nextPath=adjacentPaths[currentPath.number]; 
					nextPath!=null; nextPath=nextPath.next) {
				if (minimumPathFox[nextPath.number] > 
					minimumPathFox[currentPath.number]+nextPath.distance)  {
					minimumPathFox[nextPath.number] = minimumPathFox[currentPath.number]+nextPath.distance;
					queue.offer(new Fox(nextPath.number, minimumPathFox[nextPath.number]));
				}
			}
		}
	}
	private static void getMinimumPathWolf(int start) {
		Queue<Wolf> queue = new PriorityQueue<Wolf>();
		minimumPathWolf[0][start] = 0;
		queue.offer(new Wolf(start, minimumPathWolf[0][start], 0));
		while (!queue.isEmpty()) {
			Wolf currentPath = queue.poll();
			if (minimumPathWolf[currentPath.doubleSpeed][currentPath.number] < currentPath.pathCost)
				continue;
			for (TreePath nextPath=adjacentPaths[currentPath.number]; 
					nextPath!=null; nextPath=nextPath.next) {
				int nextDoubleSpeed = Math.abs(currentPath.doubleSpeed-1);
				int nextDistance = nextDoubleSpeed == 1 ? 
						nextPath.distance/2 : nextPath.distance*2;
				if (minimumPathWolf[nextDoubleSpeed][nextPath.number] > 
					minimumPathWolf[currentPath.doubleSpeed][currentPath.number]+
					nextDistance) {
					minimumPathWolf[nextDoubleSpeed][nextPath.number] = 
							minimumPathWolf[currentPath.doubleSpeed][currentPath.number]+
							nextDistance;
					queue.offer(new Wolf(nextPath.number, 
							minimumPathWolf[nextDoubleSpeed][nextPath.number], nextDoubleSpeed));
				}
			}
		}
	}
	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		treeCount = Integer.parseInt(tokenizer.nextToken());
		int pathCount = Integer.parseInt(tokenizer.nextToken());
		adjacentPaths = new TreePath[treeCount+1];
		minimumPathFox = new int[treeCount+1];
		minimumPathWolf = new int[2][treeCount+1];
		for (int index=0; index<pathCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			int distance = Integer.parseInt(tokenizer.nextToken())*2;
			adjacentPaths[from] = new TreePath(to, distance, adjacentPaths[from]);
			adjacentPaths[to] = new TreePath(from, distance, adjacentPaths[to]);
		}
		Arrays.fill(minimumPathFox, Integer.MAX_VALUE);
		Arrays.fill(minimumPathWolf[0], Integer.MAX_VALUE);
		Arrays.fill(minimumPathWolf[1], Integer.MAX_VALUE);
	}

}
