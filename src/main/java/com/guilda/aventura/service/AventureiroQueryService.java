package com.guilda.aventura.service;

import com.guilda.aventura.entity.Aventureiro;
import com.guilda.aventura.enums.ClasseAventureiro;
import com.guilda.aventura.projection.AventureiroDetalheView;
import com.guilda.aventura.projection.RankingAventureiroView;
import com.guilda.aventura.repository.AventureiroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AventureiroQueryService {

    private final AventureiroRepository repository;

    public AventureiroQueryService(AventureiroRepository repository) {
        this.repository = repository;
    }

    public Page<Aventureiro> listar(Long orgId, Boolean ativo,
                                     ClasseAventureiro classe,
                                     Integer nivelMinimo, Pageable pageable) {
        String classeStr = classe != null ? classe.name() : null;
        return repository.listarComFiltros(orgId, ativo, classeStr, nivelMinimo, pageable);
    }

    public Page<Aventureiro> buscarPorNome(Long orgId, String termo, Pageable pageable) {
        return repository.buscarPorNome(orgId, termo, pageable);
    }

    public Optional<AventureiroDetalheView> detalhe(Long id) {
        return repository.buscarDetalhe(id);
    }

    public List<RankingAventureiroView> ranking(Long orgId,
                                                 LocalDateTime inicio,
                                                 LocalDateTime fim,
                                                 String statusMissao) {
        return repository.ranking(orgId, inicio, fim, statusMissao);
    }
}
