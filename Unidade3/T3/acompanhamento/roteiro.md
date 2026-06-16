# Ficha de Acompanhamento — Grupo C

> **Problema:** [CSES 1696 — School Dance](https://cses.fi/problemset/task/1696)  
> **Foco:** Emparelhamento bipartido como rede de fluxo com capacidades unitárias  
> **Disciplina:** Resolução de Problemas com Grafos — Prof. Me Ricardo Carubbi

---

## 1. Resumo do Problema em Linguagem Própria

Há `n` rapazes e `m` moças. Algumas duplas são compatíveis para dançar juntas.
Cada pessoa pode participar de **no máximo um par** de dança.
O objetivo é encontrar o **maior número possível de pares simultâneos** e listar
quais duplas foram formadas.

Em outras palavras: queremos um **emparelhamento máximo** em um grafo bipartido
onde um lado representa os rapazes e o outro as moças, e as arestas indicam
compatibilidade.

---

## 2. Interpretação da Entrada e da Saída

### Entrada

```
n m k
a₁ b₁
a₂ b₂
...
aₖ bₖ
```

| Campo | Significado |
|---|---|
| `n` | Número de rapazes (1 ≤ n ≤ 500) |
| `m` | Número de moças (1 ≤ m ≤ 500) |
| `k` | Número de pares compatíveis (0 ≤ k ≤ 1 000) |
| `aᵢ bᵢ` | Rapaz `aᵢ` é compatível com moça `bᵢ` (indexação 1-based) |

### Saída

```
p
a₁ b₁
...
aₚ bₚ
```

| Campo | Significado |
|---|---|
| `p` | Quantidade máxima de pares formados |
| Linhas seguintes | Cada par efetivamente formado (qualquer solução ótima é aceita) |

---

## 3. Modelagem da Rede de Fluxo

### Vértices

| Vértice | Índice | Papel |
|---|---|---|
| **Fonte S** | `n + m` | Distribui exatamente 1 unidade para cada rapaz |
| **Rapazes** | `0` a `n-1` | Cada rapaz é um nó; recebe 1 unidade de S |
| **Moças** | `n` a `n+m-1` | Cada moça é um nó; envia 1 unidade ao sorvedouro |
| **Sorvedouro T** | `n + m + 1` | Coleta os pares; fluxo total = número de pares |

**Total de vértices:** `V = n + m + 2`

### Arestas e Capacidades

```
S         ──(cap=1)──► rapaz_i       para todo i ∈ [1..n]
rapaz_i   ──(cap=1)──► moça_j       para todo par compatível (i, j)
moça_j    ──(cap=1)──► T             para todo j ∈ [1..m]
```

### Justificativa das Capacidades

- **S → rapaz (cap = 1):** cada rapaz pode fazer parte de **no máximo 1 par**.
  A capacidade 1 impede que um mesmo rapaz seja emparelhado duas vezes.
- **rapaz → moça (cap = 1):** a aresta representa uma compatibilidade.
  Fluxo = 1 significa que a dupla foi efetivamente escolhida; cap = 1 garante
  que cada par é contado uma única vez.
- **moça → T (cap = 1):** cada moça pode fazer parte de **no máximo 1 par**.
  A capacidade 1 impede emparelhamentos duplos do lado feminino.

**Conclusão:** 1 unidade de fluxo de S a T equivale exatamente a 1 decisão
válida de emparelhamento. O valor do fluxo máximo é a resposta do problema.

---

## 4. Justificativa da Escolha: Edmonds-Karp

### Comparação

| Aspecto | Ford-Fulkerson (DFS) | Edmonds-Karp (BFS) |
|---|---|---|
| Busca do caminho aumentante | DFS — qualquer caminho | BFS — caminho mais curto |
| Complexidade geral | O(E · max_flow) | O(V · E²) |
| Com capacidades unitárias | O(E · min(n,m)) — pode ser lento | O(E · √V) — garantia adicional |
| Previsibilidade | Depende da ordem das arestas | Determinístico e previsível |

### Decisão

**Escolhemos Edmonds-Karp.**

Com capacidades unitárias e `n, m ≤ 500`, o fluxo máximo é no máximo
`min(500, 500) = 500`. Com Ford-Fulkerson e DFS, o custo no pior caso seria
`O(E · 500)`, o que pode ser aceitável, mas depende da ordem em que os
caminhos são explorados.

O Edmonds-Karp, ao usar BFS, garante que cada caminho aumentante encontrado
tem o menor número de arestas possível. Isso produz garantias mais fortes de
convergência e é a escolha **mais segura e previsível** para este problema.

---

## 5. Instância Pequena

Usamos o exemplo do enunciado do problema:

```
n=3, m=2, k=4

Compatibilidades:
  Rapaz 1 ↔ Moça 1
  Rapaz 1 ↔ Moça 2
  Rapaz 2 ↔ Moça 1
  Rapaz 3 ↔ Moça 1
```

### Grafo Construído

```
          Rapazes          Moças
          ┌───┐            ┌───┐
S ─(1)──► │ 1 │ ─(1)────► │ 1 │ ─(1)──► T
          └───┘    ╲       └───┘
          ┌───┐     ╲      ┌───┐
S ─(1)──► │ 2 │ ─(1)─┼──► │ 2 │ ─(1)──► T
          └───┘        ╲   └───┘
          ┌───┐          ╲
S ─(1)──► │ 3 │ ─(1)─────►(Moça 1 já conectada)
          └───┘
```

Numerando os índices internos (0-based):
- S = 5, T = 6
- Rapazes: 0 (R1), 1 (R2), 2 (R3)
- Moças: 3 (M1), 4 (M2)

Arestas criadas:

| Aresta | Cap |
|---|---|
| S(5) → R1(0) | 1 |
| S(5) → R2(1) | 1 |
| S(5) → R3(2) | 1 |
| R1(0) → M1(3) | 1 |
| R1(0) → M2(4) | 1 |
| R2(1) → M1(3) | 1 |
| R3(2) → M1(3) | 1 |
| M1(3) → T(6) | 1 |
| M2(4) → T(6) | 1 |

---

## 6. Execução Manual Passo a Passo (Edmonds-Karp)

### Estado inicial do grafo residual

Todas as arestas diretas com cap = 1 e as arestas reversas com cap = 0.

---

### Iteração 1 — BFS de S a T

BFS explora em largura. Um caminho possível encontrado:

```
S(5) → R1(0) → M1(3) → T(6)
```

**Gargalo:** `min(1, 1, 1) = 1`

**Atualização do residual:**
- S→R1: cap 1→0 | R1→S (reversa): 0→1
- R1→M1: cap 1→0 | M1→R1 (reversa): 0→1
- M1→T: cap 1→0 | T→M1 (reversa): 0→1

**Fluxo acumulado: 1**

---

### Iteração 2 — BFS de S a T

S→R1 está saturada (cap=0). BFS encontra outro caminho:

```
S(5) → R2(1) → M1(3) → T(6)
```

M1→T também está saturada (cap=0). Caminho bloqueado.

BFS tenta:

```
S(5) → R1(0) → M2(4) → T(6)
```

S→R1 está saturada. BFS tenta:

```
S(5) → R2(1) → M1(3) → T(6)
```

M1→T saturada. BFS tenta caminho alternativo via reversa:

```
S(5) → R2(1) → M1(3) ─(reversa)─► R1(0) → M2(4) → T(6)
```

Este caminho é válido no residual:
- S→R2: cap 1 ✓
- R2→M1: cap 1 ✓
- M1→R1 (reversa): cap 1 ✓ (desfaz R1→M1)
- R1→M2: cap 1 ✓
- M2→T: cap 1 ✓

**Gargalo:** `min(1, 1, 1, 1, 1) = 1`

**Atualização do residual:**
- S→R2: 1→0 | R2→S: 0→1
- R2→M1: 1→0 | M1→R2: 0→1
- M1→R1 (reversa): 1→0 | R1→M1: 0→1 (restaura!)
- R1→M2: 1→0 | M2→R1: 0→1
- M2→T: 1→0 | T→M2: 0→1

**Fluxo acumulado: 2**

**Efeito prático:** R1 foi desemparelhado de M1 e emparelhado com M2; R2 foi
emparelhado com M1. Isso demonstra o poder das arestas reversas: desfazer uma
decisão anterior para chegar a uma solução melhor.

---

### Iteração 3 — BFS de S a T

BFS tenta encontrar novo caminho:
- S→R1: saturada (0)
- S→R2: saturada (0)
- S→R3: cap 1 ✓ → R3→M1: cap 1 ✓ → M1→T: saturada (0)
- Não há caminho viável de S a T.

**BFS falha → algoritmo termina.**

**Fluxo máximo = 2**

---

## 7. Verificação da Resposta Final

### Extração dos pares

Percorremos as arestas da camada `rapazes → moças` e coletamos aquelas com
fluxo positivo no grafo residual (ou seja, cap_residual_reversa > 0, o que
indica que fluxo = 1 passou pela aresta direta).

| Aresta | Fluxo |
|---|---|
| R1(0) → M1(3) | 0 (desfeita na iteração 2) |
| R1(0) → M2(4) | 1 ✓ |
| R2(1) → M1(3) | 1 ✓ |
| R3(2) → M1(3) | 0 |

### Saída esperada

```
2
1 2
2 1
```

### Conferência

- 2 pares formados: `(R1, M2)` e `(R2, M1)` ✓
- Cada rapaz em no máximo 1 par ✓
- Cada moça em no máximo 1 par ✓
- Nenhum par além desse é possível:
  - R3 só poderia se emparelhar com M1, que já está ocupada ✓
- R3 fica sem par — correto, pois só existe aresta R3→M1 e M1 já foi
  alocada para R2.

**Resultado verificado:** fluxo máximo = 2, correspondendo exatamente ao
emparelhamento máximo da instância.
