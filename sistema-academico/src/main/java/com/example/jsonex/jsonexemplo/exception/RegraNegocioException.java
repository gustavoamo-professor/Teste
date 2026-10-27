package com.example.jsonex.jsonexemplo.exception;

/**
 * Lançada quando uma regra de negócio é violada
 * (ex.: matrícula duplicada, disciplina de outro curso).
 * Os controllers capturam e mostram a mensagem na tela.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
