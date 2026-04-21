package com.guilda.audit.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "audit", name = "roles",
       uniqueConstraints = @UniqueConstraint(name = "uq_roles_nome_por_org",
           columnNames = {"organizacao_id", "nome"}))
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_seq")
    @SequenceGenerator(name = "roles_seq", sequenceName = "audit.roles_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false, foreignKey = @ForeignKey(name = "fk_roles_org"))
    private Organizacao organizacao;

    @Column(nullable = false, length = 60)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = "audit", name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id"),
        foreignKey = @ForeignKey(name = "fk_rp_role"),
        inverseForeignKey = @ForeignKey(name = "fk_rp_perm"))
    private List<Permission> permissions = new ArrayList<>();

    protected Role() {}

    public Role(Organizacao organizacao, String nome, String descricao) {
        this.organizacao = organizacao;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public Organizacao getOrganizacao() { return organizacao; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public List<Permission> getPermissions() { return permissions; }
}
