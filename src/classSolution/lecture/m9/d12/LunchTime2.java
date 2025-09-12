package classSolution.lecture.m9.d12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class LunchTime2 {

	// 이동중, 대기중, 내려가는중, 완료 상태
	static class Person implements Comparable<Person>{
		int r,c,arrivalTime; 
		// 이동중, 대기중, 내려가는중, 완료 상태
		// 나머지는 행, 열, 도착시간, 내려간 횟수
		public Person(int r, int c) {
			super();
			this.r = r;
			this.c = c;
		}
		@Override
		public int compareTo(Person o) {
			// 도착시간 기준 빠른 순으로 정렬하기 위함
			return Integer.compare(arrivalTime, o.arrivalTime);
		}
	}
	static int N,min,cnt; // cnt는 사람 수
	static int[][] sList; // 계단 리스트(계단:r,c,height)
	static List<Person> pList;
	static int[] selected; // 사람마다 계단 선택 입력
	
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		int TC = Integer.parseInt(in.readLine().trim());
		for (int tc=1; tc<=TC; tc++) {
			N = Integer.parseInt(in.readLine().trim());
			pList = new ArrayList<Person>();
			sList = new int[2][];
			min = Integer.MAX_VALUE;
			for (int i=0, k=0; i<N; i++) {
				StringTokenizer st = new StringTokenizer(in.readLine().trim());
				for (int j=0; j<N; j++) {
					int c = Integer.parseInt(st.nextToken());
					if (c == 1) pList.add(new Person(i, j)); // 사람
					else if (c > 1) sList[k++] = new int [] {i,j,c}; // 계단 
				}
			}
			cnt = pList.size();
			selected = new int[cnt];
			
			divide(0);
			sb.append("#").append(tc).append(" ").append(min).append("\n");
		}
		System.out.print(sb);
		in.close();
	}

	private static void divide(int index) {
		// 사람마다 계단 배정
		if (index == cnt) {
			makeList();
			return;
		}
		selected[index] = 0;
		divide(index+1);
		selected[index] = 1;
		divide(index+1);
	}

	private static void makeList() {
		@SuppressWarnings("unchecked")
		ArrayList<Person>[] list = new ArrayList[] {
			new ArrayList<Person>(),
			new ArrayList<Person>()
		};
		
		for (int i=0; i<cnt; i++) {
			Person p = pList.get(i);
			int no = selected[i];
			
			p.arrivalTime = Math.abs(p.r-sList[no][0])+
							Math.abs(p.c-sList[no][1]);
			list[no].add(p);
		}
		
		// 각 계단의 사람 리스트 이용해서 내려가기 구현
		int timeA = processDown(list[0], sList[0][2]); // a계단 사람들
		int timeB = processDown(list[1], sList[1][2]); // b계단 사람들
		int res = Math.max(timeA, timeB); // 내려간 시간이 더 큰 값이 그 상황의 소요 시간
		min = Math.min(min, res);
	}

	private static int processDown(
			ArrayList<Person> list, int height) {
		if (list.size() == 0) return 0;
		Collections.sort(list); // 계단 도착시간 기준 오름차순 정렬
		int size = list.size()+3;
		int[] D = new int[size]; // 0에서 순서대로.. 계단에 가장 빨리 도착하는 순서
		for (int i=3; i<size; i++) {
			Person p = list.get(i-3);
			if (D[i-3]<=p.arrivalTime+1)
				D[i] = p.arrivalTime+1+height;
			else D[i] = D[i-3]+height;
		}
		return D[size-1];
	}
}
