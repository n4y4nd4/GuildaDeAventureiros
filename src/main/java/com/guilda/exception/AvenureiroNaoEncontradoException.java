package com.guilda.exception;

public class AvenureiroNaoEncontradoException extends RuntimeException {
    public AvenureiroNaoEncontradoException(Long id) {
        super("Aventureiro com id " + id + " não encontrado.");
    }
}
