package com.guilda.aventura.dto;

import com.guilda.aventura.entity.ParticipacaoMissao;
import com.guilda.aventura.enums.PapelMissao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParticipacaoResponse(
        Long aventureiroId,
        String aventureiroNome,
        PapelMissao papel,
        BigDecimal recompensaOuro,
        boolean destaque,
        LocalDateTime dataRegistro
) {
    public static ParticipacaoResponse de(ParticipacaoMissao p) {
        return new ParticipacaoResponse(
            p.getAventureiro().getId(),
            p.getAventureiro().getNome(),
            p.getPapel(),
            p.getRecompensaOuro(),
            p.isDestaque(),
            p.getDataRegistro()
        );
    }
}
