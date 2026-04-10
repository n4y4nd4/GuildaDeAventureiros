package com.guilda.marketplace.controller;

import com.guilda.marketplace.dto.ProdutoDTO;
import com.guilda.marketplace.service.ElasticsearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Questão 3 — Controller REST do marketplace.
 * Toda lógica de query fica no ElasticsearchService.
 */
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ElasticsearchService service;

    public ProdutoController(ElasticsearchService service) {
        this.service = service;
    }

    // ── Parte A: Buscas Textuais ──────────────────────────────────────────────

    @GetMapping("/busca/nome")
    public ResponseEntity<List<ProdutoDTO>> buscarPorNome(@RequestParam String termo) throws Exception {
        return ResponseEntity.ok(service.buscarPorNome(termo));
    }

    @GetMapping("/busca/descricao")
    public ResponseEntity<List<ProdutoDTO>> buscarPorDescricao(@RequestParam String termo) throws Exception {
        return ResponseEntity.ok(service.buscarPorDescricao(termo));
    }

    @GetMapping("/busca/frase")
    public ResponseEntity<List<ProdutoDTO>> buscarPorFrase(@RequestParam String termo) throws Exception {
        return ResponseEntity.ok(service.buscarPorFrase(termo));
    }

    @GetMapping("/busca/fuzzy")
    public ResponseEntity<List<ProdutoDTO>> buscarFuzzy(@RequestParam String termo) throws Exception {
        return ResponseEntity.ok(service.buscarFuzzy(termo));
    }

    @GetMapping("/busca/multicampos")
    public ResponseEntity<List<ProdutoDTO>> buscarMultiCampos(@RequestParam String termo) throws Exception {
        return ResponseEntity.ok(service.buscarMultiCampos(termo));
    }

    // ── Parte B: Buscas com Filtros ───────────────────────────────────────────

    @GetMapping("/busca/com-filtro")
    public ResponseEntity<List<ProdutoDTO>> buscarComFiltro(
            @RequestParam String termo,
            @RequestParam String categoria) throws Exception {
        return ResponseEntity.ok(service.buscarComFiltroCategoria(termo, categoria));
    }

    @GetMapping("/busca/faixa-preco")
    public ResponseEntity<List<ProdutoDTO>> buscarFaixaPreco(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) throws Exception {
        return ResponseEntity.ok(service.buscarPorFaixaPreco(min, max));
    }

    @GetMapping("/busca/avancada")
    public ResponseEntity<List<ProdutoDTO>> buscarAvancada(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String raridade,
            @RequestParam(required = false) BigDecimal min,
            @RequestParam(required = false) BigDecimal max) throws Exception {
        return ResponseEntity.ok(service.buscarAvancada(categoria, raridade, min, max));
    }

    // ── Parte C: Agregações ───────────────────────────────────────────────────

    @GetMapping("/agregacoes/por-categoria")
    public ResponseEntity<Map<String, Long>> porCategoria() throws Exception {
        return ResponseEntity.ok(service.agregarPorCategoria());
    }

    @GetMapping("/agregacoes/por-raridade")
    public ResponseEntity<Map<String, Long>> porRaridade() throws Exception {
        return ResponseEntity.ok(service.agregarPorRaridade());
    }

    @GetMapping("/agregacoes/preco-medio")
    public ResponseEntity<Double> precoMedio() throws Exception {
        return ResponseEntity.ok(service.calcularPrecoMedio());
    }

    @GetMapping("/agregacoes/faixas-preco")
    public ResponseEntity<Map<String, Long>> faixasPreco() throws Exception {
        return ResponseEntity.ok(service.distribuirPorFaixaPreco());
    }
}
