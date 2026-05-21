# Grupo C - Kattis - Treehouses

## Estrutura do Projeto

```text
projeto/
├── src/
├── evidencias/
├── apresentacao/
├── dados/
└── README.md
```

## Modelagem do Grafo

- **Vértices**: cada treehouse é um vértice do grafo (índices de `1` a `n` na entrada; internamente `0` a `n-1`).
- **Arestas**: cada par de treehouses `(i, j)` pode receber uma conexão direta, logo o grafo é completo não-direcionado.
- **Pesos**: o peso da aresta é a distância euclidiana entre os pontos `(xi, yi)` e `(xj, yj)`:

  ```text
  w(i, j) = sqrt((xi - xj)^2 + (yi - yj)^2)
  ```

## Estratégia Algorítmica

A solução utiliza **Kruskal + Union-Find (DSU)**, em linha com o estilo de algoritmos do Princeton `algs4`, porém sem dependências externas.

1. **Conexões gratuitas iniciais (`e`)**
   - As primeiras `e` treehouses já estão conectadas sem custo.
   - No DSU, fazemos `union` entre elas imediatamente.

2. **Conexões gratuitas já existentes (`p`)**
   - As `p` conexões da entrada também são aplicadas no DSU sem custo.

3. **Geração de arestas candidatas**
   - Geramos todas as arestas possíveis entre pares de casas.
   - Calculamos o peso com distância euclidiana (`double`).

4. **Ordenação por peso**
   - Ordenamos as arestas em ordem crescente.

5. **Kruskal**
   - Percorremos as arestas ordenadas.
   - Se os extremos estão em componentes diferentes (`union` retorna `true`), somamos o peso.
   - O acumulado final é o menor comprimento adicional necessário.

## Estruturas Internas Implementadas em `Main.java`

- `FastScanner`: leitura rápida com `BufferedInputStream`.
- `UF`: Union-Find com **union by size** e **path compression**.
- `Edge`: representa aresta ponderada e implementa `Comparable`.

Tudo está contido em um único arquivo `src/Main.java`, sem `Point2D`, sem imports de geometria do `algs4`, e pronto para submissão no Kattis.

## Complexidade

Considere `n` treehouses:

- Número de arestas do grafo completo:
  - `m = n(n-1)/2 = O(n^2)`

### Tempo

- Gerar todas as arestas: `O(n^2)`
- Ordenar arestas: `O(m log m)`
- Kruskal com DSU: `O(m α(n))` (quase linear em `m`)

Dominante:

- **`O(m log m)`**, equivalente a **`O(n^2 log n)`**.

### Espaço

- Armazenamento das arestas: `O(m)`
- Estruturas DSU e coordenadas: `O(n)`

Total:

- **`O(m)`**, equivalente a **`O(n^2)`**.

## Compilação e Execução Local

```bash
javac src/Main.java
java -cp src Main < input.txt
```

## Saída

A saída segue o formato exigido pelo Kattis:

```java
System.out.printf(Locale.US, "%.10f%n", resposta);
```
