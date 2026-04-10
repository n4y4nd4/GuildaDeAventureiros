package com.guilda.aventura.projection;

import com.guilda.aventura.enums.NivelPerigo;
import com.guilda.aventura.enums.StatusMissao;

import java.math.BigDecimal;

public interface RelatorioMissaoView {
    Long getId();
    String getTitulo();
    StatusMissao getStatus();
    NivelPerigo getNivelPerigo();
    Long getTotalParticipantes();
    BigDecimal getTotalRecompensas();
}
