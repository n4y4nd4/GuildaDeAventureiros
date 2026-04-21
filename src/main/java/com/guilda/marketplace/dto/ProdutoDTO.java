package com.guilda.marketplace.dto;

import java.math.BigDecimal;

public class ProdutoDTO {
    private String nome;
    private String descricao;
    private String categoria;
    private String raridade;
    private BigDecimal preco;

    public ProdutoDTO() {}

    public ProdutoDTO(String nome, String descricao, String categoria,
                      String raridade, BigDecimal preco) {
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.raridade = raridade;
        this.preco = preco;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getRaridade() { return raridade; }
    public void setRaridade(String raridade) { this.raridade = raridade; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
}
