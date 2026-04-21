package com.guilda.operacoes.service;

import com.guilda.operacoes.entity.PainelTaticoMissao;
import com.guilda.operacoes.repository.PainelTaticoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PainelTaticoService {

    private final PainelTaticoRepository repository;

    public PainelTaticoService(PainelTaticoRepository repository) {
        this.repository = repository;
    }

    @Cacheable("painelTatico")
    public List<PainelTaticoMissao> buscarTop10UltimosQuinzeDias() {
        LocalDateTime dataCorte = LocalDateTime.now().minusDays(15);
        return repository.buscarPorDataCorte(dataCorte)
                .stream()
                .limit(10)
                .toList();
    }
}
