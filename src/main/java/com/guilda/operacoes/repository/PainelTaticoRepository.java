package com.guilda.operacoes.repository;

import com.guilda.operacoes.entity.PainelTaticoMissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PainelTaticoRepository extends JpaRepository<PainelTaticoMissao, Long> {

    @Query("""
        SELECT p FROM PainelTaticoMissao p
        WHERE p.ultimaAtualizacao >= :limite
        ORDER BY p.indiceProntidao DESC
        """)
    List<PainelTaticoMissao> buscarPorDataCorte(@Param("limite") LocalDateTime limite);
}
