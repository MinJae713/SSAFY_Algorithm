package homeworks.m9.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - maxHeightTop: 벽돌 개수, 밑면 넓이, 무게가 주어질 때 나올 수 있는 최대 탑의 높이 저장
 * 1. getMaxHeightTop(): maxHeightTop 값 입력
 * 	1.1. 파라미터는  벽돌 개수(count), 제한 밑면 넓이(limitWidth), 제한 무게(limitWeight)
 * 2. 벽돌 개수가 0이라면? 현재 count, limitWidth, limitWeight 해당 maxHeightTop 반환
 * 3. 현재 count 위치 벽돌을 포함하지 않는 경우 입력
 * 	3.1. maxHeightTop의 count-1, limitWidth, limitWeight 위치의 값이 0이라면?
 * 	3.2. count-1, limitWidth, limitWeight를 파라미터로 getMaxHeightTop() 재귀 호출
 * 	3.3. 위 호출 결과를 maxHeightTop에 입력
 * 4. 현재 count 위치 벽돌을 포함하는 경우 입력
 * 	4.1. maxHeightTop의 count-1, 현재 벽돌의 밑면 넓이, 현재 벽돌의 무게에 해당하는 값이 0이라면?
 * 	4.2. count-1, 현재 벽돌의 밑면 넓이, 현재 벽돌의 무게를 파라미터로 getMaxHeightTop() 재귀 호출
 * 	4.3. 위 호출 결과를 maxHeightTop에 입력
 * 5. 3, 4의 결과 입력 (각각 notContained, contained)
 * 	5.1. notContained는 maxHeightTop의 count-1, limitWidht, limitWeight 해당 위치 값 입력
 * 	5.2. contained는 maxHeightTop의 count-1, 현재 벽돌의 밑면 넓이, 현재 벽돌의 무게 해당 위치 값에
 * 	          현재 벽돌의 높이 값을 더한 값을 입력
 * 6. contained가 notContained보다 더 크다면? (좀 애매)
 * 	6.1. 총 벽돌의 개수 1 증가
 * 	6.2. 포함 벽돌 번호에 현재 벽돌 번호(count) 추가
 * 7. contained와 notContained 값 비교, 더 큰 값 반환
 */
public class StackTop {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int blockCount;
	private static int maxWidth;
	private static int maxWeight;
	private static int[] widths;
	private static int[] heights;
	private static int[] weights;
	private static int[][][] maxHeightTop;
	private static int totalBlockCount;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		
		reader.close();
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		blockCount = Integer.parseInt(reader.readLine().trim());
		maxWidth = 0;
		maxWeight = 0;
		widths = new int[blockCount+1];
		heights = new int[blockCount+1];
		weights = new int[blockCount+1];
		for (int index=1; index<=blockCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			widths[index] = Integer.parseInt(tokenizer.nextToken());
			heights[index] = Integer.parseInt(tokenizer.nextToken());
			weights[index] = Integer.parseInt(tokenizer.nextToken());
			if (maxWidth < widths[index]) maxWidth = widths[index];
			if (maxWeight < weights[index]) maxWeight = weights[index];
		}
		maxHeightTop = new int[blockCount+1][maxWidth+1][maxWeight+1];
		totalBlockCount = 0;
	}

}
