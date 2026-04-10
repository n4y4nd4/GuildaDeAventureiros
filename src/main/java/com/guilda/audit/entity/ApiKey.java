package com.guilda.audit.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

/**
 * Parte 1 — Mapeamento do schema audit (legado).
 * Tabela: audit.api_keys
 * Constraint: uq_api_keys_nome_por_org (organizacao_id, nome)
 */
@Entity
@Table(schema = "audit", name = "api_keys",
       uniqueConstraints = @UniqueConstraint(
           name = "uq_api_keys_nome_por_org",
           columnNames = {"organizacao_id", "nome"}))
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "api_keys_seq")
    @SequenceGenerator(name = "api_keys_seq",
                       sequenceName = "audit.api_keys_id_seq",
                       allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_api_keys_org"))
    private Organizacao organizacao;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(name = "key_hash", nullable = false, length = 255)
    private String keyHash;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "created_at", nullable = false,
            insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "last_used_at")
    private OffsetDateTime lastUsedAt;

    protected ApiKey() {}

    public Long getId() { return id; }
    public Organizacao getOrganizacao() { return organizacao; }
    public String getNome() { return nome; }
    public String getKeyHash() { return keyHash; }
    public boolean isAtivo() { return ativo; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(OffsetDateTime lastUsedAt) { this.lastUsedAt = lastUsedAt; }
}
