package com.guilda.common.exception;

import java.util.List;

public class RequisicaoInvalidaException extends RuntimeException {
    private final List<String> detalhes;

    public RequisicaoInvalidaException(String detalhe) {
        super("Solicitação inválida");
        this.detalhes = List.of(detalhe);
    }

    public RequisicaoInvalidaException(List<String> detalhes) {
        super("Solicitação inválida");
        this.detalhes = detalhes;
    }

    public List<String> getDetalhes() { return detalhes; }
}
