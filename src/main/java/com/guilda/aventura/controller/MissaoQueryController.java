package com.guilda.aventura.controller;

import com.guilda.aventura.dto.MissaoResponse;
import com.guilda.aventura.enums.NivelPerigo;
import com.guilda.aventura.enums.StatusMissao;
import com.guilda.aventura.projection.RelatorioMissaoView;
import com.guilda.aventura.service.MissaoQueryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/missoes")
public class MissaoQueryController {

    private final MissaoQueryService service;

    public MissaoQueryController(MissaoQueryService service) {
        this.service = service;
    }

    /** GET /missoes?orgId=1&status=CONCLUIDA&nivelPerigo=ALTO */
    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam Long orgId,
            @RequestParam(required = false) StatusMissao status,
            @RequestParam(required = false) NivelPerigo nivelPerigo,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        var pageable = PageRequest.of(page, size, Sort.by("dataCriacao").descending());
        var resultado = service.listar(orgId, status, nivelPerigo, dataInicio, dataFim, pageable);
        var dados = resultado.getContent().stream()
            .map(MissaoResponse::de)
            .toList();
        return ResponseEntity.ok(Map.of(
            "dados", dados,
            "totalElements", resultado.getTotalElements(),
            "totalPages", resultado.getTotalPages()
        ));
    }

    /** GET /missoes/{id} — detalhe com participantes */
    @GetMapping("/{id}")
    public ResponseEntity<MissaoResponse> detalhe(@PathVariable Long id) {
        return service.detalhe(id)
            .map(MissaoResponse::deComParticipantes)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /** GET /missoes/relatorio?orgId=1 */
    @GetMapping("/relatorio")
    public ResponseEntity<List<RelatorioMissaoView>> relatorio(
            @RequestParam Long orgId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {

        return ResponseEntity.ok(service.relatorio(orgId, dataInicio, dataFim));
    }
}
