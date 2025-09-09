package homeworks.m9.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - 각 위치 별로 블록을 쌓을 때 그 높이가 가장 높은 경우의 '높이', '개수', '구성'을 따로 저장함
 * - increaseHeightMemo: 각 위치 별 높이가 가장 높은 블록 구성을 만들었을 때 그 '높이'
 * - increaseCountMemo: 각 위치 별 높이가 가장 높은 블록 구성을 만들었을 때 그 '개수'
 * - increaseComposeMemo: 각 위치 별 높이가 가장 높은 블록 구성을 만들었을 때 블록 '구성'
 * => 세 memo 변수는 현재 위치 이전까지 구성으로 나올 수 있는 밑변과 무게 규칙은 지키면서
 * 	그 높이를 최대로 만들 수 있는 블록 구성의 높이, 개수, 종류를 각각 나타냄
 * 	ㄴ 이전의 각 높이, 개수, 종류에 현재 위치 블록의 높이, 개수, 종류를 덧붙이는 식으로 구현
 * 1. 입력 받은 벽돌을 밑변 넓이 순서대로 정렬
 * 2. increaseHeightMemo 각 값은 각 위치의 키로, 
 * 	increaseCountMemo 각 값은 1로 초기화
 * 	2.1. increaseComposeMemo의 각 위치에는 StringBuilder 객체 입력
 * 3. 처음~마지막 벽돌까지 반복 (currentBlock)
 * 4. 처음~currentBlock 직전 벽돌까지 반복 (fromBlock)
 * 	4.1. fromBlock보다 currentBlock이 더 가볍다면 continue
 * 	4.2. increaseHeightMemo 값이 가장 큰 fromBlock 위치 저장(maxHeightIndex)
 * 5. maxHeightIndex가 -1이 아니라면?
 * 	5.1. currentBlock 위치 increaseHeightMemo 값에 maxHeightIndex 
 * 		  해당 increaseHeightMemo값+currentBlock의 높이 입력
 * 	5.2. currentBlock 위치 increaseCountMemo 값에 maxHeightIndex
 * 		  해당 increaseCountMemo값+1 입력
 * 	5.3. currentBlock 위치 increaseComposeMemo 위치 builder에 maxHeightIndex
 * 		  해당 increaseComposeMemo에 있는 문자열을 append
 * 6. currentBlock 위치 increaseComposeMemo builder에 currentBlock의 번호 append
 * 	6.1. 현재 블록은 맨 아래로 보내기 위함
 * 
 * 음 핵심이라면 문제에서 최장 부분 증가 수열의 개수 이외에 그 수열에 들어있는 요소의 정보가 필요하다고 하면
 * 초기에 현 위치+이전까지 위치 최장 부분 증가를 만족하는 수열을 통해 현 위치의 최장 부분 증가 수열을 구성할 때
 * 추가로 필요한 정보를 이전 위치까지 만들어진 정보에서 현 위치 정보를 추가하는 식으로 생각해보면 될 듯 함
 * ㄴ 본디 문제에서는 '높이'가 가장 높은 블록에 대한 최부증수를 요구했고, 그에 따라 이전 위치들에 대한
 *   최장 부분 증가 수열 중 그 '높이' 값이 가장 높은 위치를 찾아내서 현재 위치와 결합 - 현 위치 최부증수를 만듬
 * ㄴ 다만 이 문제에서는 높이 뿐만 아니라 최부증수의 요소 수, 요소 구성을 물어봤기 때문에
 *   이를 적용하고자 '높이' 값이 가장 높은 위치를 찾아냈을 때 이전 위치에서 최부증수의 높이를 받아냈을 뿐 아니라,
 *   이전 위치의 요소 개수, 요소 구성을 받아낼 수 있도록 했고, 이를 현 위치 요소 수, 구성과 결합,
 *   이를 통해 현 위치에서 만들어지는 최부증수의 요소 개수, 요소 구성을 만들 수 있었음
 * ㄴ 비슷한 문제가 나오면... '최부증수를 만드는 과정'에서 해결을 볼 수 있다는 것을 꼭 잊지 마쇼!
 */
public class StackTop {
	static class Block implements Comparable<Block>{
		int number;
		int width;
		int height;
		int weight;
		public Block(int number, int width, int height, int weight) {
			super();
			this.number = number;
			this.width = width;
			this.height = height;
			this.weight = weight;
		}
		@Override
		public int compareTo(Block other) {
			return Integer.compare(width, other.width);
		}
	}
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int blockCount;
	private static Block[] blocks;
	private static int[] increaseHeightMemo;
	private static int[] increaseCountMemo;
	private static StringBuilder[] increaseComposeMemo;
	private static int totalMaxHeightIndex;
	private static int totalMaxHeight;

	public static void main(String[] args) throws IOException {
		initialize();
		setIncreaseBlocks();
		StringBuilder result = new StringBuilder();
		result.append(increaseCountMemo[totalMaxHeightIndex]).append("\n").
				append(increaseComposeMemo[totalMaxHeightIndex]);
		System.out.print(result);
		reader.close();
	}

	private static void setIncreaseBlocks() {
		for (int currentBlock=0; currentBlock<blockCount; currentBlock++) {
			int maxHeightIndex = -1;
			int maxHeight = 0;
			for (int fromBlock=0; fromBlock<currentBlock; fromBlock++) {
				if (blocks[currentBlock].weight < blocks[fromBlock].weight) continue;
				if (maxHeight < increaseHeightMemo[fromBlock]) {
					// 하위 블록 구성이 가장 높은 위치를 받음
					maxHeight = increaseHeightMemo[fromBlock];
					maxHeightIndex = fromBlock;
				}
			}
			if (maxHeightIndex != -1) {
				// 현재 블록 위에 올릴 블록 종류 선택 완료
				increaseHeightMemo[currentBlock] = 
						increaseHeightMemo[maxHeightIndex]+blocks[currentBlock].height;
				increaseCountMemo[currentBlock] = 
						increaseCountMemo[maxHeightIndex]+1;
				increaseComposeMemo[currentBlock].append(increaseComposeMemo[maxHeightIndex]);
			}
			increaseComposeMemo[currentBlock].append(blocks[currentBlock].number).append("\n");
			if (totalMaxHeight < increaseHeightMemo[currentBlock]) {
				totalMaxHeight = increaseHeightMemo[currentBlock];
				totalMaxHeightIndex = currentBlock;
			}
		}
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		blockCount = Integer.parseInt(reader.readLine().trim());
		blocks = new Block[blockCount];
		increaseHeightMemo = new int[blockCount];
		increaseCountMemo = new int[blockCount];
		increaseComposeMemo = new StringBuilder[blockCount];
		for (int index=0; index<blockCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int width = Integer.parseInt(tokenizer.nextToken());
			int height = Integer.parseInt(tokenizer.nextToken());
			int weight = Integer.parseInt(tokenizer.nextToken());
			blocks[index] = new Block(index+1, width, height, weight);
		}
		Arrays.sort(blocks);
		for (int index=0; index<blockCount; index++) {
			Block block = blocks[index];
			increaseHeightMemo[index] = block.height;
			increaseCountMemo[index] = 1;
			increaseComposeMemo[index] = new StringBuilder();
		}
		totalMaxHeightIndex = -1;
		totalMaxHeight = 0;
	}

}
