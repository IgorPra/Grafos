"""
   Execution:    python -m algs4.cycle filename.txt
   Data files:   ../dataset/tinyG.txt
                 ../dataset/mediumG.txt
                 ../dataset/largeG.txt
 
   Identifies a cycle.
   Runs in O(E + V) time.
 
  % python -m algs4.cycle ../dataset/tinyG.txt
   3 4 5 3
 
  % python -m algs4.cycle ../dataset/mediumG.txt
   15 0 225 15
 
  % python -m algs4.cycle ../dataset/largeG.txt
   996673 762 840164 4619 785187 194717 996673
 """

from graph import Graph


class Cycle:

    def __init__(self, G):
        self.marked = [False for _ in range(G.V)]
        self.has_cycle = False
        self.edgeTo = [0] * G.V
        self._cycle = []
        for s in range(G.V):
            if not self.marked[s]:
                self.dfs(G, s, s)


    def has_parallel_edges(self, G):
        local_marked = [False] * G.V
        for v in range(G.V):
            for w in G.adj[v]:
                if local_marked[w]:
                    self._cycle = [v, w, v]
                    return True
                local_marked[w] = True
            # Reset para o próximo vértice
            for w in G.adj[v]:
                local_marked[w] = False
        return False

    def dfs(self, G, v, u):
        self.marked[v] = True
        for w in G.adj[v]:
            if not self.marked[w]:
                self.dfs(G, w, v)
            elif w != u:
                self._cycle = []
                x = v
                while x != w:
                    self._cycle.append(x)
                    x = self.edgeTo[x]
                self._cycle.append(w)
                self._cycle.append(v)
                self.has_cycle = True

    def cycle(self):
        return self._cycle


if __name__ == "__main__":
    import sys
    f = open(sys.argv[1])
    V = int(f.readline())
    E = int(f.readline())
    g = Graph(V)
    for i in range(E):
        v, w = f.readline().split()
        g.add_edge(v, w)
    cycle = Cycle(g)
    if cycle.has_cycle:
        print("Graph is cyclic")
    else:
        print("Graph is acyclic")