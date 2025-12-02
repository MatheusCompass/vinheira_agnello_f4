# 🚀 PLANO SIMPLIFICADO - Fase 4 (Versão Rápida)
**Para quem quer entregar logo e garantir a nota**

---

## ⚠️ REALIDADE DO PROJETO DE FACULDADE

### O que importa?
✅ **Atender requisitos do enunciado**
✅ **Demonstrar que aprendeu os conceitos**
✅ **Entregar no prazo**

### O que NÃO importa?
❌ Arquitetura super elaborada
❌ Código "production-ready"
❌ Clean Code perfeito
❌ Testes unitários completos

---

## 📋 ANÁLISE DO QUE JÁ EXISTE

### Android ✅ COMPLETO
- Room Database implementado
- Entidade `Produto` (id, nome, descricao, preco, quantidade)
- CRUD funcional na UI
- 3 testes passando
- Login funcionando

**DECISÃO:** ❌ **NÃO MEXER** - Já atende o requisito

### C# Backend ❌ FALTANDO
- Nada implementado
- Precisa criar do zero

**DECISÃO:** ✅ **CRIAR VERSÃO MÍNIMA**

---

## 🎯 PLANO DE 4 HORAS

### Hora 1-2: Backend C# Básico
1. Criar projeto console (5 min)
2. Adicionar Entity Framework (5 min)
3. Copiar classe `Produto` simples (10 min)
4. Criar DbContext básico (10 min)
5. CRUD direto no Program.cs (60 min)
6. Testar todas operações (10 min)

### Hora 3: Screenshots
1. Tirar 5 prints do C# funcionando (20 min)
2. Tirar 3 prints do Android (10 min)
3. Organizar em pasta (10 min)

### Hora 4: Documento Word
1. Capa + Introdução (10 min)
2. Seção C# com prints (20 min)
3. Seção Android com prints (15 min)
4. Conclusão (10 min)
5. Revisar e exportar PDF (5 min)

---

## 💻 CÓDIGO COMPLETO SIMPLIFICADO

### Passo 1: Criar Projeto

```bash
cd "/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4"
mkdir backend-csharp
cd backend-csharp

# Criar projeto simples
dotnet new console -n EstoqueVinheria
cd EstoqueVinheria

# Adicionar EF Core
dotnet add package Microsoft.EntityFrameworkCore.Sqlite
dotnet add package Microsoft.EntityFrameworkCore.Design
```

### Passo 2: Arquivo Produto.cs (SIMPLES)

```csharp
using System.ComponentModel.DataAnnotations;

public class Produto
{
    [Key]
    public int Id { get; set; }

    [Required]
    public string Nome { get; set; } = "";

    public string? Descricao { get; set; }

    [Required]
    public double Preco { get; set; }

    [Required]
    public int Quantidade { get; set; }
}
```

### Passo 3: Arquivo AppDbContext.cs (MÍNIMO)

```csharp
using Microsoft.EntityFrameworkCore;

public class AppDbContext : DbContext
{
    public DbSet<Produto> Produtos { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder options)
    {
        options.UseSqlite("Data Source=estoque.db");
    }
}
```

### Passo 4: Program.cs (TUDO AQUI - SEM REPOSITORY)

```csharp
using Microsoft.EntityFrameworkCore;

class Program
{
    static void Main()
    {
        // Criar banco
        using (var db = new AppDbContext())
        {
            db.Database.EnsureCreated();
            Console.WriteLine("✓ Banco criado!\n");
        }

        // Menu
        while (true)
        {
            Console.WriteLine("=== ESTOQUE VINHERIA AGNELLO ===");
            Console.WriteLine("1. Listar produtos");
            Console.WriteLine("2. Adicionar produto");
            Console.WriteLine("3. Atualizar produto");
            Console.WriteLine("4. Deletar produto");
            Console.WriteLine("0. Sair");
            Console.Write("Opção: ");

            var opcao = Console.ReadLine();

            switch (opcao)
            {
                case "1": Listar(); break;
                case "2": Adicionar(); break;
                case "3": Atualizar(); break;
                case "4": Deletar(); break;
                case "0": return;
            }
        }
    }

    static void Listar()
    {
        using var db = new AppDbContext();
        var produtos = db.Produtos.ToList();

        Console.WriteLine("\n=== LISTA DE PRODUTOS ===");
        if (produtos.Count == 0)
        {
            Console.WriteLine("Nenhum produto cadastrado.\n");
            return;
        }

        foreach (var p in produtos)
        {
            Console.WriteLine($"ID: {p.Id} | {p.Nome} | R$ {p.Preco:F2} | Qtd: {p.Quantidade}");
        }
        Console.WriteLine();
    }

    static void Adicionar()
    {
        Console.WriteLine("\n=== ADICIONAR PRODUTO ===");

        var produto = new Produto();

        Console.Write("Nome: ");
        produto.Nome = Console.ReadLine() ?? "";

        Console.Write("Descrição: ");
        produto.Descricao = Console.ReadLine();

        Console.Write("Preço: ");
        produto.Preco = double.Parse(Console.ReadLine() ?? "0");

        Console.Write("Quantidade: ");
        produto.Quantidade = int.Parse(Console.ReadLine() ?? "0");

        using var db = new AppDbContext();
        db.Produtos.Add(produto);
        db.SaveChanges();

        Console.WriteLine("✓ Produto adicionado!\n");
    }

    static void Atualizar()
    {
        Console.Write("\nID do produto: ");
        var id = int.Parse(Console.ReadLine() ?? "0");

        using var db = new AppDbContext();
        var produto = db.Produtos.Find(id);

        if (produto == null)
        {
            Console.WriteLine("✗ Produto não encontrado!\n");
            return;
        }

        Console.WriteLine($"Produto: {produto.Nome}");
        Console.Write("Novo preço (Enter para manter): ");
        var precoStr = Console.ReadLine();
        if (!string.IsNullOrEmpty(precoStr))
            produto.Preco = double.Parse(precoStr);

        Console.Write("Nova quantidade (Enter para manter): ");
        var qtdStr = Console.ReadLine();
        if (!string.IsNullOrEmpty(qtdStr))
            produto.Quantidade = int.Parse(qtdStr);

        db.SaveChanges();
        Console.WriteLine("✓ Produto atualizado!\n");
    }

    static void Deletar()
    {
        Console.Write("\nID do produto: ");
        var id = int.Parse(Console.ReadLine() ?? "0");

        using var db = new AppDbContext();
        var produto = db.Produtos.Find(id);

        if (produto == null)
        {
            Console.WriteLine("✗ Produto não encontrado!\n");
            return;
        }

        Console.Write($"Deletar '{produto.Nome}'? (s/N): ");
        if (Console.ReadLine()?.ToLower() == "s")
        {
            db.Produtos.Remove(produto);
            db.SaveChanges();
            Console.WriteLine("✓ Produto deletado!\n");
        }
    }
}
```

### Passo 5: Executar

```bash
dotnet run
```

**Pronto! Funciona.**

---

## 📸 SCREENSHOTS NECESSÁRIOS

### Backend C# (5 screenshots)
1. **Estrutura do projeto** (3 arquivos: Produto.cs, AppDbContext.cs, Program.cs)
2. **Código do Produto.cs** (mostra a entidade)
3. **Menu funcionando** (lista inicial vazia)
4. **Adicionar produto** (preenchendo dados)
5. **Listar produtos** (mostrando produto adicionado)

### Android (3 screenshots - JÁ TEM TUDO)
1. **Tela de lista** (produtos cadastrados)
2. **Formulário de adicionar** (preenchido)
3. **Resultado dos testes** (3 passing)

---

## 📄 DOCUMENTO WORD SIMPLIFICADO

### Estrutura (10-12 páginas)

```
1. CAPA
   - Seu nome completo
   - RM
   - "Fase 4 - Persistência de Dados"

2. INTRODUÇÃO (1 página)
   - "Este trabalho implementa persistência de dados
      usando Entity Framework Core (C#) e Room Database (Android)"

3. BACKEND C# (4 páginas)
   3.1 Tecnologias
       - .NET 8
       - Entity Framework Core
       - SQLite

   3.2 Modelo de Dados
       [Screenshot do Produto.cs]
       Explicar: "Classe Produto com 5 atributos..."

   3.3 DbContext
       [Screenshot do AppDbContext.cs]
       Explicar: "OnConfiguring com SQLite..."

   3.4 Operações CRUD
       [Screenshot do menu]
       [Screenshot adicionar produto]
       [Screenshot listar produtos]
       Explicar: "Implementado Add, Find, Remove, SaveChanges..."

4. ANDROID ROOM (3 páginas)
   4.1 Implementação Existente
       [Screenshot lista de produtos]
       [Screenshot formulário]
       [Screenshot testes]
       Explicar: "Room Database com @Entity, @Dao, @Database..."

5. COMPARAÇÃO (1 página)
   | Aspecto | EF Core | Room |
   |---------|---------|------|
   | Linguagem | C# | Kotlin |
   | Banco | SQLite | SQLite |
   | Migrations | CLI | Versões |

6. CONCLUSÃO (1 página)
   - "Ambas as tecnologias facilitam persistência..."
   - "Entity Framework é mais flexível..."
   - "Room é otimizado para Android..."

7. REFERÊNCIAS
   - Material didático Fase 04
```

---

## ✅ CHECKLIST FINAL

### Backend C#
- [ ] Projeto criado
- [ ] 3 arquivos (Produto, DbContext, Program)
- [ ] `dotnet run` funciona
- [ ] CRUD testado (adicionar, listar, atualizar, deletar)
- [ ] 5 screenshots tirados

### Android
- [ ] Já funciona (não mexer)
- [ ] 3 screenshots tirados

### Documentação
- [ ] Word com 10-12 páginas
- [ ] 8 screenshots inseridos
- [ ] Nome do arquivo: `SeuNome_RM_Fase4.docx`

---

## ⏱️ TIMELINE REALISTA

**Sexta à noite (2h):**
- Criar C# backend
- Testar funcionando
- Tirar screenshots

**Sábado manhã (2h):**
- Montar Word
- Revisar
- Exportar PDF
- **ENTREGAR**

---

## 🎓 POR QUE ESSA VERSÃO É SUFICIENTE?

### Atende o enunciado?
✅ "Persistência de dados com C#" → TEM
✅ "Entity Framework Core" → TEM
✅ "CRUD (Create, Read, Update, Delete)" → TEM
✅ "Room Database no Android" → JÁ TINHA
✅ "Screenshots e código" → TEM

### Professor vai reclamar?
❌ NÃO - Porque você demonstrou os conceitos:
- DbContext
- DbSet
- SaveChanges
- LINQ (ToList, Find)
- Code-First (EnsureCreated)

### Dá nota máxima?
✅ SIM - Se:
- Código funciona
- Screenshots são claros
- Documento está bem formatado
- Explica os conceitos

---

## 🚫 O QUE NÃO FAZER (PERDE TEMPO)

❌ Repository Pattern (complexidade desnecessária)
❌ Fluent API detalhada (Data Annotations bastam)
❌ Migrations explícitas (EnsureCreated é suficiente)
❌ Testes unitários C# (não foi pedido)
❌ Refatorar Android (já funciona)
❌ README detalhado (não foi pedido)
❌ Validações complexas (não foi pedido)
❌ Tratamento de erros elaborado (try-catch básico basta)

---

## 💡 DICAS EXTRAS

### Se der erro ao rodar:
```bash
dotnet restore
dotnet build
dotnet run
```

### Se quiser dados iniciais:
Adicione no Main, depois do `EnsureCreated()`:
```csharp
if (!db.Produtos.Any())
{
    db.Produtos.Add(new Produto {
        Nome = "Vinho Tinto",
        Preco = 50,
        Quantidade = 10
    });
    db.SaveChanges();
}
```

### Para impressionar (OPCIONAL):
Adicione 1 consulta LINQ no método Listar:
```csharp
var produtos = db.Produtos
    .Where(p => p.Quantidade > 0)
    .OrderBy(p => p.Nome)
    .ToList();
```

---

## 📞 CONCLUSÃO

**Versão simplificada:**
- ✅ 4 horas de trabalho
- ✅ 100% dos requisitos
- ✅ Código limpo e funcional
- ✅ Fácil de explicar
- ✅ Professor vai aprovar

**Versão completa (plano original):**
- ⚠️ 1-2 dias de trabalho
- ⚠️ Over-engineering
- ⚠️ Mais código para debugar
- ⚠️ Não vale nota extra

**Escolha sabiamente!** 🎯

---

**Criado em:** 2025-12-02
**Versão:** 1.0 - Simplificada e Realista
**Status:** ✅ Pronto para Executar
