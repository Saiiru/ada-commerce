package com.ada.commerce.cli;

/**
 * Classe de resposta padronizada para operações da CLI.
 * <p>
 * Encapsula o resultado de uma operação (sucesso/falha) e uma mensagem descritiva.
 * Isso permite que os controllers retornem um feedback consistente, separando a lógica
 * de negócio da apresentação na interface do usuário.
 */
public class Response {
    private final boolean success;
    private final String message;

    /**
     * Construtor para criar uma nova instância de Response.
     * @param success Indica se a operação foi bem-sucedida.
     * @param message Uma mensagem descritiva sobre o resultado da operação.
     */
    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Verifica se a operação foi bem-sucedida.
     * @return true se a operação foi bem-sucedida, false caso contrário.
     */
    public boolean isSuccess() { return success; }

    /**
     * Retorna a mensagem descritiva do resultado da operação.
     * @return A mensagem de feedback.
     */
    public String getMessage() { return message; }
}