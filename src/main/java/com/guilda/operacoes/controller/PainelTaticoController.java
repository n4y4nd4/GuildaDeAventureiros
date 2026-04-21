package com.guilda.operacoes.controller;

import com.guilda.operacoes.dto.PainelTaticoResponse;
import com.guilda.operacoes.service.PainelTaticoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/missoes")
public class PainelTaticoController {

    private final PainelTaticoService service;

    public PainelTaticoController(PainelTaticoService service) {
        this.service = service;
    }

    /** GET /missoes/top15dias — Top 10 dos últimos 15 dias, cacheado por 5min */
    @GetMapping("/top15dias")
    public ResponseEntity<List<PainelTaticoResponse>> top15Dias() {
        var resultado = service.buscarTop10UltimosQuinzeDias().stream()
            .map(PainelTaticoResponse::de)
            .toList();
        return ResponseEntity.ok(resultado);
    }
}
