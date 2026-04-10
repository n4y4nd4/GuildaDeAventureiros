package com.guilda.dto.response;

import com.guilda.enums.EspecieCompanheiro;
import com.guilda.model.Companheiro;

public class CompanheiroResponse {
    private String nome;
    private EspecieCompanheiro especie;
    private Integer lealdade;

    public static CompanheiroResponse de(Companheiro c) {
        if (c == null) return null;
        CompanheiroResponse r = new CompanheiroResponse();
        r.nome = c.getNome();
        r.especie = c.getEspecie();
        r.lealdade = c.getLealdade();
        return r;
    }

    public String getNome() { return nome; }
    public EspecieCompanheiro getEspecie() { return especie; }
    public Integer getLealdade() { return lealdade; }
}
