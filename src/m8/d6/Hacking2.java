package m8.d6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 컴퓨터 수, 신뢰 수 입력
 * 2. 컴퓨터 수+1*컴퓨터 수+1 크기의 신뢰 배열 생성
 * 3. 컴퓨터 수 크기+1의 컴퓨터별 해킹 수 배열 생성
 * 	3.1. 해당 위치의 컴퓨터는 한번에 얼마나 많이 해킹할 수 있는지를 나타냄
 * 4. 신뢰 수 만큼 신뢰 관계 입력 -> 신뢰 배열 값 지정
 * 5. 신뢰 관계로 들어가는 컴퓨터 방문 여부 저장 배열 생성 (중복 호출 방지)
 * 6. 컴퓨터 수 만큼 반복 - 컴퓨터 별 해킹 수 찾는 메소드(hacking) 호출
 * 	6.1. 파라미터는 현재 컴퓨터 번호(computer), 시작 컴퓨터 번호(from)
 * 	6.2. from 위치 컴퓨터 해킹 수 1 증가
 * 	6.3. 컴퓨터 수 만큼 반복(trustComputer): 신뢰 관계가 맞는 경우 수행
 * 		6.3.1. computer -> trustComputer로 이미 방문했는지 확인 -> 방문하지 않은 경우, true로 지정
 * 		6.3.2. 신뢰 컴퓨터(trustComputer), 시작 컴퓨터 번호(from)을 파라미터로 hacking 재귀 호출
 * 6. 컴퓨터별 해킹 수 배열 출력
 */
public class Hacking2 {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int computerCount;
	private static int trustCount;
	private static boolean[][] hackingPath;
	private static boolean[][] visited;
	private static int[] hackingCount;
	
	public static void main(String[] args) throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		tokenizer = new StringTokenizer(reader.readLine());
		computerCount = Integer.parseInt(tokenizer.nextToken());
		hackingPath = new boolean[computerCount+1][computerCount+1];
		visited = new boolean[computerCount+1][computerCount+1];
		hackingCount = new int[computerCount+1];
		trustCount = Integer.parseInt(tokenizer.nextToken());
		for (int trustIndex=0; trustIndex<trustCount; trustIndex++) {
			tokenizer = new StringTokenizer(reader.readLine());
			int trust = Integer.parseInt(tokenizer.nextToken());
			int isTrusted = Integer.parseInt(tokenizer.nextToken());
			hackingPath[isTrusted][trust] = true;
		}
		for (int computer=1; computer<=computerCount; computer++) {
			hacking(computer, computer);
			for (int computerRow=1; computerRow<computerCount; computerRow++)
				for (int computerColumn=1; computerColumn<computerColumn; computerColumn++)
					visited[computerRow][computerColumn] = false;
		}
		System.out.println(Arrays.toString(hackingCount));
		reader.close();
	}
	private static void hacking(int computer, int from) {
		hackingCount[from]++;
		for (int trustComputer=1; trustComputer<=computerCount; trustComputer++)
			if (hackingPath[computer][trustComputer] && !visited[computer][trustComputer]) {
				visited[computer][trustComputer] = true;
				if (!visited[trustComputer][computer]) // 이미 computer->trustComputer로 온 경로라면 해킹x
					hacking(trustComputer, from);
			}
	}
}
