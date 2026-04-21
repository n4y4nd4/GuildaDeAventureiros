package com.guilda.aventura.controller;

import com.guilda.aventura.dto.AventureiroResponse;
import com.guilda.aventura.enums.ClasseAventureiro;
import com.guilda.aventura.projection.AventureiroDetalheView;
import com.guilda.aventura.projection.RankingAventureiroView;
import com.guilda.aventura.service.AventureiroQueryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/aventureiros")
public class AventureiroQueryController {

    private final AventureiroQueryService service;

    public AventureiroQueryController(AventureiroQueryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam Long orgId,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) ClasseAventureiro classe,
            @RequestParam(required = false) Integer nivelMinimo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort) {

        var pageable = PageRequest.of(page, size, Sort.by(sort));
        var resultado = service.listar(orgId, ativo, classe, nivelMinimo, pageable);
        var dados = resultado.getContent().stream().map(AventureiroResponse::de).toList();
        return ResponseEntity.ok(Map.of(
            "dados", dados,
            "totalElements", resultado.getTotalElements(),
            "totalPages", resultado.getTotalPages(),
            "page", page,
            "size", size
        ));
    }

    @GetMapping("/busca")
    public ResponseEntity<?> buscarPorNome(
            @RequestParam Long orgId,
            @RequestParam String termo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        var pageable = PageRequest.of(page, size);
        var resultado = service.buscarPorNome(orgId, termo, pageable);
        var dados = resultado.getContent().stream().map(AventureiroResponse::de).toList();
        return ResponseEntity.ok(Map.of(
            "dados", dados,
            "totalElements", resultado.getTotalElements()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AventureiroDetalheView> detalhe(@PathVariable Long id) {
        return service.detalhe(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingAventureiroView>> ranking(
            @RequestParam Long orgId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam(required = false) String statusMissao) {

        return ResponseEntity.ok(service.ranking(orgId, inicio, fim, statusMissao));
    }
}
