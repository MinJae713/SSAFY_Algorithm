package homeworks.m9.d2.monkeyLikeHorse;

import java.io.*;
import java.util.*;

public class MonkeyLikeHorseBFS_GPT {
    private static BufferedReader reader;
    private static StringTokenizer tokenizer;
    private static int[][] delta;
    private static int maxHorseCount;
    private static int width;
    private static int height;
    private static int[][] map;
    private static boolean[][][] visited;

    public static void main(String[] args) throws IOException {
        initialize();
        int result = bfs();
        System.out.println(result);
        reader.close();
    }

    private static int bfs() {
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(0, 0, 0, 0));
        visited[0][0][0] = true;

        while (!queue.isEmpty()) {
            Node cur = queue.poll();

            // 목적지 도착 시 즉시 반환 (BFS이므로 최단거리 보장)
            if (cur.x == width - 1 && cur.y == height - 1) {
                return cur.count;
            }

            // 말 이동 가능할 때
            if (cur.horseCount < maxHorseCount) {
                for (int i = 0; i < 8; i++) {
                    int ny = cur.y + delta[i][0];
                    int nx = cur.x + delta[i][1];
                    int nh = cur.horseCount + 1;
                    if (checkNext(nx, ny, nh)) {
                        visited[ny][nx][nh] = true;
                        queue.offer(new Node(nx, ny, cur.count + 1, nh));
                    }
                }
            }

            // 원숭이 일반 이동 (상하좌우)
            for (int i = 8; i < 12; i++) {
                int ny = cur.y + delta[i][0];
                int nx = cur.x + delta[i][1];
                int nh = cur.horseCount;
                if (checkNext(nx, ny, nh)) {
                    visited[ny][nx][nh] = true;
                    queue.offer(new Node(nx, ny, cur.count + 1, nh));
                }
            }
        }

        return -1; // 도달 불가능한 경우
    }

    private static boolean checkNext(int nextX, int nextY, int horseCount) {
        return 0 <= nextY && nextY < height &&
               0 <= nextX && nextX < width &&
               !visited[nextY][nextX][horseCount] &&
               map[nextY][nextX] != 1;
    }

    private static void initialize() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        maxHorseCount = Integer.parseInt(reader.readLine().trim());
        tokenizer = new StringTokenizer(reader.readLine().trim());
        width = Integer.parseInt(tokenizer.nextToken());
        height = Integer.parseInt(tokenizer.nextToken());
        map = new int[height][width];
        delta = new int[][]{
            {-2, 1}, {-1, 2}, {1, 2}, {2, 1},
            {2, -1}, {1, -2}, {-1, -2}, {-2, -1},
            {-1, 0}, {0, 1}, {1, 0}, {0, -1}
        };
        for (int row = 0; row < height; row++) {
            tokenizer = new StringTokenizer(reader.readLine().trim());
            for (int col = 0; col < width; col++) {
                map[row][col] = Integer.parseInt(tokenizer.nextToken());
            }
        }
        visited = new boolean[height][width][maxHorseCount + 1];
    }

    private static class Node {
        int x, y, count, horseCount;
        Node(int x, int y, int count, int horseCount) {
            this.x = x;
            this.y = y;
            this.count = count;
            this.horseCount = horseCount;
        }
    }
}
