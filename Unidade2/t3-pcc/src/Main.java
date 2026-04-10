import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String caminho = "dados/entrada_eulerizada.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(caminho));

            int V = Integer.parseInt(br.readLine().trim());
            int E = Integer.parseInt(br.readLine().trim());

            // Grafo com pesos
            EdgeWeightedDigraph weightedGraph = new EdgeWeightedDigraph(V);

            Digraph graph = new Digraph(V);

            for (int i = 0; i < E; i++) {
                String[] parts = br.readLine().split(" ");

                int v = Integer.parseInt(parts[0]);
                int w = Integer.parseInt(parts[1]);
                double peso = Double.parseDouble(parts[2]);

                weightedGraph.addEdge(new DirectedEdge(v, w, peso));
                graph.addEdge(v, w);
            }

            br.close();


            // Graus de saida e entrada
            System.out.println("Graus dos vértices:");
            boolean balanceado = true;

            for (int v = 0; v < V; v++) {
                int out = graph.outdegree(v);
                int in = graph.indegree(v);

                System.out.println("Vértice " + v +
                        " | out = " + out +
                        " | in = " + in);

                if (out != in) balanceado = false;
            }

            if (!balanceado) {
                System.out.println("\nGrafo NÃO está balanceado.");
                return;
            }

            System.out.println("\nGrafo está balanceado!");


            // Hierholzer
            DirectedEulerianCycle euler = new DirectedEulerianCycle(graph);

            if (!euler.hasEulerianCycle()) {
                System.out.println("Não existe circuito euleriano.");
                return;
            }


            //circuito euleriano
            System.out.println("\nCircuito Euleriano:");

            List<Integer> ciclo = new ArrayList<>();

            for (int v : euler.cycle()) {
                ciclo.add(v);
                System.out.print(v + " ");
            }

            System.out.println();



            //Custo Total
            double custoTotal = 0.0;

            Map<String, Deque<Double>> pesos = new HashMap<>();

            for (int v = 0; v < V; v++) {
                for (DirectedEdge e : weightedGraph.adj(v)) {
                    String chave = e.from() + "-" + e.to();

                    pesos.putIfAbsent(chave, new ArrayDeque<Double>());
                    pesos.get(chave).addLast(e.weight());
                }
            }

            for (int i = 0; i < ciclo.size() - 1; i++) {
                int v = ciclo.get(i);
                int w = ciclo.get(i + 1);

                String chave = v + "-" + w;

                if (!pesos.containsKey(chave) || pesos.get(chave).isEmpty()) {
                    System.out.println("Erro ao encontrar peso da aresta: " + chave);
                    continue;
                }

                double peso = pesos.get(chave).removeFirst(); 
                custoTotal += peso;
            }

            System.out.println("\nCusto total: " + custoTotal);

        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }
}
