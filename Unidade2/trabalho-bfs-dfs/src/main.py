from pathlib import Path
from graph import Graph
from depth_first_paths import DepthFirstPaths
from breadth_first_paths import BreadthFirstPaths


arquivo_nordeste = Path.cwd() / "dados" / "nordeste.txt"
arquivo_entrada = Path.cwd() / "dados" / "entrada.txt"



#Leitura do arquivos de entrada e deinição da origem e do destino
def ler_arquivo_entrada(caminho: Path):
    file = caminho.open("r", encoding="utf-8")
    origem = int(file.readline().strip())
    destino = int(file.readline().strip())

    file.close()
    
    return origem,destino

origem,destino = ler_arquivo_entrada(arquivo_entrada)




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
graph = new_graph(arquivo_nordeste)

dfs = DepthFirstPaths(graph,origem)

bfs = BreadthFirstPaths(graph,origem)






nomes_estados = ["AL", "BA", "CE", "MA", "PB", "PE", "PI", "RN", "SE"]


#Questões
print(f"--- Relatório de Viagem de {origem} para {destino} ---")

# 1. É possível chegar de origem a destino?
if dfs.has_path_to(destino):
    print("Sim, é possível chegar atravessando fronteiras terrestres.")
else:
    print("Não, os estados não estão conectados por terra.")

# 2. Caminho encontrado pela DFS
if dfs.has_path_to(destino):
    caminho_dfs = " -> ".join([nomes_estados[v] for v in dfs.path_to(destino)])
    print(f"Caminho DFS de {nomes_estados[origem]} até {nomes_estados[destino]}: {caminho_dfs}")

# 3. Caminho encontrado pela BFS
if bfs.has_path_to(destino):
    caminho_bfs = " -> ".join([nomes_estados[v] for v in bfs.path_to(destino)])
    print(f"Caminho BFS de {nomes_estados[origem]} até {nomes_estados[destino]}: {caminho_bfs}")

# 4. Quais estados são alcançáveis a partir de origem?
estados_alcancaveis = [v for v in range(graph.V) if dfs.has_path_to(v)]
print(f"Estados alcançáveis a partir de {origem}: {estados_alcancaveis}")



# 5. Ordem de visita DFS
print(f"Ordem de visita (DFS): {dfs.visited_order}")

# 6. Ordem de visita BFS
print(f"Ordem de visita (BFS): {bfs.visited_order}")




# AL (0)
# BA (1)
# CE (2)
# MA (3)
# PB (4)
# PE (5)
# PI (6)
# RN (7)
# SE (8)
