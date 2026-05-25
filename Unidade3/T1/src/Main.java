import java.util.Locale;
import algs4.Edge;
import algs4.EdgeWeightedGraph;
import algs4.KruskalMST;
import algs4.StdIn;

public class Main {

    public static void main(String[] args) throws Exception {
        java.io.File file = new java.io.File("dados/dados02.txt");
        if (file.exists()) {
            System.setIn(new java.io.FileInputStream(file));
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
