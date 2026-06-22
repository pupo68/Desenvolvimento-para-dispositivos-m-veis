# Guia Rápido: Como Rodar no Android Studio

## 📋 Pré-requisitos

1. **Android Studio** instalado (versão Hedgehog 2023.1.1 ou superior)
2. **Java JDK** 17 ou 21
3. **Android SDK** com API 34 instalado

## 🚀 Passo a Passo

### 1. Abrir o Projeto

```
1. Inicie o Android Studio
2. Clique em "Open" (ou File → Open)
3. Navegue até: C:\Users\STR69\Documents\PUC-PR\PARTE 2\Desenvolvimento para dispositivos móveis
4. Clique em "OK"
```

### 2. Aguardar Gradle Sync

```
O Android Studio irá:
- Indexar arquivos
- Baixar dependências (pode levar 2-5 minutos na primeira vez)
- Sincronizar Gradle

✅ Quando terminar, você verá "BUILD SUCCESSFUL" na barra inferior
```

### 3. Criar Emulador (se não tiver um)

```
1. Tools → Device Manager
2. Clique em "Create Device"
3. Selecione: Pixel 6 (ou qualquer outro)
4. Clique em "Next"
5. Selecione: API 33 (Android 13) ou superior
6. Clique em "Next" → "Finish"
```

### 4. Rodar o App

```
1. Selecione o emulador no dropdown superior
2. Clique no botão ▶️ verde (Run)
3. Ou pressione: Shift + F10

O app será instalado e iniciará automaticamente!
```

## 🎮 Testando o App

### Adicionar Item
1. Clique no botão **+** (FAB) no canto inferior direito
2. Digite o nome (ex: "Arroz")
3. Digite a quantidade (ex: "5 kg")
4. Clique em "Salvar"

### Marcar como Comprado
- Clique no **checkbox** ao lado do item
- O texto será tachado e movido para o final da lista

### Editar Item
- Clique em **qualquer parte do card** do item
- O formulário abrirá com os dados preenchidos
- Edite e clique em "Salvar"

### Excluir Item
**Opção 1 - Botão:**
- Clique no ícone de **lixeira** no card
- Confirme no dialog

**Opção 2 - Swipe:**
- Deslize o dedo (ou mouse) para a **esquerda** no item
- Confirme no dialog

**Desfazer:**
- Após excluir, um Snackbar aparece com "Desfazer"
- Clique antes de 3 segundos para restaurar

### Empty State
- Quando a lista está vazia, mostra:
  - Ilustração de carrinho vazio
  - Texto "Sua lista está vazia"
  - Botão "Adicionar Item"

## 🧪 Rodando Testes

### Testes no Android Studio

```
1. Navegue até: app/src/test/java/br/edu/pucpr/listacompras/
2. Clique direito em um arquivo de teste (ex: ItemCompraTest.kt)
3. Selecione "Run 'ItemCompraTest'"
4. Veja os resultados na janela "Run"
```

### Todos os Testes

```
1. Run → Run 'All Tests'
2. Ou: Ctrl + Shift + F10

Isso roda:
- ItemCompraTest ✅
- ItemCompraDaoTest ✅
- ComprasRepositoryTest ✅
```

## 🐛 Problemas Comuns

### "Gradle Sync Failed"

**Solução:**
```
1. File → Invalidate Caches → Invalidate and Restart
2. Aguarde o Android Studio reiniciar
3. O Gradle Sync será executado novamente
```

### "No Emulator Found"

**Solução:**
```
1. Tools → Device Manager
2. Delete o emulador existente
3. Crie um novo (Pixel 6, API 33+)
4. Clique no ícone de Play para iniciar o emulador
5. Após iniciar, rode o app novamente
```

### "Unresolved Reference"

**Solução:**
```
1. File → Sync Project with Gradle Files
2. Ou: Build → Rebuild Project
3. Ou: Ctrl + Shift + O (Clean + Rebuild)
```

### Build Lento

**Dicas:**
```
- Mantenha o Gradle Daemon habilitado
- Aumente RAM do emulador para 2GB
- Feche outros aplicativos pesados
- Use SSD (o build é I/O intensive)
```

## 📁 Estrutura do Projeto no Android Studio

```
app/
├── java/br.edu.pucpr.listacompras/
│   ├── model/
│   │   └── ItemCompra.kt          ✅
│   ├── db/
│   │   └── ItemCompraDao.kt       ✅
│   ├── data/
│   │   └── AppDatabase.kt         ✅
│   ├── repository/
│   │   └── ComprasRepository.kt   ✅
│   ├── ui/
│   │   ├── ComprasViewModel.kt    ✅
│   │   ├── ComprasAdapter.kt      ✅
│   │   ├── ItemFormDialog.kt      ✅
│   │   └── MainActivity.kt        ✅
│   └── utils/ (se necessário)
├── res/
│   ├── layout/
│   │   ├── activity_main.xml      ✅
│   │   ├── item_compra.xml        ✅
│   │   └── dialog_item_form.xml   ✅
│   ├── drawable/
│   │   ├── ic_add.xml             ✅
│   │   ├── ic_delete.xml          ✅
│   │   └── ic_empty_cart.xml      ✅
│   └── values/
│       ├── strings.xml            ✅
│       ├── colors.xml             ✅
│       └── themes.xml             ✅
└── test/java/.../
    ├── model/
    │   ├── ItemCompraTest.kt      ✅
    │   └── SimpleTest.kt          ✅
    ├── db/
    │   └── ItemCompraDaoTest.kt   ✅
    └── repository/
        └── ComprasRepositoryTest.kt ✅
```

## ✅ Checklist de Validação

Antes de considerar pronto:

- [ ] App inicia sem crashes
- [ ] Lista está vazia no início (empty state)
- [ ] Adicionar item funciona
- [ ] Checkbox marca/desmarca
- [ ] Items comprados vão para o final (tachados)
- [ ] Editar item funciona
- [ ] Excluir com confirmação funciona
- [ ] Swipe para excluir funciona
- [ ] Snackbar "Desfazer" aparece
- [ ] Rotação de tela não perde dados
- [ ] Strings vêm do strings.xml (nada hardcoded)

## 🎬 Gravando o Vídeo Demonstrativo

Para a entrega da Somativa 1:

```
1. Ferramentas:
   - OBS Studio (grátis) ou
   - Scrn.li (online) ou
   - Gravação de tela do próprio celular

2. Roteiro (máx 3 min):
   - 0:00-0:15: Mostra app vazio (empty state)
   - 0:15-0:45: Adiciona 3-4 items
   - 0:45-1:15: Marca alguns como comprados
   - 1:15-1:45: Edita um item
   - 1:45-2:15: Exclui um item (mostra desfazer)
   - 2:15-2:45: Swipe para excluir
   - 2:45-3:00: Mostra código/README (opcional)

3. Upload:
   - YouTube (não listado) ou
   - Google Drive (compartilhável) ou
   - Vimeo
```

---

**Boa sorte com a apresentação! 🚀**