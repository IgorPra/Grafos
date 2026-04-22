public class GraphColoringDSatur {
    private final Graph graph;
    private final int[] colors;
    private final int[] coloringOrder;
    private int coloredCount;
    private int colorCount;
    private boolean colored;

    public GraphColoringDSatur(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("graph nao pode ser nulo");
        }
        this.graph = graph;
        this.colors = new int[graph.V()];
        this.coloringOrder = new int[graph.V()];
        this.coloredCount = 0;
        this.colorCount = 0;
        this.colored = false;
    }

    public Graph getGraph() {
        return graph;
    }

    public void color() {
        resetColoring();

        if (graph.V() == 0) {
            colored = true;
            return;
        }

        int firstVertex = findHighestDegreeUncoloredVertex();
        assignColor(firstVertex, 1);
        colorCount = 1;

        while (coloredCount < graph.V()) {
            int nextVertex = selectNextVertex();
            int nextColor = findSmallestFeasibleColor(nextVertex);

            assignColor(nextVertex, nextColor);
            if (nextColor > colorCount) {
                colorCount = nextColor;
            }
        }

        colored = true;
    }

    public int getColor(int vertex) {
        validateVertex(vertex);
        return colors[vertex];
    }

    public int getColorCount() {
        return colorCount;
    }

    public int[] getColoringOrder() {
        int[] order = new int[coloredCount];
        for (int i = 0; i < coloredCount; i++) {
            order[i] = coloringOrder[i];
        }
        return order;
    }

    public boolean isValidColoring() {
        if (!colored || coloredCount != graph.V()) {
            return false;
        }

        for (int v = 0; v < graph.V(); v++) {
            if (colors[v] <= 0) {
                return false;
            }

            for (int w : graph.adj(v)) {
                if (colors[v] == colors[w]) {
                    return false;
                }
            }
        }

        return true;
    }

    public String getLabel(int vertex) {
        validateVertex(vertex);
        return String.valueOf(vertex);
    }

    private void resetColoring() {
        for (int v = 0; v < graph.V(); v++) {
            colors[v] = 0;
            coloringOrder[v] = 0;
        }
        coloredCount = 0;
        colorCount = 0;
        colored = false;
    }

    private int findHighestDegreeUncoloredVertex() {
        int bestVertex = -1;
        int bestDegree = -1;

        for (int v = 0; v < graph.V(); v++) {
            if (colors[v] != 0) {
                continue;
            }

            int degree = graph.degree(v);
            if (degree > bestDegree || (degree == bestDegree && v < bestVertex)) {
                bestDegree = degree;
                bestVertex = v;
            }
        }

        return bestVertex;
    }

    private int selectNextVertex() {
        int bestVertex = -1;
        int bestSaturation = -1;
        int bestDegree = -1;

        for (int v = 0; v < graph.V(); v++) {
            if (colors[v] != 0) {
                continue;
            }

            int saturation = getSaturationDegree(v);
            int degree = graph.degree(v);

            if (saturation > bestSaturation
                    || (saturation == bestSaturation && degree > bestDegree)
                    || (saturation == bestSaturation && degree == bestDegree && v < bestVertex)) {
                bestVertex = v;
                bestSaturation = saturation;
                bestDegree = degree;
            }
        }

        return bestVertex;
    }

    private int getSaturationDegree(int vertex) {
        boolean[] usedColors = new boolean[colorCount + 1];
        int saturation = 0;

        for (int neighbor : graph.adj(vertex)) {
            int neighborColor = colors[neighbor];
            if (neighborColor > 0 && !usedColors[neighborColor]) {
                usedColors[neighborColor] = true;
                saturation++;
            }
        }

        return saturation;
    }

    private int findSmallestFeasibleColor(int vertex) {
        for (int candidateColor = 1; candidateColor <= colorCount + 1; candidateColor++) {
            if (isFeasibleColor(vertex, candidateColor)) {
                return candidateColor;
            }
        }

        return colorCount + 1;
    }

    private boolean isFeasibleColor(int vertex, int candidateColor) {
        for (int neighbor : graph.adj(vertex)) {
            if (neighbor != vertex && colors[neighbor] == candidateColor) {
                return false;
            }
        }
        return true;
    }

    private void assignColor(int vertex, int color) {
        colors[vertex] = color;
        coloringOrder[coloredCount] = vertex;
        coloredCount++;
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= graph.V()) {
            throw new IllegalArgumentException(
                    "vertice " + vertex + " nao esta entre 0 e " + (graph.V() - 1)
            );
        }
    }
}
