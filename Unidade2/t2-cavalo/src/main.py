from pathlib import Path
from graph import Graph
from bag import Bag
from depth_first_paths import DepthFirstPaths
from breadth_first_paths import BreadthFirstPaths
from cc import CC
from cycle import Cycle

arquivo_entrada = Path.cwd() / "dados" / "entrada.txt"

def new_graph(caminho: Path):
    with caminho.open("r", encoding="utf-8") as f:
        line_v = f.readline().strip()
        line_e = f.readline().strip()
        
        if not line_v or not line_e:
            return None
            
        V = int(line_v)
        E = int(line_e)
        
        g = Graph(V)
        
        for _ in range(E):
            linha = f.readline().split()
            if len(linha) == 2:
                v = int(linha[0])
                w = int(linha[1])
                g.add_edge(v, w)
    return g

#Criação do Grafo e do dfs e bfs com base na origem do arquivo de entrada
graph = new_graph(arquivo_entrada)



print(graph)


cc = CC(graph)
print(cc.count, " components")

components = []
for i in range(cc.count):
    components.append(Bag())

for v in range(graph.V):
    components[cc.id[v]].add(v)

for i in range(cc.count):
    for v in components[i]:
        print(v, " ", end='')
    print()

bfs = BreadthFirstPaths(graph, 0)

count = -1
for x in bfs.path_to(8):
    count += 1
print(count)


cycle = Cycle(graph)

if cycle.has_cycle:
    print("Graph is cyclic")
else:
    print("Graph is acyclic")


#print(cycle.cycle())

















# movimentos = [(2,1),(2,-1),(-2,1),(-2,-1),
#               (1,2),(1,-2),(-1,2),(-1,-2)]

# arestas = []

# for linha in range(3):
#   for coluna in range(3):
#     origem = linha*3 + coluna
    
#     for dl, dc in movimentos:
#       nl = linha + dl
#       nc = coluna + dc
      
#       if 0 <= nl < 3 and 0 <= nc < 3:
#         destino = nl*3 + nc
        
#         if origem < destino:
#           arestas.append((origem, destino))

# print(9)
# print(len(arestas))
# for a in arestas:
#     print(a[0], a[1])
