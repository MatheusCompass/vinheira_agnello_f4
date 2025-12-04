# Backend C# - Módulo de Persistência

Módulo de persistência de dados para o sistema de estoque da Vinheria Agnello, implementado em **C# .NET** com **Entity Framework Core** e **SQLite**.

## Sobre a Solução

Console Application que demonstra persistência de dados usando:
- **Code-First:** Banco de dados gerado automaticamente a partir das classes de domínio
- **Entity Framework Core:** ORM para mapeamento objeto-relacional
- **SQLite:** Banco de dados local em arquivo (`estoque.db`)
- **CRUD Completo:** Criar, Listar, Atualizar e Deletar produtos

## Pré-requisitos

- **.NET SDK 8.0 ou superior** ([Download](https://dotnet.microsoft.com/download))
  - Projeto configurado para .NET 10.0, mas compatível com .NET 8.0+

### Verificar instalação do .NET

```bash
# Windows, macOS, Linux
dotnet --version
```

Deve retornar versão 8.0 ou superior.

## Como Executar

### Passo 1: Navegar até o projeto

#### Windows (PowerShell ou CMD)
```powershell
cd backend-csharp\EstoqueVinheria
```

#### macOS/Linux
```bash
cd backend-csharp/EstoqueVinheria
```

### Passo 2: Restaurar dependências (opcional)

```bash
# Windows, macOS, Linux
dotnet restore
```

### Passo 3: Executar o projeto

```bash
# Windows, macOS, Linux
dotnet run
```

Na **primeira execução**, o sistema:
1. Cria automaticamente o arquivo `estoque.db`
2. Gera o schema do banco de dados
3. Insere 10 produtos de exemplo (vinhos)

## Menu Interativo

Após executar `dotnet run`, você verá um menu com as seguintes opções:

```
=== Sistema de Estoque - Vinheria Agnello ===
1. Listar produtos
2. Adicionar produto
3. Atualizar produto
4. Deletar produto
0. Sair
```

### Operações Disponíveis

- **Listar produtos:** Exibe todos os produtos cadastrados
- **Adicionar produto:** Solicita Nome, Descrição, Preço e Quantidade
- **Atualizar produto:** Permite alterar Preço e Quantidade de um produto existente
- **Deletar produto:** Remove um produto pelo ID (com confirmação)

## Estrutura do Projeto

```
EstoqueVinheria/
├── EstoqueVinheria.csproj    # Configuração do projeto
├── Program.cs                # Ponto de entrada com menu CLI
├── AppDbContext.cs           # Contexto do Entity Framework
├── Produto.cs                # Entidade (modelo de dados)
└── estoque.db                # Banco de dados SQLite (criado na 1ª execução)
```

## Tecnologias Utilizadas

| Componente | Versão |
|:-----------|:-------|
| .NET Target Framework | 10.0 (compatível com 8.0+) |
| Entity Framework Core | 10.0.0 |
| EF Core SQLite Provider | 10.0.0 |
| EF Core Design Tools | 10.0.0 |

## Comandos Úteis

```bash
# Build do projeto
dotnet build

# Executar sem rebuild
dotnet run --no-build

# Limpar artefatos de build
dotnet clean

# Restaurar pacotes NuGet
dotnet restore
```

## Modelo de Dados

### Entidade `Produto`

| Campo | Tipo | Descrição |
|:------|:-----|:----------|
| `Id` | int | Chave primária (auto-incremento) |
| `Nome` | string | Nome do produto (obrigatório) |
| `Descricao` | string? | Descrição do produto (opcional) |
| `Preco` | double | Preço unitário (obrigatório) |
| `Quantidade` | int | Quantidade em estoque (obrigatório) |

## Atendimento aos Requisitos Acadêmicos

| Requisito | Implementação |
|:----------|:--------------|
| Persistência de dados em C# | Entity Framework Core |
| Módulo de persistência | `AppDbContext` + Entidade `Produto` |
| Gerenciamento de estoque | CRUD completo via menu interativo |
| Code-First | Banco criado automaticamente via `EnsureCreated()` |

## Resolução de Problemas

### Erro: "dotnet: command not found"
- Instale o .NET SDK: https://dotnet.microsoft.com/download
- Verifique se o SDK está no PATH do sistema

### Erro: "The framework 'Microsoft.NETCore.App', version '10.0.0' was not found"
- Instale .NET 10.0 SDK **ou**
- Edite `EstoqueVinheria.csproj` e altere `<TargetFramework>net10.0</TargetFramework>` para `net8.0`

### Banco de dados corrompido
```bash
# Deletar e recriar o banco
rm estoque.db      # macOS/Linux
del estoque.db     # Windows

# Executar novamente
dotnet run
```

---

**Desenvolvido para a Fase 4 - Vinheria Agnello**
