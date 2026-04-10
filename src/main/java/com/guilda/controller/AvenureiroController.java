package com.guilda.controller;

import com.guilda.dto.request.AtualizarAvenureiroRequest;
import com.guilda.dto.request.CompanheiroRequest;
import com.guilda.dto.request.CriarAvenureiroRequest;
import com.guilda.dto.response.AvenureiroDetalheResponse;
import com.guilda.dto.response.AvenureiroResumoResponse;
import com.guilda.enums.ClasseAventureiro;
import com.guilda.exception.RequisicaoInvalidaException;
import com.guilda.service.AvenureiroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/aventureiros")
public class AvenureiroController {

    private final AvenureiroService service;

    public AvenureiroController(AvenureiroService service) {
        this.service = service;
    }

    // 1) Registrar aventureiro — POST /aventureiros → 201
    @PostMapping
    public ResponseEntity<AvenureiroDetalheResponse> registrar(@Valid @RequestBody CriarAvenureiroRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(req));
    }

    // 2) Listar com filtros e paginação — GET /aventureiros → 200
    @GetMapping
    public ResponseEntity<List<AvenureiroResumoResponse>> listar(
            @RequestParam(required = false) ClasseAventureiro classe,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Integer nivelMinimo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0) throw new RequisicaoInvalidaException("page não pode ser negativo");
        if (size < 1 || size > 50) throw new RequisicaoInvalidaException("size deve estar entre 1 e 50");

        Map<String, Object> resultado = service.listar(classe, ativo, nivelMinimo, page, size);

        @SuppressWarnings("unchecked")
        List<AvenureiroResumoResponse> dados = (List<AvenureiroResumoResponse>) resultado.get("dados");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", String.valueOf(resultado.get("totalCount")));
        headers.set("X-Page", String.valueOf(resultado.get("page")));
        headers.set("X-Size", String.valueOf(resultado.get("size")));
        headers.set("X-Total-Pages", String.valueOf(resultado.get("totalPages")));

        return ResponseEntity.ok().headers(headers).body(dados);
    }

    // 3) Consultar por id — GET /aventureiros/{id} → 200 | 404
    @GetMapping("/{id}")
    public ResponseEntity<AvenureiroDetalheResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // 4) Atualizar — PUT /aventureiros/{id} → 200 | 400 | 404
    @PutMapping("/{id}")
    public ResponseEntity<AvenureiroDetalheResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarAvenureiroRequest req) {
        return ResponseEntity.ok(service.atualizar(id, req));
    }

    // 5) Encerrar vínculo — PATCH /aventureiros/{id}/desativar → 204
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    // 6) Recrutar novamente — PATCH /aventureiros/{id}/reativar → 204
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        service.reativar(id);
        return ResponseEntity.noContent().build();
    }

    // 7) Definir/substituir companheiro — PUT /aventureiros/{id}/companheiro → 200
    @PutMapping("/{id}/companheiro")
    public ResponseEntity<AvenureiroDetalheResponse> definirCompanheiro(
            @PathVariable Long id,
            @Valid @RequestBody CompanheiroRequest req) {
        return ResponseEntity.ok(service.definirCompanheiro(id, req));
    }

    // 8) Remover companheiro — DELETE /aventureiros/{id}/companheiro → 204
    @DeleteMapping("/{id}/companheiro")
    public ResponseEntity<Void> removerCompanheiro(@PathVariable Long id) {
        service.removerCompanheiro(id);
        return ResponseEntity.noContent().build();
    }
}
