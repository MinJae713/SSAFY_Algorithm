package m8.d6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class Wedding {
	private static BufferedReader reader;
	private static StringTokenizer tokenizer;
	private static int friendCount;
	private static boolean[][] relation;
	private static int inviteCount;
	private static boolean[] visited;

	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("input.txt"));
		reader = new BufferedReader(new InputStreamReader(System.in));
		friendCount = Integer.parseInt(reader.readLine().trim());
		relation = new boolean[friendCount+1][friendCount+1];
		inviteCount = 0;
		visited = new boolean[friendCount+1];
		int relationCount = Integer.parseInt(reader.readLine().trim());
		for (int index=0; index<relationCount; index++) {
			tokenizer = new StringTokenizer(reader.readLine().trim());
			int friendOne = Integer.parseInt(tokenizer.nextToken());
			int friendTwo = Integer.parseInt(tokenizer.nextToken());
			relation[friendOne][friendTwo] = true;
			relation[friendTwo][friendOne] = true;
		}
		inviteFriendsBFS();
		System.out.println(inviteCount);
		reader.close();
	}
	
	private static void inviteFriendsBFS() {
		Deque<Integer> friendVisit = new ArrayDeque<Integer>();
		Deque<Integer> friendDepth = new ArrayDeque<Integer>();
		friendVisit.offer(1);
		friendDepth.offer(0);
		while (!friendVisit.isEmpty()) {
			int friend = friendVisit.poll();
			int depth = friendDepth.poll();
			if (visited[friend]) continue;
			visited[friend] = true;
			if (friend != 1 && depth <= 2) inviteCount++;
			for (int friendIndex=1; friendIndex<=friendCount; friendIndex++)
				if (relation[friend][friendIndex] && !visited[friendIndex]) {
					friendVisit.offer(friendIndex);
					friendDepth.offer(depth+1);
				}
		}
	}
}
