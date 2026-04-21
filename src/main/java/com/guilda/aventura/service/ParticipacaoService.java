package com.guilda.aventura.service;

import com.guilda.aventura.entity.Aventureiro;
import com.guilda.aventura.entity.Missao;
import com.guilda.aventura.entity.ParticipacaoMissao;
import com.guilda.aventura.enums.PapelMissao;
import com.guilda.aventura.enums.StatusMissao;
import com.guilda.aventura.repository.AventureiroRepository;
import com.guilda.aventura.repository.MissaoRepository;
import com.guilda.aventura.repository.ParticipacaoMissaoRepository;
import com.guilda.common.exception.RequisicaoInvalidaException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ParticipacaoService {

    private final ParticipacaoMissaoRepository participacaoRepository;
    private final AventureiroRepository aventureiroRepository;
    private final MissaoRepository missaoRepository;

    public ParticipacaoService(ParticipacaoMissaoRepository participacaoRepository,
                                AventureiroRepository aventureiroRepository,
                                MissaoRepository missaoRepository) {
        this.participacaoRepository = participacaoRepository;
        this.aventureiroRepository = aventureiroRepository;
        this.missaoRepository = missaoRepository;
    }

    public ParticipacaoMissao associar(Long missaoId, Long aventureiroId,
                                        PapelMissao papel, BigDecimal recompensaOuro,
                                        boolean destaque) {

        Aventureiro aventureiro = aventureiroRepository.findById(aventureiroId)
            .orElseThrow(() -> new RequisicaoInvalidaException(
                "Aventureiro com id " + aventureiroId + " não encontrado."));

        if (!aventureiro.isAtivo()) {
            throw new RequisicaoInvalidaException(
                "Aventureiro inativo não pode ser associado a novas missões.");
        }

        Missao missao = missaoRepository.findById(missaoId)
            .orElseThrow(() -> new RequisicaoInvalidaException(
                "Missão com id " + missaoId + " não encontrada."));

        if (missao.getStatus() == StatusMissao.CONCLUIDA
                || missao.getStatus() == StatusMissao.CANCELADA) {
            throw new RequisicaoInvalidaException(
                "Missão com status " + missao.getStatus() + " não aceita novos participantes.");
        }

        if (!missao.getOrganizacao().getId().equals(aventureiro.getOrganizacao().getId())) {
            throw new RequisicaoInvalidaException(
                "Aventureiro e missão pertencem a organizações diferentes.");
        }

        ParticipacaoMissao participacao = new ParticipacaoMissao(missao, aventureiro, papel);
        participacao.setRecompensaOuro(recompensaOuro != null ? recompensaOuro : BigDecimal.ZERO);
        participacao.setDestaque(destaque);
        return participacaoRepository.save(participacao);
    }
}
