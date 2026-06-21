# Plano de Implementação: App Lista de Compras

## Fase 1: Modelo de Domínio e Estrutura Base

- [x] Task: Criar estrutura de diretórios do projeto e arquivos base (b10da2d)
    - [x] Criar diretórios: `model/`, `data/`, `ui/`, `viewmodel/` sob `main/java/br/edu/pucpr/listacompras/`
    - [x] Criar `AndroidManifest.xml` com `MainActivity` declarada
    - [x] Criar `res/values/strings.xml`, `res/values/themes.xml`, `res/values/colors.xml` com valores base Material Design 3
- [x] Task: Implementar modelo `ItemCompra.kt` com TDD
    - [x] Escrever teste `ItemCompraTest.kt` validando criação com valores padrão, igualdade (data class) e propriedade `isComprado`
    - [x] Implementar `data class ItemCompra` com anotações `@Entity`, `@PrimaryKey(autoGenerate = true)`
    - [x] Executar testes e confirmar que passam
- [x] Task: Conductor - User Manual Verification 'Fase 1: Modelo de Domínio e Estrutura Base' (Protocol in workflow.md)

## Fase 2: Camada de Dados com Room

- [x] Task: Implementar `ItemCompraDao.kt` com TDD (b10da2d)
    - [x] Escrever teste `ItemCompraDaoTest.kt` validando insert, update, delete e getAllItems (Flow)
    - [x] Implementar `@Dao interface ItemCompraDao` com operações CRUD
    - [x] Executar testes e confirmar que passam
- [x] Task: Implementar `AppDatabase.kt` (b10da2d)
    - [x] Criar `@Database` abstrata com singleton thread-safe
    - [x] Expor `itemCompraDao()` como método abstrato
- [x] Task: Implementar `ComprasRepository.kt` com TDD (b10da2d)
    - [x] Escrever teste `ComprasRepositoryTest.kt` mockando o DAO com MockK
    - [x] Implementar Repository expondo `Flow<List<ItemCompra>>` e operações CRUD em `Dispatchers.IO`
    - [x] Executar testes e confirmar que passam
- [ ] Task: Conductor - User Manual Verification 'Fase 2: Camada de Dados com Room' (Protocol in workflow.md)

## Fase 3: Lógica de Negócios (ViewModel e Adapter)

- [ ] Task: Implementar `ComprasViewModel.kt` com TDD
    - [ ] Escrever teste `ComprasViewModelTest.kt` mockando Repository com MockK
    - [ ] Validar: carregamento da lista, adição, edição, deleção, toggle de `isComprado`
    - [ ] Implementar ViewModel com `viewModelScope`, expondo `StateFlow<List<ItemCompra>>`
    - [ ] Implementar ordenação: não-comprados primeiro, comprados ao final
    - [ ] Executar testes e confirmar que passam
- [ ] Task: Implementar `ComprasViewModelFactory.kt`
    - [ ] Criar `ViewModelProvider.Factory` para injeção manual do Repository no ViewModel
- [ ] Task: Implementar `ComprasAdapter.kt` com `ListAdapter` e `DiffUtil`
    - [ ] Criar `DiffUtil.ItemCallback<ItemCompra>` comparando `id` e conteúdo
    - [ ] Implementar `ComprasViewHolder` com ViewBinding para `item_compra.xml`
    - [ ] Receber lambdas: `onItemClick`, `onCheckChanged`, `onDeleteClick`
    - [ ] Aplicar strikethrough condicional no nome quando `isComprado == true`
- [ ] Task: Conductor - User Manual Verification 'Fase 3: Lógica de Negócios' (Protocol in workflow.md)

## Fase 4: Interface do Usuário e Integração Final

- [ ] Task: Implementar `ComprasApplication.kt`
    - [ ] Criar classe `Application` personalizada para inicializar `AppDatabase` e `ComprasRepository`
    - [ ] Registrar no `AndroidManifest.xml`
- [ ] Task: Criar layouts XML com Material Design 3
    - [ ] `activity_main.xml`: `ConstraintLayout` com `RecyclerView`, `FAB`, e layout de empty state (icon + texto + CTA)
    - [ ] `item_compra.xml`: `MaterialCardView` com `CheckBox`, `TextView` (nome), `TextView` (quantidade), `ImageButton` (lixeira)
    - [ ] `fragment_form_bottom_sheet.xml`: `TextInputLayout` para Nome e Quantidade, `MaterialButton` para salvar
    - [ ] Todas as strings em `res/values/strings.xml`
- [ ] Task: Implementar `FormBottomSheetFragment.kt`
    - [ ] Receber item existente (edição) ou null (adição) via `arguments`
    - [ ] Validação inline: campos vazios mostram erro no `TextInputLayout`
    - [ ] Foco automático no campo Nome + teclado aberto
    - [ ] Comunicar resultado de volta para a Activity (via `setFragmentResult` ou callback)
- [ ] Task: Implementar `MainActivity.kt`
    - [ ] Configurar RecyclerView com `ComprasAdapter` e `LinearLayoutManager`
    - [ ] Observar `StateFlow` do ViewModel e submeter lista ao adapter
    - [ ] FAB abre `FormBottomSheetFragment` em modo adição
    - [ ] Clique no item abre `FormBottomSheetFragment` em modo edição
    - [ ] Checkbox alterna `isComprado` via ViewModel
    - [ ] Botão lixeira exclui item via ViewModel + Snackbar com \"Desfazer\"
    - [ ] `ItemTouchHelper` para swipe-to-delete com Snackbar + \"Desfazer\"\n    - [ ] Exibir/ocultar empty state conforme lista vazia/preenchida
- [ ] Task: Polimento final e validação
    - [ ] Verificar zero strings hardcoded
    - [ ] Validar rotação de tela sem perda de dados
    - [ ] Revisar tema Material Design 3 (cores, tipografia)
    - [ ] Limpar imports não utilizados e código morto
- [ ] Task: Conductor - User Manual Verification 'Fase 4: Interface do Usuário e Integração Final' (Protocol in workflow.md)