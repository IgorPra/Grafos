package src;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TreeIsomorphism {
    private final Graph graph;

    public TreeIsomorphism(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("graph nao pode ser nulo");
        }
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public boolean isTree() {
        return graph.V() > 0 && graph.E() == graph.V() - 1 && isConnected();
    }

    public String getValidationMessage() {
        if (graph.V() == 0) {
            return "Entrada invalida: grafo sem vertices.";
        }
        if (graph.E() != graph.V() - 1) {
            return "Entrada invalida: uma arvore com " + graph.V() +
                    " vertices deve ter " + (graph.V() - 1) + " arestas.";
        }
        if (!isConnected()) {
            return "Entrada invalida: o grafo nao eh conexo.";
        }
        return "Entrada valida: o grafo eh uma arvore.";
    }

    public int[] getCenters() {
        if (!isTree()) {
            return new int[0];
        }

        int n = graph.V();
        int[] degree = new int[n];
        ArrayList<Integer> leaves = new ArrayList<Integer>();

        for (int v = 0; v < n; v++) {
            degree[v] = graph.degree(v);
            if (degree[v] <= 1) {
                leaves.add(v);
            }
        }

        int processed = leaves.size();

        while (processed < n) {
            ArrayList<Integer> newLeaves = new ArrayList<Integer>();

            for (int leaf : leaves) {
                for (int neighbor : graph.adj(leaf)) {
                    degree[neighbor]--;
                    if (degree[neighbor] == 1) {
                        newLeaves.add(neighbor);
                    }
                }
            }

            processed += newLeaves.size();
            leaves = newLeaves;
        }

        Collections.sort(leaves);
        int[] centers = new int[leaves.size()];
        for (int i = 0; i < leaves.size(); i++) {
            centers[i] = leaves.get(i);
        }
        return centers;
    }

    public String getCanonicalEncoding() {
        if (!isTree()) {
            return "";
        }

        int[] centers = getCenters();
        ArrayList<String> encodings = new ArrayList<String>();

        for (int center : centers) {
            encodings.add(encode(center, -1));
        }

        Collections.sort(encodings);
        return encodings.get(0);
    }

    private boolean isConnected() {
        boolean[] visited = new boolean[graph.V()];
        dfs(0, visited);

        for (boolean wasVisited : visited) {
            if (!wasVisited) {
                return false;
            }
        }
        return true;
    }

    private void dfs(int vertex, boolean[] visited) {
        visited[vertex] = true;
        for (int neighbor : graph.adj(vertex)) {
            if (!visited[neighbor]) {
                dfs(neighbor, visited);
            }
        }
    }

    private String encode(int vertex, int parent) {
        ArrayList<String> childrenCodes = new ArrayList<String>();

        for (int neighbor : graph.adj(vertex)) {
            if (neighbor != parent) {
                childrenCodes.add(encode(neighbor, vertex));
            }
        }

        Collections.sort(childrenCodes);

        StringBuilder code = new StringBuilder();
        code.append("(");
        for (String childCode : childrenCodes) {
            code.append(childCode);
        }
        code.append(")");

        return code.toString();
    }

    public String centersAsString() {
        return Arrays.toString(getCenters());
    }
}
