import algs4.Bipartite;
import algs4.Graph;
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
}
