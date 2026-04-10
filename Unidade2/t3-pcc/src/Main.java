import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        // Caminho do arquivo conforme a estrutura pedida no T3
        String filename = "dados/entrada_eulerizada.txt";
        
        try {
            Scanner in = new Scanner(new File(filename));
            
            if (!in.hasNextInt()) return;
            int V = in.nextInt(); // Número de vértices
            int E_count = in.nextInt(); // Número de arestas
            
            // Usamos EdgeWeightedDigraph para suportar os pesos exigidos
            EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);
            
            System.out.println("--- Análise do Grafo Eulerizado ---");
            
            // 1. Leitura e construção
            for (int i = 0; i < E_count; i++) {
                int v = in.nextInt();
                int w = in.nextInt();
                double weight = in.nextDouble();
                G.addEdge(new DirectedEdge(v, w, weight));
            }
            in.close();

            // 2. Cálculo de Graus e Verificação de Balanço
            // Em um dígrafo euleriano, in-degree(v) == out-degree(v) para todos os v
            System.out.println("Vértice\tGrau Entrada\tGrau Saída\tStatus");
            boolean isBalanced = true;
            for (int v = 0; v < G.V(); v++) {
                int inDegree = 0;
                // Cálculo manual do in-degree (dependendo da versão da algs4)
                for (DirectedEdge e : G.edges()) {
                    if (e.to() == v) inDegree++;
                }
                int outDegree = G.outdegree(v);
                
                String status = (inDegree == outDegree) ? "OK" : "DESBALANCEADO";
                if (inDegree != outDegree) isBalanced = false;
                
                System.out.printf("%d\t%d\t\t%d\t\t%s\n", v, inDegree, outDegree, status);
            }

            if (!isBalanced) {
                System.out.println("\nERRO: O grafo não está balanceado. Verifique a eulerização manual.");
                return;
            }

            // 3. Execução do Método de Hierholzer
            // A classe DirectedEulerianCycle da algs4 implementa Hierholzer
            DirectedEulerianCycle eulerian = new DirectedEulerianCycle(G);

            if (eulerian.hasEulerianCycle()) {
                System.out.println("\n--- Circuito Euleriano Encontrado ---");
                
                double totalCost = 0;
                StringBuilder path = new StringBuilder();
                
                // O Iterable retorna as ARESTAS do ciclo
                for (DirectedEdge e : eulerian.cycle()) {
                    path.append(e.from()).append(" -> ");
                    totalCost += e.weight();
                }
                
                // Para fechar a visualização com o último vértice
                if (eulerian.cycle().iterator().hasNext()) {
                    // Pega o destino da última aresta para fechar o ciclo no print
                    // Mas o algs4 já costuma retornar o ciclo completo
                }

                System.out.println(path.toString() + "Fim");
                System.out.printf("Custo Total do Circuito: %.2f\n", totalCost);
                System.out.println("-------------------------------------");
            } else {
                System.out.println("\nNão foi possível encontrar um circuito euleriano.");
            }

        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo '" + filename + "' não encontrado.");
        } catch (Exception e) {
            System.err.println("Erro durante a execução: " + e.getMessage());
            e.printStackTrace();
        }
    }
}