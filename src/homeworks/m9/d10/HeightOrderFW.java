package homeworks.m9.d10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * - heigherThanMemo: 학생과 학생 사이에 몇 명의 키 비교 대상이 있는지 표시
 * - 플로이드-워샬 적용: 각 학생마다 연결된 경로 확인, 
 * 	  ㄴ 수행 결과 연결이 안되있다면 그 위치는 최대값으로 표시될 것
 * 1. 각 학생에 대해 반복 (connectStudent)
 * 	1.1. 각 학생에 대해 반복 (fromStudent)
 * 		1.1.1. 각 학생에 대해 반복 (toStudent)
 * 		1.1.2. heigherThanMemo의 from, toStudent 해당 값 입력(noConnect)
 * 		1.1.3. heigherThanMemo의 from, connect 해당 값 입력(connect1)
 * 		1.1.4. heigherThanMemo의 connect, to 해당 값 입력(connect2)
 * 		1.1.5. connect1이나 connect2가 초기값이면?
 * 			1.1.5.1. heigherThanMemo의 from, toStudent 위치에 noConnect 입력
 * 		1.1.6. 아니라면? noConnect와 connect1+connect2의 대소 비교, 더 작은 값을
 * 			   heigherThanMemo의 from, toStudent 위치에 입력
 * 2. heigherThanMemo의 각 위치에 대해 반복 (fromStudent, toStudent)
 * 	2.1. heigherThanMemo fromStudent, toStudent 해당 위치가 초기값이 아니라면?
 * 	2.2. 학생 별 키 대소 관계 파악 수(knowableCounts) from, to 위치 1 증가
 * 	2.3. from 위치는 자신 보다 큰 학생 수가 파악되었다는 것
 * 	2.4. to 위치는 자신보다 작은 학생 수가 파악 되었다는 것
 * 3. 각 학생에 대해 반복, knowableCount+1이 총 학생수인 학생의 수 계산
 * 	3.1. 자기 자신 빼고 나머지 학생 수 모두 파악이 되었다면 모든 학생의 키를 알 수 있는 학생
 */
public class HeightOrderFW {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int studentCount;
	private static int compareCount;
	private static int[][] heigherThanMemo;
	private static int[] knowableCounts;
	private static int resultCount;
	private static final int INF = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		for (int connectStudent=1; connectStudent<=studentCount; connectStudent++) {
			for (int fromStudent=1; fromStudent<=studentCount; fromStudent++)
				for (int toStudent=1; toStudent<=studentCount; toStudent++) {
					int noConnect = heigherThanMemo[fromStudent][toStudent];
					int connect1 = heigherThanMemo[fromStudent][connectStudent];
					int connect2 = heigherThanMemo[connectStudent][toStudent];
					if (connect1 == INF || connect2 == INF)
						heigherThanMemo[fromStudent][toStudent] = noConnect;
					else heigherThanMemo[fromStudent][toStudent] = 
							Math.min(noConnect, connect1+connect2);
				}
		}
		for (int fromStudent=1; fromStudent<=studentCount; fromStudent++)
			for (int toStudent=1; toStudent<=studentCount; toStudent++) {
				if (fromStudent == toStudent ||
						heigherThanMemo[fromStudent][toStudent] == INF) continue;
				knowableCounts[fromStudent]++;
				knowableCounts[toStudent]++;
			}
		for (int student=1; student<=studentCount; student++)
			resultCount += knowableCounts[student]+1 == studentCount ? 1 : 0;
		System.out.println(resultCount);
		reader.close();
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		tokenizer = new StringTokenizer(reader.readLine().trim());
		studentCount = Integer.parseInt(tokenizer.nextToken());
		compareCount = Integer.parseInt(tokenizer.nextToken());
		heigherThanMemo = new int[studentCount+1][studentCount+1];
		knowableCounts = new int[studentCount+1];
		for (int student=1; student<=studentCount; student++) {
			Arrays.fill(heigherThanMemo[student], INF);
			heigherThanMemo[student][student] = 0;
		}
		for (int count=0; count<compareCount; count++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int from = Integer.parseInt(tokenizer.nextToken());
			int to = Integer.parseInt(tokenizer.nextToken());
			heigherThanMemo[from][to] = 1;
		}
		resultCount = 0;
	}

}
