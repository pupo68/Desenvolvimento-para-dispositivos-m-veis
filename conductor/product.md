# Initial Concept

a atividade somativa 1

# Product Guide: Atividade Somativa 1 - Lista de Compras

## 1. Visão do Produto
Um aplicativo Android nativo para gerenciamento de listas de compras. O objetivo principal é fornecer uma ferramenta simples, rápida e eficiente para que o usuário possa adicionar, visualizar, editar e remover itens que precisa comprar, com persistência local de dados para não perder informações ao fechar o aplicativo.

## 2. Público-Alvo
Qualquer pessoa que necessite organizar suas compras de mercado, feira ou tarefas diárias de consumo de maneira digital e offline.

## 3. Principais Funcionalidades (Core Features)
- **Visualização de Itens (Read):** Uma tela principal exibindo todos os itens da lista de compras em um `RecyclerView`.
- **Adição de Itens (Create):** Capacidade de cadastrar um novo item (nome e quantidade) através de uma tela de formulário ou caixa de diálogo.
- **Edição e Conclusão (Update):** Permitir a alteração dos dados de um item existente ou marcá-lo como "comprado/concluído".
- **Remoção de Itens (Delete):** Permitir que o usuário exclua itens da lista (com opção de feedback/desfazer se aplicável).
- **Persistência de Dados:** O aplicativo salvará todos os dados localmente usando o Room Database (SQLite), garantindo acesso offline e persistência entre sessões.

## 4. Requisitos Não Funcionais
- **Plataforma:** Android (Nativo).
- **Linguagem:** Kotlin.
- **Arquitetura:** Padrão MVVM (Model-View-ViewModel).
- **Interface:** Layouts baseados em XML, seguindo os princípios do Material Design 3. As strings devem ser internacionalizáveis (`strings.xml`).
- **Desempenho:** A lista deve renderizar as informações de forma eficiente utilizando `Adapter` e `ViewHolder`.

## 5. Critérios de Aceite (MVP)
- A interface exibe a lista usando os componentes nativos corretamente.
- A persistência local garante que as inserções, exclusões e edições sejam salvas permanentemente no dispositivo.
- O projeto não apresenta vazamentos de memória (uso adequado do ciclo de vida e `Dispatchers.IO` para DB).
- O aplicativo passa sem erros ou travamentos pelo fluxo de CRUD completo.