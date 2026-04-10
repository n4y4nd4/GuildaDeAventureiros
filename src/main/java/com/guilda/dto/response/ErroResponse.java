package com.guilda.dto.response;

import java.util.List;

public class ErroResponse {
    private String mensagem;
    private List<String> detalhes;

    public ErroResponse(String mensagem, List<String> detalhes) {
        this.mensagem = mensagem;
        this.detalhes = detalhes;
    }

    public String getMensagem() { return mensagem; }
    public List<String> getDetalhes() { return detalhes; }
}
