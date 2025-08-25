package base;

public class Combination {
	
	static int SELECT_COUNT = 3;
	static int ELEMENT_COUNT = 2;

	static int[] elementArray; // 원소 배열
	static int[] selectElementArray; // 선택해서 담을 배열

	public static void main(String[] args) {
		elementArray = new int[ELEMENT_COUNT];
		selectElementArray = new int[SELECT_COUNT];
		
		for (int elementIndex=0; elementIndex<ELEMENT_COUNT; elementIndex++)
			elementArray[elementIndex] = elementIndex+1;
	}
	public static void combination(int elementIndex, int selectIndex) {
		// 1. 기저 조건 (종료 조건)
		if (selectIndex == SELECT_COUNT) {
			// 다 선택했으면 선택한 것 출력
			// 출력 코드
			return;
		}
		if (elementIndex == ELEMENT_COUNT)
			return;
		
		// 2. 아직 원소를 다 안봤거나, 아직 다 선택하지 않았거나.
		
		// 선택, 복구
		selectElementArray[selectIndex] = elementArray[elementIndex];
		combination(elementIndex+1, selectIndex+1);
		
		selectElementArray[selectIndex] = 0; // 초기화
		combination(elementIndex+1, selectIndex);
		
	}

}
