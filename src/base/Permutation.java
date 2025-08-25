package base;

public class Permutation {
	
	static int SELECT_COUNT = 3;
	static int ELEMENT_COUNT = 2;

	static int[] elementArray; // 원소 배열
	static int[] selectElementArray; // 선택해서 담을 배열
	static boolean[] isSelected; // 특정 원소를 선택했는지 확인하는 배열

	public static void main(String[] args) {
		
		elementArray = new int[ELEMENT_COUNT];
		selectElementArray = new int[SELECT_COUNT];
		isSelected = new boolean[ELEMENT_COUNT];
		
		for (int elementIndex=0; elementIndex<ELEMENT_COUNT; elementIndex++)
			elementArray[elementIndex] = elementIndex+1;
		
		permutation(0);
	}

	private static void permutation(int selectIndex) {
		
		// 기저 조건 (종료 조건)
		// 내가 모두 선택했다면 종료
		if (selectIndex == SELECT_COUNT) {
			for (int index=0; index<SELECT_COUNT; index++)
				System.out.print(selectElementArray[index]+" ");
			System.out.println();
			return;
		}
		for (int elementIndex=0; elementIndex<ELEMENT_COUNT; elementIndex++) {
			if (isSelected[elementIndex]) continue;
			selectElementArray[selectIndex] = elementArray[elementIndex];
			isSelected[elementArray[elementIndex]] = true;
			permutation(selectIndex+1);
			isSelected[elementArray[elementIndex]] = false;
		}
	}

}
