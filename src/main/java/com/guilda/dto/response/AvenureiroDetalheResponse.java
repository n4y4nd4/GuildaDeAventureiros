package com.guilda.dto.response;

import com.guilda.enums.ClasseAventureiro;
import com.guilda.model.Aventureiro;

public class AvenureiroDetalheResponse {
    private Long id;
    private String nome;
    private ClasseAventureiro classe;
    private Integer nivel;
    private boolean ativo;
    private CompanheiroResponse companheiro;

    public static AvenureiroDetalheResponse de(Aventureiro a) {
        AvenureiroDetalheResponse r = new AvenureiroDetalheResponse();
        r.id = a.getId();
        r.nome = a.getNome();
        r.classe = a.getClasse();
        r.nivel = a.getNivel();
        r.ativo = a.isAtivo();
        r.companheiro = CompanheiroResponse.de(a.getCompanheiro());
        return r;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public ClasseAventureiro getClasse() { return classe; }
    public Integer getNivel() { return nivel; }
    public boolean isAtivo() { return ativo; }
    public CompanheiroResponse getCompanheiro() { return companheiro; }
}
