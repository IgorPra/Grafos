from pathlib import Path
from graph import Graph
from bag import Bag
from depth_first_paths import DepthFirstPaths
from breadth_first_paths import BreadthFirstPaths
from cc import CC
from cycle import Cycle

BASE_DIR = Path(__file__).resolve().parent.parent
ARQUIVO_ENTRADA = BASE_DIR / "dados" / "entrada.txt"

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

graph = new_graph(ARQUIVO_ENTRADA)

if not graph:
    raise ValueError("O arquivo de entrada está vazio ou não contém as informações necessárias.")

print("Lista de adjacência:")
print(graph)

cc = CC(graph)
print("Componentes conexas:", cc.count)

components = []
for i in range(cc.count):
    components.append(Bag())

for v in range(graph.V):
    components[cc.id[v]].add(v)

for i in range(cc.count):
    count = 1
    print(f"Vértices da componente {i}: ", end='')
    for v in components[i]:
        print(v, end=' ')
    print()
    count += 1

# Distância entre os vértices 0 e 8
bfs = BreadthFirstPaths(graph, 0)

count = -1
for x in bfs.path_to(8):
    count += 1
print(f"Distância mínima entre 0(0,0) e 8(2,2): {count}")

cycle = Cycle(graph)

print(f"O grafo possui ciclo: {'Sim' if cycle.has_cycle else 'Não'}")

print(f"Ciclo encontrado: {cycle.cycle()}")

