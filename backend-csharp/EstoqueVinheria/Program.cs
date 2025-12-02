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
            
            // Dados iniciais se vazio
            if (!db.Produtos.Any())
            {
                db.Produtos.Add(new Produto {
                    Nome = "Vinho Tinto",
                    Preco = 50,
                    Quantidade = 10,
                    Descricao = "Vinho suave da casa"
                });
                db.SaveChanges();
                Console.WriteLine("✓ Dados iniciais inseridos!\n");
            }
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
