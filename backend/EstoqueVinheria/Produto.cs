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
