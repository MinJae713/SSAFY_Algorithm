package homeworks.m9.d4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * [문제 풀이]
 * 1. 고객 별 방문 시간 입력 받아서 각 시간 별 방문 고객 연결 리스트 입력 (VisitUser[])
 * 2. 처리 고객 수(finished) 0으로 초기화
 * 3. 시간(time)을 1 증가시키며 반복, 처리 고객 수가 총 고객 수 보다 작은 동안 실행
 * 4. time이 마지막 고객이 방문하는 시간보다 작은 경우, 
 * 	time 시간에 방문하는 고객 번호 접수 대기 큐(acceptWaitQueue)에 입력
 * 	4.1. 입력 시 VisitUser를 Proceed로 변환
 * 5. 정비 처리 배열(repairProceed)의 각 요소에 대해 반복(repairIndex, 1부터 시작)
 * 	5.1. repairIndex 위치 정비 처리 정보가 null이거나, 
 * 		  repairIndex 위치 정비 처리 정보의 종료 시간(finishTime)이 현재 시간과 다르면 continue
 * 	5.2. repairIndex 위치 정비 처리 정보의 접수 창구 번호(acceptIndex)와 
 * 		  찾으려는 접수 창구 번호와 동일하고, repairIndex와 찾으려는 정비 창구 번호와 같다면?
 * 		5.2.1. 해당 정비 처리 정보의 고객 번호(userNumber)를 고객 번호 총합에 합산
 * 	5.3. repairIndex 위치 정비 처리 정보 null로 지정
 * 	5.4. 비어있는 정비 창구 수(emptyRepairCount) 1 증가
 * 	5.5. finished 1 증가, 증가한 결과가 총 고객 수와 동일하면 반복 종료
 * 6. 비어있는 정비 창구 수가 0보다 크고, 정비 대기 큐에 요소가 있다면? -> 번호가 작은 정비 창구 부터 입력
 * 	6.1. 정비 창구 수 만큼 반복(1부터 시작), 해당 위치 정비 창구가 null아니면 continue
 * 	6.2. null이면 정비 대기 큐(repairWaitQueue)에서 poll (proceed)
 * 	6.3. proceed의 finishTime은 현재 시간(time)에 해당 위치 정비 창구의 소요 시간을 더한 값으로 입력
 * 	6.4. proceed를 해당 위치 정비 창구에 입력
 * 	6.5. 비어있는 정비 창구 수 1 감소
 * 7. 접수 처리 배열(acceptProceed)의 각 요소에 대해 반복(acceptIndex, 1부터 시작)
 * 	7.1. acceptIndex 위치 접수 처리 정보가 null이거나 
 * 		  acceptIndex 위치 접수 처리 정보의 종료 시간(finishTime)이 현재 시간과 다르면 continue
 * 	7.2. 비어있는 정비 창구 수(emptyRepairCount)가 0보다 크다면?
 * 		7.2.1. acceptIndex 위치 접수 처리 정보를 정비 창구에 입력(6번 과정 동일)
 * 	7.3. 비어있는 정비 창구 수(emptyRepairCount)가 0이라면?
 * 		7.3.1. acceptIndex 위치 접수 처리 정보 정비 대기 큐에 입력
 * 	7.4. acceptIndex 위치 접수 처리 정보 null로 지정
 * 	7.5. 비어있는 접수 창구 수(emptyAcceptCount) 1 증가
 * 8. 비어있는 접수 창구 수가 0보다 크고, 접수 대기 큐에 요소가 있다면? - 번호가 작은 접수 창구 부터 입력
 * 	8.1. 접수 창구 수 만큼 반복(1부터 시작), 해당 위치 접수 창구가 null이 아니면 continue
 * 	8.2. null이면 접수 대기 큐(acceptWaitQueue)에서 poll (proceed)
 * 	8.3. proceed의 접수 창구 번호(acceptIndex)는 해당 위치 접수 창구 번호로 입력
 * 	8.4. proceed의 finishTime은 현재 시간(time)에 해당 위치 접수 창구의 소요 시간을 더한 값으로 입력
 * 	8.5. proceed를 해당 위치 접수 창구에 입력
 * 	8.6. 비어있는 접수 창구 수 1 감소 및 break
 *
 */
public class CarRepair {
	static class VisitUser {
		int number;
		VisitUser next;
		public VisitUser(int number) {
			super();
			this.number = number;
			this.next = null;
		}
	}
	static class Proceed {
		int number;
		int finishTime;
		int acceptIndex;
		public Proceed(int number) {
			super();
			this.number = number;
		}
		@Override
		public String toString() {
			return String.format("[번호:%d, 종료:%d, 접수 창구:%d]", number, finishTime, acceptIndex);
		}
	}
	private static BufferedReader reader;
	private static StringBuilder builder;
	private static StringTokenizer tokenizer;
	private static int acceptCount;
	private static int repairCount;
	private static int userCount;
	private static int targetAccept;
	private static int targetRepair;
	private static int[] acceptTime; // 1부터 시작
	private static int[] repairTime; // 1부터 시작
	private static VisitUser[] userVisitTime;
	private static int totalUserNumber; // 1부터 시작
	private static int visitStart;
	private static int visitEnd;
	
	private static Queue<Proceed> acceptWaitQueue;
	private static Queue<Proceed> repairWaitQueue;
	private static Proceed[] acceptProceed;
	private static int emptyAcceptCount;
	private static Proceed[] repairProceed;
	private static int emptyRepairCount;
	
	public static void main(String[] args) throws IOException {
//		System.setIn(new FileInputStream("resources/sample_input.txt"));
		int testCaseCount = initializeTest();
		for (int testCase=1; testCase<=testCaseCount; testCase++) {
			initialize();
			// logic
			int finished = 0;
			for (int time=visitStart; finished<userCount; time++) {
				if (time <= visitEnd)
					for (VisitUser user=userVisitTime[time]; user!=null; user=user.next)
						acceptWaitQueue.offer(new Proceed(user.number));
				
				// 정비 처리 완료된 고객 확인
				for (int repairIndex=1; repairIndex<=repairCount; repairIndex++) {
					Proceed proceed = repairProceed[repairIndex];
					if (proceed == null || 
							proceed.finishTime != time) continue;
					else if (proceed.acceptIndex == targetAccept && 
							repairIndex == targetRepair)
						totalUserNumber += proceed.number;
					repairProceed[repairIndex] = null;
					emptyRepairCount++;
					finished++;
				}
				if (finished == userCount) continue;
				
				// 정비 대기 큐에서 정비 처리로 입력
				for (int repairIndex=1; emptyRepairCount>0 && 
						!repairWaitQueue.isEmpty() &&
						repairIndex<=repairCount; repairIndex++) {
					if (repairProceed[repairIndex] != null) continue;
					Proceed proceed = repairWaitQueue.poll();
					proceed.finishTime = time+repairTime[repairIndex];
					repairProceed[repairIndex] = proceed;
					emptyRepairCount--;
				}
				
				// 접수 처리 완료된 고객 확인
				for (int acceptIndex=1; acceptIndex<=acceptCount; acceptIndex++) {
					Proceed proceed = acceptProceed[acceptIndex];
					if (proceed == null || 
							proceed.finishTime != time) continue;
					else if (emptyRepairCount > 0) {
						// 정비 창구에 자리가 있는 상황 -> 큐에는 요소가 없기에 정비 창구로 바로 입력
						for (int repairIndex=1; repairIndex<=repairCount; repairIndex++) {
							if (repairProceed[repairIndex] != null) continue;
							proceed.finishTime = time+repairTime[repairIndex];
							repairProceed[repairIndex] = proceed;
							emptyRepairCount--;
							break;
						}
					} else if (emptyRepairCount == 0)
						repairWaitQueue.offer(proceed);
					acceptProceed[acceptIndex] = null;
					emptyAcceptCount++;
				}
				
				// 접수 대기 큐에서 접수 처리로 입력
				for (int acceptIndex=1; emptyAcceptCount>0 && 
						!acceptWaitQueue.isEmpty() && 
						acceptIndex<=acceptCount; acceptIndex++) {
					if (acceptProceed[acceptIndex] != null) continue;
					Proceed proceed = acceptWaitQueue.poll();
					proceed.acceptIndex = acceptIndex;
					proceed.finishTime = time+acceptTime[acceptIndex];
					acceptProceed[acceptIndex] = proceed;
					emptyAcceptCount--;
				}
				// 고객들 접수 대기 큐에 입력
			}
			
			builder.append("#").append(testCase).append(" ").
					append(totalUserNumber == 0 ? -1 : totalUserNumber).append("\n");
		}
		System.out.print(builder);
		reader.close();
	}

	private static int initializeTest() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		builder = new StringBuilder();
		return Integer.parseInt(reader.readLine().trim());
	}

	private static void initialize() throws IOException {
		tokenizer = new StringTokenizer(reader.readLine().trim());
		acceptCount = Integer.parseInt(tokenizer.nextToken());
		repairCount = Integer.parseInt(tokenizer.nextToken());
		userCount = Integer.parseInt(tokenizer.nextToken());
		targetAccept = Integer.parseInt(tokenizer.nextToken());
		targetRepair = Integer.parseInt(tokenizer.nextToken());
		acceptTime = new int[acceptCount+1];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int accept=1; accept<=acceptCount; accept++)
			acceptTime[accept] = Integer.parseInt(tokenizer.nextToken());
		repairTime = new int[repairCount+1];
		tokenizer = new StringTokenizer(reader.readLine().trim());
		for (int repair=1; repair<=repairCount; repair++)
			repairTime[repair] = Integer.parseInt(tokenizer.nextToken());
		tokenizer = new StringTokenizer(reader.readLine().trim());
		int[] timeArray = new int[userCount+1];
		for (int user=1; user<=userCount; user++)
			timeArray[user] = Integer.parseInt(tokenizer.nextToken());
		visitStart = timeArray[1];
		visitEnd = timeArray[userCount];
		userVisitTime = new VisitUser[visitEnd+1]; // 시간 최대 크기여야 함!
		VisitUser lastUser = null;
		for (int user=1; user<=userCount; user++) {
			int time = timeArray[user];
			// 고객은 연결리스트 꼬리에 붙임
			if (userVisitTime[time] == null) {
				userVisitTime[time] = new VisitUser(user);
				lastUser = userVisitTime[time];
			} else {
				lastUser.next = new VisitUser(user);
				lastUser = lastUser.next;
			}
		}
		totalUserNumber = 0;
		acceptWaitQueue = new ArrayDeque<Proceed>();
		repairWaitQueue = new ArrayDeque<Proceed>();
		acceptProceed = new Proceed[acceptCount+1];
		emptyAcceptCount = acceptCount;
		repairProceed = new Proceed[repairCount+1];
		emptyRepairCount = repairCount;
	}
}
