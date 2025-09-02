package homeworks.m8.d29;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 숫자 갯수(numberCount)/4 만큼 반복 (rotateCount)
 * 	1.1. 영역별 문자열 배열(areaStrings) 초기화
 * 2. 처음 위치에서 rotateCount 만큼 뒤로 간 위치 계산(getRotatedIndex)
 * 	2.1. getRotatedIndex(): 입력 받은 위치(index)에서 회전 횟수(rotateCount) 만큼 합산,
 * 		2.1.1. 합산 결과가 음수라면 그 값에서 숫자 갯수를 더하고, 
 * 			      합산 결과가 숫자 갯수 이상이라면 숫자 갯수를 뺀 값을 반환
 * 		2.1.2. 그 이외 경우는 계산 결과 바로 반환
 * 	2.1. 계산 결과는 초기 위치(first)에 입력
 * 3. first, first+numberCount/4, first+numberCount/2, 
 * 	  first+(numberCount/4)*3의 위치를 배열(areaStarts)에 입력
 * 	3.1. 바로 +한 위치가 아닌 getRotatedIndex한 결과를 입력
 * 4. 숫자 갯수/4 만큼 반복 (distance)
 * 	4.1. areaStarts의 요소 갯수 만큼 반복 (areaIndex)
 * 		4.1.1. areaIndex+distance 위치의 문자 추출
 * 		4.1.2. 추출한 문자는 영역별 문자열(areaStrings)의 areaIndex 위치에 붙임
 * 5. areaStrings의 문자열들 10진수로 변환
 * 6. 변환한 값은 조합 가능한 숫자들 집합(possibleNumberSet)에 입력
 * 	6.1. 자료구조가 집합이기에, 이미 들어간 값은 들어가지 않음
 * 7. 모든 값을 넣은 이후, 배열로 변환 (possibleNumberArray)
 * 8. possibleNumberArray 내림차순 정렬 및 찾으려는 위치 값(targetIndex) 반환
 */
public class TressurePassword {
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int numberCount;
	private static int targetIndex;
	private static String tressureBox;
	private static int[] areaStarts;
	private static StringBuilder[] areaStrings;
	private static Set<Integer> possibleNumberSet;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			rotateTressure();
			Integer[] possibleNumberArray = possibleNumberSet.toArray(new Integer[0]);
			Arrays.sort(possibleNumberArray, new Comparator<Integer>() {
				@Override
				public int compare(Integer number1, Integer number2) {
					return Integer.compare(number1, number2)*(-1);
				}
			});
			builder.append("#").append(testCase).append(" ").
					append(possibleNumberArray[targetIndex]).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static void rotateTressure() {
		for (int rotateCount=0; rotateCount<numberCount/4; rotateCount++) {
			// 회전
			initializeAreaString();
			int first = getRotatedIndex(0, -rotateCount);
			for (int areaIndex=0; areaIndex<4; areaIndex++)
				areaStarts[areaIndex] = getRotatedIndex(first, (numberCount/4)*areaIndex);
			for (int distance=0; distance<numberCount/4; distance++) {
				// 각 변의 시작 위치~distance 만큼 떨어진 거리의 문자 추출
				for (int areaIndex=0; areaIndex<4; areaIndex++) {
					int extractIndex = getRotatedIndex(areaStarts[areaIndex], distance);
					areaStrings[areaIndex].append(tressureBox.charAt(extractIndex));
				}
			}
			for (int areaIndex=0; areaIndex<4; areaIndex++)
				possibleNumberSet.add(Integer.parseInt(areaStrings[areaIndex].toString(), 16));
		}
	}

	private static int getRotatedIndex(int index, int rotateCount) {
		index += rotateCount;
		if (index < 0) index += numberCount;
		else if (index >= numberCount) index -= numberCount;
		return index;
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		int testCaseCount = Integer.parseInt(reader.readLine().trim());
		return testCaseCount;
	}
	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		numberCount = Integer.parseInt(tokenizer.nextToken());
		targetIndex = Integer.parseInt(tokenizer.nextToken())-1;
		tressureBox = reader.readLine().trim();
		areaStarts = new int[4];
		possibleNumberSet = new HashSet<>();
	}

	private static void initializeAreaString() {
		// Arrays.fill() 쓰면 메모리가 공유되나보다?
		areaStrings = new StringBuilder[] {
			new StringBuilder(),
			new StringBuilder(),
			new StringBuilder(),
			new StringBuilder(),
		};
	}
}
