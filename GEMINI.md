# GEMINI.md - Diretrizes do Projeto (Lista de Compras)

Este documento define os padrões técnicos, arquiteturais e de fluxo de trabalho para o desenvolvimento da Somativa 1. Siga estas diretrizes para garantir um código de nível sênior, manutenível e performante.

---

## 🏛️ Arquitetura e Padrões
- **Padrão de Projeto:** MVVM (Model-View-ViewModel).
    - **View:** Activities/Fragments (apenas lógica de UI).
    - **ViewModel:** Gerenciamento de estado e lógica de negócio.
    - **Model:** Entidades de dados e Repositories.
- **Injeção de Dependência:** Manual (via construtores) ou Hilt/Koin (se houver complexidade).
- **Persistência:** Room Database (abstração do SQLite).

## 🛠️ Tech Stack
- **Linguagem:** Kotlin 1.9+.
- **Build System:** Gradle (Kotlin DSL recomendado).
- **Interface:** XML Layouts com View Binding.
- **Componentes:** RecyclerView, FloatingActionButton, ViewModel, LiveData/Flow.

## 🎨 Padrões de Código (Kotlin Idioms)
- **Nomenclatura:** 
    - Classes: `PascalCase`.
    - Variáveis/Métodos: `camelCase`.
    - Recursos (XML/Imagens): `snake_case`.
- **Null Safety:** Use `?.` e `?:` (Elvis operator). Evite o uso de `!!`.
- **Imutabilidade:** Prefira `val` a `var`. Use `List` em vez de `MutableList` ao expor dados do ViewModel.
- **Strings:** Nunca use textos fixos (hardcoded) no código. Utilize sempre `res/values/strings.xml`.

## 📐 Diretrizes de UI/UX
- **Material Design 3:** Utilize as cores e estilos definidos no tema do projeto.
- **Feedback Visual:** Use `Snackbars` para confirmações de deleção (com opção de "Desfazer") e `Dialogs` para edições importantes.
- **Responsividade:** Utilize `ConstraintLayout` para garantir que a interface funcione em diferentes tamanhos de tela.

## 🔄 Fluxo de Trabalho
1. **Feature branch:** Trabalhe em branches separadas se estiver usando Git.
2. **Build:** Antes de cada commit, verifique se o projeto compila sem erros.
3. **Lint:** Execute `./gradlew lint` para verificar violações de estilo.
4. **Clean Code:** Remova imports não utilizados e códigos comentados.

## ✅ Validação (Checklist Sênior)
- [ ] O `RecyclerView` utiliza um `ListAdapter` ou `DiffUtil` para atualizações eficientes?
- [ ] O banco de dados é acessado fora da Thread principal (Dispatchers.IO)?
- [ ] As strings estão internacionalizadas?
- [ ] O app lida corretamente com mudanças de orientação (rotação de tela)?

---
*Referência de Implementação:* Veja `SOMATIVA_1_GUIA.md` para o passo a passo da Somativa 1.
