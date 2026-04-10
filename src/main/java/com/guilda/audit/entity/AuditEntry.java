package com.guilda.audit.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.net.InetAddress;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Parte 1 — Mapeamento do schema audit (legado).
 * Tabela: audit.audit_entries
 */
@Entity
@Table(schema = "audit", name = "audit_entries")
public class AuditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "audit_entries_seq")
    @SequenceGenerator(name = "audit_entries_seq",
                       sequenceName = "audit.audit_entries_id_seq",
                       allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_audit_org"))
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id",
                foreignKey = @ForeignKey(name = "fk_audit_actor_user"))
    private Usuario actorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_api_key_id",
                foreignKey = @ForeignKey(name = "fk_audit_actor_api_key"))
    private ApiKey actorApiKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AuditAction action;

    @Column(name = "entity_schema", nullable = false, length = 60)
    private String entitySchema;

    @Column(name = "entity_name", nullable = false, length = 80)
    private String entityName;

    @Column(name = "entity_id", length = 80)
    private String entityId;

    @Column(name = "occurred_at", nullable = false,
            insertable = false, updatable = false)
    private OffsetDateTime occurredAt;

    @Column(columnDefinition = "inet")
    private String ip;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> diff;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    @Column(nullable = false)
    private boolean success = true;

    protected AuditEntry() {}

    public Long getId() { return id; }
    public Organizacao getOrganizacao() { return organizacao; }
    public Usuario getActorUser() { return actorUser; }
    public ApiKey getActorApiKey() { return actorApiKey; }
    public AuditAction getAction() { return action; }
    public String getEntitySchema() { return entitySchema; }
    public String getEntityName() { return entityName; }
    public String getEntityId() { return entityId; }
    public OffsetDateTime getOccurredAt() { return occurredAt; }
    public String getIp() { return ip; }
    public String getUserAgent() { return userAgent; }
    public Map<String, Object> getDiff() { return diff; }
    public Map<String, Object> getMetadata() { return metadata; }
    public boolean isSuccess() { return success; }
}
