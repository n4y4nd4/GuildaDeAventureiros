package com.guilda.dto.request;

import com.guilda.enums.EspecieCompanheiro;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CompanheiroRequest {

    @NotBlank(message = "nome do companheiro é obrigatório")
    private String nome;

    @NotNull(message = "especie é obrigatória")
    private EspecieCompanheiro especie;

    @NotNull(message = "lealdade é obrigatória")
    @Min(value = 0, message = "lealdade deve ser maior ou igual a 0")
    @Max(value = 100, message = "lealdade deve ser menor ou igual a 100")
    private Integer lealdade;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public EspecieCompanheiro getEspecie() { return especie; }
    public void setEspecie(EspecieCompanheiro especie) { this.especie = especie; }
    public Integer getLealdade() { return lealdade; }
    public void setLealdade(Integer lealdade) { this.lealdade = lealdade; }
}
