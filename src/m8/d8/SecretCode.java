package m8.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * <변수 및 메소드>
 * 1. reader: 입력 값 받을 때 사용
 * 2. builder: 출력 값 지정 시 사용
 * 3. tokenizer: 입력받은 값 공백으로 구분하여 반환
 * 4. secretCode: 8자리의 수를 받아서 암호가 만들어지는 배열
 * 5. pointer: secretCode의 각 자리에 접근하는 포인터
 * 6. decrease: secretCode의 각 자리 감소량
 * 7. finish: 암호 생성 완료 여부
 * 8. initialize(): 변수 값 초기화
 * 9. setInitialCode(): 암호 생성 코드 초기 값 입력
 * 10. makeSecretCode(): 암호 생성
 * 11. appendToBuilder(): builder에 암호 값 입력
 * 
 * <로직>
 * 1. reader, builder 초기화
 * 2. 10번 반복, 테스트 케이스 입력
 * 3. pointer, decrease, finish, secretCode 값 초기화
 * 4. 암호 생성
 * 	4.1. finish가 true가 될 때 까지 반복
 * 	4.2. secretCode의 pointer 위치 값 decrease 만큼 감소
 * 	4.3. 감소 이후 값이 0 이하라면 finish 값 true로 변경, 해당 위치 값 0으로 지정
 * 	4.3. pointer 1 증가, 만일 8이라면 0으로 초기화
 * 	4.4. decrease 1 증가, 만일 6이라면 1로 초기화
 * 5. 생성 결과 builder에 입력 (pointer 위치~0이 나온 위치 순서대로 입력)
 * 6. builder 출력
 */
public class SecretCode {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int[] secretCode;
	private static int pointer;
	private static int decrease;
	private static boolean finish;
	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		for (int tc=1; tc<=10; tc++) {
			int testCase = Integer.parseInt(reader.readLine().trim());
			initialize();
			setInitialCode();
			makeSecretCode();
			appendToBuilder(testCase);
		}
		System.out.print(builder);
		reader.close();
	}
	private static void initialize() {
		pointer = 0;
		decrease = 1;
		finish = false;
		secretCode = new int[8];
	}
	private static void setInitialCode() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int index=0; index<8; index++)
			secretCode[index] = Integer.parseInt(tokenizer.nextToken());
	}
	private static void makeSecretCode() {
		while (!finish) {
			secretCode[pointer] -= decrease;
			finish = secretCode[pointer] <= 0;
			if (finish) secretCode[pointer] = 0;
			pointer = ++pointer == 8 ? 0 : pointer;
			decrease = ++decrease == 6 ? 1 : decrease;
		}
	}
	private static void appendToBuilder(int testCase) {
		builder.append("#").append(testCase).append(" ");
		int pointerIndex=pointer;
		do {
			builder.append(secretCode[pointerIndex]).append(" ");
			pointerIndex = (pointerIndex+1)%8;
		} while (pointerIndex != pointer);
		builder.append("\n");
	}
}
