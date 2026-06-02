import java.util.HashMap;

import algs4.EdgeWeightedDigraph;
import algs4.DijkstraSP;
import algs4.DirectedEdge;
import algs4.StdIn;

public class Main {
    public static void main(String[] args) throws Exception {
        java.io.File file = new java.io.File("dados/dados02.txt");
        if (file.exists()) {
            System.setIn(new java.io.FileInputStream(file));
        }

        int s = StdIn.readInt();

        for (int i = 0; i < s; i++) {

            HashMap<String, Integer> map = new HashMap<>();
            
            int n = StdIn.readInt();

            EdgeWeightedDigraph graph = new EdgeWeightedDigraph(n);

            for (int j = 0; j < n; j++) {
                
                String name = StdIn.readString();


                map.put(name, j);

                int p = StdIn.readInt(); // numero de vizinhos

                for (int k = 0; k < p; k++) {

                    int nr = StdIn.readInt(); // indice do vizinho

                    double cost = StdIn.readInt();

                    graph.addEdge(new DirectedEdge(j, nr - 1, cost));

                }
            }

            int r = StdIn.readInt(); // numero de caminhos
    
            for (int j = 0; j < r; j++) {
                String name1 = StdIn.readString();
                int index1 = map.get(name1);
    
                String name2 = StdIn.readString();
                int index2 = map.get(name2);

                DijkstraSP dijkstra = new DijkstraSP(graph, index1);

                System.out.println((int) dijkstra.distTo(index2));
            }
        }
    }
}
