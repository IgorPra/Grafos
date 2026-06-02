import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("dados/dados02.txt");
        if (file.exists()) {
            System.setIn(new FileInputStream(file));
        }

        FastScanner in = new FastScanner();
        StringBuilder out = new StringBuilder();

        int tests = in.nextInt();
        for (int test = 0; test < tests; test++) {
            int n = in.nextInt();
            HashMap<String, Integer> cityIndex = new HashMap<>(n * 2);
            ArrayList<Edge>[] graph = createGraph(n);

            for (int city = 0; city < n; city++) {
                cityIndex.put(in.next(), city);

                int neighbours = in.nextInt();
                for (int j = 0; j < neighbours; j++) {
                    int to = in.nextInt() - 1;
                    int cost = in.nextInt();
                    graph[city].add(new Edge(to, cost));
                }
            }

            int queries = in.nextInt();
            for (int query = 0; query < queries; query++) {
                int source = cityIndex.get(in.next());
                int target = cityIndex.get(in.next());
                out.append(dijkstra(graph, source, target)).append('\n');
            }
        }

        System.out.print(out);
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<Edge>[] createGraph(int n) {
        ArrayList<Edge>[] graph = (ArrayList<Edge>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        return graph;
    }

    private static int dijkstra(ArrayList<Edge>[] graph, int source, int target) {
        int[] dist = new int[graph.length];
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }

        PriorityQueue<State> pq = new PriorityQueue<>();
        dist[source] = 0;
        pq.add(new State(source, 0));

        while (!pq.isEmpty()) {
            State current = pq.poll();
            if (current.distance != dist[current.vertex]) {
                continue;
            }
            if (current.vertex == target) {
                return current.distance;
            }

            for (Edge edge : graph[current.vertex]) {
                int newDistance = current.distance + edge.cost;
                if (newDistance < dist[edge.to]) {
                    dist[edge.to] = newDistance;
                    pq.add(new State(edge.to, newDistance));
                }
            }
        }

        return dist[target];
    }
}

class Edge {
    final int to;
    final int cost;

    Edge(int to, int cost) {
        this.to = to;
        this.cost = cost;
    }
}

class State implements Comparable<State> {
    final int vertex;
    final int distance;

    State(int vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    @Override
    public int compareTo(State that) {
        return Integer.compare(this.distance, that.distance);
    }
}

class FastScanner {
    private final BufferedInputStream in = new BufferedInputStream(System.in);
    private final byte[] buffer = new byte[1 << 16];
    private int pointer = 0;
    private int length = 0;

    private int read() throws Exception {
        if (pointer >= length) {
            length = in.read(buffer);
            pointer = 0;
            if (length <= 0) {
                return -1;
            }
        }
        return buffer[pointer++];
    }

    String next() throws Exception {
        StringBuilder value = new StringBuilder();
        int c;
        do {
            c = read();
        } while (c <= ' ' && c != -1);

        while (c > ' ') {
            value.append((char) c);
            c = read();
        }

        return value.toString();
    }

    int nextInt() throws Exception {
        int c;
        do {
            c = read();
        } while (c <= ' ' && c != -1);

        int sign = 1;
        if (c == '-') {
            sign = -1;
            c = read();
        }

        int value = 0;
        while (c > ' ') {
            value = value * 10 + c - '0';
            c = read();
        }

        return value * sign;
    }
}
