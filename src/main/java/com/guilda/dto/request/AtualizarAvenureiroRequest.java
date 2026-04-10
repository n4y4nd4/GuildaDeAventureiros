package com.guilda.dto.request;

import com.guilda.enums.ClasseAventureiro;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AtualizarAvenureiroRequest {

    @NotBlank(message = "nome é obrigatório")
    private String nome;

    @NotNull(message = "classe é obrigatória")
    private ClasseAventureiro classe;

    @NotNull(message = "nivel é obrigatório")
    @Min(value = 1, message = "nivel deve ser maior ou igual a 1")
    private Integer nivel;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public ClasseAventureiro getClasse() { return classe; }
    public void setClasse(ClasseAventureiro classe) { this.classe = classe; }
    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }
}
