package com.guilda.aventura.repository;

import com.guilda.aventura.entity.Missao;
import com.guilda.aventura.enums.NivelPerigo;
import com.guilda.aventura.enums.StatusMissao;
import com.guilda.aventura.projection.RelatorioMissaoView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MissaoRepository extends JpaRepository<Missao, Long> {

    @Query(value = """
        SELECT * FROM operacoes.missao m
        WHERE m.organizacao_id = :orgId
          AND (CAST(:status AS text) IS NULL OR m.status = CAST(:status AS text))
          AND (CAST(:nivelPerigo AS text) IS NULL OR m.nivel_perigo = CAST(:nivelPerigo AS text))
          AND (CAST(:dataInicio AS timestamp) IS NULL OR m.data_inicio >= CAST(:dataInicio AS timestamp))
          AND (CAST(:dataFim AS timestamp) IS NULL OR m.data_fim <= CAST(:dataFim AS timestamp))
        """,
        countQuery = """
        SELECT COUNT(*) FROM operacoes.missao m
        WHERE m.organizacao_id = :orgId
          AND (CAST(:status AS text) IS NULL OR m.status = CAST(:status AS text))
          AND (CAST(:nivelPerigo AS text) IS NULL OR m.nivel_perigo = CAST(:nivelPerigo AS text))
          AND (CAST(:dataInicio AS timestamp) IS NULL OR m.data_inicio >= CAST(:dataInicio AS timestamp))
          AND (CAST(:dataFim AS timestamp) IS NULL OR m.data_fim <= CAST(:dataFim AS timestamp))
        """,
        nativeQuery = true)
    Page<Missao> listarComFiltros(
            @Param("orgId") Long orgId,
            @Param("status") String status,
            @Param("nivelPerigo") String nivelPerigo,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            Pageable pageable);

    @Query("""
        SELECT DISTINCT m FROM Missao m
        LEFT JOIN FETCH m.participacoes p
        LEFT JOIN FETCH p.aventureiro
        WHERE m.id = :id
        """)
    Optional<Missao> buscarComParticipantes(@Param("id") Long id);

    @Query(value = """
        SELECT m.id, m.titulo, m.status, m.nivel_perigo AS nivelPerigo,
               COUNT(p.aventureiro_id) AS totalParticipantes,
               COALESCE(SUM(p.recompensa_ouro), 0) AS totalRecompensas
        FROM operacoes.missao m
        LEFT JOIN operacoes.participacao_missao p ON m.id = p.missao_id
        WHERE m.organizacao_id = :orgId
          AND (CAST(:dataInicio AS timestamp) IS NULL OR m.data_criacao >= CAST(:dataInicio AS timestamp))
          AND (CAST(:dataFim AS timestamp) IS NULL OR m.data_criacao <= CAST(:dataFim AS timestamp))
        GROUP BY m.id, m.titulo, m.status, m.nivel_perigo
        ORDER BY m.data_criacao DESC
        """, nativeQuery = true)
    List<RelatorioMissaoView> relatorio(
            @Param("orgId") Long orgId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
}
