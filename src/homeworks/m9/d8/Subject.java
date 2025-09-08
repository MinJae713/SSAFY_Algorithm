package homeworks.m9.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - maxTotalImport
 * 	  ㄴ 과목 수와 최대 공부 시간이 주어질 때 나올 수 있는 중요도 합의 최대 저장
 * 1. getMaxTotalImport(): maxTotalImport에 값을 채우는 함수
 * 	1.1. 파라미터로 과목 수(count), 최대 공부 시간(limitTime)을 받음
 * 2. 과목 수가 0이 되면? 현재 count와 limitTime 해당 maxTotalImport 값 반환
 * 3. count-1위치 limitTime위치 해당 maxTotalImport가 0이라면?
 * 	3.1. count-1, limitTime을 파라미터로 getMaxTotalImport() 재귀 호출
 * 	3.2. 위 호출 결과를 maxTotalImport의 count-1위치 limitTime위치에 입력
 * 4. 현재 최대 공부 시간보다 현재 과목의 공부 시간이 같거나 더 작고,
 * 	  count-1위치 limitTime-현재 과목의 공부 시간 해당 maxTotalImport가 0이라면?
 * 	4.1. count-1, limitTime-현재 과목의 공부 시간을 파라미터로 
 * 		 getMaxTotalImport() 재귀 호출
 * 	4.2. 위 호출 결과를 maxTotalImport의 count-1, 
 * 		 limitTime-현재 과목의 공부 시간 위치에 입력
 * 5. 3, 4 실행 결과 입력 (각각 notContained, contained)
 * 	5.1. contained의 경우, 현재 최대 공부 시간보다 현재 과목의 공부 시간이 같거나 
 * 		  더 작다면 maxTotalImport의 count-1, limitTime-현재 과목의 공부 시간 위치에
 * 		  현재 과목의 중요도를 더한 값을, 아니면 0을 입력
 * 6. notContained와 contained 중 더 큰 값 반환
 */
public class Subject {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int limitTime;
	private static int count;
	private static int[] importants;
	private static int[] times;
	private static int[][] maxTotalImport;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		System.out.println(getMaxTotalImport(count, limitTime));
		reader.close();
	}

	private static int getMaxTotalImport(int count, int limitTime) {
		if (count == 0) 
			return maxTotalImport[count][limitTime];
		if (maxTotalImport[count-1][limitTime] == 0)
			maxTotalImport[count-1][limitTime] = 
				getMaxTotalImport(count-1, limitTime);
		if (limitTime >= times[count] && 
				maxTotalImport[count-1][limitTime-times[count]] == 0)
			maxTotalImport[count-1][limitTime-times[count]] = 
				getMaxTotalImport(count-1, limitTime-times[count]);
		int notContained = maxTotalImport[count-1][limitTime];
		int contained = limitTime >= times[count] ?
				maxTotalImport[count-1][limitTime-times[count]]+importants[count] : 0;
		return Math.max(notContained, contained);
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		limitTime = Integer.parseInt(tokenizer.nextToken());
		count = Integer.parseInt(tokenizer.nextToken());
		importants = new int[count+1];
		times = new int[count+1];
		maxTotalImport = new int[count+1][limitTime+1];
		for (int index=1; index<=count; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			importants[index] = Integer.parseInt(tokenizer.nextToken());
			times[index] = Integer.parseInt(tokenizer.nextToken());
		}
	}

}
