package base;

public class DisjointSet {
//	private static final int ELEMENT_COUNT = 5;
	private static int[] parentArray;
	private static int[] rankArray;
//	private static void make() {
//		parentArray = new int[ELEMENT_COUNT+1];
//		rankArray = new int[ELEMENT_COUNT+1];
//	}
	public static void union(int element1, int element2) {
		// 두 노드를 하나의 집합으로 묶음
		// 각 부모를 찾아옴
		int e1Parent = find(element1);
		int e2Parent = find(element2);
		// 동일 조상 -> 동일 그룹에 속했다는 것
		if (e1Parent == e2Parent) return;
		// 두 집합의 랭크 판단
		if (rankArray[e1Parent] > rankArray[e2Parent]) {
			parentArray[e2Parent] = e1Parent; // 방향 잘 확인하쇼
			return;
		}
		// e1보다 e2의 랭크가 더 큰 경우 -> e1은 e2 밑으로 들어감
		parentArray[e1Parent] = e2Parent;
		if (rankArray[e1Parent] == rankArray[e2Parent])
			rankArray[e2Parent]++;
	}
	public static int find(int element) {
		// 부모를 찾아오는 메소드
		if (parentArray[element] == element) return element;
		return parentArray[element] = find(parentArray[element]);
		// 경로 압축
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
