package homeworks.m9.d2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 입력 받을 때, 각 나무 간 거리는 기존 값의 2배로 입력
 * 	1.1. 늑대의 움직임 고려, 2배로 움직일 때는 2로 나눠주기 위함
 * 2. 나무 그루터기별 여우의 최단 경로 입력
 * 	2.1. 1번 지점의 최소 경로 비용(minimumPathFox) 값은 0으로 지정
 * 	2.2. 나무의 개수 만큼 반복
 * 	2.3. minimumPathFox 값이 가장 작은 나무 번호 반환(currentTree)
 * 		2.3.1. 이미 방문한 나무는 건너뜀
 * 	2.4. currentTree 방문 true 지정(visited)
 * 	2.5. currentTree와 인접한 나무들에 대해 반복(nextTree)
 * 		2.5.1. 이미 방문한 나무라면 continue
 * 		2.5.2. nextTree의 minimumPathFox가 currentTree의 
 * 				 minimumPathFox+currentTree에서 nextTree의 길이보다 크다면?
 * 		2.5.3. nextTree의 minimumPathFox는 currentTree의 
 * 				 minimumPathFox+currentTree에서 nextTree의 길이로 지정
 * 3. visited 배열 초기화
 * 4. 나무 그루터기별 늑대의 최단 경로 입력
 * 	4.1. 
 * 5. 각 나무 그루터기들의 최단 경로 값 비교
 * 	5.1. 여우가 먼저 도착하는 그루터기의 수 확인
 */
public class MoonFox {
	static class WolfPath {
		int distance;
		boolean isDoubleMoved;
		public WolfPath(int distance) {
			super();
			this.distance = distance;
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
	private static TreePath[] treePaths;
	private static boolean[] visited;
	private static int[] minimumPathFox;
	private static WolfPath[] minimumPathWolf;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		getFoxMinimumPath();
		visited = new boolean[treeCount+1];
		getWolfMinimumPath();
		System.out.println(getResult());
		reader.close();
	}

	private static int getResult() {
		int count = 0;
		for (int tree=1; tree<=treeCount; tree++) {
			System.out.println(minimumPathFox[tree]+", "+minimumPathWolf[tree].distance);
			count += minimumPathFox[tree] < minimumPathWolf[tree].distance ? 1 : 0;
		}
		return count;
	}

	private static void getFoxMinimumPath() {
		// 여우 최단 경로
		minimumPathFox[1] = 0;
		for (int count=0; count<treeCount; count++) {
			int currentTree = -1;
			int minimumDistance = Integer.MAX_VALUE;
			for (int tree=1; tree<=treeCount; tree++) {
				if (visited[tree]) continue;
				else if (minimumDistance > minimumPathFox[tree]) {
					minimumDistance = minimumPathFox[tree];
					currentTree = tree;
				}
			}
			visited[currentTree] = true;
			for (TreePath nextTree=treePaths[currentTree]; 
					nextTree!=null; nextTree=nextTree.next) {
				if (visited[nextTree.number]) continue;
				else if (minimumPathFox[nextTree.number] > 
					minimumPathFox[currentTree]+nextTree.distance)
					minimumPathFox[nextTree.number] = 
							minimumPathFox[currentTree]+nextTree.distance;
			}
		}
	}
	
	private static void getWolfMinimumPath() {
		//늑대 최단 경로
		minimumPathWolf[1].distance = 0;
		minimumPathWolf[1].isDoubleMoved = false;
		for (int count=0; count<treeCount; count++) {
			int currentTree = -1;
			int minimumDistance = Integer.MAX_VALUE;
			for (int tree=1; tree<=treeCount; tree++) {
				if (visited[tree]) continue;
				else if (minimumDistance > minimumPathWolf[tree].distance) {
					minimumDistance = minimumPathWolf[tree].distance;
					currentTree = tree;
				}
			}
			visited[currentTree] = true;
			for (TreePath nextTree=treePaths[currentTree]; 
					nextTree!=null; nextTree=nextTree.next) {
				if (visited[nextTree.number]) continue;
				int nextDistance = minimumPathWolf[currentTree].isDoubleMoved ? 
						nextTree.distance*2 : nextTree.distance/2;
				System.out.println(currentTree+"->"+nextTree.number+": "+
						nextDistance+" 원래는? "+nextTree.distance);
				if (minimumPathWolf[nextTree.number].distance > 
						minimumPathWolf[currentTree].distance+nextDistance) {
					minimumPathWolf[nextTree.number].distance = 
							minimumPathWolf[currentTree].distance+nextDistance;
					minimumPathWolf[nextTree.number].isDoubleMoved = 
							!minimumPathWolf[currentTree].isDoubleMoved;
				}
			}
		}
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		treeCount = Integer.parseInt(tokenizer.nextToken());
		treePaths = new TreePath[treeCount+1];
		visited = new boolean[treeCount+1];
		minimumPathFox = new int[treeCount+1];
		minimumPathWolf = new WolfPath[treeCount+1];
		int pathCount = Integer.parseInt(tokenizer.nextToken());
		for (int index=0; index<pathCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			int distance = Integer.parseInt(tokenizer.nextToken())*2;
			treePaths[from] = new TreePath(to, distance, treePaths[from]);
			treePaths[to] = new TreePath(from, distance, treePaths[to]);
		}
		Arrays.fill(minimumPathFox, Integer.MAX_VALUE);
		for (int index=1; index<=treeCount; index++)
			minimumPathWolf[index] = new WolfPath(Integer.MAX_VALUE);
	}

}
