package com.guilda.aventura.entity;

import com.guilda.aventura.enums.EspecieCompanheiro;
import jakarta.persistence.*;

@Entity
@Table(schema = "operacoes", name = "companheiro")
public class Companheiro {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "aventureiro_id")
    private Aventureiro aventureiro;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EspecieCompanheiro especie;

    @Column(name = "indice_lealdade", nullable = false)
    private Integer lealdade;

    protected Companheiro() {}

    public Companheiro(Aventureiro aventureiro, String nome, EspecieCompanheiro especie, Integer lealdade) {
        this.aventureiro = aventureiro;
        this.nome = nome;
        this.especie = especie;
        this.lealdade = lealdade;
    }

    public Long getId() { return id; }
    public Aventureiro getAventureiro() { return aventureiro; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public EspecieCompanheiro getEspecie() { return especie; }
    public void setEspecie(EspecieCompanheiro especie) { this.especie = especie; }
    public Integer getLealdade() { return lealdade; }
    public void setLealdade(Integer lealdade) { this.lealdade = lealdade; }
}
