package com.guilda.aventura.dto;

import com.guilda.aventura.entity.Missao;
import com.guilda.aventura.enums.NivelPerigo;
import com.guilda.aventura.enums.StatusMissao;

import java.time.LocalDateTime;
import java.util.List;

public record MissaoResponse(
        Long id,
        String titulo,
        StatusMissao status,
        NivelPerigo nivelPerigo,
        Long organizacaoId,
        LocalDateTime dataCriacao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        List<ParticipacaoResponse> participacoes
) {
    /** Para listagem — sem participantes */
    public static MissaoResponse de(Missao m) {
        return new MissaoResponse(
            m.getId(),
            m.getTitulo(),
            m.getStatus(),
            m.getNivelPerigo(),
            m.getOrganizacao().getId(),
            m.getDataCriacao(),
            m.getDataInicio(),
            m.getDataFim(),
            List.of()
        );
    }

    /** Para detalhe — com participantes */
    public static MissaoResponse deComParticipantes(Missao m) {
        List<ParticipacaoResponse> parts = m.getParticipacoes().stream()
            .map(ParticipacaoResponse::de)
            .toList();
        return new MissaoResponse(
            m.getId(),
            m.getTitulo(),
            m.getStatus(),
            m.getNivelPerigo(),
            m.getOrganizacao().getId(),
            m.getDataCriacao(),
            m.getDataInicio(),
            m.getDataFim(),
            parts
        );
    }
}
