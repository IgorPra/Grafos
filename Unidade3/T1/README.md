# Treehouses - Kattis

## Informacoes do Trabalho

- **Nome do problema:** Treehouses
- **Link do problema:** <https://open.kattis.com/problems/treehouses>
- **Grupo:** Grupo C
- **Integrantes:** Igor Praciano, Rafael Lima e Tiago Silveira
- **Linguagem utilizada:** Java

## Evidencia de Accepted

![Submissao accepted no Kattis](evidencias/accepted.png)

## Estrutura do Projeto

```text
.
├── dados/
│   ├── dados01.txt
│   ├── dados02.txt
│   └── dados03.txt
├── evidencias/
│   └── accepted.png
├── src/
│   ├── Main.java
│   └── algs4/
│       ├── Edge.java
│       ├── EdgeWeightedGraph.java
│       ├── PrimMST.java
│       └── ...
└── README.md
```

## Como Executar a Solucao

Compile colocando as classes em uma pasta de saida:

```bash
javac -d out src/algs4/*.java src/Main.java
```

Execute com um arquivo de entrada:

```bash
java -cp out Main dados/dados02.txt
```

Observacao: se nenhum argumento for passado, a `Main.java` tenta abrir `dados/dados02.txt` automaticamente se esse arquivo existir. Isso facilita testes locais. No Kattis, esse arquivo nao existe no ambiente de submissao, entao a solucao usa normalmente a entrada padrao.

## Modelagem como Grafo Ponderado

O problema pode ser modelado como um **grafo nao direcionado ponderado**:

- **Vertices:** cada treehouse e um vertice do grafo.
- **Arestas:** cada par de treehouses pode receber um cabo, entao o grafo considerado e completo.
- **Pesos:** o peso de uma aresta e o comprimento do cabo entre duas treehouses.
- **Arestas gratuitas:** conexoes que ja existem entram no grafo com peso `0.0`.

Na entrada, as treehouses sao numeradas a partir de `1`. Na implementacao, elas sao convertidas para indices de `0` a `n - 1`, que e o padrao usado pelas estruturas do `algs4`.

O peso entre duas treehouses `i` e `j` e calculado pela distancia euclidiana:

```text
dist(i, j) = sqrt((xi - xj)^2 + (yi - yj)^2)
```

Assim, encontrar o menor comprimento adicional de cabos equivale a encontrar uma **Arvore Geradora Minima** no grafo.

## Algoritmo Utilizado

A solucao usa **Prim**, implementado pela classe `PrimMST` da pasta `src/algs4`.

Na `Main.java`, o fluxo principal e:

1. Ler `n`, `e` e `p`.
2. Ler as coordenadas das `n` treehouses.
3. Criar um `EdgeWeightedGraph` com `n` vertices.
4. Adicionar arestas de peso `0.0` para as primeiras `e` treehouses, que ja estao conectadas.
5. Adicionar arestas de peso `0.0` para os `p` cabos ja existentes informados na entrada.
6. Adicionar todas as arestas possiveis entre pares de treehouses, usando distancia euclidiana como peso.
7. Executar `PrimMST`.
8. Imprimir `mst.weight()`, que representa o menor comprimento adicional necessario.

## Papel da Fila de Prioridade (Prim)

No Prim, mantemos uma "fronteira" entre os vertices ja conectados na arvore e os vertices ainda fora dela.

Em `PrimMST`, uma fila de prioridade (estrutura `IndexMinPQ`) ajuda a escolher sempre a proxima aresta de menor peso que conecta um vertice novo a arvore.

Isso garante que, a cada passo, a arvore cresce adicionando o vertice mais barato de ser conectado, sem precisar ordenar todas as arestas globalmente.

## Variacao de MST Usada

A variacao usada e uma MST com **arestas preexistentes de custo zero**.

As conexoes que ja existem no problema nao precisam ser pagas novamente, entao elas sao representadas como arestas de peso `0.0`. Com isso, o Prim prioriza naturalmente essas arestas na fronteira quando elas forem a opcao mais barata, e o peso final da MST representa apenas o custo adicional que ainda precisa ser construido.

## Casos Especiais Relevantes

- Se as primeiras `e` treehouses ja conectam parte do grafo, essas conexoes entram com custo zero.
- Se existem `p` cabos ja instalados, eles tambem entram com custo zero.
- Se todas as treehouses ja estiverem conectadas pelas arestas gratuitas, a resposta pode ser `0.0000000000`.
- A entrada usa indices iniciando em `1`, mas o codigo converte para indices iniciando em `0`.
- A saida usa `Locale.US` para garantir ponto decimal, como esperado pelo Kattis.

## Analise de Complexidade

Como o grafo e completo, o numero de arestas candidatas e:

```text
e = v(v - 1) / 2
```

Considerando `v` como o numero de vertices e `e` como o numero de arestas:

- Gerar todas as arestas: `O(v^2)`;
- Executar Prim com fila de prioridade: `O(e log v)`;
- Armazenar o grafo e as arestas: `O(e)`.

A etapa dominante e a fila de prioridade, entao a complexidade de tempo fica em:

```text
O(e log v) = O(v^2 log v)
```

A complexidade de espaco e:

```text
O(e) = O(v^2)
```
