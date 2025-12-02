# 🍷 Módulo de Persistência - Vinheria Agnello (Backend C#)

Este projeto implementa o módulo de persistência de dados para o sistema de estoque da Vinheria Agnello, utilizando **C# .NET 8** e **Entity Framework Core**.

## 📋 Sobre a Solução

Para atender ao requisito de *"criar um módulo de persistência em C#"* de forma objetiva e funcional, foi desenvolvida uma **Console Application** que isola a lógica de banco de dados.

Esta abordagem foi escolhida por:
1.  **Foco na Persistência:** Permite demonstrar claramente o uso do ORM (Entity Framework) sem a complexidade adicional de configuração de servidores web (IIS/Kestrel) ou rotas de API.
2.  **Simplicidade e Robustez:** Garante que o código rode em qualquer ambiente com .NET instalado, sem dependências de infraestrutura complexa.
3.  **Code-First:** O banco de dados é gerado automaticamente a partir das classes de domínio, demonstrando o domínio da técnica solicitada.

### Tecnologias Utilizadas
*   **Linguagem:** C# (.NET 8)
*   **ORM:** Entity Framework Core
*   **Banco de Dados:** SQLite (Arquivo local `estoque.db` para portabilidade)
*   **Arquitetura:** Camada de Dados (`AppDbContext`, `Produto`) separada da Lógica de Apresentação (`Program.cs`).

---

## 🚀 Como Executar

### Pré-requisitos
*   [.NET 8 SDK](https://dotnet.microsoft.com/download) instalado.

### Passo a Passo

1.  Abra o terminal na pasta do projeto:
    ```bash
    cd EstoqueVinheria
    ```

2.  Execute o projeto:
    ```bash
    dotnet run
    ```

3.  **Interaja com o Menu:**
    O sistema apresentará um menu no terminal para realizar as operações de CRUD:
    *   `1. Listar produtos` (Verifique a carga inicial de dados)
    *   `2. Adicionar produto`
    *   `3. Atualizar produto`
    *   `4. Deletar produto`

> **Nota:** Na primeira execução, o sistema criará automaticamente o arquivo do banco de dados `estoque.db` e inserirá uma carga inicial de vinhos (mock data) para facilitar os testes.

---

## ✅ Atendimento aos Requisitos

| Requisito da Faculdade | Implementação |
| :--- | :--- |
| **"Implementação de persistência de dados utilizando C#"** | Utilizado **Entity Framework Core** para mapear objetos para o banco. |
| **"Criar um módulo de persistência"** | Implementado através da classe `AppDbContext` e entidade `Produto`. |
| **"Gerenciar o estoque"** | Funcionalidades completas de **CRUD** (Criar, Ler, Atualizar, Deletar) acessíveis via menu. |
| **"Code-First"** | O banco é criado via `db.Database.EnsureCreated()` baseado na classe `Produto`. |

---

**Desenvolvido para a Fase 4 - Vinheria Agnello**
