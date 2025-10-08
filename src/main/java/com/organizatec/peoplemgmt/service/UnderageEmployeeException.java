package com.organizatec.peoplemgmt.service;


/**
 * Exceção customizada para representar a tentativa de cadastrar um funcionário
 * menor de idade.
 * <p>
 * É uma {@link RuntimeException} para evitar a obrigatoriedade de cláusulas `throws`
 * em assinaturas de métodos.
 */
public class UnderageEmployeeException extends RuntimeException {

    /**
     * Construtor que aceita uma mensagem de erro.
     * @param message A mensagem detalhando o erro.
     */
    public UnderageEmployeeException(String message) { super(message); }
}
