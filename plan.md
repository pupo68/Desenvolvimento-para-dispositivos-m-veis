# Plano de Desenvolvimento - Lista de Compras

Este arquivo é a fonte de verdade para o progresso do projeto. Cada tarefa segue o workflow TDD do Conductor.

## Fase 1: Setup do Projeto e Modelo de Domínio
- [x] Configurar os arquivos base do Gradle Kotlin DSL (settings, build.gradle, gradle.properties) e estrutura de diretórios do app (aaffb72)
- [~] Criar classe de modelo `ItemCompra.kt` com TDD (failing test em `ItemCompraTest.kt` -> implementação -> passing test)

## Fase 2: Camada de Dados com Room
- [ ] Configurar Room Database (`ItemCompraDao.kt`, `AppDatabase.kt`, `ComprasRepository.kt`) com TDD (`ComprasRepositoryTest.kt`)

## Fase 3: Camada de Lógica de Negócios (ViewModel e Adapter)
- [ ] Implementar `ComprasViewModel.kt` com TDD (`ComprasViewModelTest.kt` simulando o repositório com MockK)
- [ ] Implementar `ComprasAdapter.kt` estendendo `ListAdapter` com `DiffUtil`

## Fase 4: Interface do Usuário e Fluxo de Telas (CRUD)
- [ ] Configurar `ComprasApplication.kt` e injeção de dependência manual (Factory)
- [ ] Desenvolver layouts XML (`activity_main.xml`, `activity_form.xml`, `item_compra.xml`) com Material Design 3 e strings internacionalizadas
- [ ] Implementar `MainActivity.kt` e `FormActivity.kt` conectando com ViewModel e tratando rotação de tela
- [ ] Realizar build final do app e gerar APK para validação manual
