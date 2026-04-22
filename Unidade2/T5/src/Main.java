public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException(
                    "informe o arquivo de entrada. Ex.: java Main ../dados/brasil.txt"
            );
        }

        In in = new In(args[0]);
        Graph graph = new Graph(in);
        GraphColoringDSatur dsatur = new GraphColoringDSatur(graph);

        StdOut.println("Grafo carregado:");
        StdOut.println(graph);
        StdOut.println();

        dsatur.color();

        StdOut.println("Ordem de coloracao:");
        int[] order = dsatur.getColoringOrder();
        for (int i = 0; i < order.length; i++) {
            StdOut.println((i + 1) + ": vertice " + dsatur.getLabel(order[i]));
        }
        StdOut.println();

        StdOut.println("Cores finais dos vertices:");
        for (int v = 0; v < graph.V(); v++) {
            StdOut.println("vertice " + dsatur.getLabel(v) + " -> cor " + dsatur.getColor(v));
        }
        StdOut.println();

        StdOut.println("Total de cores utilizadas: " + dsatur.getColorCount());
        StdOut.println("Coloracao valida: " + dsatur.isValidColoring());
    }
}
