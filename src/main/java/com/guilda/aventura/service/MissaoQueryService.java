package com.guilda.aventura.service;

import com.guilda.aventura.entity.Missao;
import com.guilda.aventura.enums.NivelPerigo;
import com.guilda.aventura.enums.StatusMissao;
import com.guilda.aventura.projection.RelatorioMissaoView;
import com.guilda.aventura.repository.MissaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MissaoQueryService {

    private final MissaoRepository repository;

    public MissaoQueryService(MissaoRepository repository) {
        this.repository = repository;
    }

    public Page<Missao> listar(Long orgId, StatusMissao status,
                                NivelPerigo nivelPerigo,
                                LocalDateTime dataInicio,
                                LocalDateTime dataFim,
                                Pageable pageable) {
        return repository.listarComFiltros(orgId, status, nivelPerigo, dataInicio, dataFim, pageable);
    }

    public Optional<Missao> detalhe(Long id) {
        return repository.buscarComParticipantes(id);
    }

    public List<RelatorioMissaoView> relatorio(Long orgId,
                                                LocalDateTime dataInicio,
                                                LocalDateTime dataFim) {
        return repository.relatorio(orgId, dataInicio, dataFim);
    }
}
