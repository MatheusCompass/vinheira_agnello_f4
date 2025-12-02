# Plano de Implementação - Fase 4 Vinheria Agnello
**Persistência de Dados: C# + Entity Framework Core e Android + Room Database**

---

## 📋 Resumo Executivo

### Situação Atual
- ✅ **Android Room Database**: Implementado com entidade `Produto` genérica
- ❌ **Backend C#**: Ausente - precisa ser criado do zero
- ⚠️ **Coerência**: Modelo genérico não alinha com domínio específico (vinheria)

### Decisões Tomadas
1. **Modelo de Dados**: Refatorar Android para `Vinho` + implementar `Vinho` no C#
2. **Localização**: Backend C# em `/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4/backend-csharp/`

### Requisitos da Fase 4 (Enunciado)
1. ✅ Persistência local Android com Room Database (já implementado, requer ajuste)
2. ❌ Persistência servidor com C# + Entity Framework Core (criar do zero)
3. 📄 Documentação com screenshots e código-fonte

---

## 🎯 Estratégia de Implementação

### Prioridade 1: Backend C# (Essencial)
Criar console application simples que demonstre persistência com Entity Framework Core:
- Entidade `Vinho` com campos do domínio
- DbContext com SQLite
- Repository com CRUD completo
- Menu interativo para testes

### Prioridade 2: Refatoração Android (Importante)
Migrar modelo de `Produto` para `Vinho`:
- Renomear entidade e campos
- Criar migration no Room Database
- Atualizar DAO, Repository, ViewModel
- Atualizar UI e testes

### Prioridade 3: Documentação (Obrigatória)
- README técnico de cada módulo
- Screenshots das operações CRUD
- Documento Word para entrega

---

## 📚 Alinhamento com Material Didático

### Entity Framework Core (Material: 3ESO Fase 04 - 03)
- **Code-First**: Classes C# → Banco de dados gerado automaticamente
- **DbContext**: Classe central que gerencia conexão e sessão
- **Fluent API**: Configuração programática no `OnModelCreating` (preferível)
- **Migrations**: `Add-Migration` → `Update-Database` (controle de versão do schema)
- **CRUD**: `Add()`, `Find()`, `Update()`, `Remove()` + `SaveChanges()`
- **LINQ**: Consultas tipadas (`.Where()`, `.OrderBy()`, `.Include()`)

### Room Database (Material: 3ESO Fase 04 - 07)
- **@Entity**: Define tabela SQLite
- **@Dao**: Interface com métodos de acesso aos dados
- **@Database**: Classe abstrata que estende RoomDatabase
- **Singleton Pattern**: Uma única instância do banco
- **Migrations**: Controle de mudanças no schema com `fallbackToDestructiveMigration()` ou migrations explícitas

---

## 🔧 FASE 1: Backend C# com Entity Framework Core

### 1.1 Estrutura de Diretórios
```
/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4/
├── app/                                    # Projeto Android (existente)
└── backend-csharp/                         # NOVO
    ├── VinheriaAgnello.CSharp/
    │   ├── Models/
    │   │   └── Vinho.cs
    │   ├── Data/
    │   │   ├── VinheriaContext.cs
    │   │   └── Migrations/
    │   ├── Repositories/
    │   │   └── VinhoRepository.cs
    │   ├── Program.cs
    │   ├── VinheriaAgnello.CSharp.csproj
    │   └── appsettings.json
    ├── VinheriaAgnello.CSharp.sln
    └── README.md
```

### 1.2 Comandos de Criação
```bash
# Navegar para diretório da Fase 4
cd "/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4"

# Criar estrutura de diretórios
mkdir -p backend-csharp/VinheriaAgnello.CSharp/{Models,Data/Migrations,Repositories}

# Criar projeto console
cd backend-csharp
dotnet new console -n VinheriaAgnello.CSharp -f net8.0
cd VinheriaAgnello.CSharp

# Adicionar pacotes NuGet (Entity Framework Core + SQLite)
dotnet add package Microsoft.EntityFrameworkCore.Sqlite --version 8.0.0
dotnet add package Microsoft.EntityFrameworkCore.Design --version 8.0.0
dotnet add package Microsoft.EntityFrameworkCore.Tools --version 8.0.0

# Criar solution
cd ..
dotnet new sln -n VinheriaAgnello.CSharp
dotnet sln add VinheriaAgnello.CSharp/VinheriaAgnello.CSharp.csproj
```

### 1.3 Implementação - Models/Vinho.cs
**Alinhamento:** Data Annotations + Fluent API (híbrido, conforme material)

```csharp
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace VinheriaAgnello.CSharp.Models
{
    [Table("Vinhos")]
    public class Vinho
    {
        [Key]
        public int Id { get; set; }

        [Required(ErrorMessage = "Nome é obrigatório")]
        [StringLength(200)]
        public string Nome { get; set; } = string.Empty;

        [Required]
        [StringLength(50)]
        public string Tipo { get; set; } = string.Empty; // Tinto, Branco, Espumante

        [Required]
        [StringLength(100)]
        public string Uva { get; set; } = string.Empty; // Casta

        [Required]
        [StringLength(100)]
        public string Regiao { get; set; } = string.Empty; // Origem geográfica

        [Required]
        [StringLength(10)]
        public string Safra { get; set; } = string.Empty; // Ano

        [Required]
        [StringLength(50)]
        public string Corpo { get; set; } = string.Empty; // Leve, Médio, Encorpado

        [Required]
        [StringLength(50)]
        public string Docura { get; set; } = string.Empty; // Seco, Meio-Seco, Doce

        [StringLength(500)]
        public string? Harmonizacao { get; set; }

        [Column(TypeName = "decimal(4,2)")]
        public double TeorAlcoolico { get; set; }

        [Required]
        [Column(TypeName = "decimal(10,2)")]
        public double Preco { get; set; }

        [Required]
        public int Estoque { get; set; }

        public bool EscolhaAgnello { get; set; } = false;

        [StringLength(500)]
        public string? DescricaoCurta { get; set; }

        [StringLength(1000)]
        public string? DescricaoAgnello { get; set; }

        [StringLength(500)]
        public string? Imagem { get; set; }
    }
}
```

### 1.4 Implementação - Data/VinheriaContext.cs
**Alinhamento:** Fluent API no `OnModelCreating` (recomendado pelo material)

```csharp
using Microsoft.EntityFrameworkCore;
using VinheriaAgnello.CSharp.Models;

namespace VinheriaAgnello.CSharp.Data
{
    public class VinheriaContext : DbContext
    {
        public DbSet<Vinho> Vinhos { get; set; } = null!;

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseSqlite("Data Source=vinheria.db");
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Fluent API - configuração adicional
            modelBuilder.Entity<Vinho>(entity =>
            {
                entity.ToTable("Vinhos");
                entity.HasKey(e => e.Id);

                // Propriedades obrigatórias
                entity.Property(e => e.Nome).IsRequired().HasMaxLength(200);
                entity.Property(e => e.Tipo).IsRequired().HasMaxLength(50);
                entity.Property(e => e.Uva).IsRequired().HasMaxLength(100);
                entity.Property(e => e.Regiao).IsRequired().HasMaxLength(100);
                entity.Property(e => e.Safra).IsRequired().HasMaxLength(10);
                entity.Property(e => e.Corpo).IsRequired().HasMaxLength(50);
                entity.Property(e => e.Docura).IsRequired().HasMaxLength(50);

                // Propriedades decimais
                entity.Property(e => e.TeorAlcoolico).HasColumnType("decimal(4,2)");
                entity.Property(e => e.Preco).HasColumnType("decimal(10,2)");

                // Propriedades opcionais
                entity.Property(e => e.Harmonizacao).HasMaxLength(500);
                entity.Property(e => e.DescricaoCurta).HasMaxLength(500);
                entity.Property(e => e.DescricaoAgnello).HasMaxLength(1000);
                entity.Property(e => e.Imagem).HasMaxLength(500);

                // Índice para busca por nome
                entity.HasIndex(e => e.Nome);

                // Valor padrão
                entity.Property(e => e.EscolhaAgnello).HasDefaultValue(false);
            });
        }
    }
}
```

### 1.5 Implementação - Repositories/VinhoRepository.cs
**Alinhamento:** Repository Pattern (separa lógica de dados da UI)

```csharp
using Microsoft.EntityFrameworkCore;
using VinheriaAgnello.CSharp.Data;
using VinheriaAgnello.CSharp.Models;

namespace VinheriaAgnello.CSharp.Repositories
{
    public class VinhoRepository
    {
        private readonly VinheriaContext _context;

        public VinhoRepository(VinheriaContext context)
        {
            _context = context;
        }

        // CREATE
        public void Adicionar(Vinho vinho)
        {
            _context.Vinhos.Add(vinho);
            _context.SaveChanges();
            Console.WriteLine($"✓ Vinho '{vinho.Nome}' adicionado com ID: {vinho.Id}");
        }

        // READ - Listar todos
        public List<Vinho> ListarTodos()
        {
            return _context.Vinhos.OrderBy(v => v.Nome).ToList();
        }

        // READ - Buscar por ID
        public Vinho? BuscarPorId(int id)
        {
            return _context.Vinhos.Find(id);
        }

        // READ - Buscar por nome (LINQ - conforme material)
        public List<Vinho> BuscarPorNome(string nome)
        {
            return _context.Vinhos
                .Where(v => v.Nome.Contains(nome))
                .OrderBy(v => v.Nome)
                .ToList();
        }

        // READ - Filtro por tipo
        public List<Vinho> BuscarPorTipo(string tipo)
        {
            return _context.Vinhos
                .Where(v => v.Tipo.ToLower() == tipo.ToLower())
                .OrderBy(v => v.Nome)
                .ToList();
        }

        // READ - "Escolha Agnello"
        public List<Vinho> ListarEscolhaAgnello()
        {
            return _context.Vinhos
                .Where(v => v.EscolhaAgnello)
                .OrderBy(v => v.Nome)
                .ToList();
        }

        // UPDATE
        public void Atualizar(Vinho vinho)
        {
            _context.Vinhos.Update(vinho);
            _context.SaveChanges();
            Console.WriteLine($"✓ Vinho '{vinho.Nome}' atualizado");
        }

        // DELETE
        public void Excluir(int id)
        {
            var vinho = _context.Vinhos.Find(id);
            if (vinho != null)
            {
                _context.Vinhos.Remove(vinho);
                _context.SaveChanges();
                Console.WriteLine($"✓ Vinho '{vinho.Nome}' excluído");
            }
            else
            {
                Console.WriteLine("✗ Vinho não encontrado");
            }
        }

        // Estatísticas (usando LINQ)
        public int TotalVinhos() => _context.Vinhos.Count();

        public int TotalEstoque() => _context.Vinhos.Sum(v => v.Estoque);

        public List<Vinho> EstoqueBaixo(int limite = 10)
        {
            return _context.Vinhos
                .Where(v => v.Estoque < limite)
                .OrderBy(v => v.Estoque)
                .ToList();
        }
    }
}
```

### 1.6 Implementação - Program.cs
**Console Application com menu interativo para demonstração**

```csharp
using Microsoft.EntityFrameworkCore;
using VinheriaAgnello.CSharp.Data;
using VinheriaAgnello.CSharp.Repositories;
using VinheriaAgnello.CSharp.Models;

namespace VinheriaAgnello.CSharp
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("╔═══════════════════════════════════════════════════════╗");
            Console.WriteLine("║  Vinheria Agnello - Sistema de Estoque              ║");
            Console.WriteLine("║  C# + Entity Framework Core + SQLite                 ║");
            Console.WriteLine("╚═══════════════════════════════════════════════════════╝\n");

            // Criar banco se não existir e aplicar migrations
            using (var context = new VinheriaContext())
            {
                context.Database.EnsureCreated();
                Console.WriteLine("✓ Banco de dados inicializado com sucesso!\n");
            }

            // Criar repositório
            var repository = new VinhoRepository(new VinheriaContext());

            // Verificar se precisa popular dados iniciais
            if (repository.TotalVinhos() == 0)
            {
                PopularDadosIniciais(repository);
            }

            // Menu interativo
            bool executando = true;
            while (executando)
            {
                Console.WriteLine("\n╔═══ MENU PRINCIPAL ═══╗");
                Console.WriteLine("║ 1. Listar todos os vinhos");
                Console.WriteLine("║ 2. Buscar vinho por ID");
                Console.WriteLine("║ 3. Buscar vinho por nome");
                Console.WriteLine("║ 4. Filtrar por tipo");
                Console.WriteLine("║ 5. Adicionar novo vinho");
                Console.WriteLine("║ 6. Atualizar vinho");
                Console.WriteLine("║ 7. Excluir vinho");
                Console.WriteLine("║ 8. Listar 'Escolha Agnello'");
                Console.WriteLine("║ 9. Estatísticas do estoque");
                Console.WriteLine("║ 0. Sair");
                Console.WriteLine("╚══════════════════════╝");
                Console.Write("Escolha uma opção: ");

                var opcao = Console.ReadLine();

                switch (opcao)
                {
                    case "1": ListarVinhos(repository); break;
                    case "2": BuscarPorId(repository); break;
                    case "3": BuscarPorNome(repository); break;
                    case "4": FiltrarPorTipo(repository); break;
                    case "5": AdicionarVinho(repository); break;
                    case "6": AtualizarVinho(repository); break;
                    case "7": ExcluirVinho(repository); break;
                    case "8": ListarEscolhaAgnello(repository); break;
                    case "9": MostrarEstatisticas(repository); break;
                    case "0":
                        executando = false;
                        Console.WriteLine("\nEncerrando sistema...");
                        break;
                    default:
                        Console.WriteLine("✗ Opção inválida!");
                        break;
                }
            }
        }

        static void PopularDadosIniciais(VinhoRepository repo)
        {
            Console.WriteLine("\n⚙️  Populando dados iniciais...");

            var vinhos = new List<Vinho>
            {
                new Vinho
                {
                    Nome = "Château Ausone Saint-Émilion",
                    Tipo = "Tinto",
                    Uva = "Merlot, Cabernet Franc",
                    Regiao = "França",
                    Safra = "2018",
                    Corpo = "Encorpado",
                    Docura = "Seco",
                    Harmonizacao = "Carnes vermelhas, queijos envelhecidos",
                    TeorAlcoolico = 14.5,
                    Preco = 1846.92,
                    Estoque = 15,
                    EscolhaAgnello = true,
                    DescricaoCurta = "Vinho premium francês excepcional",
                    Imagem = "chateau-ausone.jpg"
                },
                new Vinho
                {
                    Nome = "Barolo Riserva",
                    Tipo = "Tinto",
                    Uva = "Nebbiolo",
                    Regiao = "Itália",
                    Safra = "2015",
                    Corpo = "Encorpado",
                    Docura = "Seco",
                    Harmonizacao = "Massas, risotos, carnes",
                    TeorAlcoolico = 14.0,
                    Preco = 450.00,
                    Estoque = 8,
                    EscolhaAgnello = true,
                    DescricaoCurta = "Elegância italiana em cada gole"
                },
                new Vinho
                {
                    Nome = "Chablis Grand Cru",
                    Tipo = "Branco",
                    Uva = "Chardonnay",
                    Regiao = "França",
                    Safra = "2020",
                    Corpo = "Médio",
                    Docura = "Seco",
                    Harmonizacao = "Frutos do mar, peixes",
                    TeorAlcoolico = 13.0,
                    Preco = 320.00,
                    Estoque = 12,
                    EscolhaAgnello = false,
                    DescricaoCurta = "Branco mineral e refrescante"
                }
            };

            foreach (var vinho in vinhos)
            {
                repo.Adicionar(vinho);
            }

            Console.WriteLine($"✓ {vinhos.Count} vinhos adicionados ao banco\n");
        }

        static void ListarVinhos(VinhoRepository repo)
        {
            Console.WriteLine("\n╔═══ LISTA DE VINHOS ═══╗");
            var vinhos = repo.ListarTodos();

            if (!vinhos.Any())
            {
                Console.WriteLine("Nenhum vinho cadastrado.");
                return;
            }

            foreach (var vinho in vinhos)
            {
                var estrela = vinho.EscolhaAgnello ? "⭐" : "  ";
                Console.WriteLine($"{estrela} ID: {vinho.Id,-3} | {vinho.Nome,-40} | {vinho.Tipo,-10} | R$ {vinho.Preco,8:F2} | Estoque: {vinho.Estoque,3}");
            }
            Console.WriteLine($"\nTotal: {vinhos.Count} vinhos");
        }

        static void BuscarPorId(VinhoRepository repo)
        {
            Console.Write("\nDigite o ID do vinho: ");
            if (!int.TryParse(Console.ReadLine(), out var id))
            {
                Console.WriteLine("✗ ID inválido!");
                return;
            }

            var vinho = repo.BuscarPorId(id);
            if (vinho == null)
            {
                Console.WriteLine("✗ Vinho não encontrado!");
                return;
            }

            ExibirDetalhesVinho(vinho);
        }

        static void BuscarPorNome(VinhoRepository repo)
        {
            Console.Write("\nDigite parte do nome: ");
            var nome = Console.ReadLine();

            if (string.IsNullOrWhiteSpace(nome))
            {
                Console.WriteLine("✗ Nome inválido!");
                return;
            }

            var vinhos = repo.BuscarPorNome(nome);

            if (!vinhos.Any())
            {
                Console.WriteLine($"✗ Nenhum vinho encontrado com '{nome}'");
                return;
            }

            Console.WriteLine($"\n╔═══ RESULTADOS PARA '{nome}' ═══╗");
            foreach (var vinho in vinhos)
            {
                Console.WriteLine($"ID: {vinho.Id,-3} | {vinho.Nome,-40} | {vinho.Tipo,-10} | R$ {vinho.Preco:F2}");
            }
        }

        static void FiltrarPorTipo(VinhoRepository repo)
        {
            Console.WriteLine("\nTipos disponíveis: Tinto, Branco, Espumante, Rosé");
            Console.Write("Digite o tipo: ");
            var tipo = Console.ReadLine();

            if (string.IsNullOrWhiteSpace(tipo))
            {
                Console.WriteLine("✗ Tipo inválido!");
                return;
            }

            var vinhos = repo.BuscarPorTipo(tipo);

            if (!vinhos.Any())
            {
                Console.WriteLine($"✗ Nenhum vinho do tipo '{tipo}' encontrado");
                return;
            }

            Console.WriteLine($"\n╔═══ VINHOS DO TIPO '{tipo.ToUpper()}' ═══╗");
            foreach (var vinho in vinhos)
            {
                Console.WriteLine($"ID: {vinho.Id,-3} | {vinho.Nome,-40} | R$ {vinho.Preco:F2} | Estoque: {vinho.Estoque}");
            }
        }

        static void AdicionarVinho(VinhoRepository repo)
        {
            Console.WriteLine("\n╔═══ ADICIONAR NOVO VINHO ═══╗");

            var vinho = new Vinho();

            Console.Write("Nome: ");
            vinho.Nome = Console.ReadLine() ?? string.Empty;

            Console.Write("Tipo (Tinto/Branco/Espumante/Rosé): ");
            vinho.Tipo = Console.ReadLine() ?? string.Empty;

            Console.Write("Uva (casta): ");
            vinho.Uva = Console.ReadLine() ?? string.Empty;

            Console.Write("Região: ");
            vinho.Regiao = Console.ReadLine() ?? string.Empty;

            Console.Write("Safra (ano): ");
            vinho.Safra = Console.ReadLine() ?? string.Empty;

            Console.Write("Corpo (Leve/Médio/Encorpado): ");
            vinho.Corpo = Console.ReadLine() ?? string.Empty;

            Console.Write("Doçura (Seco/Meio-Seco/Doce): ");
            vinho.Docura = Console.ReadLine() ?? string.Empty;

            Console.Write("Harmonização (opcional): ");
            vinho.Harmonizacao = Console.ReadLine();

            Console.Write("Teor Alcoólico (%): ");
            if (double.TryParse(Console.ReadLine(), out var teor))
                vinho.TeorAlcoolico = teor;

            Console.Write("Preço (R$): ");
            if (double.TryParse(Console.ReadLine(), out var preco))
                vinho.Preco = preco;

            Console.Write("Estoque inicial: ");
            if (int.TryParse(Console.ReadLine(), out var estoque))
                vinho.Estoque = estoque;

            Console.Write("É 'Escolha Agnello'? (s/N): ");
            vinho.EscolhaAgnello = (Console.ReadLine()?.ToLower() == "s");

            Console.Write("Descrição curta (opcional): ");
            vinho.DescricaoCurta = Console.ReadLine();

            try
            {
                repo.Adicionar(vinho);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"✗ Erro ao adicionar vinho: {ex.Message}");
            }
        }

        static void AtualizarVinho(VinhoRepository repo)
        {
            Console.Write("\nDigite o ID do vinho para atualizar: ");
            if (!int.TryParse(Console.ReadLine(), out var id))
            {
                Console.WriteLine("✗ ID inválido!");
                return;
            }

            var vinho = repo.BuscarPorId(id);
            if (vinho == null)
            {
                Console.WriteLine("✗ Vinho não encontrado!");
                return;
            }

            Console.WriteLine($"\nDados atuais: {vinho.Nome} | {vinho.Tipo} | R$ {vinho.Preco:F2} | Estoque: {vinho.Estoque}");
            Console.WriteLine("(Deixe em branco para manter o valor atual)\n");

            Console.Write($"Nome [{vinho.Nome}]: ");
            var nome = Console.ReadLine();
            if (!string.IsNullOrWhiteSpace(nome))
                vinho.Nome = nome;

            Console.Write($"Preço [{vinho.Preco}]: ");
            var precoStr = Console.ReadLine();
            if (!string.IsNullOrWhiteSpace(precoStr) && double.TryParse(precoStr, out var preco))
                vinho.Preco = preco;

            Console.Write($"Estoque [{vinho.Estoque}]: ");
            var estoqueStr = Console.ReadLine();
            if (!string.IsNullOrWhiteSpace(estoqueStr) && int.TryParse(estoqueStr, out var estoque))
                vinho.Estoque = estoque;

            try
            {
                repo.Atualizar(vinho);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"✗ Erro ao atualizar vinho: {ex.Message}");
            }
        }

        static void ExcluirVinho(VinhoRepository repo)
        {
            Console.Write("\nDigite o ID do vinho para excluir: ");
            if (!int.TryParse(Console.ReadLine(), out var id))
            {
                Console.WriteLine("✗ ID inválido!");
                return;
            }

            var vinho = repo.BuscarPorId(id);
            if (vinho == null)
            {
                Console.WriteLine("✗ Vinho não encontrado!");
                return;
            }

            Console.Write($"Tem certeza que deseja excluir '{vinho.Nome}'? (s/N): ");
            if (Console.ReadLine()?.ToLower() == "s")
            {
                try
                {
                    repo.Excluir(id);
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"✗ Erro ao excluir vinho: {ex.Message}");
                }
            }
            else
            {
                Console.WriteLine("Operação cancelada.");
            }
        }

        static void ListarEscolhaAgnello(VinhoRepository repo)
        {
            Console.WriteLine("\n╔═══ ESCOLHA AGNELLO ⭐ ═══╗");
            var vinhos = repo.ListarEscolhaAgnello();

            if (!vinhos.Any())
            {
                Console.WriteLine("Nenhum vinho 'Escolha Agnello' cadastrado.");
                return;
            }

            foreach (var vinho in vinhos)
            {
                Console.WriteLine($"⭐ ID: {vinho.Id,-3} | {vinho.Nome,-40} | {vinho.Tipo,-10} | R$ {vinho.Preco:F2}");
            }
        }

        static void MostrarEstatisticas(VinhoRepository repo)
        {
            Console.WriteLine("\n╔═══ ESTATÍSTICAS DO ESTOQUE ═══╗");
            Console.WriteLine($"Total de vinhos cadastrados: {repo.TotalVinhos()}");
            Console.WriteLine($"Total de unidades em estoque: {repo.TotalEstoque()}");

            var estoqueBaixo = repo.EstoqueBaixo();
            if (estoqueBaixo.Any())
            {
                Console.WriteLine("\n⚠️  VINHOS COM ESTOQUE BAIXO (< 10 unidades):");
                foreach (var vinho in estoqueBaixo)
                {
                    Console.WriteLine($"  • {vinho.Nome}: {vinho.Estoque} unidades");
                }
            }
            else
            {
                Console.WriteLine("✓ Todos os vinhos possuem estoque adequado.");
            }
        }

        static void ExibirDetalhesVinho(Vinho vinho)
        {
            Console.WriteLine("\n╔═══ DETALHES DO VINHO ═══╗");
            Console.WriteLine($"ID: {vinho.Id}");
            Console.WriteLine($"Nome: {vinho.Nome}");
            Console.WriteLine($"Tipo: {vinho.Tipo}");
            Console.WriteLine($"Uva: {vinho.Uva}");
            Console.WriteLine($"Região: {vinho.Regiao}");
            Console.WriteLine($"Safra: {vinho.Safra}");
            Console.WriteLine($"Corpo: {vinho.Corpo}");
            Console.WriteLine($"Doçura: {vinho.Docura}");
            Console.WriteLine($"Teor Alcoólico: {vinho.TeorAlcoolico}%");
            Console.WriteLine($"Preço: R$ {vinho.Preco:F2}");
            Console.WriteLine($"Estoque: {vinho.Estoque} unidades");
            Console.WriteLine($"Escolha Agnello: {(vinho.EscolhaAgnello ? "Sim ⭐" : "Não")}");
            if (!string.IsNullOrEmpty(vinho.Harmonizacao))
                Console.WriteLine($"Harmonização: {vinho.Harmonizacao}");
            if (!string.IsNullOrEmpty(vinho.DescricaoCurta))
                Console.WriteLine($"Descrição: {vinho.DescricaoCurta}");
        }
    }
}
```

### 1.7 Criar Migrations (Conforme Material Didático)
```bash
cd VinheriaAgnello.CSharp

# Criar migration inicial
dotnet ef migrations add InitialCreate

# Aplicar migration ao banco
dotnet ef database update

# (O banco vinheria.db será criado automaticamente)
```

### 1.8 Testar o Sistema
```bash
# Executar aplicação
dotnet run

# Testar todas as operações:
# 1. Listar vinhos (deve mostrar 3 vinhos iniciais)
# 2. Adicionar um novo vinho
# 3. Buscar vinho por nome
# 4. Atualizar estoque
# 5. Ver estatísticas
# 6. Excluir um vinho
```

---

## 🔄 FASE 2: Refatoração Android (Produto → Vinho)

### 2.1 Arquivos a Modificar
```
/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4/app/src/main/java/com/example/vinheira_agnello_f4/
├── data/
│   ├── Produto.kt              → Vinho.kt (renomear e adicionar campos)
│   ├── ProdutoDao.kt           → VinhoDao.kt (renomear)
│   ├── ProdutoRepository.kt    → VinhoRepository.kt (renomear)
│   └── VinheriaDatabase.kt     (atualizar versão e entidade)
├── viewmodel/
│   └── ProdutoViewModel.kt     → VinhoViewModel.kt (renomear)
└── ui/
    └── ProductListScreen.kt    → VinhoListScreen.kt (renomear e atualizar campos)
```

### 2.2 Nova Entidade - data/Vinho.kt
```kotlin
package com.example.vinheira_agnello_f4.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vinhos")
data class Vinho(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nome: String,
    val tipo: String,                    // Tinto, Branco, Espumante, Rosé
    val uva: String,                     // Casta
    val regiao: String,                  // Origem geográfica
    val safra: String,                   // Ano
    val corpo: String,                   // Leve, Médio, Encorpado
    val docura: String,                  // Seco, Meio-Seco, Doce

    @ColumnInfo(name = "harmonizacao")
    val harmonizacao: String? = null,

    @ColumnInfo(name = "teor_alcoolico")
    val teorAlcoolico: Double = 0.0,

    val preco: Double,
    val quantidade: Int,                 // Estoque

    @ColumnInfo(name = "escolha_agnello")
    val escolhaAgnello: Boolean = false,

    @ColumnInfo(name = "descricao_curta")
    val descricaoCurta: String? = null,

    @ColumnInfo(name = "descricao_agnello")
    val descricaoAgnello: String? = null,

    val imagem: String? = null
)
```

### 2.3 Novo DAO - data/VinhoDao.kt
```kotlin
package com.example.vinheira_agnello_f4.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VinhoDao {
    @Query("SELECT * FROM vinhos ORDER BY nome ASC")
    fun getAll(): Flow<List<Vinho>>

    @Query("SELECT * FROM vinhos WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Vinho?

    @Query("SELECT * FROM vinhos WHERE escolha_agnello = 1 ORDER BY nome ASC")
    fun getEscolhaAgnello(): Flow<List<Vinho>>

    @Query("SELECT * FROM vinhos WHERE tipo = :tipo ORDER BY nome ASC")
    fun getByTipo(tipo: String): Flow<List<Vinho>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vinho: Vinho): Long

    @Update
    suspend fun update(vinho: Vinho)

    @Delete
    suspend fun delete(vinho: Vinho)
}
```

### 2.4 Atualizar Database - data/VinheriaDatabase.kt
```kotlin
package com.example.vinheira_agnello_f4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Vinho::class],
    version = 2,  // INCREMENTAR VERSÃO
    exportSchema = false
)
abstract class VinheriaDatabase : RoomDatabase() {
    abstract fun vinhoDao(): VinhoDao

    companion object {
        @Volatile
        private var INSTANCE: VinheriaDatabase? = null

        fun getInstance(context: Context): VinheriaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VinheriaDatabase::class.java,
                    "vinheria_database"
                )
                .fallbackToDestructiveMigration()  // Para desenvolvimento
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

### 2.5 Atualizar Repository - data/VinhoRepository.kt
```kotlin
package com.example.vinheira_agnello_f4.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VinhoRepository @Inject constructor(
    private val vinhoDao: VinhoDao
) {
    val vinhos: Flow<List<Vinho>> = vinhoDao.getAll()

    val escolhaAgnello: Flow<List<Vinho>> = vinhoDao.getEscolhaAgnello()

    suspend fun insert(vinho: Vinho): Long {
        return vinhoDao.insert(vinho)
    }

    suspend fun update(vinho: Vinho) {
        vinhoDao.update(vinho)
    }

    suspend fun delete(vinho: Vinho) {
        vinhoDao.delete(vinho)
    }

    fun getByTipo(tipo: String): Flow<List<Vinho>> {
        return vinhoDao.getByTipo(tipo)
    }
}
```

### 2.6 Atualizar ViewModel - viewmodel/VinhoViewModel.kt
```kotlin
package com.example.vinheira_agnello_f4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.vinheira_agnello_f4.data.Vinho
import com.example.vinheira_agnello_f4.data.VinhoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VinhoViewModel @Inject constructor(
    application: Application,
    private val repository: VinhoRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    val vinhos: StateFlow<List<Vinho>> = repository.vinhos
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val escolhaAgnello: StateFlow<List<Vinho>> = repository.escolhaAgnello
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insert(vinho: Vinho) {
        viewModelScope.launch {
            repository.insert(vinho)
        }
    }

    fun update(vinho: Vinho) {
        viewModelScope.launch {
            repository.update(vinho)
        }
    }

    fun delete(vinho: Vinho) {
        viewModelScope.launch {
            repository.delete(vinho)
        }
    }
}
```

### 2.7 Atualizar DI - di/DataModule.kt
```kotlin
package com.example.vinheira_agnello_f4.di

import android.content.Context
import com.example.vinheira_agnello_f4.data.VinheriaDatabase
import com.example.vinheira_agnello_f4.data.VinhoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VinheriaDatabase {
        return VinheriaDatabase.getInstance(context)
    }

    @Provides
    fun provideVinhoDao(db: VinheriaDatabase): VinhoDao {
        return db.vinhoDao()
    }
}
```

### 2.8 Atualizar UI - ui/VinhoListScreen.kt
**Atualizar formulário para incluir novos campos:**
- Tipo (Dropdown: Tinto, Branco, Espumante, Rosé)
- Uva, Região, Safra
- Corpo (Dropdown: Leve, Médio, Encorpado)
- Doçura (Dropdown: Seco, Meio-Seco, Doce)
- Teor Alcoólico
- Checkbox "Escolha Agnello"

### 2.9 Atualizar Testes - androidTest/VinhoDaoTest.kt
```kotlin
package com.example.vinheira_agnello_f4

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.vinheira_agnello_f4.data.Vinho
import com.example.vinheira_agnello_f4.data.VinheriaDatabase
import com.example.vinheira_agnello_f4.data.VinhoDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class VinhoDaoTest {

    private lateinit var database: VinheriaDatabase
    private lateinit var dao: VinhoDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            VinheriaDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.vinhoDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndReadVinho() = runBlocking {
        val vinho = Vinho(
            nome = "Teste Vinho Tinto",
            tipo = "Tinto",
            uva = "Cabernet Sauvignon",
            regiao = "Brasil",
            safra = "2020",
            corpo = "Encorpado",
            docura = "Seco",
            teorAlcoolico = 13.5,
            preco = 50.0,
            quantidade = 10,
            escolhaAgnello = true
        )

        dao.insert(vinho)
        val vinhos = dao.getAll().first()

        assertEquals(1, vinhos.size)
        assertEquals("Teste Vinho Tinto", vinhos[0].nome)
        assertEquals("Tinto", vinhos[0].tipo)
        assertTrue(vinhos[0].escolhaAgnello)
    }

    @Test
    fun updateVinho() = runBlocking {
        val vinho = Vinho(
            nome = "Vinho Original",
            tipo = "Branco",
            uva = "Chardonnay",
            regiao = "França",
            safra = "2021",
            corpo = "Leve",
            docura = "Seco",
            teorAlcoolico = 12.0,
            preco = 80.0,
            quantidade = 5
        )

        val id = dao.insert(vinho)
        val vinhoInserido = dao.getById(id)
        assertNotNull(vinhoInserido)

        val vinhoAtualizado = vinhoInserido!!.copy(preco = 90.0, quantidade = 15)
        dao.update(vinhoAtualizado)

        val vinhoFinal = dao.getById(id)
        assertEquals(90.0, vinhoFinal?.preco)
        assertEquals(15, vinhoFinal?.quantidade)
    }

    @Test
    fun deleteVinho() = runBlocking {
        val vinho = Vinho(
            nome = "Vinho para Deletar",
            tipo = "Espumante",
            uva = "Moscato",
            regiao = "Itália",
            safra = "2022",
            corpo = "Leve",
            docura = "Doce",
            teorAlcoolico = 7.5,
            preco = 60.0,
            quantidade = 8
        )

        dao.insert(vinho)
        var vinhos = dao.getAll().first()
        assertEquals(1, vinhos.size)

        dao.delete(vinho)
        vinhos = dao.getAll().first()
        assertTrue(vinhos.isEmpty())
    }

    @Test
    fun filterByTipo() = runBlocking {
        val vinhoTinto = Vinho(
            nome = "Tinto 1", tipo = "Tinto", uva = "Merlot", regiao = "Chile",
            safra = "2019", corpo = "Médio", docura = "Seco", teorAlcoolico = 13.0,
            preco = 40.0, quantidade = 10
        )
        val vinhoBranco = Vinho(
            nome = "Branco 1", tipo = "Branco", uva = "Sauvignon Blanc", regiao = "Argentina",
            safra = "2021", corpo = "Leve", docura = "Seco", teorAlcoolico = 11.5,
            preco = 35.0, quantidade = 12
        )

        dao.insert(vinhoTinto)
        dao.insert(vinhoBranco)

        val tintos = dao.getByTipo("Tinto").first()
        assertEquals(1, tintos.size)
        assertEquals("Tinto 1", tintos[0].nome)
    }

    @Test
    fun filterEscolhaAgnello() = runBlocking {
        val vinhoComum = Vinho(
            nome = "Vinho Comum", tipo = "Tinto", uva = "Tempranillo", regiao = "Espanha",
            safra = "2020", corpo = "Médio", docura = "Seco", teorAlcoolico = 13.5,
            preco = 45.0, quantidade = 15, escolhaAgnello = false
        )
        val vinhoEspecial = Vinho(
            nome = "Vinho Especial", tipo = "Tinto", uva = "Pinot Noir", regiao = "França",
            safra = "2018", corpo = "Encorpado", docura = "Seco", teorAlcoolico = 14.0,
            preco = 200.0, quantidade = 5, escolhaAgnello = true
        )

        dao.insert(vinhoComum)
        dao.insert(vinhoEspecial)

        val escolhaAgnello = dao.getEscolhaAgnello().first()
        assertEquals(1, escolhaAgnello.size)
        assertEquals("Vinho Especial", escolhaAgnello[0].nome)
        assertTrue(escolhaAgnello[0].escolhaAgnello)
    }
}
```

### 2.10 Executar Testes
```bash
cd /Volumes/SSD\ EXTERNO/Faculdade/vinheira_agnello_f4

# Executar todos os testes
./gradlew :app:connectedAndroidTest --info

# Verificar se todos passam (deve ter 5 testes passing)
```

---

## 📝 FASE 3: Testes e Validação

### 3.1 Checklist de Testes - Backend C#

| ID | Operação | Entrada | Saída Esperada | Status |
|----|----------|---------|----------------|--------|
| TC-C#-01 | Criar banco | `dotnet run` | Banco vinheria.db criado com 3 vinhos | ⬜ |
| TC-C#-02 | Listar vinhos | Opção 1 do menu | Exibe 3 vinhos iniciais | ⬜ |
| TC-C#-03 | Adicionar vinho | Opção 5 com dados válidos | Vinho adicionado, novo ID gerado | ⬜ |
| TC-C#-04 | Buscar por ID | Opção 2 com ID existente | Exibe detalhes completos do vinho | ⬜ |
| TC-C#-05 | Buscar por nome | Opção 3 com "Château" | Encontra vinho correspondente | ⬜ |
| TC-C#-06 | Filtrar por tipo | Opção 4 com "Tinto" | Lista apenas vinhos tintos | ⬜ |
| TC-C#-07 | Atualizar vinho | Opção 6, alterar preço | Preço atualizado no banco | ⬜ |
| TC-C#-08 | Excluir vinho | Opção 7 com confirmação | Vinho removido do banco | ⬜ |
| TC-C#-09 | Escolha Agnello | Opção 8 | Lista apenas vinhos marcados | ⬜ |
| TC-C#-10 | Estatísticas | Opção 9 | Total de vinhos e estoque | ⬜ |

### 3.2 Checklist de Testes - Android

| ID | Operação | Entrada | Saída Esperada | Status |
|----|----------|---------|----------------|--------|
| TC-AND-01 | Testes unitários DAO | `./gradlew test` | 5 testes passing | ⬜ |
| TC-AND-02 | Login | Credenciais válidas | Navega para lista de vinhos | ⬜ |
| TC-AND-03 | Listar vinhos | Abrir tela principal | Exibe lista de vinhos cadastrados | ⬜ |
| TC-AND-04 | Adicionar vinho | Preencher formulário completo | Vinho aparece na lista | ⬜ |
| TC-AND-05 | Editar vinho | Clicar em vinho, alterar dados | Dados atualizados na lista | ⬜ |
| TC-AND-06 | Excluir vinho | Botão delete | Vinho removido da lista | ⬜ |
| TC-AND-07 | Validação campos | Tentar salvar vazio | Exibe erro de validação | ⬜ |
| TC-AND-08 | Persistência | Fechar e reabrir app | Dados permanecem salvos | ⬜ |

### 3.3 Comandos de Teste
```bash
# Backend C#
cd "/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4/backend-csharp/VinheriaAgnello.CSharp"
dotnet run

# Android
cd "/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4"
./gradlew :app:connectedAndroidTest --info
./gradlew :app:assembleDebug
```

---

## 📄 FASE 4: Documentação e Entrega

### 4.1 README.md - Backend C#
Criar em `/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4/backend-csharp/README.md`:

```markdown
# Vinheria Agnello - Backend C# com Entity Framework Core

Sistema de persistência para controle de estoque de vinhos.

## Tecnologias
- .NET 8.0
- Entity Framework Core 8.0
- SQLite

## Estrutura do Projeto
- **Models**: Entidade Vinho com campos do domínio
- **Data**: DbContext com configuração Fluent API
- **Repositories**: Padrão Repository para acesso aos dados
- **Program.cs**: Console application com menu interativo

## Como Executar
```bash
cd VinheriaAgnello.CSharp
dotnet restore
dotnet ef database update
dotnet run
```

## Funcionalidades Implementadas
- ✅ CRUD completo de vinhos
- ✅ Busca por nome e tipo
- ✅ Filtro "Escolha Agnello"
- ✅ Estatísticas de estoque
- ✅ Migrations com Entity Framework

## Conceitos Aplicados (Material Didático)
- **Code-First**: Classes C# → Banco de dados
- **Fluent API**: Configuração no OnModelCreating
- **Repository Pattern**: Separação de responsabilidades
- **LINQ**: Consultas tipadas (.Where, .OrderBy)
```

### 4.2 README.md - Android (Atualizar)
Atualizar `/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4/README.md`:

```markdown
# Vinheria Agnello - App Android com Room Database

Sistema de controle de estoque local de vinhos para dispositivos Android.

## Mudanças na Versão 2.0
- Migração de entidade genérica `Produto` para `Vinho` específico
- Adição de campos do domínio: tipo, uva, região, safra, corpo, doçura
- Filtros avançados: por tipo, "Escolha Agnello"
- 5 testes instrumentados implementados

## Tecnologias
- Kotlin + Jetpack Compose
- Room Database 2.6.1
- Hilt (Dependency Injection)
- Coroutines + Flow

## Como Executar
```bash
./gradlew :app:assembleDebug
./gradlew :app:connectedAndroidTest
```

## Conceitos Aplicados (Material Didático)
- **@Entity**: Define tabela SQLite
- **@Dao**: Interface de acesso aos dados
- **@Database**: RoomDatabase com Singleton
- **Repository Pattern**: Isolamento de dados
- **Migrations**: Controle de versão do schema
```

### 4.3 Estrutura do Documento Word

**Arquivo:** `LucasSeunome_RM123456_Fase4_Atividade.docx`

**Estrutura sugerida:**

```
1. CAPA
   - Nome completo
   - RM
   - Título: Fase 4 - Persistência de Dados

2. PARTE 1: BACKEND C# COM ENTITY FRAMEWORK CORE
   2.1 Introdução
       - Objetivo: sistema de estoque com C#
       - Tecnologias: .NET 8, EF Core, SQLite

   2.2 Modelo de Dados
       - Screenshot do código Vinho.cs
       - Explicação dos campos do domínio

   2.3 DbContext e Configuração
       - Screenshot do VinheriaContext.cs
       - Fluent API no OnModelCreating

   2.4 Repository Pattern
       - Screenshot do VinhoRepository.cs
       - Explicação dos métodos CRUD

   2.5 Migrations
       - Screenshot do comando Add-Migration
       - Screenshot do comando Update-Database
       - Explicação do processo

   2.6 Demonstração Funcional
       - Screenshot do menu interativo
       - Screenshot: Listar vinhos (3 iniciais)
       - Screenshot: Adicionar novo vinho
       - Screenshot: Buscar por nome
       - Screenshot: Atualizar estoque
       - Screenshot: Estatísticas
       - Screenshot: Excluir vinho

   2.7 Conceitos Aplicados do Material Didático
       - Code-First Approach
       - Fluent API vs Data Annotations
       - LINQ para consultas
       - Ciclo de vida das entidades

3. PARTE 2: ANDROID COM ROOM DATABASE
   3.1 Introdução
       - Objetivo: controle de estoque local mobile
       - Tecnologias: Kotlin, Room, Hilt

   3.2 Refatoração Produto → Vinho
       - Justificativa: alinhamento com domínio
       - Screenshot: entidade Vinho.kt
       - Comparação antes/depois

   3.3 Componentes Room
       - Screenshot: VinhoDao.kt
       - Screenshot: VinheriaDatabase.kt
       - Screenshot: VinhoRepository.kt

   3.4 Integração com UI
       - Screenshot: VinhoViewModel.kt
       - Screenshot: VinhoListScreen.kt (formulário completo)

   3.5 Testes Instrumentados
       - Screenshot: VinhoDaoTest.kt
       - Screenshot: Resultado dos testes (5 passing)

   3.6 Demonstração Funcional
       - Screenshot: Tela de login
       - Screenshot: Lista de vinhos
       - Screenshot: Formulário de adicionar (campos completos)
       - Screenshot: Edição de vinho
       - Screenshot: Lista atualizada

   3.7 Conceitos Aplicados do Material Didático
       - @Entity, @Dao, @Database
       - Singleton Pattern
       - Repository Pattern
       - Flow para reatividade

4. COMPARAÇÃO ENTRE AS TECNOLOGIAS
   4.1 Tabela Comparativa

   | Aspecto | Entity Framework Core | Room Database |
   |---------|----------------------|---------------|
   | Linguagem | C# | Kotlin |
   | Banco | SQLite (também SQL Server, Oracle) | SQLite |
   | ORM | Sim (LINQ) | Sim (com anotações) |
   | Migrations | CLI: Add-Migration | Incremental (version) |
   | Reatividade | Não nativo | Flow nativo |
   | Validação | Runtime | Compile-time (SQL) |

   4.2 Semelhanças
       - Ambos são ORMs
       - Code-First approach
       - Repository Pattern
       - CRUD completo

   4.3 Diferenças
       - EF Core: mais flexível (múltiplos bancos)
       - Room: otimizado para Android
       - EF Core: Fluent API robusta
       - Room: Compile-time verification

5. CONCLUSÃO
   - Aprendizados sobre persistência de dados
   - Importância de ORMs
   - Aplicabilidade prática

6. REFERÊNCIAS
   - Material didático: 3ESO - Fase 04 - 03 (Entity Framework)
   - Material didático: 3ESO - Fase 04 - 07 (Room Database)
   - Documentação oficial Microsoft Entity Framework Core
   - Documentação oficial Android Room
```

### 4.4 Screenshots Necessários

**Backend C# (10 screenshots):**
1. Estrutura de pastas do projeto
2. Código Vinho.cs (modelo completo)
3. Código VinheriaContext.cs (OnModelCreating)
4. Código VinhoRepository.cs (métodos CRUD)
5. Terminal: `dotnet ef migrations add InitialCreate`
6. Terminal: `dotnet ef database update`
7. Menu interativo do console
8. Operação: Listar vinhos (3 iniciais)
9. Operação: Adicionar novo vinho (formulário preenchido)
10. Operação: Estatísticas do estoque

**Android (10 screenshots):**
1. Estrutura de pastas do projeto
2. Código Vinho.kt (entidade completa)
3. Código VinhoDao.kt (operações)
4. Código VinheriaDatabase.kt (versão 2)
5. Código VinhoDaoTest.kt (5 testes)
6. Resultado dos testes: 5 passing
7. Tela de login
8. Tela: Lista de vinhos
9. Tela: Formulário de adicionar vinho (campos completos)
10. Tela: Lista atualizada após adicionar

### 4.5 Código-Fonte para Anexar

**Organizar em pasta:**
```
LucasSeunome_RM123456_Fase4_CodigoFonte/
├── backend-csharp/
│   ├── Models/Vinho.cs
│   ├── Data/VinheriaContext.cs
│   ├── Repositories/VinhoRepository.cs
│   └── Program.cs
└── android/
    ├── data/Vinho.kt
    ├── data/VinhoDao.kt
    ├── data/VinheriaDatabase.kt
    ├── data/VinhoRepository.kt
    ├── viewmodel/VinhoViewModel.kt
    └── androidTest/VinhoDaoTest.kt
```

---

## 📁 Estrutura Final do Projeto

```
/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4/
├── app/                                          # Android (refatorado)
│   ├── src/
│   │   ├── main/java/com/example/vinheira_agnello_f4/
│   │   │   ├── data/
│   │   │   │   ├── Vinho.kt                     ✨ NOVO
│   │   │   │   ├── VinhoDao.kt                   ✨ NOVO
│   │   │   │   ├── VinhoRepository.kt            ✨ NOVO
│   │   │   │   └── VinheriaDatabase.kt           📝 ATUALIZADO (v2)
│   │   │   ├── viewmodel/
│   │   │   │   └── VinhoViewModel.kt             ✨ NOVO
│   │   │   ├── ui/
│   │   │   │   └── VinhoListScreen.kt            ✨ NOVO
│   │   │   ├── di/
│   │   │   │   └── DataModule.kt                 📝 ATUALIZADO
│   │   │   └── MainActivity.kt
│   │   └── androidTest/java/.../
│   │       └── VinhoDaoTest.kt                   📝 ATUALIZADO (5 testes)
│   └── build.gradle.kts
├── backend-csharp/                               ✨ NOVO (Completo)
│   ├── VinheriaAgnello.CSharp/
│   │   ├── Models/
│   │   │   └── Vinho.cs
│   │   ├── Data/
│   │   │   ├── VinheriaContext.cs
│   │   │   └── Migrations/
│   │   │       └── 20240101_InitialCreate.cs
│   │   ├── Repositories/
│   │   │   └── VinhoRepository.cs
│   │   ├── Program.cs
│   │   ├── VinheriaAgnello.CSharp.csproj
│   │   └── vinheria.db                           (gerado)
│   ├── VinheriaAgnello.CSharp.sln
│   └── README.md
├── README.md                                      📝 ATUALIZADO
└── ENTREGA/
    ├── LucasSeunome_RM123456_Fase4_Atividade.docx  ✨ NOVO
    └── CodigoFonte/                               ✨ NOVO
        ├── backend-csharp/
        └── android/
```

---

## ✅ Checklist de Entrega Final

### Backend C# ✨
- [ ] Projeto criado com `dotnet new console`
- [ ] Pacotes NuGet instalados (EF Core + SQLite)
- [ ] Entidade `Vinho` com todos os campos
- [ ] `VinheriaContext` com Fluent API
- [ ] `VinhoRepository` com CRUD completo
- [ ] `Program.cs` com menu interativo
- [ ] Migrations criadas e aplicadas
- [ ] Banco `vinheria.db` gerado
- [ ] 3 vinhos iniciais inseridos
- [ ] Todas as operações testadas (10 operações)
- [ ] README.md criado
- [ ] 10 screenshots capturados

### Android 🔄
- [ ] Entidade `Produto` renomeada para `Vinho`
- [ ] Campos específicos adicionados (tipo, uva, região, etc.)
- [ ] `VinhoDao` com queries completas
- [ ] `VinheriaDatabase` versão 2
- [ ] `VinhoRepository` atualizado
- [ ] `VinhoViewModel` refatorado
- [ ] UI atualizada (formulário completo)
- [ ] Módulo DI atualizado
- [ ] 5 testes instrumentados implementados
- [ ] Todos os testes passing
- [ ] README.md atualizado
- [ ] 10 screenshots capturados

### Documentação 📄
- [ ] Documento Word criado
- [ ] Capa com nome e RM
- [ ] Seção C# com explicações
- [ ] Seção Android com explicações
- [ ] 20 screenshots inseridos (10 C# + 10 Android)
- [ ] Comparação entre tecnologias
- [ ] Conceitos do material didático citados
- [ ] Códigos-fonte anexados
- [ ] Arquivo nomeado corretamente: `NomeCompleto_RM_Fase4_Atividade.docx`

---

## 🛠️ Comandos Úteis - Referência Rápida

### Backend C#
```bash
# Navegar para projeto
cd "/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4/backend-csharp/VinheriaAgnello.CSharp"

# Compilar
dotnet build

# Executar
dotnet run

# Migrations
dotnet ef migrations add NomeDaMigracao
dotnet ef database update
dotnet ef migrations list

# Limpar
dotnet clean
```

### Android
```bash
# Navegar para projeto
cd "/Volumes/SSD EXTERNO/Faculdade/vinheira_agnello_f4"

# Compilar
./gradlew build

# Executar testes
./gradlew :app:connectedAndroidTest --info

# Limpar
./gradlew clean

# Gerar APK
./gradlew :app:assembleDebug
```

### Banco de Dados SQLite
```bash
# Abrir banco C#
cd backend-csharp/VinheriaAgnello.CSharp
sqlite3 vinheria.db

# Comandos SQLite
.tables                 # Listar tabelas
.schema Vinhos          # Ver estrutura da tabela
SELECT * FROM Vinhos;   # Ver dados
.quit                   # Sair
```

---

## 🎯 Prioridades de Implementação

### URGENTE (Fazer primeiro - 2-3 dias)
1. ✅ Backend C# completo
   - Criar projeto e estrutura
   - Implementar todas as classes
   - Testar menu interativo
   - Capturar screenshots

### IMPORTANTE (Fazer em seguida - 1-2 dias)
2. ✅ Refatoração Android
   - Renomear arquivos
   - Adicionar campos
   - Atualizar testes
   - Capturar screenshots

### NECESSÁRIO (Finalizar - 1 dia)
3. ✅ Documentação
   - Criar documento Word
   - Inserir screenshots
   - Escrever explicações
   - Anexar código-fonte

### OPCIONAL (Se sobrar tempo)
4. ⭐ Melhorias extras
   - Adicionar mais vinhos iniciais
   - Criar seed data a partir do vinhos.json
   - Implementar paginação
   - Adicionar gráficos de estoque

---

## 📚 Conceitos do Material Didático Aplicados

### Entity Framework Core (Material: 3ESO - Fase 04 - 03)
✅ **Code-First**: Classes C# → Banco gerado automaticamente
✅ **DbContext**: Classe `VinheriaContext` gerencia conexão
✅ **DbSet<T>**: `DbSet<Vinho>` representa tabela
✅ **Fluent API**: Configuração no `OnModelCreating`
✅ **Data Annotations**: `[Required]`, `[StringLength]`, etc.
✅ **Migrations**: `Add-Migration` + `Update-Database`
✅ **CRUD Operations**: `Add()`, `Find()`, `Update()`, `Remove()`
✅ **LINQ Queries**: `.Where()`, `.OrderBy()`, `.Contains()`
✅ **Repository Pattern**: Separação de responsabilidades

### Room Database (Material: 3ESO - Fase 04 - 07)
✅ **@Entity**: Define tabela SQLite
✅ **@PrimaryKey**: Chave primária com autoincrement
✅ **@ColumnInfo**: Customização de nomes de colunas
✅ **@Dao**: Interface com métodos SQL
✅ **@Query**: SQL validado em compile-time
✅ **@Insert/@Update/@Delete**: Operações CRUD
✅ **RoomDatabase**: Classe abstrata com singleton
✅ **Migrations**: `.fallbackToDestructiveMigration()`
✅ **Repository Pattern**: Isolamento de dados
✅ **Flow**: Reatividade para atualizações automáticas

---

## 🎓 Observações Finais

### Alinhamento com Enunciado
✅ **Persistência Local Android**: Room Database implementado
✅ **Persistência Servidor C#**: Entity Framework Core implementado
✅ **CRUD Completo**: Ambas as plataformas
✅ **Screenshots**: Evidências de todas as operações
✅ **Código-fonte**: Organizado e documentado

### Diferencial Implementado
⭐ Modelo de dados específico do domínio (Vinho, não Produto genérico)
⭐ Continuidade com fases anteriores
⭐ Dados iniciais coerentes com o negócio
⭐ Consultas avançadas (filtros, busca, estatísticas)
⭐ Testes abrangentes (5 testes Android + validação manual C#)

### Próximos Passos (Após Entrega)
- Revisar feedback do professor
- Possível integração entre Android e backend C# (Web API)
- Sincronização de dados entre plataformas
- Deploy em ambiente de produção

---

**Criado em:** 2025-12-02
**Versão:** 1.0 - Plano Consolidado e Corrigido
**Status:** ✅ Pronto para Execução
