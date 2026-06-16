# CSES School Dance — Emparelhamento Bipartido por Fluxo Máximo

> **Problema:** [CSES 1696 — School Dance](https://cses.fi/problemset/task/1696)  
> **Plataforma:** [cses.fi](https://cses.fi/)  
> **Grupo C — Disciplina: Resolução de Problemas com Grafos**  
> **Professor:** Prof. Me Ricardo Carubbi
> **LINK DO VIDEO:** [https://youtu.be/tU6A-vBVyDA](https://youtu.be/tU6A-vBVyDA)

---

## Integrantes

| Nome | |
|---|---|
| Igor Praciano Thomaz | |
| Rafael Lima Cacau | |
| Tiago Silveira Taumartugo | |

---

## Linguagem Utilizada

**Java** — arquivo principal: `src/Solution.java`  
(Sem dependências externas; todas as estruturas são implementadas no próprio arquivo)

---

## Como Executar a Solução

### Pré-requisitos

- Java 17+ instalado (`java -version`)

### Compilar

```bash
cd src
javac Solution.java
```

### Executar (leitura pela entrada padrão — modo CSES)

```bash
cd src
java Solution
```

Cole a entrada no terminal conforme o formato do problema, ou redirecione um arquivo:

```bash
cd src
java Solution < ../dados/dados02.txt
```

### Formato da Entrada

```
n m k
a₁ b₁
a₂ b₂
...
aₖ bₖ
```

- `n` — número de rapazes  
- `m` — número de moças  
- `k` — número de pares compatíveis  
- Cada linha `aᵢ bᵢ`: rapaz `aᵢ` pode dançar com moça `bᵢ`

### Formato da Saída

```
p
a₁ b₁
...
aₚ bₚ
```

- `p` — número máximo de pares de dança  
- Cada linha: um par válido formado

### Exemplo

**Entrada** (`dados/dados02.txt`):
```
3 2 4
1 1
1 2
2 1
3 1
```

**Saída:**
```
2
1 2
2 1
```

---

## Modelagem como Rede de Fluxo

O problema de **emparelhamento bipartido máximo** é resolvido por redução a fluxo máximo em rede capacitada.

### Vértices

| Vértice | Índice | Papel na rede |
|---|---|---|
| **Fonte S** | `n + m` | Nó artificial que distribui 1 unidade de fluxo para cada rapaz |
| **Rapazes** | `0` a `n-1` | Cada rapaz é um vértice; recebe 1 unidade da fonte |
| **Moças** | `n` a `n+m-1` | Cada moça é um vértice; envia 1 unidade ao sorvedouro |
| **Sorvedouro T** | `n + m + 1` | Coleta todos os pares formados; fluxo total = resposta |

**Total:** `V = n + m + 2` vértices.

### Arestas e Capacidades

```
S  ──(cap=1)──► rapaz_i        para todo i em [1..n]
rapaz_i ──(cap=1)──► moça_j   para todo par compatível (i,j)
moça_j  ──(cap=1)──► T         para todo j em [1..m]
```

#### Por que capacidade unitária?

- **S → rapaz (cap=1):** Cada rapaz participa de **no máximo 1 par** de dança.
- **rapaz → moça (cap=1):** Representa uma compatibilidade. O fluxo indica se o par foi efetivamente formado.
- **moça → T (cap=1):** Cada moça participa de **no máximo 1 par** de dança.

Portanto, **1 unidade de fluxo = 1 decisão válida de emparelhamento**. O valor do fluxo máximo é exatamente o número máximo de pares de dança.

---

## Algoritmo Utilizado: Edmonds-Karp

O algoritmo de **Edmonds-Karp** é a implementação de Ford-Fulkerson que usa **BFS** para encontrar o **caminho aumentante mais curto** (em número de arestas) da fonte S ao sorvedouro T a cada iteração.

### Por que Edmonds-Karp?

- Com **capacidades unitárias**, o fluxo máximo é no máximo `min(n, m)`.
- O Ford-Fulkerson com DFS poderia ter custo O(E · max_flow), que em casos adversos é lento.
- O Edmonds-Karp garante O(V · E²) no pior caso geral e O(E · √V) em redes com capacidades unitárias, sendo **mais previsível e seguro** para este problema.

---

## Papel do Grafo Residual

O **grafo residual** `Gf` é mantido durante todo o algoritmo e possui, para cada aresta `u → v` com capacidade `c` e fluxo atual `f`:

- **Aresta direta** `u → v`: capacidade residual `c - f` (fluxo ainda enviável)
- **Aresta reversa** `v → u`: capacidade residual `f` (fluxo cancelável)

As **arestas reversas** são essenciais: elas permitem desfazer decisões de emparelhamento feitas em iterações anteriores, garantindo que o algoritmo sempre encontre a solução ótima global — mesmo que um caminho "errado" tenha sido escolhido no início.

**Condição de parada:** o algoritmo termina quando a BFS não encontra nenhum caminho de S a T no grafo residual. Nesse momento, o fluxo máximo foi atingido.

---

## Como o Resultado do Fluxo é Convertido na Resposta

Após o Edmonds-Karp terminar:

1. **Número de pares** = valor total do fluxo máximo (`maxFlow`).
2. **Pares formados** = arestas `rapaz_i → moça_j` com `flow > 0` na `FlowNetwork`.

```java
// Extração dos pares
for (int v = 0; v < n; v++) {
    for (FlowEdge e : flowNetwork.adj(v)) {
        if (e.from() == v && e.to() >= n && e.to() < n + m && e.flow() > EPSILON) {
            int w = e.to();
            System.out.println((v + 1) + " " + (w - n + 1)); // converte para 1-based
        }
    }
}
```

---

## Análise de Complexidade

| Aspecto | Complexidade |
|---|---|
| **Tempo — Edmonds-Karp (geral)** | O(V · E²) |
| **Tempo — com capacidades unitárias** | O(E · √V) |
| **Memória — grafo residual** | O(V + E) |

**Para os limites do CSES 1696:**
- n, m ≤ 500 → V ≤ 1002
- k ≤ 1000 → E ≤ 2002

A solução é extremamente eficiente dentro dos limites do problema.

---

## Casos Especiais Relevantes

| Caso | Tratamento |
|---|---|
| **Sem pares compatíveis** (`k=0`) | BFS já falha na 1ª tentativa. Saída: `0` |
| **Rapaz sem compatibilidade** | A aresta S→rapaz existe, mas sem saída para moça, nunca recebe fluxo |
| **Moça sem compatibilidade** | A aresta moça→T existe, mas sem entrada de rapaz, nunca é emparelhada |
| **Todos compatíveis** (`k = n*m`) | Emparelhamento máximo = `min(n, m)` |
| **Múltiplas soluções ótimas** | O problema aceita qualquer solução com o número máximo de pares; o Edmonds-Karp retorna uma delas |
| **Escolha do valor de INF** | Não usamos INF neste problema; todas as capacidades são exatamente `1.0` |

---

## Evidência de Accepted

> A imagem comprovando o resultado **Accepted** está disponível em:  
> [`evidencias/accepted.png`](evidencias/accepted.png)

---

## Estrutura do Repositório

```
T3/
├── README.md
├── acompanhamento/
│   └── roteiro.md
├── src/
│   ├── Main.java          ← versão com debug/logs e dependência algs4/
│   ├── Solution.java      ← versão autocontida para submissão (sem deps externas)
│   └── algs4/             ← módulos de apoio algs4 (usados em Main.java)
├── evidencias/
│   └── accepted.png
├── apresentacao/
│   └── apresentacao.md
└── dados/
    └── dados02.txt        ← exemplo de entrada local para testes
```

---

## Referências

- [CSES 1696 — School Dance](https://cses.fi/problemset/task/1696)
- Aula da disciplina — AVA/Moodle: Fluxo máximo, Edmonds-Karp, emparelhamento bipartido
- Sedgewick & Wayne — Algorithms, 4th ed. (base `algs4`)
