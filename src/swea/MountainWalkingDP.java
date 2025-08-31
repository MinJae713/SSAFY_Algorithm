package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 처음 위치를 큐에 enque 
 * 	1.1. 처음 위치 최소 랭크(rankMemo) 0 표시 후 enque
 * 2. 큐에 요소가 있는 동안 반복
 * 3. 요소 deque(current)
 * 4. 현 위치가 3이라면?
 * 	4.1. 현재 위치의 최소 난이도와 최소 난이도(minimumRank) 비교 -> 더 낮은 값으로 값 초기화
 * 5. 4방향에 대해 반복 (nextX, nextY)
 * 	5.1. 다음 위치가 이동할 수 없거나, 이미 방문한 위치라면 continue
 * 	5.2. 방향이 위(0)라면?
 * 		5.2.1. nextY에서 맨 위 위치까지 반복 -> 현 위치가 0이면 nextY 감소
 * 		5.2.2. nextY가 -1이라면 continue
 * 		5.2.3. 현재 y위치와 nextY의 차이 계산(nextRank)
 * 		5.2.4. nextRank를 현재 위치 최소 난이도와 비교, 더 큰 값으로 nextRank 입력
 * 			5.2.4.1. 지금까지 등산 난이도 중, 최대값을 그대로 가져가기 위함
 * 			5.2.4.2. 난이도 최대값은 아래에서 다음 위치 rank 값으로서 rankMemo에 입력됨
 * 	5.3. 방향이 아래(2)라면?
 * 		5.3.1. nextY에서 맨 아래 위치(height-1)까지 반복 -> 현 위치가 0이면 nextY 증가
 * 		5.3.2. nextY가 height라면 continue
 * 		5.3.3. nextY와 현재 y위치와 차이 계산(nextRank)
 * 		5.3.4. nextRank를 현재 난이도와 비교, 더 큰 값으로 nextRank 입력
 * 	5.4. 방향이 양옆(1, 3)라면?
 * 		5.4.1. 해당 위치가 0이라면 continue
 * 	5.5. nextRank보다 다음 위치 최소 난이도가 더 크다면?
 * 		5.5.1. 다음 위치 최소 난이도를 nextRank로 입력
 * 		5.5.2. nextX, nextY를 enque
 * etc) BFS/DFS를 쓰는데 어떤 위치를 각자 다른 경로를 통해 중복으로 방문해서 
 * 	  시간 초과가 나고, 그 위치를 한번만 방문하도록 하고 싶다면?
 * 	  ㄴ DP를 고려해보자!
 * 	  ㄴ 이러한 문제 유형은 어쨌든 어디든 최종 목적지가 있을 것,
 * 	  ㄴ 그 최종 목적지에 도달하면 그 길까지 오는 길의 최적 경로를 물어봄
 * 		  ㄴ 이 문제에서는 난이도가 가장 낮은 길을 물어봤음
 * 	  ㄴ 어느 위치에 어떤 값을 '메모이제이션'한다 -> 메모이제이션 한 값 기준으로
 *         더 나은 값이면 그 길로 들어가보고, 아니라면 들어가지 않는다 
 *         ㄴ 이 방식으로 중복 방문을 어느 정도 방지할 수 있음 (BFS의 enque, DFS의 재귀 등)
 *      ㄴ 그럼 뭘 기록해야 할까? 어떤 값을 선택해서 기록했을 때, 
 *      	  그 값이 다른 경로를 통해 들어온 값과 비교함으로 인해 더 최적임을 보장해야 함
 *      ㄴ 이건 문제를 좀 많이 풀어봐야 아는 '감'의 영역이라 뭐라고 딱 찝기가 애매하네...?
 *      ㄴ 다만 이 문제의 경우는, 중복 경로마다 서로 다르게 지정되는 난이도 수치의 최소 값을 봤음
 *      ㄴ ex) (1, 1)을 (0, 1) or (1, 0)에서 들어온다면?
 *         ㄴ 앞선 경우를 통해 이미 (1, 0)에서 0의 난이도로 들어오는거 확인했다면,
 *            1의 난이도를 갖고 들어오는 (0, 1)의 경로는 아무래도 난이도가 더 크니까 선택되지 않음
 *         ㄴ 이러면 (1, 1)로 들어오는 최적 경로는 (1, 0)에서 들어오는 길이 되는거고,
 *            (1, 1) 위치가 갖는 최적 경로 메모이제이션 값은 0이 되는겨
 *         ㄴ 이 메모이제이션 값을 통해 다른 경로에서 들어올 일이 있을때 다시 비교할 거고,
 *         	  혹시나 그럴일은 여기서 없지만 난이도가 음수가 나오는 일이 있다? 
 *         	  그러면 (1, 1) 메모이제이션은 바뀌면서 그에 따라 enque or 재귀가 일어날 것
 *         	  ㄴ 이거슨 0보다 더 최적의 경로가 나왔기 때문임 -> 그에 따라 (1, 1)을 통과하는 길들은
 *         		  그들의 메모이제이션 값을 다시 갱신해야 함 (이는 각작의 메모이제이션 값을 초기화 하기 위함임)
 *      ㄴ 암튼 기존의 BFS/DFS에서 visited를 쓸 때 이전 경로 방문 여부를 삭제하는 부분 때문에 애로사항이 있다면
 *      ㄴ visited에 boolean이 아닌 그 위치로 들어오는 최적의 값을 기록해두고, 다른 경로에서 그 위치로 들어올 일이 있을때
 *      ㄴ 그 위치의 최적 값과 비교하여 그 위치로 들어갈지 말지를 결정하도록 하는게....? 좋지 않을까?
 *      ㄴ 뭐 말은 쉬운디 문제 적용이 좀 고민이네요
 *      ㄴ 이상 9월 첫날 새벽 2시의 유민재였습니다
 */
public class MountainWalkingDP {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[][] delta;
	private static int height;
	private static int width;
	private static int[][] mountain;
	private static int startX;
	private static int startY;
	private static int endX;
	private static int endY;
	private static int minimumRank;
	private static int[][] rankMemo;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			getMinimumRank();
			builder.append("#").append(testCase).
						append(" ").append(minimumRank).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void getMinimumRank() {
		Queue<int[]> queue = new ArrayDeque<int[]>();
		rankMemo[startY][startX] = 0;
		queue.offer(new int[] {startY, startX});
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			int rank = rankMemo[current[0]][current[1]];
			if (current[0] == endY && current[1] == endX) {
				minimumRank = Math.min(minimumRank, rank);
				if (minimumRank == 1) return;
				continue;
			}
			for (int deltaIndex=0; deltaIndex<4; deltaIndex++) {
				int nextY = current[0]+delta[deltaIndex][0];
				int nextX = current[1]+delta[deltaIndex][1];
				if (!moveAble(nextY, nextX)) continue;
				int nextRank = rank;
				if (deltaIndex == 0) {
					while (nextY>=0 && mountain[nextY][nextX] == 0) nextY--;
					if (nextY < 0) continue;
					nextRank = Math.max(rank, current[0]-nextY);
				} else if (deltaIndex == 2) {
					while (nextY<height && mountain[nextY][nextX] == 0) nextY++;
					if (nextY >= height) continue;
					nextRank = Math.max(rank, nextY-current[0]);
				} else {
					if (mountain[nextY][nextX] == 0)	continue;
				}
				if (nextRank < rankMemo[nextY][nextX]) {
					rankMemo[nextY][nextX] = nextRank;
					queue.offer(new int[] {nextY, nextX});
				}
			}
		}
	}

	private static boolean moveAble(int nextY, int nextX) {
		return 0<=nextY && nextY<height &&
				0<=nextX && nextX<width;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		delta = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		height = Integer.parseInt(tokenizer.nextToken());
		width = Integer.parseInt(tokenizer.nextToken());
		mountain = new int[height][width];
		for (int row=0; row<height; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<width; column++) {
				mountain[row][column] = Integer.parseInt(tokenizer.nextToken());
				if (mountain[row][column] == 2) {
					startX = column;
					startY = row;
				} else if (mountain[row][column] == 3) {
					endX = column;
					endY = row;
				}
			}
		}
		minimumRank = Integer.MAX_VALUE;
		rankMemo = new int[height][width];
		for (int row=0; row<height; row++)
			Arrays.fill(rankMemo[row], Integer.MAX_VALUE);
	}
}
