# Apresentação — Grupo C
## CSES School Dance

**Disciplina:** Resolução de Problemas com Grafos  
**Professor:** Prof. Me Ricardo Carubbi  
**Trabalho:** T3 — Unidade 3  
**Problema:** [CSES 1696 — School Dance](https://cses.fi/problemset/task/1696)  
**Linguagem:** Java  
**Algoritmo:** Edmonds-Karp (BFS no grafo residual)

**Integrantes do Grupo C:**
- Igor Praciano Thomaz
- Rafael Lima Cacau
- Tiago Silveira Taumartugo

---

## 1. Contexto do Problema e Objetivo

> _Tempo sugerido: até 1 minuto_

O problema **School Dance** descreve uma situação em que há **n rapazes** e **m moças** que desejam participar de uma dança. Cada rapaz pode dançar com algumas moças específicas, e cada pessoa pode dançar no máximo **uma vez** (com um único parceiro).

**O que precisa ser calculado:**
- O número **máximo** de pares que podem dançar juntos (o emparelhamento máximo do grafo bipartido).
- A listagem de todos os pares formados.

**Por que fluxo máximo resolve isso?**  
O emparelhamento bipartido é um caso especial de fluxo máximo. Transformamos o problema em uma rede onde **1 unidade de fluxo = 1 par de dança válido**. O valor do fluxo máximo encontrado é exatamente o número máximo de pares.

---

## 2. Modelagem da Rede de Fluxo

> _Tempo sugerido: até 1 minuto_

### Vértices da Rede

| Vértice | Significado |
|---|---|
| **S** (Fonte / Source) | Nó artificial de origem. Representa a "oferta" de dançarinos. Índice: `n + m` |
| **Rapazes** `b₁ … bₙ` | Cada rapaz `i` é um vértice com índice `i-1` (0 a n-1) |
| **Moças** `g₁ … gₘ` | Cada moça `j` é um vértice com índice `n + j - 1` (n a n+m-1) |
| **T** (Sorvedouro / Sink) | Nó artificial de destino. Representa os pares formados. Índice: `n + m + 1` |

**Total de vértices:** `V = n + m + 2`

### Arestas e Capacidades

```
S  ──(cap=1)──► rapaz_i       para todo i em [1..n]
rapaz_i ──(cap=1)──► moça_j   para todo par compatível (i,j) na entrada
moça_j  ──(cap=1)──► T        para todo j em [1..m]
```

#### Por que capacidade 1 em todas as arestas?

- **S → rapaz_i (cap=1):** Garante que cada rapaz participe de **no máximo 1 par**. Um rapaz não pode ter 2 unidades de fluxo saindo, pois a aresta da fonte limita a 1.
- **rapaz_i → moça_j (cap=1):** Representa a **compatibilidade**: o par só pode ser formado se essa aresta existe. A capacidade 1 impede que a mesma aresta seja usada duas vezes (o que não faria sentido num par de dança).
- **moça_j → T (cap=1):** Garante que cada moça participe de **no máximo 1 par**. Mesmo que múltiplos rapazes sejam compatíveis com ela, só 1 unidade de fluxo pode chegar ao sorvedouro via essa moça.

### Justificativa da Escolha de Origem e Sorvedouro

- **Fonte S:** Não é nem rapaz nem moça — é um nó artificial que "alimenta" todos os rapazes com 1 unidade de fluxo possível cada. Ele centraliza a oferta.
- **Sorvedouro T:** Coleta todos os pares formados. Chegar a T com 1 unidade significa que uma moça foi efetivamente emparelhada. O valor total do fluxo em T = número de pares de dança.

### Diagrama da Rede (Instância Pequena: n=3, m=2, k=4 pares)

```
Pares compatíveis: (1,1), (1,2), (2,1), (3,1)

         cap=1        cap=1        cap=1
    ┌──────────► B1 ──────────► G1 ──────────┐
    │            │               ▲            │
    │            │ cap=1         │ cap=1      ▼
    │            └──────────► G2 ──────────► T
    S ─cap=1──► B2 ──────────► G1            ▲
    │         cap=1      (via G1, mesma)      │
    │                                         │
    └──────────► B3 ──────────► G1 ──────────┘
       cap=1         cap=1
```

Simplificando:

```
         1          1          1
    S ──────► B1 ──────► G1 ──────► T
    │          \                    ▲
    │  1     1  \──────► G2 ───1───┘
    S ──────► B2 ──────► G1
    │  1          1
    S ──────► B3 ──────► G1
         1          1
```

---

## 3. Algoritmo Usado e Funcionamento do Grafo Residual

> _Tempo sugerido: até 1 minuto_

### Algoritmo: Edmonds-Karp

Optamos por **Edmonds-Karp**, que é a implementação de Ford-Fulkerson que usa **BFS** para encontrar o caminho aumentante mais curto (em número de arestas) da fonte S ao sorvedouro T.

#### Por que Edmonds-Karp e não Ford-Fulkerson com DFS?

| Critério | Ford-Fulkerson (DFS) | Edmonds-Karp (BFS) |
|---|---|---|
| Caminho aumentante | DFS — qualquer caminho | BFS — caminho mais curto |
| Complexidade | O(E · max_flow) | O(V · E²) |
| Previsibilidade | Pode ser lento com capacidades grandes | Número de aumentos é polinomial em V e E |
| Adequação para capacidades unitárias | Funciona, mas menos previsível | **Ideal**: com cap=1, cada BFS é O(E) e há no máximo O(√V·E) aumentos |

Como todas as capacidades neste problema são **unitárias**, o Edmonds-Karp é a escolha mais robusta e previsível.

### Grafo Residual

O **grafo residual** `Gf` representa o quanto de fluxo ainda pode ser enviado ou cancelado em cada aresta:

- Para uma aresta `u → v` com capacidade `c` e fluxo atual `f`:
  - **Capacidade residual direta** `u → v`: `c - f` (quanto ainda pode fluir para frente)
  - **Capacidade residual reversa** `v → u`: `f` (quanto fluxo pode ser cancelado/devolvido)

As **arestas reversas** são fundamentais: elas permitem ao algoritmo **desfazer decisões ruins** feitas em iterações anteriores, garantindo que a solução ótima seja sempre encontrada.

### Condição de Parada

O algoritmo para quando a BFS **não consegue mais encontrar um caminho** da fonte S ao sorvedouro T no grafo residual. Isso significa que todos os caminhos aumentantes foram explorados e o fluxo máximo foi atingido.

---

## 4. Passo a Passo — Instância Manual

> _Tempo sugerido: até 1 minuto_

**Entrada:** `n=3, m=2, k=4`  
**Pares compatíveis:** (1,1), (1,2), (2,1), (3,1)

**Indexação interna (0-based):**
- S = 5, T = 6
- B1=0, B2=1, B3=2 (rapazes)
- G1=3, G2=4 (moças)

---

**Iteração 1 — BFS encontra caminho aumentante:**

```
Caminho: S → B1 → G1 → T
Gargalo: min(1, 1, 1) = 1
Fluxo: +1
```

Estado do grafo residual após aumentar:
- S→B1: cap_residual = 0 | B1→S: cap_residual = 1
- B1→G1: cap_residual = 0 | G1→B1: cap_residual = 1
- G1→T: cap_residual = 0 | T→G1: cap_residual = 1

**Max flow atual = 1**

---

**Iteração 2 — BFS encontra novo caminho:**

G1→T está saturada (cap_residual=0). A BFS tenta outro caminho:

```
Caminho: S → B1 → G2 → T
Gargalo: min(cap_residual S→B1=0, ...) — B1 já está saturado!
```

BFS tenta B2:
```
Caminho: S → B2 → G1 → T
Mas G1→T está saturada! cap_residual = 0.
```

BFS usa a aresta reversa: G1→B1 (cap_residual=1) — pode desfazer o par B1-G1 e tentar outro caminho para B1:
```
Caminho: S → B2 → G1 → B1 → G2 → T
Gargalo: min(1, 1, 1, 1, 1) = 1
Fluxo: +1
```

Resultado desta iteração: B2 emparelha com G1, e B1 é "redirecionado" para G2 graças à aresta reversa!

**Max flow atual = 2**

---

**Iteração 3 — BFS tenta novo caminho:**

- S→B3 disponível (cap=1). B3 só é compatível com G1.
- G1→T: cap_residual=0. Aresta reversa G1→B2 (cap=1). B2 não tem outro caminho.
- Não há caminho aumentante para T.

**BFS falha → Algoritmo encerra. Fluxo máximo = 2.**

---

**Extração dos Pares:**

Lemos as arestas `rapaz_i → moça_j` com `flow > 0`:

```
B1 → G2: flow=1  →  Par (1, 2)
B2 → G1: flow=1  →  Par (2, 1)
```

**Saída:**
```
2
1 2
2 1
```

**Verificação:** O emparelhamento `{(B1,G2), (B2,G1)}` é válido pois:
- B1-G2 é par compatível ✓ (estava na entrada)
- B2-G1 é par compatível ✓ (estava na entrada)
- B3 não tem par exclusivo disponível ✓ (G1 já usada)

---

## 5. Como o Fluxo é Convertido na Resposta

> _Tempo sugerido: até 1 minuto_

Após o algoritmo terminar, percorremos todas as arestas `rapaz_i → moça_j` na `FlowNetwork`. Qualquer aresta com `flow > 0` representa um par de dança confirmado.

```java
// Extração dos pares emparelhados
for (int v = 0; v < n; v++) {
    for (FlowEdge e : flowNetwork.adj(v)) {
        if (e.from() == v && e.to() >= n && e.to() < n + m && e.flow() > EPSILON) {
            int w = e.to();
            // Converte índices 0-based de volta para 1-based da saída
            System.out.println((v + 1) + " " + (w - n + 1));
        }
    }
}
```

A **saída** do problema exige:
1. Primeira linha: número máximo de pares (= valor do fluxo máximo)
2. Linhas seguintes: cada par `a b` (1-based)

---

## 6. Complexidade e Casos Especiais

### Complexidade

| Aspecto | Valor |
|---|---|
| **Tempo — Edmonds-Karp** | O(V · E²) no caso geral |
| **Tempo — com cap. unitárias** | O(E · √V) pois há no máximo O(√V) fases de BFS |
| **Memória — grafo residual** | O(V + E) — lista de arestas com referência para a reversa |
| **V (vértices)** | n + m + 2 |
| **E (arestas)** | n + m + k (fonte→rapazes + moças→sorvedouro + compatibilidades) |

**Limites do problema CSES 1696:**
- n, m ≤ 500 → V ≤ 1002
- k ≤ 1000 (pares compatíveis) → E ≤ 2000
- Solução cabe fácil dentro do tempo limite.

### Casos Especiais

| Caso | Comportamento |
|---|---|
| **Nenhum par compatível** (`k=0`) | BFS já falha na 1ª iteração. Saída: `0` (sem pares) |
| **Todos compatíveis** (`k = n*m`) | Emparelhamento máximo = `min(n, m)` |
| **Rapaz sem compatibilidade** | Aresta S→rapaz existe mas nenhuma aresta para moça. Aquele rapaz nunca recebe fluxo |
| **Moça sem compatibilidade** | Aresta moça→T existe mas sem entrada. Aquela moça nunca é emparelhada |
| **Múltiplos pares ótimos** | O problema aceita qualquer solução válida com o número máximo de pares. O Edmonds-Karp retorna um deles |
| **Arestas paralelas no input** | O enunciado garante pares únicos, mas se houvesse, a capacidade da aresta seria duplicada — isso não ocorre aqui pois usamos cap=1 e a estrutura de lista permite múltiplas arestas, porém a modelagem bipartida torna isso irrelevante |

---

## Resumo Final

```
Problema: CSES School Dance
Tipo:     Emparelhamento bipartido máximo por fluxo
Rede:     S → {rapazes} → {moças} → T, todas cap=1
Algoritmo: Edmonds-Karp (BFS no grafo residual)
Resultado: max_flow = número máximo de pares
Extração:  arestas rapaz→moça com flow > 0
Complexidade: O(E · √V) com cap. unitárias
```
