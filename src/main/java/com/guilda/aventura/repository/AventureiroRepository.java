package com.guilda.aventura.repository;

import com.guilda.aventura.entity.Aventureiro;
import com.guilda.aventura.enums.ClasseAventureiro;
import com.guilda.aventura.projection.AventureiroDetalheView;
import com.guilda.aventura.projection.RankingAventureiroView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AventureiroRepository
        extends JpaRepository<Aventureiro, Long>, JpaSpecificationExecutor<Aventureiro> {

    @Query("""
        SELECT a FROM Aventureiro a
        WHERE (:ativo IS NULL OR a.ativo = :ativo)
          AND (:classe IS NULL OR a.classe = :classe)
          AND (:nivelMinimo IS NULL OR a.nivel >= :nivelMinimo)
          AND a.organizacao.id = :orgId
        """)
    Page<Aventureiro> listarComFiltros(
            @Param("orgId") Long orgId,
            @Param("ativo") Boolean ativo,
            @Param("classe") ClasseAventureiro classe,
            @Param("nivelMinimo") Integer nivelMinimo,
            Pageable pageable);

    @Query("""
        SELECT a FROM Aventureiro a
        WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
          AND a.organizacao.id = :orgId
        """)
    Page<Aventureiro> buscarPorNome(
            @Param("orgId") Long orgId,
            @Param("termo") String termo,
            Pageable pageable);

    @Query("""
        SELECT a.id AS id,
               a.nome AS nome,
               a.classe AS classe,
               a.nivel AS nivel,
               a.ativo AS ativo,
               c.nome AS companheiroNome,
               c.especie AS companheiroEspecie,
               c.lealdade AS companheiroLealdade,
               COUNT(p.missao) AS totalParticipacoes,
               MAX(p.dataRegistro) AS ultimaParticipacaoEm
        FROM Aventureiro a
        LEFT JOIN a.companheiro c
        LEFT JOIN a.participacoes p
        WHERE a.id = :id
        GROUP BY a.id, a.nome, a.classe, a.nivel, a.ativo,
                 c.nome, c.especie, c.lealdade
        """)
    Optional<AventureiroDetalheView> buscarDetalhe(@Param("id") Long id);

    @Query("""
        SELECT a.id AS id,
               a.nome AS nome,
               COUNT(p.missao) AS totalParticipacoes,
               COALESCE(SUM(p.recompensaOuro), 0) AS totalRecompensas,
               SUM(CASE WHEN p.destaque = true THEN 1 ELSE 0 END) AS totalMvps
        FROM Aventureiro a
        JOIN a.participacoes p
        JOIN p.missao m
        WHERE a.organizacao.id = :orgId
          AND (:inicio IS NULL OR p.dataRegistro >= :inicio)
          AND (:fim IS NULL OR p.dataRegistro <= :fim)
          AND (:statusMissao IS NULL OR CAST(m.status AS string) = :statusMissao)
        GROUP BY a.id, a.nome
        ORDER BY totalParticipacoes DESC, totalRecompensas DESC
        """)
    List<RankingAventureiroView> ranking(
            @Param("orgId") Long orgId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusMissao") String statusMissao);
}
