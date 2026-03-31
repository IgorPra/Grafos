movimentos = [(2,1),(2,-1),(-2,1),(-2,-1),
              (1,2),(1,-2),(-1,2),(-1,-2)]

arestas = []

for linha in range(3):
  for coluna in range(3):
    origem = linha*3 + coluna
    
    for dl, dc in movimentos:
      nl = linha + dl
      nc = coluna + dc
      
      if 0 <= nl < 3 and 0 <= nc < 3:
        destino = nl*3 + nc
        
        if origem < destino:
          arestas.append((origem, destino))

print(9)
print(len(arestas))
for a in arestas:
    print(a[0], a[1])
