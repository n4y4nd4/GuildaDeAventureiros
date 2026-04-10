package com.guilda.dto.response;

import com.guilda.enums.ClasseAventureiro;
import com.guilda.model.Aventureiro;

public class AvenureiroResumoResponse {
    private Long id;
    private String nome;
    private ClasseAventureiro classe;
    private Integer nivel;
    private boolean ativo;

    public static AvenureiroResumoResponse de(Aventureiro a) {
        AvenureiroResumoResponse r = new AvenureiroResumoResponse();
        r.id = a.getId();
        r.nome = a.getNome();
        r.classe = a.getClasse();
        r.nivel = a.getNivel();
        r.ativo = a.isAtivo();
        return r;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public ClasseAventureiro getClasse() { return classe; }
    public Integer getNivel() { return nivel; }
    public boolean isAtivo() { return ativo; }
}
