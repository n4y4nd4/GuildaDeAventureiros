package com.guilda.aventura.entity;

import com.guilda.aventura.enums.PapelMissao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Mapeado sobre operacoes.participacao_missao (DDL real do banco).
 * Colunas: missao_id, aventureiro_id, papel, recompensa_ouro, destaque, data_registro
 * PK composta: (missao_id, aventureiro_id)
 */
@Entity
@IdClass(ParticipacaoMissaoId.class)
@Table(schema = "operacoes", name = "participacao_missao")
public class ParticipacaoMissao {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "missao_id", nullable = false)
    private Missao missao;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aventureiro_id", nullable = false)
    private Aventureiro aventureiro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PapelMissao papel;

    @Column(name = "recompensa_ouro", precision = 10, scale = 2)
    private BigDecimal recompensaOuro = BigDecimal.ZERO;

    // coluna real: destaque (não mvp)
    @Column(nullable = false)
    private boolean destaque = false;

    @Column(name = "data_registro", insertable = false, updatable = false)
    private LocalDateTime dataRegistro;

    protected ParticipacaoMissao() {}

    public ParticipacaoMissao(Missao missao, Aventureiro aventureiro, PapelMissao papel) {
        this.missao = missao;
        this.aventureiro = aventureiro;
        this.papel = papel;
    }

    public Missao getMissao() { return missao; }
    public Aventureiro getAventureiro() { return aventureiro; }
    public PapelMissao getPapel() { return papel; }
    public void setPapel(PapelMissao papel) { this.papel = papel; }
    public BigDecimal getRecompensaOuro() { return recompensaOuro; }
    public void setRecompensaOuro(BigDecimal recompensaOuro) { this.recompensaOuro = recompensaOuro; }
    public boolean isDestaque() { return destaque; }
    public void setDestaque(boolean destaque) { this.destaque = destaque; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
}
