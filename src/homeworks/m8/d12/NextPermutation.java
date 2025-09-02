package homeworks.m8.d12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 숫자 갯수 및 숫자 갯수 만큼 숫자를 배열에 입력
 * 2. 배열 맨 뒤 위치(index)부터 1씩 감소하면서 그 배열의 가장 큰 값 위치 찾음
 * 	2.1. 현재 위치가 바로 앞 위치보다 작으면 index 1 줄임
 * 	2.2. index가 0이 되거나, 바로 앞 위치보다 크면 반복 종료
 * 3. index가 0이면? -> 결과(result)는 -1 저장
 * 4. 0보다 크다면 ? index-1 위치와 마지막 위치~index 값 중에서
 *    index-1 위치보다 큰 값들 중 가장 작은 값이 있는 위치의 값 교환
 * 5. index 위치~배열 마지막 위치 정렬
 * 	5.1. index 위치(start), 배열 마지막 위치(end) 초기화
 * 	5.2. start가 end보다 작은 경우에 수행
 * 		5.2.1. start 위치와 end 위치 교환
 * 		5.2.2. start 1 증가, end 1 감소
 * 6. 결과에 배열 요소 하나씩 저장
 * 7. 결과 출력
 */
public class NextPermutation {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int length;
	private static int[] permuteArray;

	public static void main(String[] args) throws IOException {
		initialize();
		getNextPermuteArray();
		System.out.println(builder);
		reader.close();
	}
	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		length = Integer.parseInt(reader.readLine().trim());
		permuteArray = new int[length];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<length; index++) 
			permuteArray[index] = Integer.parseInt(tokenizer.nextToken());
	}
	private static void getNextPermuteArray() {
		int top = length-1;
		while (top > 0 && permuteArray[top-1] >= permuteArray[top])
			top--;
		if (top == 0)
			builder.append(-1);
		else {
			int swapTarget = length-1;
			while (permuteArray[top-1] >= permuteArray[swapTarget])
				swapTarget--;
			swap(top-1, swapTarget);
			swapTarget = length-1;
			while (top < swapTarget)
				swap(top++, swapTarget--);
			for (int index=0; index<length; index++)
				builder.append(permuteArray[index]).append(" ");
		}
	}
	private static void swap(int from, int to) {
		int temp = permuteArray[to];
		permuteArray[to] = permuteArray[from];
		permuteArray[from] = temp;
	}
}
