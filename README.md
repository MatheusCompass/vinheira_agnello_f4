# Vinheria Agnello - Sistema de Estoque

Sistema de gerenciamento de estoque da Vinheria Agnello desenvolvido como projeto acadêmico, com **frontend Android** (Jetpack Compose + Room) e **backend C#** (Entity Framework Core + SQLite).

---

## 📁 Estrutura do Projeto

```
vinheira_agnello_f4/
├── frontend/              # Aplicativo Android (Kotlin + Jetpack Compose)
├── backend/               # Backend C# (Console App + EF Core)
└── README.md             # Este arquivo
```

---

## 🚀 Quick Start

### Frontend Android

```bash
# Navegar até a pasta
cd frontend

# Build (Windows)
.\gradlew.bat :app:assembleDebug

# Build (macOS/Linux)
./gradlew :app:assembleDebug
```

**Pré-requisitos:** JDK 11-17, Android SDK, Emulador/Device

📄 **Documentação completa:** [frontend/README.md](frontend/README.md)

### Backend C#

```bash
# Navegar até a pasta
cd backend/EstoqueVinheria

# Executar (Windows, macOS, Linux)
dotnet run
```

**Pré-requisitos:** .NET SDK 8.0 ou 10.0

📄 **Documentação completa:** [backend/README.md](backend/README.md)

---

## 📱 Frontend Android

### Tecnologias
- Kotlin 2.0.21
- Jetpack Compose (UI moderna)
- Room Database (SQLite local)
- Hilt (Injeção de dependências)
- Material Design 3
- Gradle 8.13

### Funcionalidades
- ✅ Tela de login
- 📋 Listagem de produtos (vinhos)
- ➕ Adicionar produtos
- ✏️ Editar produtos
- 🗑️ Deletar produtos
- 💾 Persistência local automática

### Pré-requisitos

| Componente | Versão/Requisito |
|:-----------|:-----------------|
| **JDK** | 11 a 17 (recomendado: 17) ⚠️ Não use 18+ |
| **Android SDK** | API 24+ (Android 7.0 ou superior) |
| **Emulador/Device** | AVD configurado ou dispositivo físico |

### Comandos Rápidos

#### Windows
```powershell
cd frontend
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:connectedAndroidTest
```

#### macOS/Linux
```bash
cd frontend
./gradlew :app:assembleDebug
./gradlew :app:connectedAndroidTest
```

#### Android Studio
1. Abra a pasta `frontend` no Android Studio
2. Aguarde sincronização do Gradle
3. Configure um emulador (AVD Manager)
4. Clique em **Run** ▶️

---

## 🖥️ Backend C#

### Tecnologias
- C# .NET 10.0 (compatível com 8.0+)
- Entity Framework Core 10.0
- SQLite (banco local)
- Console Application

### Funcionalidades
- 📋 Listar produtos
- ➕ Adicionar produtos
- ✏️ Atualizar produtos (preço e quantidade)
- 🗑️ Deletar produtos
- 🗄️ Code-First (banco criado automaticamente)

### Pré-requisitos

| Componente | Versão |
|:-----------|:-------|
| **.NET SDK** | 8.0 ou superior |

Verificar instalação:
```bash
dotnet --version
```

### Comandos

```bash
# Navegar até o projeto
cd backend/EstoqueVinheria

# Executar (cria o banco automaticamente na 1ª vez)
dotnet run

# Comandos adicionais
dotnet restore    # Restaurar pacotes
dotnet build      # Build sem executar
dotnet clean      # Limpar artefatos
```

### Menu Interativo

```
=== Sistema de Estoque - Vinheria Agnello ===
1. Listar produtos
2. Adicionar produto
3. Atualizar produto
4. Deletar produto
0. Sair
```

---

## 🏗️ Arquitetura

### Frontend (Android)
```
UI (Compose) → ViewModel → Repository → DAO → Room Database
       ↑            ↑           ↑         ↑
       └────────── Hilt DI ──────────────┘
```
- Padrão MVVM
- Single Activity com Compose
- Reactive UI (StateFlow)

### Backend (C#)
```
Console UI → DbContext (EF Core) → SQLite
```
- Code-First approach
- Entity Framework Core como ORM
- Operações CRUD síncronas

---

## 📊 Modelo de Dados

### Produto (Frontend e Backend)

| Campo | Tipo | Descrição |
|:------|:-----|:----------|
| `Id` | int | Chave primária (auto-incremento) |
| `Nome` | string | Nome do produto |
| `Descricao` | string | Descrição detalhada |
| `Uva` | string | Tipo de uva (somente frontend) |
| `Ano` | int | Ano de safra (somente frontend) |
| `Preco` | double | Preço unitário |
| `Quantidade` | int | Quantidade em estoque |

**Nota:** Frontend e backend usam bancos independentes. O frontend tem campos adicionais específicos para vinhos.

---

## 🧪 Testes

### Frontend
```bash
cd frontend

# Testes instrumentados (requer emulador rodando)
./gradlew :app:connectedAndroidTest

# Relatórios em: app/build/reports/androidTests/connected/
```

Testes cobrem:
- CRUD do DAO (ProdutoDaoTest)
- Operações de banco de dados

---

## ❓ Resolução de Problemas

### Frontend Android

**Erro: "JAVA_HOME is not set"**
```bash
# Windows (PowerShell Admin)
setx JAVA_HOME "C:\Program Files\Java\jdk-17" /M

# macOS/Linux
export JAVA_HOME=/caminho/para/jdk-17
```

**Erro: "Unsupported class file major version"**
- Você está usando JDK 18+
- Solução: Instale JDK 17 ou inferior

**Build lenta**
- Habilite daemon e cache no `frontend/gradle.properties`

### Backend C#

**Erro: "dotnet: command not found"**
- Instale o .NET SDK: https://dotnet.microsoft.com/download

**Erro: "framework version '10.0.0' was not found"**
- Instale .NET 10.0 **ou**
- Edite `EstoqueVinheria.csproj` e mude para `net8.0`

**Banco corrompido**
```bash
# Deletar e recriar
rm backend/EstoqueVinheria/estoque.db  # macOS/Linux
del backend\EstoqueVinheria\estoque.db # Windows
dotnet run
```

---

## 📝 Notas Técnicas

- ✅ Frontend e backend são **independentes** (não se comunicam)
- ✅ Ambos demonstram persistência local com bancos SQLite separados
- ✅ Primeira execução de cada módulo cria dados de exemplo automaticamente
- ✅ Frontend: 10 vinhos pré-cadastrados
- ✅ Backend: 10 produtos pré-cadastrados

---

## 📚 Documentação Detalhada

- **Frontend:** [frontend/README.md](frontend/README.md)
- **Backend:** [backend/README.md](backend/README.md)

---

**Desenvolvido para a Fase 4 - Vinheria Agnello**
