package com.guilda.operacoes.service;

import com.guilda.operacoes.entity.PainelTaticoMissao;
import com.guilda.operacoes.repository.PainelTaticoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Questão 1 — Regra de negócio: últimos 15 dias + top 10.
 * Questão 2 — @Cacheable com Caffeine (TTL 5min, configurado em application.properties).
 *
 * Por que Caffeine?
 * - TTL configurável via application.properties (expireAfterWrite=5m)
 * - Melhor desempenho que ConcurrentMapCacheManager
 * - A view muda pouco entre chamadas; 5min reduz carga no banco sem
 *   comprometer a consistência para uso operacional do painel tático.
 */
@Service
public class PainelTaticoService {

    private final PainelTaticoRepository repository;

    public PainelTaticoService(PainelTaticoRepository repository) {
        this.repository = repository;
    }

    /**
     * Retorna o Top 10 missões dos últimos 15 dias, ordenadas por indice_prontidao DESC.
     * Resultado cacheado por 5 minutos (Questão 2).
     */
    @Cacheable("painelTatico")
    public List<PainelTaticoMissao> buscarTop10UltimosQuinzeDias() {
        // Questão 1 — regra dos 15 dias fica aqui no Service
        LocalDateTime dataCorte = LocalDateTime.now().minusDays(15);
        return repository.buscarPorDataCorte(dataCorte)
                .stream()
                .limit(10)
                .toList();
    }
}
