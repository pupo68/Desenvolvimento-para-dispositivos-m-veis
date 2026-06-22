# Descrição do Projeto de Software (SDD) - App Lista de Compras

## 1. Introdução
Este documento descreve a arquitetura e o design do aplicativo Android "Lista de Compras", desenvolvido como parte da Atividade Somativa 1 da disciplina de Desenvolvimento para Dispositivos Móveis.

## 2. Arquitetura Geral
O aplicativo segue a arquitetura **MVVM (Model-View-ViewModel)** com as seguintes camadas:
- **Camada de Dados:** Responsável pelo acesso aos dados (Room Database)
- **Camada de Domínio:** Contém as regras de negócio (Repository, Use Cases)
- **Camada de Apresentação:** Responsável pela UI e interação do usuário (ViewModel, Views)

## 3. Decisões de Design Arquitetural

### 3.1 Escolha do MVVM
- **Motivo:** Melhor separação de preocupações, testabilidade e sobrevivência a mudanças de configuração (rotação de tela)
- **Alternativas consideradas:** MVC, MVP, MVI
- **Justificativa:** MVVM é bem suportado pelas bibliotecas Android Jetpack (ViewModel, LiveData/Flow, Data Binding)

### 3.2 Uso do Room Database
- **Motivo:** Solução oficial de persistência do Android, integração com coroutines e LiveData/Flow
- **Alternativas considerados:** SQLite puro, SharedPreferences, Firebase
- **Justificativa:** Room fornece camada de abstração sobre SQLite com verificação em tempo de compilado

### 3.3 ViewBinding em vez de Data Binding ou findViewById
- **Motivo:** Segurança de tipos, melhor desempenho que findViewById, mais simples que Data Binding para este caso
- **Alternativas considerados:** Data Binding, ButterKnife (descontinuado), sintetic properties (Kotlin Android Extensions - descontinuado)
- **Justificativa:** ViewBinding é recomendado oficialmente pelo Android para acesso a views

### 3.4 Coroutines para operações assíncronas
- **Motivo:** Código assíncrono mais legível, integração nativa com Room e Lifecycle
- **Alternativas considerados:** RxJava, AsyncTask (descontinuado), Threads puro
- **Justificativa:** Coroutines são a recomendação atual do Android para programação assíncrona

## 4. Design dos Componentes Principais

### 4.1 Camada de Dados
#### 4.1.1 Modelo de Dados (ItemCompra)
```kotlin
@Entity(tableName = "item_compra")
data class ItemCompra(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val quantidade: String,
    val isComprado: Boolean = false
)
```
- **Decisão:** Usar `data class` para automaticamente gerar equals(), hashCode(), toString(), copy()
- **Anotações Room:** @Entity, @PrimaryKey com autoGenerate

#### 4.1.2 DAO (ItemCompraDao)
- **Operações CRUD:** insert, update, delete como suspend functions
- **Query reativa:** getAllItems() retorna Flow<List<ItemCompra>> com ordenação (não-comprados primeiro)
- **Decisão:** Usar Flow para atualizações reativas automáticas na UI

#### 4.1.3 Database (AppDatabase)
- **Singleton thread-safe:** Implementado com double-checked locking e volatile
- **Configuração:** fallbackToDestructiveMigration() para simplicidade neste projeto acadêmico
- **Nome do banco:** "compras_database"

#### 4.1.4 Repository (ComprasRepository)
- **Abstração:** Isola o ViewModel dos detalhes de implementação do DAO
- **Threading:** Operações de banco executadas em Dispatchers.IO (via Room já faz isso internamente)
- **Interface:** Expõe Flow<List<ItemCompra>> para observação reativa

### 4.2 Camada de Domínio
#### 4.2.1 Use Cases (implícitos no Repository)
Neste projeto simples, as regras de negócio são implementadas diretamente no Repository:
- Inserção, atualização, exclusão de items
- Toggle do status isComprado
- Ordenação da lista (não-comprados primeiro)

### 4.3 Camada de Apresentação
#### 4.3.1 ViewModel (ComprasViewModel)
- **Estado:** StateFlow<List<ItemCompra>> para expor a lista de forma imutável
- **Inicialização:** Coleta o Flow do Repository em init block
- **Operações:** Métodos públicos que delegam ao Repository com viewModelScope.launch
- **Sobrevivência a configuração:** ViewModel sobrevive a rotações de tela por padrão

#### 4.3.2 Adapter (ComprasAdapter)
- **Base:** ListAdapter com DiffUtil.ItemCallback para atualizações eficientes
- **DiffUtil:** Compara por id (areItemsTheSame) e conteúdo completo (areContentsTheSame)
- **ViewHolder:** Usa ViewBinding para acessar views do item_compra.xml
- **Renderização condicional:** Aplica strikethrough no texto do nome quando isComprado == true
- **Ordenação:** Delegada à query do DAO (ORDER BY isComprado ASC)

#### 4.3.3 Navegação e UI
- **MainActivity:** 
  - Configura RecyclerView com LinearLayoutManager
  - Observa StateFlow do ViewModel
  - FAB abre BottomSheet para adicionar item
  - Clique no item abre BottomSheet para edição
  - ItemTouchHelper para swipe-to-delete
- **BottomSheetDialogFragment:**
  - Modo duplo (adição/edição) via argumentos
  - Validação inline de campos obrigatórios
  - Foco automático no campo nome + abertura do teclado
- **Layouts:**
  - ConstraintLayout para responsividade
  - Material Design 3 components (MaterialCardView, TextInputLayout, FAB, etc.)
  - Empty state amigável quando lista vazia

## 5. Padrões de Projeto Utilizados
- **Repository Pattern:** Abstração de fontes de dados
- **ViewModel Pattern:** Gerenciamento de estado relacionado à UI
- **Observer Pattern:** Via Flow/StateFlow para atualizações reativas
- **Builder Pattern:** Indiretamente através das cópias de data classes (item.copy())
- **Factory Pattern:** ViewModelProvider.Factory para injeção de dependência manual

## 6. Tecnologias e Bibliotecas
- **Linguagem:** Kotlin 1.9+
- **SDK mínimo:** API 26 (Android 8.0)
- **SDK alvo:** API 34 (Android 14)
- **Bibliotecas principais:**
  - AndroidX Core, AppCompat, Material
  - Room Database (runtime, ktx, testing)
  - Coroutines (core, android, testing)
  - Lifecycle (runtime, ktx)
  - ViewBinding
  - RecyclerView
  - JUnit, MockK para testes
  - AndroidX Test (core, runner, rules, ext)

## 7. Qualidade e Boas Práticas
- **Kotlin idiomático:** Preferência por val sobre var, uso seguro de nulls (?., ?:), evitando !!
- **Strings internacionalizadas:** Todas em res/values/strings.xml
- **Nenhuma string hardcoded** no código ou layouts
- **Threading correto:** Operações de banco fora da main thread (tratado pelo Room/Coroutines)
- **Gerenciamento de lifecycle:** Uso correto de viewModelScope, evita vazamentos
- **Material Design 3:** Uso de componentes nativos e tema material3

## 8. Rastreabilidade com Requisitos
Este design implementa todos os requisitos funcionais e não funcionais especificados em spec.md:
- ✅ CRUD completo com Room
- ✅ RecyclerView com ListAdapter + DiffUtil
- ✅ Persistência com sobrevivência ao fechamento/reabertura
- ✅ Rotação de tela sem perda de dados (ViewModel)
- ✅ Marcar como comprado (checkbox + strikethrough + ordenação)
- ✅ Excluir via swipe e botão com Snackbar Desfazer
- ✅ Empty state amigável
- ✅ Zero strings hardcoded
- ✅ Compila e executa sem erros

## 9. Próximos Passos / Melhorias Futuras
- Adicionar testes de unidade para todas as camadas
- Implementar navegação com Navigation Component (opcional)
- Adicionar tema escuro
- Implementar backup/restauração via export/import JSON
- Adicionar filtros e busca na lista