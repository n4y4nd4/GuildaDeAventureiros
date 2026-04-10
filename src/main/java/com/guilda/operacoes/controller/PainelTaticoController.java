package com.guilda.operacoes.controller;

import com.guilda.operacoes.entity.PainelTaticoMissao;
import com.guilda.operacoes.service.PainelTaticoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Questão 1 — Endpoint GET /missoes/top15dias
 */
@RestController
@RequestMapping("/missoes")
public class PainelTaticoController {

    private final PainelTaticoService service;

    public PainelTaticoController(PainelTaticoService service) {
        this.service = service;
    }

    /**
     * GET /missoes/top15dias
     * Retorna Top 10 missões dos últimos 15 dias ordenadas por indice_prontidao DESC.
     * Resultado servido do cache Caffeine após primeira chamada (Questão 2).
     */
    @GetMapping("/top15dias")
    public ResponseEntity<List<PainelTaticoMissao>> top15Dias() {
        return ResponseEntity.ok(service.buscarTop10UltimosQuinzeDias());
    }
}
