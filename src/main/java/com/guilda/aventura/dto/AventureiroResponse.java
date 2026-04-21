package com.guilda.aventura.dto;

import com.guilda.aventura.entity.Aventureiro;
import com.guilda.aventura.enums.ClasseAventureiro;

import java.time.LocalDateTime;

public record AventureiroResponse(
        Long id,
        String nome,
        ClasseAventureiro classe,
        Integer nivel,
        boolean ativo,
        Long organizacaoId,
        LocalDateTime dataCriacao
) {
    public static AventureiroResponse de(Aventureiro a) {
        return new AventureiroResponse(
            a.getId(), a.getNome(), a.getClasse(), a.getNivel(),
            a.isAtivo(), a.getOrganizacao().getId(), a.getDataCriacao()
        );
    }
}
