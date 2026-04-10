package com.guilda.service;

import com.guilda.dto.request.AtualizarAvenureiroRequest;
import com.guilda.dto.request.CompanheiroRequest;
import com.guilda.dto.request.CriarAvenureiroRequest;
import com.guilda.dto.response.AvenureiroDetalheResponse;
import com.guilda.dto.response.AvenureiroResumoResponse;
import com.guilda.enums.ClasseAventureiro;
import com.guilda.exception.AvenureiroNaoEncontradoException;
import com.guilda.model.Aventureiro;
import com.guilda.model.Companheiro;
import com.guilda.repository.AvenureiroRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class AvenureiroService {

    private final AvenureiroRepository repository;

    public AvenureiroService(AvenureiroRepository repository) {
        this.repository = repository;
    }

    public AvenureiroDetalheResponse registrar(CriarAvenureiroRequest req) {
        Aventureiro a = new Aventureiro(null, req.getNome(), req.getClasse(), req.getNivel());
        repository.salvar(a);
        return AvenureiroDetalheResponse.de(a);
    }

    public Map<String, Object> listar(ClasseAventureiro classe, Boolean ativo, Integer nivelMinimo, int page, int size) {
        List<Aventureiro> filtrados = repository.buscarTodos().stream()
            .filter(a -> classe == null || a.getClasse() == classe)
            .filter(a -> ativo == null || a.isAtivo() == ativo)
            .filter(a -> nivelMinimo == null || a.getNivel() >= nivelMinimo)
            .sorted(Comparator.comparingLong(Aventureiro::getId))
            .toList();

        int total = filtrados.size();
        int totalPages = (int) Math.ceil((double) total / size);

        List<AvenureiroResumoResponse> pagina = filtrados.stream()
            .skip((long) page * size)
            .limit(size)
            .map(AvenureiroResumoResponse::de)
            .toList();

        return Map.of(
            "dados", pagina,
            "totalCount", total,
            "page", page,
            "size", size,
            "totalPages", totalPages
        );
    }

    public AvenureiroDetalheResponse buscarPorId(Long id) {
        return AvenureiroDetalheResponse.de(encontrar(id));
    }

    public AvenureiroDetalheResponse atualizar(Long id, AtualizarAvenureiroRequest req) {
        Aventureiro a = encontrar(id);
        a.setNome(req.getNome());
        a.setClasse(req.getClasse());
        a.setNivel(req.getNivel());
        repository.salvar(a);
        return AvenureiroDetalheResponse.de(a);
    }

    public void desativar(Long id) {
        Aventureiro a = encontrar(id);
        a.setAtivo(false);
        repository.salvar(a);
    }

    public void reativar(Long id) {
        Aventureiro a = encontrar(id);
        a.setAtivo(true);
        repository.salvar(a);
    }

    public AvenureiroDetalheResponse definirCompanheiro(Long id, CompanheiroRequest req) {
        Aventureiro a = encontrar(id);
        a.setCompanheiro(new Companheiro(req.getNome(), req.getEspecie(), req.getLealdade()));
        repository.salvar(a);
        return AvenureiroDetalheResponse.de(a);
    }

    public void removerCompanheiro(Long id) {
        Aventureiro a = encontrar(id);
        a.setCompanheiro(null);
        repository.salvar(a);
    }

    private Aventureiro encontrar(Long id) {
        return repository.buscarPorId(id)
            .orElseThrow(() -> new AvenureiroNaoEncontradoException(id));
    }
}
