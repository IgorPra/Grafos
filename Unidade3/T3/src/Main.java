import algs4.Bipartite;
import algs4.FlowEdge;
import algs4.FlowNetwork;
import algs4.Graph;
import algs4.Queue;
import algs4.StdIn;

public class Main {
  public static void main(String[] args) throws Exception {

    print();
    print("=".repeat(40));
    print();

    java.io.File file = new java.io.File("dados/dados02.txt");
    if (file.exists()) {
      System.setIn(new java.io.FileInputStream(file));
    }

    print("File path: " + file);

    print();
    print("=".repeat(40));
    print();

    int n = StdIn.readInt();
    int m = StdIn.readInt();
    int k = StdIn.readInt();

    Graph graph = new Graph(n + m);

    for (int _ : range(k)) {
      int a = StdIn.readInt();
      int b = StdIn.readInt();
      int b_m = b + n;

      graph.addEdge(a - 1, b_m - 1);
    }

    Bipartite bipartite = new Bipartite(graph);
    print(bipartite.isBipartite());
    solve(graph, n, m);
  }

  private static int[] range(int n) {
    int[] list = new int[n];
    for (int i = 0; i < n; i++)
      list[i] = i;
    return list;
  }

  private static void print(Object object) {
    System.out.println(object);
  }

  private static void print() {
    System.out.println();
  }

  private static final double FLOATING_POINT_EPSILON = 1.0E-10;

  public static void solve(Graph graph, int n, int m) {
    int V = n + m + 2;
    int S = n + m;
    int T = n + m + 1;

    FlowNetwork flowNetwork = new FlowNetwork(V);

    // 1. Connect Source S to all Boys
    for (int i = 0; i < n; i++) {
      flowNetwork.addEdge(new FlowEdge(S, i, 1.0));
    }

    // 2. Connect all Girls to Sink T
    for (int j = n; j < n + m; j++) {
      flowNetwork.addEdge(new FlowEdge(j, T, 1.0));
    }

    // 3. Connect Boys to Girls based on the bipartite graph
    for (int v = 0; v < n; v++) {
      for (int w : graph.adj(v)) {
        if (w >= n && w < n + m) {
          flowNetwork.addEdge(new FlowEdge(v, w, 1.0));
        }
      }
    }

    // 4. Edmonds-Karp Algorithm (shortest augmenting path using BFS)
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

      // If no augmenting path to T is found, stop
      if (!visited[T]) {
        break;
      }

      // Find bottleneck capacity along the augmenting path
      double bottleneck = Double.POSITIVE_INFINITY;
      for (int v = T; v != S; v = edgeTo[v].other(v)) {
        bottleneck = Math.min(bottleneck, edgeTo[v].residualCapacityTo(v));
      }

      // Augment the flow along the path
      for (int v = T; v != S; v = edgeTo[v].other(v)) {
        edgeTo[v].addResidualFlowTo(v, bottleneck);
      }

      maxFlow += bottleneck;
    }

    // Output the maximum number of dance pairs
    System.out.println((int) Math.round(maxFlow));

    // Output the matched pairs (translated to 1-based indexing for the output)
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
