# Pilha de Tecnologia (Tech Stack)

Este documento define as tecnologias, frameworks e bibliotecas escolhidas para o desenvolvimento da Atividade Somativa 1.

## Linguagem
- **Kotlin:** Linguagem principal do projeto. Escolhida por ser moderna, concisa, segura contra nulidade (Null Safety) e a linguagem oficial recomendada pelo Google para o desenvolvimento Android.

## Plataforma
- **Android Nativo:** O aplicativo será desenvolvido usando o SDK nativo do Android.

## Arquitetura
- **MVVM (Model-View-ViewModel):** Padrão arquitetural escolhido para separar a lógica de negócios da interface do usuário, facilitando a manutenção e testes.

## Interface do Usuário (UI)
- **XML Layouts & ViewBinding:** A interface será construída usando a abordagem tradicional de arquivos XML combinada com ViewBinding para acesso seguro aos componentes da UI no código.
- **RecyclerView:** Componente essencial e obrigatório para a listagem performática dos itens de compra.

## Persistência de Dados
- **Room Database:** Abstração oficial do Google sobre o SQLite. Facilita a criação do CRUD local, verificação de erros em tempo de compilação e integração nativa com Kotlin Coroutines/Flow.

## Assincronismo
- **Kotlin Coroutines:** Utilizadas para operações de banco de dados (I/O) e outras tarefas assíncronas, garantindo que a Thread Principal (UI) não seja bloqueada.