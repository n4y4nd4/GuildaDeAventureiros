package com.guilda.audit.entity;

import jakarta.persistence.*;

/**
 * Parte 1 — Mapeamento do schema audit (legado).
 * Tabela: audit.permissions
 * Constraint: permissions_code_key (code único)
 */
@Entity
@Table(schema = "audit", name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "permissions_seq")
    @SequenceGenerator(name = "permissions_seq",
                       sequenceName = "audit.permissions_id_seq",
                       allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 80, unique = true)
    private String code;

    @Column(nullable = false, length = 255)
    private String descricao;

    protected Permission() {}

    public Permission(String code, String descricao) {
        this.code = code;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getDescricao() { return descricao; }
}
