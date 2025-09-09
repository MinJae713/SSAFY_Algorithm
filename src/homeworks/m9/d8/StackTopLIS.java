package homeworks.m9.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - increaseWeightMemo
 *   ㄴ 처음 위치 벽돌에서 특정 위치 벽돌까지 무게 순서대로 벽돌을 선택할 때,
 *     그 높이의 합이 최대가 되는 벽돌의 개수 저장
 * 1. 입력 받은 벽돌을 밑변 넓이 순서대로 정렬
 * 2. increaseWeightMemo 각 값은 1로 초기화
 * 3. 처음~마지막 벽돌까지 반복 (targetBlock)
 * 4. 처음~targetBlock 직전 벽돌까지 반복 (fromBlock)
 * 	4.1. fromBlock보다 targetBlock이 더 가볍다면 continue
 * 	4.2. increaseWeightMemo 값이 가장 큰 fromBlock 위치 값 저장(maxCount)
 * 5. targetBlock 위치 increaseWeightMemo 값에 maxHeight+1 입력
 * 
 * 뭔가 애매하다.... 반복문 따라가면서 LIS 적용 과정을 다시 한번 봐야것슈?
 */
public class StackTopLIS {
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
	private static int[] increaseWeightMemo;
	private static StringBuilder[] buildingMemo;
	private static int[] countMemo;

	public static void main(String[] args) throws IOException {
		initialize();
		for (int targetBlock=0; targetBlock<blockCount; targetBlock++) {
			int maxHeight = 0;
			int maxFrom = -1;
			buildingMemo[targetBlock] = new StringBuilder();
			for (int fromBlock=0; fromBlock<targetBlock; fromBlock++) {
				if (blocks[fromBlock].weight > 
					blocks[targetBlock].weight) continue;
				if (maxHeight < increaseWeightMemo[fromBlock]) {
					maxHeight = increaseWeightMemo[fromBlock];
					maxFrom = fromBlock;
				}
			}
			if (maxFrom != -1) 
				buildingMemo[targetBlock].append(buildingMemo[maxFrom]);
			buildingMemo[targetBlock].append(blocks[targetBlock].number).append("\n");
			increaseWeightMemo[targetBlock] = maxHeight+blocks[targetBlock].height;
		}
		System.out.println(buildingMemo[blockCount-1]);
		reader.close();
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		blockCount = Integer.parseInt(reader.readLine().trim());
		blocks = new Block[blockCount];
		increaseWeightMemo = new int[blockCount];
		buildingMemo = new StringBuilder[blockCount];
		countMemo = new int[blockCount];
		for (int index=0; index<blockCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int width = Integer.parseInt(tokenizer.nextToken());
			int height = Integer.parseInt(tokenizer.nextToken());
			int weight = Integer.parseInt(tokenizer.nextToken());
			blocks[index] = new Block(index+1, width, height, weight);
			increaseWeightMemo[index] = height;
		}
		Arrays.sort(blocks);
		Arrays.fill(countMemo, 1);
	}

}
