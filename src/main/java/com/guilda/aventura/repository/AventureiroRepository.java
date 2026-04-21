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

    @Query(value = """
        SELECT * FROM operacoes.aventureiro a
        WHERE a.organizacao_id = :orgId
          AND (CAST(:ativo AS boolean) IS NULL OR a.ativo = CAST(:ativo AS boolean))
          AND (CAST(:classe AS text) IS NULL OR a.classe = CAST(:classe AS text))
          AND (CAST(:nivelMinimo AS integer) IS NULL OR a.nivel >= CAST(:nivelMinimo AS integer))
        """,
        countQuery = """
        SELECT COUNT(*) FROM operacoes.aventureiro a
        WHERE a.organizacao_id = :orgId
          AND (CAST(:ativo AS boolean) IS NULL OR a.ativo = CAST(:ativo AS boolean))
          AND (CAST(:classe AS text) IS NULL OR a.classe = CAST(:classe AS text))
          AND (CAST(:nivelMinimo AS integer) IS NULL OR a.nivel >= CAST(:nivelMinimo AS integer))
        """,
        nativeQuery = true)
    Page<Aventureiro> listarComFiltros(
            @Param("orgId") Long orgId,
            @Param("ativo") Boolean ativo,
            @Param("classe") String classe,
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

    @Query(value = """
        SELECT a.id, a.nome,
               COUNT(p.missao_id) AS totalParticipacoes,
               COALESCE(SUM(p.recompensa_ouro), 0) AS totalRecompensas,
               SUM(CASE WHEN p.destaque = true THEN 1 ELSE 0 END) AS totalMvps
        FROM operacoes.aventureiro a
        JOIN operacoes.participacao_missao p ON a.id = p.aventureiro_id
        JOIN operacoes.missao m ON m.id = p.missao_id
        WHERE a.organizacao_id = :orgId
          AND (CAST(:inicio AS timestamp) IS NULL OR p.data_registro >= CAST(:inicio AS timestamp))
          AND (CAST(:fim AS timestamp) IS NULL OR p.data_registro <= CAST(:fim AS timestamp))
          AND (CAST(:statusMissao AS text) IS NULL OR m.status = CAST(:statusMissao AS text))
        GROUP BY a.id, a.nome
        ORDER BY totalParticipacoes DESC, totalRecompensas DESC
        """, nativeQuery = true)
    List<RankingAventureiroView> ranking(
            @Param("orgId") Long orgId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusMissao") String statusMissao);
}
