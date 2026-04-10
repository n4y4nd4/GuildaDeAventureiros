package com.guilda.operacoes.repository;

import com.guilda.operacoes.entity.PainelTaticoMissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Questão 1 — Repository da view vw_painel_tatico_missao.
 *
 * A query filtra ultima_atualizacao >= :limite e ordena por indice_prontidao DESC.
 * O limite de 10 registros é aplicado no Service (regra de negócio).
 */
public interface PainelTaticoRepository extends JpaRepository<PainelTaticoMissao, Long> {

    @Query("""
        SELECT p FROM PainelTaticoMissao p
        WHERE p.ultimaAtualizacao >= :limite
        ORDER BY p.indiceProntidao DESC
        """)
    List<PainelTaticoMissao> buscarPorDataCorte(@Param("limite") LocalDateTime limite);
}
