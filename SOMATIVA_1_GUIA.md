# Guia de Realização: Atividade Somativa 1
**Disciplina:** Desenvolvimento para Dispositivos Móveis
**Tema:** Aplicativo de Lista de Compras
**Tecnologia:** Android Nativo (Kotlin)

---

## 📌 Visão Geral
O objetivo desta atividade é desenvolver um aplicativo Android que funcione como uma **Lista de Compras**, permitindo que o usuário gerencie seus itens (CRUD completo). Você deverá demonstrar domínio da linguagem **Kotlin**, uso de **RecyclerView** para listagem e **Persistência de Dados**.

---

## 🛠️ Requisitos Técnicos
1. **Linguagem:** Kotlin (Sintaxe moderna e idiomática).
2. **Interface:** Material Design, layouts XML responsivos.
3. **Listagem:** Uso obrigatório de `RecyclerView`.
4. **CRUD:** Criar, Ler (Listar), Atualizar e Deletar itens.
5. **Persistência:** Banco de dados local (Room ou SQLite).

---

## 📖 Mapeamento por Unidade de Ensino

### [Unidade 1] Estrutura e Interface
* **O que aplicar:** Criação de Activities e layouts XML.
* **Ação:** Desenvolva a `MainActivity` (lista) e uma `FormActivity` (cadastro/edição). Utilize `ViewBinding` para organizar seu código.

### [Unidade 2] Programação em Kotlin
* **O que aplicar:** Data classes, `val`/`var`, Null Safety e Lambdas.
* **Ação:** Crie a classe de modelo `ItemCompra.kt`. Utilize lambdas para tratar os cliques nos botões da lista.

### [Unidade 3] RecyclerView e Listas Dinâmicas
* **O que aplicar:** `Adapter`, `ViewHolder` e `DiffUtil` (opcional).
* **Ação:** Implemente o `ComprasAdapter.kt`. Ele deve receber a lista de itens e inflar o layout de cada linha.

### [Unidade 4] Persistência com Banco de Dados
* **O que aplicar:** SQLite ou Room Database.
* **Ação:** Configure o banco para salvar os itens. Garanta que ao excluir um item na tela, ele também seja removido do banco.

---

## 🚀 Passo a Passo Sugerido

1. **Configuração Inicial:** 
   * Crie o projeto no Android Studio (Empty Views Activity).
   * Adicione as dependências do Room (opcional, mas recomendado) no `build.gradle`.

2. **Criação do Modelo:**
   ```kotlin
   data class ItemCompra(
       val id: Int = 0,
       val nome: String,
       val quantidade: String,
       val isComprado: Boolean = false
   )
   ```

3. **Layout da Lista:**
   * No `activity_main.xml`, coloque o `RecyclerView` e um `FloatingActionButton` para adicionar.
   * Crie o `item_compra.xml` com o design de cada linha da lista.

4. **Lógica do CRUD:**
   * **Create:** Tela com campos Nome e Quantidade.
   * **Read:** Carregar a lista do banco no `onResume` da Activity.
   * **Update:** Permitir editar o item ou marcar como "comprado".
   * **Delete:** Botão de excluir em cada item da lista.

---

## ⚖️ Critérios de Avaliação (Resumo)
* **Critério 1 (25%):** Uso correto do `RecyclerView` e componentes de interface.
* **Critério 2 (25%):** Uso de Kotlin de forma consistente e moderna.
* **Critério 3 (25%):** Lógica do CRUD funcional e código organizado.
* **Critério 4 (25%):** Interface clara, funcional e alinhada às diretrizes do Android.

---

## 📦 Checklist de Entrega
- [ ] Código fonte no **GitHub** (repositório público).
- [ ] Arquivo **README.md** com instruções básicas.
- [ ] **Vídeo demonstrativo** (máximo 3 minutos) mostrando:
  - Cadastro de um novo item.
  - Listagem dos itens.
  - Edição de um item.
  - Exclusão de um item.
- [ ] Certificar-se de que não há "hardcoded strings" (use `strings.xml`).

---
*Bons estudos e boa codificação!*
