package com.guilda.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Questão 2 — Configuração de cache com Caffeine.
 *
 * Estratégia escolhida: expireAfterWrite com TTL de 5 minutos.
 * - A view vw_painel_tatico_missao é atualizada com baixa frequência.
 * - 5 minutos é um equilíbrio entre consistência e redução de carga no banco.
 * - maximumSize=10 porque o endpoint retorna no máximo 10 registros.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("painelTatico");
        manager.setCaffeine(
            Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(10)
        );
        return manager;
    }
}
