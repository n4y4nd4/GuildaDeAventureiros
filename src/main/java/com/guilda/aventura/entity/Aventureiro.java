package com.guilda.aventura.entity;

import com.guilda.audit.entity.Organizacao;
import com.guilda.audit.entity.Usuario;
import com.guilda.aventura.enums.ClasseAventureiro;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapeado sobre operacoes.aventureiro (DDL real do banco).
 * Colunas: id, organizacao_id, usuario_cadastro_id, nome, classe, nivel,
 *          ativo, data_criacao, data_atualizacao
 */
@Entity
@Table(schema = "operacoes", name = "aventureiro")
public class Aventureiro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aventureiro_seq")
    @SequenceGenerator(name = "aventureiro_seq",
                       sequenceName = "operacoes.aventureiro_id_seq",
                       allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_cadastro_id", nullable = false)
    private Usuario cadastradoPor;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ClasseAventureiro classe;

    @Column(nullable = false)
    private Integer nivel;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "data_criacao", insertable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", insertable = false, updatable = false)
    private LocalDateTime dataAtualizacao;

    @OneToOne(mappedBy = "aventureiro", cascade = CascadeType.ALL,
              orphanRemoval = true, fetch = FetchType.LAZY)
    private Companheiro companheiro;

    @OneToMany(mappedBy = "aventureiro", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ParticipacaoMissao> participacoes = new ArrayList<>();

    protected Aventureiro() {}

    public Aventureiro(Organizacao organizacao, Usuario cadastradoPor,
                       String nome, ClasseAventureiro classe, Integer nivel) {
        this.organizacao = organizacao;
        this.cadastradoPor = cadastradoPor;
        this.nome = nome;
        this.classe = classe;
        this.nivel = nivel;
    }

    public Long getId() { return id; }
    public Organizacao getOrganizacao() { return organizacao; }
    public Usuario getCadastradoPor() { return cadastradoPor; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public ClasseAventureiro getClasse() { return classe; }
    public void setClasse(ClasseAventureiro classe) { this.classe = classe; }
    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public Companheiro getCompanheiro() { return companheiro; }
    public void setCompanheiro(Companheiro companheiro) { this.companheiro = companheiro; }
    public List<ParticipacaoMissao> getParticipacoes() { return participacoes; }
}
