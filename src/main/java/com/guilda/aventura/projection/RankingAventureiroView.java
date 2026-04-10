package com.guilda.aventura.projection;

import java.math.BigDecimal;

public interface RankingAventureiroView {
    Long getId();
    String getNome();
    Long getTotalParticipacoes();
    BigDecimal getTotalRecompensas();
    Long getTotalMvps();
}
