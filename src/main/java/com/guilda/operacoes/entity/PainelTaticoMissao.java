package com.guilda.operacoes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Questão 1 — Entity JPA somente leitura.
 *
 * Mapeada sobre a view operacoes.vw_painel_tatico_missao
 * (o enunciado chama de mv_painel_tatico_missao, mas no banco existe como vw_).
 *
 * @Immutable garante que o Hibernate nunca emite INSERT/UPDATE/DELETE.
 * Nenhuma coluna foi renomeada ou alterada.
 */
@Entity
@Immutable
@Table(schema = "operacoes", name = "vw_painel_tatico_missao")
public class PainelTaticoMissao {

    @Id
    @Column(name = "missao_id", insertable = false, updatable = false)
    private Long missaoId;

    @Column(name = "titulo", insertable = false, updatable = false)
    private String titulo;

    @Column(name = "status", insertable = false, updatable = false)
    private String status;

    @Column(name = "nivel_perigo", insertable = false, updatable = false)
    private String nivelPerigo;

    @Column(name = "organizacao_id", insertable = false, updatable = false)
    private Long organizacaoId;

    @Column(name = "total_participantes", insertable = false, updatable = false)
    private Long totalParticipantes;

    @Column(name = "nivel_medio_equipe", insertable = false, updatable = false)
    private BigDecimal nivelMedioEquipe;

    @Column(name = "total_recompensa", insertable = false, updatable = false)
    private BigDecimal totalRecompensa;

    @Column(name = "total_mvps", insertable = false, updatable = false)
    private Long totalMvps;

    @Column(name = "participantes_com_companheiro", insertable = false, updatable = false)
    private Long participantesComCompanheiro;

    // timestamp without time zone → LocalDateTime (sem timezone, conforme DDL real)
    @Column(name = "ultima_atualizacao", insertable = false, updatable = false)
    private LocalDateTime ultimaAtualizacao;

    @Column(name = "indice_prontidao", insertable = false, updatable = false)
    private BigDecimal indiceProntidao;

    protected PainelTaticoMissao() {}

    public Long getMissaoId() { return missaoId; }
    public String getTitulo() { return titulo; }
    public String getStatus() { return status; }
    public String getNivelPerigo() { return nivelPerigo; }
    public Long getOrganizacaoId() { return organizacaoId; }
    public Long getTotalParticipantes() { return totalParticipantes; }
    public BigDecimal getNivelMedioEquipe() { return nivelMedioEquipe; }
    public BigDecimal getTotalRecompensa() { return totalRecompensa; }
    public Long getTotalMvps() { return totalMvps; }
    public Long getParticipantesComCompanheiro() { return participantesComCompanheiro; }
    public LocalDateTime getUltimaAtualizacao() { return ultimaAtualizacao; }
    public BigDecimal getIndiceProntidao() { return indiceProntidao; }
}
