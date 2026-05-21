import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Main {

    static class FastScanner {
        private final BufferedInputStream in = new BufferedInputStream(System.in);
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0;
        private int len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) {
                    return -1;
                }
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = read();
            } while (c <= ' ' && c != -1);

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

        double nextDouble() throws IOException {
            int c;
            do {
                c = read();
            } while (c <= ' ' && c != -1);

            int sign = 1;
            if (c == '-') {
                sign = -1;
                c = read();
            }

            double val = 0.0;
            while (c > ' ' && c != '.' && c != 'e' && c != 'E') {
                val = val * 10.0 + (c - '0');
                c = read();
            }

            if (c == '.') {
                double scale = 1.0;
                c = read();
                while (c > ' ' && c != 'e' && c != 'E') {
                    scale *= 0.1;
                    val += (c - '0') * scale;
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
                val = val * Math.pow(10, expSign * exp);
            }

            return sign * val;
        }
    }

    static class UF {
        private final int[] parent;
        private final int[] size;

        UF(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int p) {
            while (p != parent[p]) {
                parent[p] = parent[parent[p]];
                p = parent[p];
            }
            return p;
        }

        boolean connected(int a, int b) {
            return find(a) == find(b);
        }

        boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) {
                return false;
            }
            if (size[ra] < size[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
            }
            return true;
        }
    }

    static class Edge implements Comparable<Edge> {
        final int u;
        final int v;
        final double w;

        Edge(int u, int v, double w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.w, other.w);
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();

        int n = fs.nextInt();
        int e = fs.nextInt();
        int p = fs.nextInt();

        double[] x = new double[n];
        double[] y = new double[n];

        for (int i = 0; i < n; i++) {
            x[i] = fs.nextDouble();
            y[i] = fs.nextDouble();
        }

        UF uf = new UF(n);

        // As primeiras e treehouses est?o conectadas gratuitamente.
        for (int i = 1; i < e; i++) {
            uf.union(0, i);
        }

        // Conex?es j? existentes da entrada tamb?m s?o gratuitas.
        for (int i = 0; i < p; i++) {
            int a = fs.nextInt() - 1;
            int b = fs.nextInt() - 1;
            uf.union(a, b);
        }

        ArrayList<Edge> edges = new ArrayList<>(n * (n - 1) / 2);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = x[i] - x[j];
                double dy = y[i] - y[j];
                double dist = Math.sqrt(dx * dx + dy * dy);
                edges.add(new Edge(i, j, dist));
            }
        }

        Collections.sort(edges);

        double answer = 0.0;
        for (Edge edge : edges) {
            if (uf.union(edge.u, edge.v)) {
                answer += edge.w;
            }
        }

        System.out.printf(Locale.US, "%.10f%n", answer);
    }
}
