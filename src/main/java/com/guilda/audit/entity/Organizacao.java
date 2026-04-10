package com.guilda.audit.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Parte 1 — Mapeamento do schema audit (legado).
 * Tabela: audit.organizacoes
 */
@Entity
@Table(schema = "audit", name = "organizacoes")
public class Organizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "organizacoes_seq")
    @SequenceGenerator(name = "organizacoes_seq",
                       sequenceName = "audit.organizacoes_id_seq",
                       allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String nome;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "created_at", nullable = false,
            insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    // Relacionamentos
    @OneToMany(mappedBy = "organizacao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> usuarios = new ArrayList<>();

    @OneToMany(mappedBy = "organizacao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    protected Organizacao() {}

    public Organizacao(String nome) { this.nome = nome; }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public List<Usuario> getUsuarios() { return usuarios; }
    public List<Role> getRoles() { return roles; }
}
