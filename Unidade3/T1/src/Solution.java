import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;

class Solution {

    public static void main(String[] args) throws Exception {
        if (args != null && args.length > 0 && args[0] != null && !args[0].isBlank()) {
            File file = new File(args[0]);
            if (file.exists()) {
                System.setIn(new FileInputStream(file));
            }
        }

        FastScanner in = new FastScanner();

        int n = in.nextInt();
        int e = in.nextInt();
        int p = in.nextInt();

        double[] x = new double[n];
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.nextDouble();
            y[i] = in.nextDouble();
        }

        EdgeWeightedGraph graph = new EdgeWeightedGraph(n);

        for (int i = 1; i < e; i++) {
            graph.addEdge(new Edge(0, i, 0.0));
        }

        for (int i = 0; i < p; i++) {
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
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

        PrimMST mst = new PrimMST(graph);
        System.out.printf(Locale.US, "%.10f%n", mst.weight());
    }

    private static final class FastScanner {
        private final BufferedInputStream input = new BufferedInputStream(System.in);
        private final byte[] buffer = new byte[1 << 16];
        private int len = 0;
        private int ptr = 0;

        private int read() {
            if (ptr >= len) {
                try {
                    len = input.read(buffer);
                    ptr = 0;
                } catch (Exception e) {
                    return -1;
                }
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        private int skipSpaces() {
            int c;
            while ((c = read()) != -1) {
                if (c > ' ') return c;
            }
            return -1;
        }

        int nextInt() {
            int c = skipSpaces();
            int sign = 1;
            if (c == '-') {
                sign = -1;
                c = read();
            }
            int val = 0;
            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = read();
            }
            return val * sign;
        }

        double nextDouble() {
            int c = skipSpaces();
            int sign = 1;
            if (c == '-') {
                sign = -1;
                c = read();
            }
            long intPart = 0;
            while (c > ' ' && c != '.' && c != 'e' && c != 'E') {
                intPart = intPart * 10 + (c - '0');
                c = read();
            }
            double val = intPart;
            if (c == '.') {
                double div = 1.0;
                c = read();
                while (c > ' ' && c != 'e' && c != 'E') {
                    div *= 10.0;
                    val += (c - '0') / div;
                    c = read();
                }
            }
            if (c == 'e' || c == 'E') {
                int expSign = 1;
                int exp = 0;
                c = read();
                if (c == '-') {
                    expSign = -1;
                    c = read();
                } else if (c == '+') {
                    c = read();
                }
                while (c > ' ') {
                    exp = exp * 10 + (c - '0');
                    c = read();
                }
                val = val * Math.pow(10.0, expSign * exp);
            }
            return val * sign;
        }
    }

    private static final class Edge implements Comparable<Edge> {
        private final int v;
        private final int w;
        private final double weight;

        Edge(int v, int w, double weight) {
            this.v = v;
            this.w = w;
            this.weight = weight;
        }

        double weight() {
            return weight;
        }

        int either() {
            return v;
        }

        int other(int vertex) {
            return (vertex == v) ? w : v;
        }

        @Override
        public int compareTo(Edge that) {
            return Double.compare(this.weight, that.weight);
        }
    }

    private static final class EdgeWeightedGraph {
        private final int V;
        private final java.util.ArrayList<Edge>[] adj;

        @SuppressWarnings("unchecked")
        EdgeWeightedGraph(int V) {
            this.V = V;
            adj = (java.util.ArrayList<Edge>[]) new java.util.ArrayList[V];
            for (int i = 0; i < V; i++) adj[i] = new java.util.ArrayList<>();
        }

        int V() {
            return V;
        }

        void addEdge(Edge e) {
            int v = e.either();
            int w = e.other(v);
            adj[v].add(e);
            adj[w].add(e);
        }

        Iterable<Edge> adj(int v) {
            return adj[v];
        }
    }

    private static final class PrimMST {
        private final Edge[] edgeTo;
        private final double[] distTo;
        private final boolean[] marked;
        private final IndexMinPQ<Double> pq;

        PrimMST(EdgeWeightedGraph graph) {
            int n = graph.V();
            edgeTo = new Edge[n];
            distTo = new double[n];
            marked = new boolean[n];
            pq = new IndexMinPQ<>(n);
            for (int v = 0; v < n; v++) distTo[v] = Double.POSITIVE_INFINITY;

            for (int v = 0; v < n; v++) {
                if (!marked[v]) prim(graph, v);
            }
        }

        private void prim(EdgeWeightedGraph graph, int s) {
            distTo[s] = 0.0;
            pq.insert(s, 0.0);
            while (!pq.isEmpty()) {
                int v = pq.delMin();
                scan(graph, v);
            }
        }

        private void scan(EdgeWeightedGraph graph, int v) {
            marked[v] = true;
            for (Edge e : graph.adj(v)) {
                int w = e.other(v);
                if (marked[w]) continue;
                if (e.weight() < distTo[w]) {
                    distTo[w] = e.weight();
                    edgeTo[w] = e;
                    if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                    else pq.insert(w, distTo[w]);
                }
            }
        }

        double weight() {
            double sum = 0.0;
            for (Edge e : edgeTo) {
                if (e != null) sum += e.weight();
            }
            return sum;
        }
    }

    private static final class IndexMinPQ<Key extends Comparable<Key>> {
        private final int maxN;
        private int n;
        private final int[] pq;
        private final int[] qp;
        private final Key[] keys;

        @SuppressWarnings("unchecked")
        IndexMinPQ(int maxN) {
            this.maxN = maxN;
            n = 0;
            keys = (Key[]) new Comparable[maxN];
            pq = new int[maxN + 1];
            qp = new int[maxN];
            for (int i = 0; i < maxN; i++) qp[i] = -1;
        }

        boolean isEmpty() {
            return n == 0;
        }

        boolean contains(int i) {
            return qp[i] != -1;
        }

        void insert(int i, Key key) {
            qp[i] = ++n;
            pq[n] = i;
            keys[i] = key;
            swim(n);
        }

        int delMin() {
            int min = pq[1];
            exch(1, n--);
            sink(1);
            qp[min] = -1;
            keys[min] = null;
            pq[n + 1] = -1;
            return min;
        }

        void decreaseKey(int i, Key key) {
            keys[i] = key;
            swim(qp[i]);
        }

        private boolean greater(int i, int j) {
            return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
        }

        private void exch(int i, int j) {
            int swap = pq[i];
            pq[i] = pq[j];
            pq[j] = swap;
            qp[pq[i]] = i;
            qp[pq[j]] = j;
        }

        private void swim(int k) {
            while (k > 1 && greater(k / 2, k)) {
                exch(k, k / 2);
                k /= 2;
            }
        }

        private void sink(int k) {
            while (2 * k <= n) {
                int j = 2 * k;
                if (j < n && greater(j, j + 1)) j++;
                if (!greater(k, j)) break;
                exch(k, j);
                k = j;
            }
        }
    }
}
