import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.LinkedList;

public class Solution {
    private static final double FLOATING_POINT_EPSILON = 1.0E-10;

    public static void main(String[] args) throws Exception {
        File file = new File("dados/dados02.txt");
        if (file.exists()) {
            System.setIn(new FileInputStream(file));
        }

        int n = StdIn.readInt();
        int m = StdIn.readInt();
        int k = StdIn.readInt();

        Graph graph = new Graph(n + m);

        for (int i = 0; i < k; i++) {
            int a = StdIn.readInt();
            int b = StdIn.readInt();
            int b_m = b + n;

            graph.addEdge(a - 1, b_m - 1);
        }

        solve(graph, n, m);
    }

    public static void solve(Graph graph, int n, int m) {
        int V = n + m + 2;
        int S = n + m;
        int T = n + m + 1;

        FlowNetwork flowNetwork = new FlowNetwork(V);

        for (int i = 0; i < n; i++) {
            flowNetwork.addEdge(new FlowEdge(S, i, 1.0));
        }

        for (int j = n; j < n + m; j++) {
            flowNetwork.addEdge(new FlowEdge(j, T, 1.0));
        }

        for (int v = 0; v < n; v++) {
            for (int w : graph.adj(v)) {
                if (w >= n && w < n + m) {
                    flowNetwork.addEdge(new FlowEdge(v, w, 1.0));
                }
            }
        }

        double maxFlow = 0.0;
        while (true) {
            FlowEdge[] edgeTo = new FlowEdge[V];
            boolean[] visited = new boolean[V];
            Queue<Integer> queue = new Queue<>();

            queue.enqueue(S);
            visited[S] = true;

            while (!queue.isEmpty() && !visited[T]) {
                int v = queue.dequeue();
                for (FlowEdge e : flowNetwork.adj(v)) {
                    int w = e.other(v);
                    if (!visited[w] && e.residualCapacityTo(w) > FLOATING_POINT_EPSILON) {
                        edgeTo[w] = e;
                        visited[w] = true;
                        queue.enqueue(w);
                    }
                }
            }

            if (!visited[T]) {
                break;
            }

            double bottleneck = Double.POSITIVE_INFINITY;
            for (int v = T; v != S; v = edgeTo[v].other(v)) {
                bottleneck = Math.min(bottleneck, edgeTo[v].residualCapacityTo(v));
            }

            for (int v = T; v != S; v = edgeTo[v].other(v)) {
                edgeTo[v].addResidualFlowTo(v, bottleneck);
            }

            maxFlow += bottleneck;
        }

        System.out.println((int) Math.round(maxFlow));

        for (int v = 0; v < n; v++) {
            for (FlowEdge e : flowNetwork.adj(v)) {
                if (e.from() == v && e.to() >= n && e.to() < n + m && e.flow() > FLOATING_POINT_EPSILON) {
                    int w = e.to();
                    System.out.println((v + 1) + " " + (w - n + 1));
                }
            }
        }
    }
}

class StdIn {
    private static BufferedReader reader;
    private static StringTokenizer tokenizer;

    private static void init() {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
    }

    public static String next() {
        init();
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            try {
                String line = reader.readLine();
                if (line == null)
                    return null;
                tokenizer = new StringTokenizer(line);
            } catch (Exception e) {
                throw new NoSuchElementException(e.getMessage());
            }
        }
        return tokenizer.nextToken();
    }

    public static int readInt() {
        String s = next();
        if (s == null)
            throw new NoSuchElementException();
        return Integer.parseInt(s);
    }
}

class Queue<Item> {
    private final LinkedList<Item> list = new LinkedList<>();

    public void enqueue(Item item) {
        list.addLast(item);
    }

    public Item dequeue() {
        return list.removeFirst();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }
}

class Bag<Item> implements Iterable<Item> {
    private final LinkedList<Item> list = new LinkedList<>();

    public void add(Item item) {
        list.addFirst(item);
    }

    public int size() {
        return list.size();
    }

    @Override
    public Iterator<Item> iterator() {
        return list.iterator();
    }
}

class Graph {
    private final int V;
    private int E;
    private final Bag<Integer>[] adj;

    @SuppressWarnings("unchecked")
    public Graph(int V) {
        if (V < 0)
            throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    public Iterable<Integer> adj(int v) {
        return adj[v];
    }
}

class FlowEdge {
    private final int v;
    private final int w;
    private final double capacity;
    private double flow;

    public FlowEdge(int v, int w, double capacity) {
        this.v = v;
        this.w = w;
        this.capacity = capacity;
        this.flow = 0.0;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public double capacity() {
        return capacity;
    }

    public double flow() {
        return flow;
    }

    public int other(int vertex) {
        if (vertex == v)
            return w;
        else if (vertex == w)
            return v;
        else
            throw new IllegalArgumentException("invalid endpoint");
    }

    public double residualCapacityTo(int vertex) {
        if (vertex == v)
            return flow;
        else if (vertex == w)
            return capacity - flow;
        else
            throw new IllegalArgumentException("invalid endpoint");
    }

    public void addResidualFlowTo(int vertex, double delta) {
        if (vertex == v)
            flow -= delta;
        else if (vertex == w)
            flow += delta;
        else
            throw new IllegalArgumentException("invalid endpoint");

        if (Math.abs(flow) <= 1.0E-10)
            flow = 0;
        if (Math.abs(flow - capacity) <= 1.0E-10)
            flow = capacity;
    }

    public String toString() {
        return v + "->" + w + " " + flow + "/" + capacity;
    }
}

class FlowNetwork {
    private final int V;
    private int E;
    private final Bag<FlowEdge>[] adj;

    @SuppressWarnings("unchecked")
    public FlowNetwork(int V) {
        if (V < 0)
            throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.E = 0;
        adj = (Bag<FlowEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<FlowEdge>();
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(FlowEdge e) {
        int v = e.from();
        int w = e.to();
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public Iterable<FlowEdge> adj(int v) {
        return adj[v];
    }

    public Iterable<FlowEdge> edges() {
        Bag<FlowEdge> list = new Bag<FlowEdge>();
        for (int v = 0; v < V; v++)
            for (FlowEdge e : adj[v]) {
                if (e.to() != v)
                    list.add(e);
            }
        return list;
    }
}
