# Lista de Compras 🛒

Aplicativo Android nativo para gerenciamento de listas de compras, desenvolvido com Kotlin, arquitetura MVVM e Room Database.

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-purple?logo=kotlin)
![Android](https://img.shields.io/badge/Android-SDK%2026%2B-green?logo=android)
![Room](https://img.shields.io/badge/Room-2.6.1-blue)
![Material Design](https://img.shields.io/badge/Material-Design%203-pink?logo=materialdesign)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-orange)

## 📱 Sobre o Projeto

Um aplicativo simples, rápido e eficiente para organizar compras de mercado, feira ou tarefas diárias de consumo. Permite adicionar, visualizar, editar e remover itens com persistência local de dados.

### ✨ Funcionalidades

- ✅ **CRUD Completo** - Criar, ler, atualizar e excluir itens da lista
- 📦 **Persistência Local** - Room Database (SQLite) para salvar dados offline
- 🎨 **Material Design 3** - Interface moderna e responsiva
- 🔄 **Lista Reativa** - RecyclerView com ListAdapter e DiffUtil
- ✔️ **Marcar como Comprado** - Checkbox com ordenação automática
- ↩️ **Desfazer Exclusão** - Snackbar com ação de desfazer
- 🌐 **Strings Internacionalizadas** - Sem textos hardcoded

## 🏗️ Arquitetura

O projeto segue o padrão **MVVM (Model-View-ViewModel)** com as seguintes camadas:

```
┌─────────────────────────────────────────────────────┐
│                    UI Layer                         │
│  MainActivity + RecyclerView + BottomSheetDialog    │
├─────────────────────────────────────────────────────┤
│                ViewModel Layer                      │
│              ComprasViewModel                       │
├─────────────────────────────────────────────────────┤
│               Repository Layer                      │
│            ComprasRepository                        │
├─────────────────────────────────────────────────────┤
│                  Data Layer                         │
│         ItemCompraDao + AppDatabase (Room)          │
│              ItemCompra (Model)                     │
└─────────────────────────────────────────────────────┘
```

### Estrutura do Projeto

```
app/
├── src/main/
│   ├── java/br/edu/pucpr/listacompras/
│   │   ├── model/
│   │   │   └── ItemCompra.kt              # Data class da entidade
│   │   ├── db/
│   │   │   └── ItemCompraDao.kt           # DAO do Room
│   │   ├── data/
│   │   │   └── AppDatabase.kt             # Database singleton
│   │   ├── repository/
│   │   │   └── ComprasRepository.kt       # Camada de dados
│   │   ├── ui/
│   │   │   ├── MainActivity.kt            # Tela principal
│   │   │   ├── ComprasViewModel.kt        # ViewModel
│   │   │   ├── ComprasAdapter.kt          # RecyclerView adapter
│   │   │   └── ItemFormDialog.kt          # BottomSheet de formulário
│   │   └── utils/                         # Utilitários (se necessário)
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml          # Layout principal
│   │   │   ├── item_compra.xml            # Item da lista
│   │   │   └── dialog_item_form.xml       # Formulário
│   │   ├── values/
│   │   │   ├── strings.xml                # Strings internacionalizadas
│   │   │   ├── colors.xml                 # Cores Material 3
│   │   │   └── themes.xml                 # Tema do app
│   │   └── drawable/                      # Drawables e ícones
│   └── AndroidManifest.xml
├── src/test/
│   └── java/br/edu/pucpr/listacompras/
│       ├── model/
│       │   ├── ItemCompraTest.kt          # Testes do modelo
│       │   └── SimpleTest.kt
│       ├── db/
│       │   └── ItemCompraDaoTest.kt       # Testes do DAO
│       └── repository/
│           └── ComprasRepositoryTest.kt   # Testes do Repository
└── build.gradle.kts
```

## 🛠️ Tech Stack

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Kotlin** | 1.9.22 | Linguagem principal |
| **Android SDK** | 34 | Target SDK |
| **Min SDK** | 26 | Android 8.0 (Oreo) |
| **Room** | 2.6.1 | Persistência local |
| **Material Design** | 1.11.0 | Componentes de UI |
| **RecyclerView** | 1.3.2 | Listagem eficiente |
| **Coroutines** | 1.7.3 | Assincronismo |
| **ViewBinding** | - | Type-safe views |
| **MockK** | 1.13.9 | Framework de mocks |
| **JUnit 4** | 4.13.2 | Testes unitários |

## 🚀 Como Rodar o Projeto

### Método Recomendado: Android Studio

1. **Abra o Android Studio** (versão Hedgehog 2023.1.1 ou superior)

2. **Importe o projeto:**
   - `File` → `Open` → Selecione a pasta `Desenvolvimento para dispositivos móveis`
   - Aguarde o **Gradle Sync** completar (pode levar 2-5 minutos na primeira vez)

3. **Configure um emulador ou dispositivo:**
   - **Emulador:** `Tools` → `Device Manager` → `Create Device` → Escolha Pixel 6 com Android 13+
   - **Dispositivo físico:** Ative _Developer Options_ e _USB Debugging_ no seu Android

4. **Execute o app:**
   - Clique no botão ▶️ **Run** ou pressione `Shift + F10`
   - Selecione o dispositivo/emulador
   - O app será instalado e iniciará automaticamente

### Método Alternativo: Command Line (Linux/Mac)

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Instalar no dispositivo conectado
./gradlew installDebug
```

## 🧪 Rodando os Testes

### Pré-requisitos para Testes

Os testes do DAO requerem o **Android Test Framework**. Certifique-se de ter:
- Android SDK com **AndroidX Test** instalado
- Emulador ou dispositivo conectado (para testes instrumentados)

### Testes Unitários (JVM)

```bash
# Rodar testes que NÃO dependem do Android (Model, Repository)
./gradlew testDebugUnitTest
```

### Testes com Dependência Android (DAO)

Os testes do DAO usam `Room.inMemoryDatabaseBuilder` que requer Android context:

```bash
# Conecte um emulador/dispositivo primeiro, depois:
./gradlew connectedAndroidTest
```

### Executando no Android Studio

1. `Run` → `Run 'All Tests'` ou pressione `Ctrl+Shift+F10`
2. Para testes específicos: clique direito no arquivo de teste → `Run`

### Estrutura de Testes

| Classe | Descrição | Status | Cobertura |
|--------|-----------|--------|-----------|
| `ItemCompraTest` | Testes do modelo de dados | ✅ Completo | 100% |
| `SimpleTest` | Teste básico de sanity | ✅ Completo | - |
| `ItemCompraDaoTest` | Testes do DAO com Room in-memory | ✅ Completo | CRUD completo |
| `ComprasRepositoryTest` | Testes do Repository com MockK | ✅ Completo | 100% |
| `ComprasViewModelTest` | Testes do ViewModel | ⏳ Pendente | - |

## 📋 Requisitos Funcionais

### RF01 - Listar Itens
Exibir todos os itens da lista de compras em um RecyclerView, ordenados por status (não-comprados primeiro).

### RF02 - Adicionar Item
Cadastrar novo item com nome e quantidade através de BottomSheetDialog.

### RF03 - Editar Item
Modificar nome e/ou quantidade de item existente.

### RF04 - Marcar como Comprado
Alternar status `isComprado` via checkbox, aplicando strikethrough e reordenando.

### RF05 - Excluir Item
Remover item da lista com confirmação via Snackbar + opção de desfazer.

### RF06 - Persistir Dados
Salvar todos os dados localmente usando Room Database.

## 📐 Requisitos Não Funcionais

- **RNF01:** Interface em Material Design 3
- **RNF02:** Strings internacionalizadas (sem hardcoded)
- **RNF03:** Operações de banco em Dispatchers.IO
- **RNF04:** ViewModel sobrevive a rotações de tela
- **RNF05:** Layout responsivo com ConstraintLayout
- **RNF06:** Kotlin idiomático (val > var, null safety)

## 🎨 Design e UX

### Empty State
Quando a lista está vazia, exibe:
- Ilustração amigável
- Texto encorajador
- Call-to-action para adicionar primeiro item

### Feedbacks
- **Sucesso:** Snackbar ao adicionar/editar
- **Exclusão:** Snackbar com "Desfazer" (3 segundos)
- **Erros:** Mensagens inline nos campos do formulário

### Ordenação
1. Items não-comprados (topo, ordem alfabética)
2. Items comprados (final, ordem alfabética, tachados)

## 🔧 Comandos Úteis

```bash
# Clean build
./gradlew clean

# Build + testes
./gradlew build

# Lint check
./gradlew lint

# Format code (se ktlint configurado)
./gradlew ktlintFormat

# Ver dependências
./gradlew app:dependencies

# Mostrar tarefas Gradle
./gradlew tasks
```

## 📝 Modelo de Dados

### ItemCompra

```kotlin
@Entity(tableName = "item_compra")
data class ItemCompra(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val quantidade: String,
    val isComprado: Boolean = false
)
```

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | Int | Identificador único (auto-gerado) |
| `nome` | String | Nome do item (obrigatório) |
| `quantidade` | String | Quantidade (ex: "2 kg", "5 un") |
| `isComprado` | Boolean | Status de compra (default: false) |

## 🗄️ Database Schema

### Tabela: item_compra

| Coluna | Tipo | PK | Auto | Nullable |
|--------|------|----|------|----------|
| id | INTEGER | ✅ | ✅ | ❌ |
| nome | TEXT | ❌ | ❌ | ❌ |
| quantidade | TEXT | ❌ | ❌ | ❌ |
| isComprado | INTEGER | ❌ | ❌ | ❌ |

**Índices:** Ordenação via `ORDER BY isComprado ASC, nome ASC`

## 📅 Roadmap

### ✅ Fase 1 - Fundação (COMPLETO)
- [x] Configuração do projeto
- [x] Modelo de dados (ItemCompra)
- [x] DAO e Database (Room)
- [x] Repository
- [x] ViewModel
- [x] UI (MainActivity, Adapter, Dialog)

### ✅ Fase 2 - UI e Testes (COMPLETO)
- [x] Telas e layouts (activity_main, item_compra, dialog_item_form)
- [x] RecyclerView + Adapter com DiffUtil
- [x] BottomSheet de formulário com validação
- [x] Testes de DAO (CRUD completo + ordenação)
- [x] Testes de Repository (MockK)
- [x] Drawables (ícones: add, delete, empty_cart)

### 🔄 Fase 3 - Polimento
- [x] Empty state com ilustração e CTA
- [x] Snackbar de desfazer
- [x] Validação de formulário
- [ ] Testes de ViewModel
- [x] Documentação completa (README.md)

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### Padrões de Código

- Use `val` sempre que possível
- Null safety com `?.` e `?:`
- Evite `!!`
- Strings sempre em `strings.xml`
- Nomes descritivos em testes (backticks)

## 📄 Licença

Este projeto é parte da atividade acadêmica da PUC-PR.

## 👨‍💻 Desenvolvedor

**Disciplina:** Desenvolvimento para Dispositivos Móveis  
**Curso:** Engenharia de Software / Sistemas de Informação  
**Instituição:** PUC-PR

## 📚 Referências

- [Documentação Oficial do Android](https://developer.android.com/)
- [Guia de Arquitetura MVVM](https://developer.android.com/topic/architecture)
- [Room Database Documentation](https://developer.android.com/training/data-storage/room)
- [Material Design 3](https://m3.material.io/)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)

## 🔧 Troubleshooting

### Gradle Sync Falha

**Sintoma:** "Gradle sync failed" ou "Could not resolve all dependencies"

**Soluções:**
1. `File` → `Invalidate Caches` → `Invalidate and Restart`
2. Delete a pasta `.gradle` e `.idea` do projeto
3. No terminal: `./gradlew clean` (Linux/Mac) ou `gradlew.bat clean` (Windows)

### Emulador não Inicia

**Sintoma:** AVD não inicia ou fica em "Loading"

**Soluções:**
1. `Tools` → `Device Manager` → Delete o emulador e crie um novo
2. Habilite virtualização na BIOS (VT-x/AMD-V)
3. Use um emulador com menos RAM (2GB em vez de 4GB)

### Testes Falham com "NotFoundException"

**Sintoma:** Testes do DAO falham com `RuntimeException: No packaged found`

**Solução:** Execute os testes com um emulador/dispositivo conectado via `connectedAndroidTest`

### Build Lento

**Soluções:**
1. Habilite Gradle Daemon: em `gradle.properties`, adicione `org.gradle.daemon=true`
2. Aumente heap size: `org.gradle.jvmargs=-Xmx2048m`
3. Use `--offline` para builds subsequentes sem novas dependências

### "Unresolved Reference" no Kotlin

**Sintoma:** IDE mostra erros mas o build passa

**Solução:** `File` → `Sync Project with Gradle Files` ou pressione `Ctrl+Shift+O` (rebuild)

---

<div align="center">

**Feito com ❤️ e ☕**

</div>