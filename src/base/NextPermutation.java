package base;

import java.util.Arrays;

public class NextPermutation {
	
	static final int ELEMENT_COUNT = 4;
	static int[] elementArray; // 시작은 1 2 3 4
	
	private static void swap(int leftIndex, int rightIndex) {
		int temp = elementArray[leftIndex];
		elementArray[leftIndex] = elementArray[rightIndex];
		elementArray[rightIndex] = temp;
	}
	
	private static boolean hasNext() {
		int head = 0;
		int tail = ELEMENT_COUNT-1;
		
		// 꼭대기 찾음
		int highestElementIndex = tail;
		while (highestElementIndex > 0 &&
				elementArray[highestElementIndex-1] >= 
				elementArray[highestElementIndex]) {
			highestElementIndex--;
		}
		// 꼭대기가 맨 앞이라면 더 이상 만들 수 있는 순열이 없어요
		if (highestElementIndex == head)
			return false;
		// 내림차순이 깨지는 위치 값 보다 살짝 더 큰 값의 위치를 찾음
		int brokenDescendingElementIndex = highestElementIndex-1;
		int nextLargerThanBrokenElementIndex = tail;
		
		while (elementArray[brokenDescendingElementIndex] >= 
				elementArray[nextLargerThanBrokenElementIndex])
			nextLargerThanBrokenElementIndex--;
		swap(brokenDescendingElementIndex, nextLargerThanBrokenElementIndex);
		// 사전 순으로 정렬 (내림차순 -> 오름차순 변경)
		int fairIndex = tail;
		while (highestElementIndex < fairIndex) {
			swap(highestElementIndex, fairIndex);
			highestElementIndex++;
			fairIndex--;
		}
		return true;
	}
	
	private static void nextPermutation() {
		do {
			System.out.println(Arrays.toString(elementArray));
		} while (hasNext()); // 다음 순열을 찾을 수 있음?
	}
	
	public static void main(String[] args) {
		elementArray = new int[ELEMENT_COUNT];
//		elementArray = new int[] {0, 0, 1, 1}; // 조합 뒤에서부터 1로 채우기
		
		for (int elementIndex=0; elementIndex<ELEMENT_COUNT; elementIndex++) {
			elementArray[elementIndex] = elementIndex+1;
		}
		nextPermutation();
	}

}
