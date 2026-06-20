# Especificação: Implementação Completa — App Lista de Compras

## 1. Visão Geral
Implementar o aplicativo Android nativo da **Lista de Compras** do zero, cobrindo todas as 4 unidades de ensino da Atividade Somativa 1. O app deve oferecer CRUD completo de itens de compra com persistência local via Room Database, arquitetura MVVM, interface Material Design 3 e código Kotlin idiomático.

**Contexto:** O `build.gradle.kts` já está configurado com todas as dependências necessárias (Room, Material 3, RecyclerView, ViewBinding, Coroutines, MockK). A implementação parte do zero (nenhum código-fonte Kotlin ou layout XML existe ainda).

## 2. Requisitos Funcionais

### 2.1 Modelo de Dados (`ItemCompra`)
- **Campos:** `id` (Int, PK auto-gerada), `nome` (String), `quantidade` (String), `isComprado` (Boolean, default false).
- Implementado como `data class` Kotlin e anotado como `@Entity` do Room.

### 2.2 Persistência (Room Database)
- **DAO (`ItemCompraDao`):** Operações CRUD completas via Room:
  - `insertItem(item)` — inserir novo item.
  - `updateItem(item)` — atualizar item existente (nome, quantidade, status).
  - `deleteItem(item)` — remover item.
  - `getAllItems(): Flow<List<ItemCompra>>` — retornar lista reativa de todos os itens.
- **Database (`AppDatabase`):** Singleton com Room, instância criada via `Room.databaseBuilder`.
- **Repository (`ComprasRepository`):** Camada de abstração entre DAO e ViewModel, expondo `Flow` e executando operações de escrita em `Dispatchers.IO`.

### 2.3 Lógica de Negócios (ViewModel)
- **`ComprasViewModel`:** Mantém o estado da lista via `StateFlow` ou `LiveData`, expondo lista imutável. Operações de CRUD delegadas ao Repository via `viewModelScope`.
- Sobrevive a rotações de tela (configuração).
- Criado via `ViewModelFactory` com injeção de dependência manual.

### 2.4 Interface do Usuário

#### Tela Principal (`MainActivity`)
- **RecyclerView:** Exibe todos os itens via `ComprasAdapter` (estendendo `ListAdapter` com `DiffUtil.ItemCallback`).
- **FAB (FloatingActionButton):** Abre o formulário de adição.
- **Empty State:** Quando a lista estiver vazia, exibir ícone/ilustração amigável com texto encorajador e CTA (conforme product-guidelines).
- **Layout:** `ConstraintLayout` responsivo.

#### Formulário de Adição/Edição (`BottomSheetDialogFragment`)
- **Campos:** Nome (obrigatório) e Quantidade (obrigatório).
- **Modo duplo:** Adicionar novo item OU editar item existente (pré-preenche campos).
- **Validação:** Campos vazios impedem o salvamento, exibindo erro inline no `TextInputLayout`.
- **UX:** Teclado abre automaticamente no campo Nome ao abrir o BottomSheet.

#### Item da Lista (`item_compra.xml`)
- Exibe: Nome, Quantidade, Checkbox de "comprado", Botão de excluir (lixeira).
- **Marcar como comprado:** Checkbox alterna `isComprado`. Quando marcado:
  - Texto do nome recebe **strikethrough** (tachado).
  - Item é movido visualmente para o final da lista (ordenação: não-comprados primeiro, comprados ao final).
- **Editar:** Clique no item abre o BottomSheet em modo edição.
- **Excluir (botão):** Botão de lixeira no item exclui com Snackbar + "Desfazer".
- **Excluir (swipe):** Swipe para a esquerda no item também exclui com Snackbar + "Desfazer".

### 2.5 Adapter (`ComprasAdapter`)
- Estende `ListAdapter<ItemCompra, ComprasAdapter.ComprasViewHolder>` com `DiffUtil.ItemCallback`.
- Recebe lambdas para tratamento de eventos: `onItemClick`, `onCheckChanged`, `onDeleteClick`.
- Renderização eficiente sem recriar toda a lista.

## 3. Requisitos Não Funcionais
- **Kotlin Idiomático:** `val` > `var`, null safety (`?.`, `?:`), sem `!!`, data classes, lambdas.
- **Strings Internacionalizadas:** Todas as strings visíveis em `res/values/strings.xml` (zero hardcoded).
- **Threading:** Toda operação de banco fora da Main Thread (`Dispatchers.IO`).
- **Material Design 3:** Cores do tema, componentes nativos (FAB, TextInputLayout, Snackbar, BottomSheet, Cards).
- **Sem vazamentos de memória:** Uso correto do ciclo de vida.
- **Responsivo:** Funciona em diferentes tamanhos de tela via `ConstraintLayout`.

## 4. Critérios de Aceite
- [ ] CRUD completo funcional (Create, Read, Update, Delete).
- [ ] RecyclerView com ListAdapter + DiffUtil para atualizações eficientes.
- [ ] Persistência com Room — dados sobrevivem ao fechar/reabrir o app.
- [ ] Rotação de tela não perde dados (ViewModel).
- [ ] Marcar como comprado: checkbox + strikethrough + ordenação.
- [ ] Excluir via swipe e botão com Snackbar de Desfazer.
- [ ] Empty state amigável quando a lista estiver vazia.
- [ ] Zero strings hardcoded.
- [ ] Projeto compila e executa sem erros/crashes.

## 5. Fora do Escopo
- Múltiplas listas de compras (apenas 1 lista).
- Sincronização cloud / backend remoto.
- Autenticação de usuário.
- Categorias ou tags de itens.
- Fotos de produtos.
- Notificações/lembretes.
