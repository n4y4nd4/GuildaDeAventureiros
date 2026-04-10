package com.guilda.aventura.projection;

import com.guilda.aventura.enums.ClasseAventureiro;
import com.guilda.aventura.enums.EspecieCompanheiro;

import java.time.OffsetDateTime;

public interface AventureiroDetalheView {
    Long getId();
    String getNome();
    ClasseAventureiro getClasse();
    Integer getNivel();
    Boolean getAtivo();
    String getCompanheiroNome();
    EspecieCompanheiro getCompanheiroEspecie();
    Integer getCompanheiroLealdade();
    Long getTotalParticipacoes();
    OffsetDateTime getUltimaParticipacaoEm();
}
