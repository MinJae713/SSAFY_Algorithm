package testReview.testApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Test4_유민재 {
	static class House {
		int number;
		House next;
		public House(int number, House next) {
			super();
			this.number = number;
			this.next = next;
		}
	}
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int houseCount;
	private static House[] nearHouses;
	private static boolean[] affected; // 전력을 받는 집
	private static int totalAffected;
	private static int minCount;

	public static void main(String[] args) throws IOException {
		initialize();
		// logic
		for (int count=1; count<=houseCount && 
				minCount==Integer.MAX_VALUE; count++) {
			combinationHouse(0, 0, count);
		}
		System.out.println(minCount);
		reader.close();
	}

	private static void combinationHouse(int start, int index, int count) {
		if (index == count) {
			if (totalAffected == houseCount)
				minCount = count;
			return;
		}
		for (int house=start; house<=houseCount && 
				minCount==Integer.MAX_VALUE; house++) {
			if (!affected[house]) { // 내 집 영향받음
				affected[house] = true;
				totalAffected++;
			}
			for (House nextHouse=nearHouses[house]; // 인접 집 영향 받음
					nextHouse!=null; nextHouse=nextHouse.next)
				if (!affected[nextHouse.number]) {
					affected[nextHouse.number] = true;
					totalAffected++;
				}
			
			combinationHouse(house+1, index+1, count);
			
			if (affected[house]) { // 내 집 영향 해제
				affected[house] = false;
				totalAffected--;
			}
			for (House nextHouse=nearHouses[house]; // 인접 집 영향 해제
					nextHouse!=null; nextHouse=nextHouse.next)
				if (affected[nextHouse.number]) {
					affected[nextHouse.number] = false;
					totalAffected--;
				}
		}
	}

	private static void initialize() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		houseCount = Integer.parseInt(reader.readLine().trim());
		nearHouses = new House[houseCount+1];
		affected = new boolean[houseCount+1];
		totalAffected = 0;
		minCount = Integer.MAX_VALUE;
		for (int index=0; index<houseCount-1; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int fromHouse = Integer.parseInt(tokenizer.nextToken());
			int toHouse = Integer.parseInt(tokenizer.nextToken());
			nearHouses[fromHouse] = new House(toHouse, nearHouses[fromHouse]);
			nearHouses[toHouse] = new House(fromHouse, nearHouses[toHouse]);
		}
	}

}
