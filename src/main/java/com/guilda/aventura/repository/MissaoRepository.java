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

    @Query("""
        SELECT m FROM Missao m
        WHERE m.organizacao.id = :orgId
          AND (:status IS NULL OR m.status = :status)
          AND (:nivelPerigo IS NULL OR m.nivelPerigo = :nivelPerigo)
          AND (:dataInicio IS NULL OR m.dataInicio >= :dataInicio)
          AND (:dataFim IS NULL OR m.dataFim <= :dataFim)
        """)
    Page<Missao> listarComFiltros(
            @Param("orgId") Long orgId,
            @Param("status") StatusMissao status,
            @Param("nivelPerigo") NivelPerigo nivelPerigo,
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

    @Query("""
        SELECT m.id AS id,
               m.titulo AS titulo,
               m.status AS status,
               m.nivelPerigo AS nivelPerigo,
               COUNT(p.aventureiro) AS totalParticipantes,
               COALESCE(SUM(p.recompensaOuro), 0) AS totalRecompensas
        FROM Missao m
        LEFT JOIN m.participacoes p
        WHERE m.organizacao.id = :orgId
          AND (:dataInicio IS NULL OR m.dataCriacao >= :dataInicio)
          AND (:dataFim IS NULL OR m.dataCriacao <= :dataFim)
        GROUP BY m.id, m.titulo, m.status, m.nivelPerigo
        ORDER BY m.dataCriacao DESC
        """)
    List<RelatorioMissaoView> relatorio(
            @Param("orgId") Long orgId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
}
