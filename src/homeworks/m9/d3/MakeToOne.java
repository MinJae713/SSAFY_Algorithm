package homeworks.m9.d3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * [문제 풀이]
 * 1. 큐에 연산 정보 입력(Operate)
 * 	1.1. 현재 숫자(number), 연산 횟수(count)
 * 2. 큐에 요소가 있는 동안 반복
 * 3. 큐 요소 offer(operate)
 * 4. 해당 수가 1이라면 반복 종료
 * 4. operate의 number-1 값과 연산 횟수+1을 파라미터로 Operate 생성 및 큐에 추가
 * 5. operate의 number를 2로 나눈 나머지가 0이라면?
 * 	5.1. operate의 number/2 값과 연산 횟수+1을 파라미터로 Operate 생성 및 큐에 추가
 * 6. operate의 number를 3으로 나눈 나머지가 0이라면?
 * 	6.1. operate의 number/3 값과 연산 횟수+1을 파라미터로 Operate 생성 및 큐에 추가
 */
public class MakeToOne {
	static class Operate {
		int number;
		int count;
		public Operate(int number, int count) {
			super();
			this.number = number;
			this.count = count;
		}
	}
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Queue<Operate> queue = new ArrayDeque<Operate>();
		Operate current = new Operate(Integer.parseInt(reader.readLine().trim()), 0);
		queue.offer(current);
		while (!queue.isEmpty()) {
			current = queue.poll();
			if (current.number == 1) break;
			queue.offer(new Operate(current.number-1, current.count+1));
			if (current.number%2 == 0)
				queue.offer(new Operate(current.number/2, current.count+1));
			if (current.number%3 == 0)
				queue.offer(new Operate(current.number/3, current.count+1));
		}
		System.out.println(current.count);
		reader.close();
	}

}
