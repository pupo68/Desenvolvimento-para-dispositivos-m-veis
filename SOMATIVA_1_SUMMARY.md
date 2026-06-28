# Sumativa 1 – Checklist de Conformidade e Próximas Etapas

## 📄 Visão geral da atividade
A atividade consiste em desenvolver um aplicativo Android **Lista de Compras** que implemente um fluxo completo de CRUD, usando **Kotlin**, **RecyclerView**, e **persistência local (Room)**. Os critérios de avaliação estão resumidos no arquivo `CRITERIOS.txt` e nos critérios de avaliação da guia `SOMATIVA_1_GUIA.md`.

---

## ✅ O que já está coberto pelos testes existentes

| Critério | Onde está testado | Como está testado |
|----------|-------------------|-------------------|
| **Critério 1 – UI / RecyclerView** | `MainActivity.kt`, `ComprasAdapter.kt`, layouts XML | **Nenhum teste automatizado de UI**. O código está implementado, mas não há verificação de presença do `RecyclerView`, do `Adapter`, nem de interações de toque. |
| **Critério 2 – Uso idiomático do Kotlin** | Todo o código Kotlin (model, DAO, repository, ViewModel, UI) | Passa em todos os **unit tests**, indicando que a sintaxe está correta e o código compila sem erros. |
| **Critério 3 – Lógica de CRUD** | `ItemCompraDaoTest.kt`, `ComprasRepositoryTest.kt`, `ComprasRepositoryAdditionalTest.kt`, `ComprasViewModelTest.kt` | Cobertura completa de inserção, leitura, atualização, exclusão e toggle de status em todas as camadas (DAO → Repository → ViewModel). |
| **Critério 4 – Interface clara e alinhada ao Material Design** | Layouts (`activity_main.xml`, `item_compra.xml`, `dialog_item_form.xml`) usam componentes Material (FAB, BottomSheetDialog, Snackbar, etc.) | Não há testes que validem visualmente ou de usabilidade; a implementação segue boas práticas, porém falta evidência automatizada. |

---

## ❌ O que **falta** para garantir a nota máxima

| Falta | Por quê | Como suprir |
|-------|---------|-------------|
| **Testes de UI (instrumented)** | Testes de unidade não executam o Android framework, portanto não validam componentes de interface nem interações de usuário. | Criar **instrumented tests** (`androidTest/java/...`) com **Espresso** ou **UI Automator** que verificam: <br>• Existência do `RecyclerView` e do FAB. <br>• Fluxo completo de CRUD via UI (abrir dialog, salvar, editar, marcar como comprado, deletar, desfazer). <br>• Exibição da *empty state* quando a lista está vazia. <br>• Mensagens de `Snackbar` corretas. |
| **Cobertura de casos de borda na UI** | Não há teste para validação de campos vazios, rotação de tela, estado “comprado” com strikethrough, etc. | - Testar que o botão **Salvar** fica desabilitado quando nome ou quantidade estão vazios e que os erros são exibidos. <br>- Simular rotação de tela (`ActivityScenario.recreate()`) e confirmar que a lista persiste. <br>- Verificar que itens marcados como comprados exibem `STRIKE_THRU_TEXT_FLAG`. |
| **Testes de acessibilidade (opcional, mas pontua o critério 4)** | Boa UI não garante acessibilidade. | Utilizar **AccessibilityChecks** nos instrumented tests e corrigir eventuais problemas (contentDescription, tamanho de text, contraste). |
| **Validação de strings e recursos** | O código usa `strings.xml`, porém não há verificação automática de que nenhuma string está hard‑coded. | Testar com **Robolectric** ou instrumented test que todas as mensagens exibidas são obtidas via `getString(R.string.… )`. |
| **Teste de Swipe‑to‑Delete** | `ItemTouchHelper` está configurado, porém não há teste que confirme que o swipe chama `confirmDelete` e dispara o `Snackbar` de *undo*. | Instrumented test que arrasta o item para a esquerda e verifica: <br>• Diálogo de confirmação aparece. <br>• Após confirmar, o `Snackbar` de “Item deletado” aparece. <br>• Ao tocar “Undo”, o item retorna à lista. |
| **Teste de persistência de estado ao fechar/reabrir app** | Não há verificação de que os itens permanecem no banco após a activity ser destruída. | Teste de integração que adiciona itens, finaliza a activity (ou simula `onStop/onDestroy`), reabre e verifica que a lista ainda contém os mesmos itens (usando o DAO ou o ViewModel). |

---

## 📋 Checklist de próximos passos (TDD + SDD)

1. **Criar módulo de instrumented tests** (`app/src/androidTest/java/br/edu/pucpr/listacompras/ui/`).
   - Classe `MainActivityInstrumentedTest.kt` com cenários básicos (exibição da lista, FAB, dialog). 
2. **Implementar teste de fluxo CRUD via UI** (Create → Read → Update → Delete) antes de mudar o código – **red‑green‑refactor**.
3. **Adicionar teste de Swipe‑to‑Delete** e de *undo* com `Snackbar`.
4. **Cobertura de validação de formulário** (campos vazios, mensagens de erro). 
5. **Testar rotação de tela** e garantir que o estado da lista persiste. 
6. **Executar AccessibilityChecks** e corrigir eventuais falhas (contentDescription, contraste). 
7. **Criar teste de integração de persistência** que verifica o comportamento após fechar e reabrir o app. 
8. **Rodar `./gradlew testDebugUnitTest` e `./gradlew connectedAndroidTest`** para garantir que todos os testes (unit + UI) passem. 
9. **Atualizar o relatório de critérios** (`CRITERIOS_VERIFICADOS.md`) listando cada critério e o teste correspondente que o comprova. 
10. **Documentar no README** como executar a suite completa (ex.: `./gradlew clean testDebugUnitTest connectedAndroidTest`).

---

## 🚀 Iniciando a implementação TDD/SDD
A sequência recomendada é:
1. **Escrever o primeiro instrumented test** (ex.: abrir o dialog de adição e salvar um item). <br>2. **Executar o teste** – ele falhará (vermelho). <br>3. **Implementar o mínimo necessário** no código da UI ou ViewModel para que o teste passe (verde). <br>4. **Refatorar** mantendo os testes verdes. <br>5. Repetir para cada funcionalidade (edição, toggle, delete, undo, rotação). 

Esse fluxo garante que a aplicação evolua com **alta cobertura**, alinhada aos critérios da somativa.

---

*Arquivo gerado automaticamente para auxiliar no planejamento e acompanhamento da entrega da Atividade Somativa 1.*
