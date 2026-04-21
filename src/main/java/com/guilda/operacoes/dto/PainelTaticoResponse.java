package com.guilda.operacoes.dto;

import com.guilda.operacoes.entity.PainelTaticoMissao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PainelTaticoResponse(
        Long missaoId,
        String titulo,
        String status,
        String nivelPerigo,
        Long organizacaoId,
        Long totalParticipantes,
        BigDecimal nivelMedioEquipe,
        BigDecimal totalRecompensa,
        Long totalMvps,
        Long participantesComCompanheiro,
        LocalDateTime ultimaAtualizacao,
        BigDecimal indiceProntidao
) {
    public static PainelTaticoResponse de(PainelTaticoMissao m) {
        return new PainelTaticoResponse(
            m.getMissaoId(),
            m.getTitulo(),
            m.getStatus(),
            m.getNivelPerigo(),
            m.getOrganizacaoId(),
            m.getTotalParticipantes(),
            m.getNivelMedioEquipe(),
            m.getTotalRecompensa(),
            m.getTotalMvps(),
            m.getParticipantesComCompanheiro(),
            m.getUltimaAtualizacao(),
            m.getIndiceProntidao()
        );
    }
}
