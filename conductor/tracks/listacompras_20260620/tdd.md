# Test-Driven Development (TDD) - App Lista de Compras

## 1. Introdução
Este documento descreve a abordagem de Test-Driven Development (TDD) adotada no desenvolvimento do aplicativo "Lista de Compras". TDD é uma metodologia de desenvolvimento onde os testes são escritos antes do código de produção, seguindo o ciclo "Red-Green-Refactor".

## 2. Princípios do TDD Aplicados
### 2.1 O Ciclo TDD
1. **Red:** Escrever um teste que falhe inicialmente
2. **Green:** Escrever o mínimo código necessário para fazer o teste passar
3. **Refactor:** Melhorar o código sem mudar seu comportamento (mantendo todos os testes passando)
4. **Repetir:** Para o próximo pequeno incremento de funcionalidade

### 2.2 Regras do TDD Seguidas
- Nunca escrever código de produção sem antes ter um teste falhando
- Não escrever mais código de teste do que o suficiente para falhar (ou falhar em compilação)
- Não escrever mais código de produção do que o suficiente para passar o teste que está falhando

## 3. Estratégia de Testes por Camada

### 3.1 Camada de Dados (Model)
**Objetivo:** Validar o comportamento do modelo de domínio ItemCompra
- **Testes:** Criação com valores padrão, propriedades da data class (equals, hashCode, toString, copy), comportamento de isComprado
- **Framework:** JUnit 4 + AssertJ/Truth (ou apenas Assert do JUnit para simplicidade)
- **Local:** src/test/java/br/edu/pucpr/listacompras/model/
- **Exemplo:** ItemCompraTest.kt (já existente)

### 3.2 Camada de Dados (DAO)
**Objetivo:** Validar as operações de acesso ao banco de dados
- **Testes:** insert, update, delete, queries (getAllItems com ordenação)
- **Abordagem:** 
  - Usar `androidx.room:room-testing` para criar um banco em memória
  - Testar cada método do DAO isoladamente
  - Verificar tanto o comportamento quanto as queries geradas
- **Framework:** JUnit 4 + AndroidX Test Core + Room Testing
- **Local:** src/test/java/br/edu/pucpr/listacompras/db/
- **Exemplo nome:** ItemCompraDaoTest.kt

### 3.3 Camada de Dados (Repository)
**Objetivo:** Validar a camada de abstração entre ViewModel e DAO
- **Testes:** 
  - Delegação correta ao DAO para cada operação
  - Tratamento de exceções (se aplicável)
  - Uso correto de coroutines (se houver lógica adicional)
- **Abordagem:** 
  - Mockar o DAO com MockK
  - Verificar que os métodos do DAO são chamados com os parâmetros corretos
  - Testar o exposto Flow (se houver transformação)
- **Framework:** JUnit 4 + MockK + kotlinx-coroutines-test
- **Local:** src/test/java/br/edu/pucpr/listacompras/repository/
- **Exemplo nome:** ComprasRepositoryTest.kt

### 3.4 Camada de Domínio (Use Cases - se existentes)
**Objetivo:** Validar regras de negócio puras
- **Testes:** Lógica de negócio que não depende de Android
- **Abordagem:** 
  - Testar classes puras de Kotlin
  - Usar apenas JUnit (não precisa de AndroidX Test)
- **Local:** src/test/java/br/edu/pucpr/listacompras/domain/ (se criado)
- **Nota:** Neste projeto, as regras de negócio estão no Repository/ViewModel

### 3.5 Camada de Apresentação (ViewModel)
**Objetivo:** Validar a lógica de apresentação e orquestração
- **Testes:**
  - Inicialização correta e coleta do Flow do Repository
  - Adição, atualização, deleção de items via Repository
  - Toggle do status isComprado
  - Ordenação da lista (não-comprados primeiro)
  - Tratamento de erros (se aplicável)
- **Abordagem:**
  - Mockar o Repository com MockK
  - Usar TestCoroutineDispatcher para testar coroutines
  - Verificar que StateFlow emite os valores esperados
  - Testar o ViewModelFactory
- **Framework:** JUnit 4 + MockK + androidx.test.core:core + androidx.test.ext:jkt + kotlinx-coroutines-test
- **Local:** src/test/java/br/edu/pucpr/listacompras/ui/
- **Exemplo nome:** ComprasViewModelTest.kt

### 3.6 Camada de Apresentação (Adapter)
**Objetivo:** Validar a renderização eficiente e tratamento de eventos
- **Testes:**
  - DiffUtil.detectar mudanças corretamente (mesmo id, conteúdo diferente)
  - Bind corretamente os dados nas views (com ViewBinding mockado ou real)
  - Aplicação condicional de strikethrough
  - Chamada correta dos lambdas de evento
- **Abordagem:**
  - Testar DiffUtil.ItemCallback diretamente
  - Para ViewHolder: usar Robolectric ou testes instrumentados se necessário interagir com views reais
  - Alternativa: extrair lógica pura (formatção de texto) para funções testáveis unitariamente
- **Framework:** JUnit 4 + (opcional) Robolectric ou AndroidX Test
- **Local:** src/test/java/br/edu/pucpr/listacompras/ui/adapter/
- **Exemplo nome:** ComprasAdapterTest.kt

## 4. Estrutura de Diretórios de Testes
```
src/test/
└ java/
   └ br/edu/pucpr/listacompras/
      ├ model/
      │   └ ItemCompraTest.kt
      ├ db/
      │   ├ ItemCompraDaoTest.kt
      │   └ AppDatabaseTest.kt (opcional)
      ├ repository/
      │   └ ComprasRepositoryTest.kt
      ├ ui/
      │   ├ viewmodel/
      │   │   └ ComprasViewModelTest.kt
      │   └ adapter/
      │       └ ComprasAdapterTest.kt
      └ utils/ (se houver classes de utilidade)
          └ [NomeDoUtil]Test.kt
```

## 5. Ferramentas e Bibliotecas de Teste
### 5.1 Dependências de Teste (já no build.gradle.kts)
```kotlin
// Testing
testImplementation("junit:junit:4.13.2")
testImplementation("io.mockk:mockk:1.13.9")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
testImplementation("androidx.arch.core:core-testing:2.2.0") // LiveData testing helper
testImplementation("androidx.room:room-testing:$roomVersion")
```

### 5.2 Uso de MockK
- **Vantagem sobre Mockito:** Sintaxe mais concisa e kotlin-friendly
- **Exemplo de uso:**
  ```kotlin
  private val dao = mockk<ItemCompraDao>(relaxed = true)
  private val repository = ComprasRepository(dao)
  
  @Before
  fun setup() {
      // Configura comportamento padrão se necessário
      every { dao.getAllItems() } returns flowOf(emptyList())
  }
  
  @Test
  fun `test add item delegates to dao`() = runTest {
      val item = ItemCompra(nome = "Test", quantidade = "1")
      repository.addItem(item)
      
      verify(exactly = 1) { dao.insertItem(item) }
  }
  ```

### 5.3 Testes de Coroutines
- **TestCoroutineDispatcher:** Para controlar a execução de coroutines em testes
- **runTest bloco:** Para testar código suspendido de forma síncrona no teste
- **Advance until idle / advanceTimeBy:** Para controlar o tempo virtual

## 6. Processo TDD Passo a Passo (Exemplo)
Vamos ilustrar com a implementação do `ItemCompraDao.insertItem()`:

### Passo 1: Escrever o teste falho (Red)
```kotlin
@Test
fun `test insertItem calls DAO insert`() = runTest {
    // Arrange
    val dao = mockk<ItemCompraDao>(relaxed = true)
    val repository = ComprasRepository(dao)
    val item = ItemCompra(nome = "Arroz", quantidade = "1 kg")
    
    // Act
    repository.insertItem(item)
    
    // Assert
    verify(exactly = 1) { dao.insertItem(item) }
}
```
*Este teste falhará porque o método insertItem ainda não existe no Repository.*

### Passo 2: Implementar o mínimo para passar (Green)
```kotlin
class ComprasRepository(private val dao: ItemCompraDao) {
    suspend fun insertItem(item: ItemCompra) {
        dao.insertItem(item)
    }
    // ... outros métodos
}
```
*Agora o teste deve passar.*

### Passo 3: Refactor (se necessário)
- Verificar se há duplicação
- Melhorar nomes se necessário
- Garantir que o código siga as convenções do projeto
- **Importante:** Rodar todos os testes novamente para garantir que nada quebrou

### Passo 4: Repetir para o próximo pequeno incremento
Próximo teste pode ser para updateItem, deleteItem, ou testar que getAllItems retorna o Flow correto.

## 7. Estratégia de Testes Específicos por Funcionalidade

### 7.1 CRUD Operations
Para cada operação (Create, Read, Update, Delete):
1. Teste que chama o método correto do DAO/Repository
2. Teste com dados válidos
3. Teste com dados de borda (strings vazias, valores nulos se permitido)
4. Teste de fluxo completo (se aplicável)

### 7.2 Operações Especiais do App

#### 7.2.1 Toggle isComprado
- Teste que altera apenas o campo isComprado, preservando outros campos
- Teste ViewModel.toggleItemStatus(itemId) delega corretamente
- Teste que o Flow atualiza com o item modificado

#### 7.2.2 Ordenação da Lista
- Teste DAO query: verifica ORDER BY isComprado ASC, nome ASC
- Teste ViewModel: confirma que items emitidos estão na ordem correta
- Teste de fronteira: todos comprados, todos não-comprados, mistura

#### 7.2.3 Operações com Desfazer (Snackbar)
- Este é mais de UI/integração, mas podemos testar:
  - ViewModel expõe uma forma de desfazer (opcional, depende da implementação)
  - Repository tem método para restaurar item deletado (se implementado com cópia)

### 7.3 Testes de Estado e Lifecycle
#### 7.3.1 ViewModel sobrevive a configuração
- Teste que ViewModel retém estado após "recriação" (no teste, isso é implícito se não houver vazamento)
- Mais relevante em testes instrumentados, mas podemos verificar que não há vazamentos óbvios

#### 7.3.2 Estado inicial vazio
- Teste que ao iniciar, items Flow emite lista vazia inicialmente
- Depois de inserir item, emite lista com um item

## 8. Diretrizes de Escrita de Testes Bons

### 8.1 Nomenclatura de Testes
- Usar backticks para nomes descritivos em Kotlin
- Formato: `test [o que está sendo testado] [condição] [resultado esperado]`
- Exemplo: `test addItem delegates to dao insert with correct parameters`

### 8.2 AAA Pattern (Arrange, Act, Assert)
- **Arrange:** Preparar objetos de teste e mocks
- **Act:** Executar o método sob teste
- **Assert:** Verificar o resultado

### 8.3 Testes Focados
- Cada teste deve verificar uma única coisa
- Se precisar de múltiplos asserts, verifique se estão todos relacionados ao mesmo comportamento
- Preferir múltiplos testes pequenos a um teste grande com muitos asserts

### 8.4 Evitar Testes Frágeis
- Não testar detalhes de implementação que podem mudar
- Testar o comportamento público, não métodos privados
- Para testes de UI, evitar depender de posicionamento exato de pixels quando possível

### 8.5 Manter Testes Rápidos
- Testes unitários devem rodar em milissegundos
- Evitar I/O desnecessário, uso real de Android quando possível usar mocks
- Usar bancos em memória para testes de Room

## 9. Cobertura de Testes Objetivo
- **Meta:** >80% de cobertura de linhas em código de produção (excluindo classes auto-generated como ViewBinding)
- **Foco nas camadas críticas:** Model, DAO, Repository, ViewModel, Adapter
- **Testes de UI (Activity/Fragment):** Deixar para testes instrumentados ou de aceitação separados

## 10. Integração com Desenvolvimento Contínuo
- Todos os testes devem passar no commit local antes de push
- Em ambiente ideal, seriam executados em pipeline de CI/CD
- Para este projeto acadêmico, executar localmente com:
  ```bash
  ./gradlew testDebugUnitTest
  ```
  ou
  ```bash
  ./gradlew connectedAndroidTest   # para testes instrumentados
  ```

## 11. Benefícios Observados do TDD neste Projeto
### 11.1 Design Melhor
- Código mais testável leva a baixo acoplamento e alta coesão
- Interfaces claras entre camadas (ex: Repository como contrato entre ViewModel e DAO)

### 11.2 Detecção Precoce de Defeitos
- Bugs encontrados imediatamente após escrever o código, não dias/semanas depois
- Redução significativa de tempo dépurado

### 11.3 Documentação Viva
- Testes servem como exemplos de uso da API
- Novo membro da equipe pode entender o comportamento lendo os testes

### 11.4 Confiança para Refatorar
- Pode melhorar implementação sabendo que testes capturarão regressões
- Essencial para evoluir o código à medida que se aprende mais sobre o domínio

## 12. Desafios e Como Superá-los
### 12.1 "Não sei o que testar primeiro"
- **Solução:** Comece pelo comportamento mais simples ou mais crítico
- Para este projeto: Comece pelo Model (ItemCompra), depois DAO, depois Repository

### 12.2 "Testes de Android são lentos/complicados"
- **Solução:** Maximize testes unitários puros (não exigem Android)
- Use bibliotecas de teste do AndroidX que rodam na JVM (como Architecture Components Testing)
- Reserve testes instrumentados apenas para quando realmente precisar de framework Android

### 12.3 "TDD me deixa mais lento inicialmente"
- **Solução:** O investimento inicial paga depois com menos bugs e mais confiança
- Foque em pequenos incrementos - a velocidade aumenta com prática
- Lembre-se: você não está escrevendo mais código, está escrevendo em ordem diferente

## 13. Checklist Antes de Mover para Próxima Funcionalidade
Antes de considerar uma feature "pronta" via TDD:
- [ ] Todos os testes unitários relacionados estão passando
- [ ] Testes de integração (se aplicável) estão passando
- [ ] Código foi refatorado e está limpo
- [ ] Nenhum comentário TODO relacionado à funcionalidade
- [ ] Testes cobrem casos de uso comuns e de borda
- [ ] Documentação atualizada (se necessária, como atualizar este documento)

## 14. Exemplo Completo de TDD: Implementação do Toggle isComprado

### Teste (Red)
```kotlin
@Test
fun `test toggleItemStatus calls repository to update item`() = runTest {
    // Arrange
    val repository = mockk<ComprasRepository>(relaxed = true)
    val viewModel = ComprasViewModel(repository)
    val originalItem = ItemCompra(id = 1, nome = "Test", quantidade = "1", isComprado = false)
    val toggledItem = originalItem.copy(isComprado = true)
    
    // Act
    viewModel.toggleItemStatus(originalItem)
    
    // Assert
    verify(exactly = 1) { repository.updateItem(toggledItem) }
}
```

### Implementação (Green)
```kotlin
class ComprasViewModel(private val repository: ComprasRepository) : ViewModel() {
    // ... outros códigos
    
    fun toggleItemStatus(item: ItemCompra) = viewModelScope.launch {
        val toggledItem = item.copy(isComprado = !item.isComprado)
        repository.updateItem(toggledItem)
    }
}
```

### Refactor
- Extrair lógica de toggle para função privada se usada em múltiplos lugares
- Adicionar tratamento de erro se necessário
- Verificar nomes e documentação

## 15. Conclusão
TDD não é apenas uma técnica de teste, mas uma metodologia de design que leva a código mais robusto, mantençível e bem arquitetado. Ao aplicar TDD rigorosamente neste projeto, garantimos que:
- Cada peça de funcionalidade tem cobertura de teste automatizada
- O código é projetado para ser testável desde o início
- Temos confiança para evoluir e melhorar o aplicativo ao longo do tempo
- O cumprimento dos requisitos é verificável continuamente

A disciplina de escrever testes primeiro pode parecer lenta inicialmente, mas resulta em velocidade de desenvolvimento maior a médio e longo prazo devido à redução de retrabalho e aumento da confiança no código.

---
*Este documento deve ser evolutivo. À medida que avançamos na implementação e aprendemos mais sobre o domínio, devemos revisar e atualizar nossas práticas de TDD.*