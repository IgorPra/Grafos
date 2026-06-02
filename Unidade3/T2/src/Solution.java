import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;

public class Solution {

    public static void main(String[] args) throws Exception {
        File file = new File("dados/dados02.txt");
        if (file.exists()) {
            System.setIn(new FileInputStream(file));
        }

        int n = StdIn.readInt();
        int e = StdIn.readInt();
        int p = StdIn.readInt();

        double[] x = new double[n];
        double[] y = new double[n];

        for (int i = 0; i < n; i++) {
            x[i] = StdIn.readDouble();
            y[i] = StdIn.readDouble();
        }

        EdgeWeightedGraph graph = new EdgeWeightedGraph(n);

        for (int i = 1; i < e; i++) {
            graph.addEdge(new Edge(0, i, 0.0));
        }

        for (int i = 0; i < p; i++) {
            int a = StdIn.readInt() - 1;
            int b = StdIn.readInt() - 1;
            graph.addEdge(new Edge(a, b, 0.0));
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = x[i] - x[j];
                double dy = y[i] - y[j];
                double dist = Math.sqrt(dx * dx + dy * dy);
                graph.addEdge(new Edge(i, j, dist));
            }
        }

        KruskalMST mst = new KruskalMST(graph);

        System.out.printf(Locale.US, "%.10f%n", mst.weight());
    }
}


class StdIn {
    private static final Scanner scanner = new Scanner(System.in);
    static {
        scanner.useLocale(Locale.US);
    }
    public static int readInt() {
        return scanner.nextInt();
    }
    public static double readDouble() {
        return scanner.nextDouble();
    }
}

class Edge implements Comparable<Edge> {
    private final int v;
    private final int w;
    private final double weight;

    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    public int either() {
        return v;
    }

    public int other(int vertex) {
        if (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Endpoint inválido");
    }

    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.weight, that.weight);
    }
}

class EdgeWeightedGraph {
    private final int V;
    private int E;
    private Bag<Edge>[] adj;

    @SuppressWarnings("unchecked")
    public EdgeWeightedGraph(int V) {
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Edge>();
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public Iterable<Edge> adj(int v) {
        return adj[v];
    }

    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        for (int v = 0; v < V; v++) {
            for (Edge e : adj[v]) {
                if (e.other(v) > v) {
                    list.add(e);
                }
            }
        }
        return list;
    }
}

class UF {
    private int[] parent;
    private byte[] rank;
    private int count;

    public UF(int n) {
        if (n < 0) throw new IllegalArgumentException();
        count = n;
        parent = new int[n];
        rank = new byte[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int p) {
        while (p != parent[p]) {
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    public int count() {
        return count;
    }

    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;
        if (rank[rootP] < rank[rootQ]) parent[rootP] = rootQ;
        else if (rank[rootP] > rank[rootQ]) parent[rootQ] = rootP;
        else {
            parent[rootQ] = rootP;
            rank[rootP]++;
        }
        count--;
    }
}

class Queue<Item> implements Iterable<Item> {
    private final List<Item> list = new ArrayList<>();
    public void enqueue(Item item) {
        list.add(item);
    }
    public int size() {
        return list.size();
    }
    @Override
    public Iterator<Item> iterator() {
        return list.iterator();
    }
}

class Bag<Item> implements Iterable<Item> {
    private final List<Item> list = new ArrayList<>();
    public void add(Item item) {
        list.add(item);
    }
    public int size() {
        return list.size();
    }
    @Override
    public Iterator<Item> iterator() {
        return list.iterator();
    }
}

class KruskalMST {
    private double weight;
    private final Queue<Edge> mst = new Queue<>();

    public KruskalMST(EdgeWeightedGraph G) {
        Edge[] edges = new Edge[G.E()];
        int t = 0;
        for (Edge e : G.edges()) {
            edges[t++] = e;
        }
        Arrays.sort(edges);

        UF uf = new UF(G.V());
        for (int i = 0; i < G.E() && mst.size() < G.V() - 1; i++) {
            Edge e = edges[i];
            int v = e.either();
            int w = e.other(v);
            if (uf.find(v) != uf.find(w)) {
                uf.union(v, w);
                mst.enqueue(e);
                weight += e.weight();
            }
        }
    }

    public Iterable<Edge> edges() {
        return mst;
    }

    public double weight() {
        return weight;
    }
}
