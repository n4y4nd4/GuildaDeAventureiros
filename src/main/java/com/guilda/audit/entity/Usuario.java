package com.guilda.audit.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Parte 1 — Mapeamento do schema audit (legado).
 * Tabela: audit.usuarios
 * Constraint: uq_usuarios_email_por_org (organizacao_id, email)
 * Constraint: ck_usuarios_status (ATIVO | BLOQUEADO | PENDENTE)
 */
@Entity
@Table(schema = "audit", name = "usuarios",
       uniqueConstraints = @UniqueConstraint(
           name = "uq_usuarios_email_por_org",
           columnNames = {"organizacao_id", "email"}))
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "usuarios_seq")
    @SequenceGenerator(name = "usuarios_seq",
                       sequenceName = "audit.usuarios_id_seq",
                       allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_usuarios_org"))
    private Organizacao organizacao;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 180)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UsuarioStatus status;

    @Column(name = "ultimo_login_em")
    private OffsetDateTime ultimoLoginEm;

    @Column(name = "created_at", nullable = false,
            insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false,
            insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    // N:N com roles via tabela user_roles
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        schema = "audit",
        name = "user_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"),
        foreignKey = @ForeignKey(name = "fk_ur_user"),
        inverseForeignKey = @ForeignKey(name = "fk_ur_role")
    )
    private List<Role> roles = new ArrayList<>();

    protected Usuario() {}

    public Usuario(Organizacao organizacao, String nome, String email,
                   String senhaHash, UsuarioStatus status) {
        this.organizacao = organizacao;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.status = status;
    }

    public Long getId() { return id; }
    public Organizacao getOrganizacao() { return organizacao; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public UsuarioStatus getStatus() { return status; }
    public void setStatus(UsuarioStatus status) { this.status = status; }
    public OffsetDateTime getUltimoLoginEm() { return ultimoLoginEm; }
    public void setUltimoLoginEm(OffsetDateTime ultimoLoginEm) { this.ultimoLoginEm = ultimoLoginEm; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public List<Role> getRoles() { return roles; }
}
