package com.guilda.aventura.entity;

import java.io.Serializable;
import java.util.Objects;

public class ParticipacaoMissaoId implements Serializable {

    private Long missao;
    private Long aventureiro;

    public ParticipacaoMissaoId() {}

    public ParticipacaoMissaoId(Long missao, Long aventureiro) {
        this.missao = missao;
        this.aventureiro = aventureiro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticipacaoMissaoId that)) return false;
        return Objects.equals(missao, that.missao) && Objects.equals(aventureiro, that.aventureiro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(missao, aventureiro);
    }
}
