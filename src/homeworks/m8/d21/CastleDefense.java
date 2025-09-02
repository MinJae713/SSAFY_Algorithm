package homeworks.m8.d21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 궁수가 배치될 수 있는 위치 조합 생성
 * 2. 게임에 사용되는 필드(gameField), 잡은 적의 수(catched), 현재 적 수(gameEnemyCount) 초기화
 * 3. 궁수 배치 완료시, 궁수의 행 위치가 행개수 범위 내에 있고, 
 * 		적의 수(enemyCount)가 최소 하나 이상 있는 경우 반복
 * 	3.1. 궁수의 행을 rowIndex에 배치
 * 	3.2. 각 궁수마다 공격할 적 위치 확인 - 각 궁수마다 가장 가까운 적을 선택해서, targetEnemy에 추가해야 함
 * 		3.2.1. 열의 수 만큼 반복(archery), 현재 열 값이 0이라면 continue
 * 		3.2.2. 필드의 rowIndex-1 이하 행의 모든 요소에 대해 반복 (enemyRow, enemyColumn)
 * 		3.2.3. enemyRow, enemyColumn 위치 값이 0이면 continue, 
 * 		3.2.4. 적 간 거리 계산
 * 		3.2.5. 거리가 limitBound 보다 크다면 continue
 * 		3.2.6. 적간 거리가 최소 거리(minimumDistance)보다 크거나 같으면 continue
 * 			3.2.6.1. 같은 경우: 거리가 같지만 열이 다른 경우로, 먼저 탐색을 시작한 열(왼쪽)이 우선이 됨
 * 		3.2.7. minimumDistance보다 작다면 거리 입력 및 해당 적의 위치를 minimumEnemyPosition에 입력
 * 		3.2.8. 현재 궁수에서 최소 거리의 적을 확인한 이후 -> minimumEnemyPosition을 targetEnemy에 추가
 * 			3.2.8.1. 적을 못찾았다면 추가하지 않음
 * 	3.3. 적 위치 찾아서 적 공격
 * 		3.3.1. 찾은 적 위치 0으로 변경, 남아있는 적의 수 감소, 잡은 적의 수 1 증가
 * 		3.3.2. 이미 다른 궁수가 잡은 적이라면 실행하지 않음
 * 	3.4. rowIndex-1 위치에 남아있는 적의 수 만큼 enemyCount 감소
 * 	3.5. 찾은 적 위치 초기화
 * 4. catched와 가장 많이 잡은 적의 수(maxCatched) 비교 -> 값 변경
 */
public class CastleDefense {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int rowCount;
	private static int columnCount;
	private static int limitBound;
	private static int[][] originField;
	private static int enemyCount;
	private static int[] archeryPosition;
	private static List<int[]> targetEnemy;
	private static int maxCatched;
	private static int archeryCount;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		reader = new BufferedReader(new InputStreamReader(System.in));
		initialize();
		// logic
		setArcheryPosition(0, 0);
		System.out.println(maxCatched);
		reader.close();
	}

	private static void setArcheryPosition(int column, int count) {
		if (count == archeryCount) {
			doGame();
			return;
		}
		for (int columnIndex=column; columnIndex<columnCount; columnIndex++) {
			archeryPosition[columnIndex] = 2;
			setArcheryPosition(columnIndex+1, count+1);
			archeryPosition[columnIndex] = 0;
		}
	}

	private static void doGame() {
		// 조합 완성 - 게임 진행
		int[][] gameField = copyFromOrigin();
		int catched = 0;
		int gameEnemyCount = enemyCount;
		for (int rowIndex=rowCount; rowIndex>0 && gameEnemyCount>0; rowIndex--) {
			gameField[rowIndex] = archeryPosition;
			for (int archery=0; archery<columnCount; archery++) {
				if (gameField[rowIndex][archery] == 0) continue;
				double minimumDistance = Double.MAX_VALUE;
				int[] minimumEnemyPosition = null;
				for (int enemyColumn=0; enemyColumn<columnCount; enemyColumn++)
					for (int enemyRow=rowIndex-1; enemyRow>=0; enemyRow--) {
						if (gameField[enemyRow][enemyColumn] == 0) 
							continue;
						double enemyDistance = Math.abs(rowIndex-enemyRow)+Math.abs(archery-enemyColumn);
						if (enemyDistance > limitBound || enemyDistance >= minimumDistance) 
							continue;
						minimumDistance = enemyDistance;
						minimumEnemyPosition = new int[] {enemyRow, enemyColumn};
					}
				if (minimumEnemyPosition == null) continue;
				targetEnemy.add(minimumEnemyPosition);
			}
			for (int[] enemyPosition : targetEnemy) {
				int enemyRow = enemyPosition[0];
				int enemyColumn = enemyPosition[1];
				if (gameField[enemyRow][enemyColumn] == 0) continue;
				gameField[enemyRow][enemyColumn] = 0;
				gameEnemyCount--;
				catched++;
			}
			for (int columnIndex=0; columnIndex<columnCount; columnIndex++)
				gameEnemyCount -= gameField[rowIndex-1][columnIndex];
			targetEnemy.clear();
		}
		maxCatched = Math.max(maxCatched, catched);
	}

	private static int[][] copyFromOrigin() {
		int[][] gameField = new int[rowCount+1][columnCount];
		for (int row=0; row<rowCount; row++)
			for (int column=0; column<columnCount; column++)
				gameField[row][column] = originField[row][column];
		return gameField;
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		rowCount = Integer.parseInt(tokenizer.nextToken());
		columnCount = Integer.parseInt(tokenizer.nextToken());
		limitBound = Integer.parseInt(tokenizer.nextToken());
		originField = new int[rowCount+1][columnCount];
		enemyCount = 0;
		for (int row=0; row<rowCount; row++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			for (int column=0; column<columnCount; column++) {
				originField[row][column] = Integer.parseInt(tokenizer.nextToken());
				enemyCount += originField[row][column];
			}
		}
		archeryPosition = new int[columnCount];
		targetEnemy = new ArrayList<int[]>();
		maxCatched = Integer.MIN_VALUE;
		archeryCount = 3;
	}

}
