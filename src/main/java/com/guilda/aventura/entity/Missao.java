package com.guilda.aventura.entity;

import com.guilda.audit.entity.Organizacao;
import com.guilda.aventura.enums.NivelPerigo;
import com.guilda.aventura.enums.StatusMissao;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapeado sobre operacoes.missao (DDL real do banco).
 * Colunas: id, organizacao_id, titulo, nivel_perigo, status,
 *          data_criacao, data_inicio, data_fim
 */
@Entity
@Table(schema = "operacoes", name = "missao")
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "missao_seq")
    @SequenceGenerator(name = "missao_seq",
                       sequenceName = "operacoes.missao_id_seq",
                       allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_perigo", nullable = false, length = 20)
    private NivelPerigo nivelPerigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMissao status = StatusMissao.PLANEJADA;

    @Column(name = "data_criacao", insertable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @OneToMany(mappedBy = "missao", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ParticipacaoMissao> participacoes = new ArrayList<>();

    protected Missao() {}

    public Missao(Organizacao organizacao, String titulo, NivelPerigo nivelPerigo) {
        this.organizacao = organizacao;
        this.titulo = titulo;
        this.nivelPerigo = nivelPerigo;
    }

    public Long getId() { return id; }
    public Organizacao getOrganizacao() { return organizacao; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public NivelPerigo getNivelPerigo() { return nivelPerigo; }
    public void setNivelPerigo(NivelPerigo nivelPerigo) { this.nivelPerigo = nivelPerigo; }
    public StatusMissao getStatus() { return status; }
    public void setStatus(StatusMissao status) { this.status = status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public List<ParticipacaoMissao> getParticipacoes() { return participacoes; }
}
