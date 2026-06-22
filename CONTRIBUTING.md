# Guia de Contribuição Rápida

## 📝 Padrões de Código

### Kotlin

```kotlin
// ✅ CERTO
val items: List<ItemCompra> = emptyList()  // Imutável
val nome: String? = null                   // Null safety
items.forEach { process(it) }              // Lambdas

// ❌ ERRADO
var items = mutableListOf()                // Evitar var
val nome = getString()!!                   // Nunca usar !!
for (item in items) { process(item) }      // Preferir forEach
```

### Nomenclatura

```
Classes: PascalCase (ex: ItemCompra, ComprasViewModel)
Funções/Métodos: camelCase (ex: addItem, toggleStatus)
Variáveis: camelCase (ex: itemList, isComprado)
Constants: UPPER_SNAKE_CASE (ex: MAX_ITEMS)
Resources: snake_case (ex: item_compra, ic_add, md_theme_light_primary)
```

### Testes

```kotlin
// ✅ Nomes descritivos com backticks
@Test
fun `test addItem delegates to dao insert`() { ... }

@Test
fun `test toggleItemStatus changes isComprado from false to true`() { ... }

// ❌ Evitar nomes genéricos
@Test
fun testAdd() { ... }  // Muito vago
```

## 🔄 Git Workflow

### Commit Messages

```
feat: add swipe-to-delete functionality
fix: correct ordering of purchased items
docs: update README with troubleshooting section
test: add unit tests for ComprasRepository
refactor: extract validation logic to separate function
```

### Branch Naming

```
feature/add-item-validation
fix/swipe-delete-bug
docs/update-readme
test/viewmodel-tests
```

## ✅ Checklist de Code Review

Antes de fazer push:

- [ ] Código compila sem erros
- [ ] Todos os testes passam
- [ ] Zero strings hardcoded (usar strings.xml)
- [ ] Zero warnings do Lint
- [ ] Imports organizados (Ctrl+Alt+O)
- [ ] Código formatado (Ctrl+Alt+L)
- [ ] Nomes descritivos de variáveis/funções
- [ ] Funções pequenas (< 30 linhas idealmente)
- [ ] Comentários apenas se necessário (código deve ser autoexplicativo)

## 🧪 Rodando Testes Antes de Commit

```bash
# Testes JVM (rápidos, não precisam de emulador)
./gradlew testDebugUnitTest

# Testes Android (precisam de emulador/dispositivo)
./gradlew connectedAndroidTest

# Apenas testes específicos
./gradlew testDebugUnitTest --tests "ItemCompraTest"
```

## 📁 Estrutura de Pastas

```
src/main/java/br/edu/pucpr/listacompras/
├── model/         # Data classes (entidades)
├── db/            # DAOs do Room
├── data/          # Database, converters
├── repository/    # Repositórios
├── ui/            # Activity, ViewModel, Adapter, Dialog
└── utils/         # Extension functions, helpers

src/test/java/br/edu/pucpr/listacompras/
├── model/         # Testes de modelo
├── db/            # Testes de DAO
├── repository/    # Testes de repository
└── ui/            # Testes de ViewModel, Adapter
```

## 🎯 Padrões Específicos do Projeto

### ViewModel

- Sempre usar `StateFlow` ou `LiveData` para estado
- Eventos de UI (Snackbar, navigation) usam `Event` wrapper
- Operations são `suspend functions` com `viewModelScope.launch`

### Adapter

- Sempre usar `ListAdapter` com `DiffUtil`
- ViewBinding para acesso às views
- Callbacks são lambdas passadas no construtor

### Room DAO

- Queries retornam `Flow<List<T>>` para reatividade
- Write operations são `suspend functions`
- Ordenação definida na query (ORDER BY)

### Repository

- Abstração entre ViewModel e DAO
- Expõe `Flow` para leitura
- Write operations delegam ao DAO

## 🐛 Debugging Tips

### Logcat Filtros

```
# Apenas logs do app
package:br.edu.pucpr.listacompras

# Apenas errors
package:br.edu.pucpr.listacompras loglevel:error

# ViewModel logs
tag:ComprasViewModel
```

### Breakpoints Comuns

1. `ComprasViewModel.addItem` - Debug de adição
2. `ComprasAdapter.onBindViewHolder` - Debug de renderização
3. `ItemCompraDao.insertItem` - Debug de database

## 📚 Recursos Úteis

- [Android Developers Guide](https://developer.android.com/guide)
- [Kotlin Docs](https://kotlinlang.org/docs/home.html)
- [Room Documentation](https://developer.android.com/training/data-storage/room)
- [Material Design 3](https://m3.material.io/)

---

**Dúvidas?** Consulte o README.md ou o COMO_USAR.md