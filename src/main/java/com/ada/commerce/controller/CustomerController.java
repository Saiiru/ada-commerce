package com.ada.commerce.controller;

import com.ada.commerce.service.ports.*;
import com.ada.commerce.cli.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller responsável pelas operações relacionadas a Clientes.
 * <p>
 * Atua como uma camada de aplicação, orquestrando as chamadas aos gateways
 * e encapsulando a lógica de negócio e validações para operações de cliente.
 * Retorna objetos {@link Response} para feedback padronizado.
 */
public class CustomerController {
    private final CustomerGateway customerGateway;

    /**
     * Construtor do CustomerController.
     * @param customerGateway O gateway para operações de persistência e consulta de clientes.
     */
    public CustomerController(CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    /**
     * Executa a criação de um novo cliente.
     * Realiza validações básicas e delega a criação ao gateway.
     * @param name O nome do cliente.
     * @param document O documento (CPF/CNPJ) do cliente.
     * @param email O email do cliente (opcional).
     * @return Um objeto {@link Response} indicando o sucesso ou falha da operação e uma mensagem.
     */
    public Response executeCreateCustomer(String name, String document, String email) {
        if (name == null || name.isBlank()) return new Response(false, "Nome obrigatório.");
        if (document == null || document.isBlank()) return new Response(false, "Documento obrigatório.");
        try {
            UUID id = customerGateway.createCustomer(name, document, email.isBlank() ? null : email);
            return new Response(true, "Cliente criado: " + id);
        } catch (Exception e) {
            return new Response(false, "Erro ao criar cliente: " + e.getMessage());
        }
    }

    /**
     * Lista todos os clientes disponíveis.
     * @return Uma lista de {@link CustomerView} contendo os clientes.
     */
    public List<CustomerView> listCustomers() {
        return customerGateway.listCustomers();
    }

    /**
     * Executa a atualização de um cliente existente.
     * @param id O ID do cliente a ser atualizado.
     * @param name O novo nome do cliente (pode ser null para não alterar).
     * @param email O novo email do cliente (pode ser null para não alterar).
     * @return Um objeto {@link Response} indicando o sucesso ou falha da operação e uma mensagem.
     */
    public Response executeUpdateCustomer(UUID id, String name, String email) {
        try {
            customerGateway.updateCustomer(id, name, email.isBlank() ? null : email);
            return new Response(true, "Cliente atualizado: " + id);
        } catch (Exception e) {
            return new Response(false, "Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    /**
     * Obtém um cliente pelo seu ID.
     * @param id O ID do cliente.
     * @return Um {@link Optional} contendo o {@link CustomerView} se encontrado, ou vazio caso contrário.
     */
    public Optional<CustomerView> getCustomer(UUID id) {
        return customerGateway.getCustomer(id);
    }

    /**
     * Busca um cliente pelo seu documento numérico.
     * @param numericDocument O documento do cliente (apenas dígitos).
     * @return Um {@link Optional} contendo o {@link CustomerView} se encontrado, ou vazio caso contrário.
     */
    public Optional<CustomerView> findByDocument(String numericDocument) {
        return customerGateway.findByDocument(numericDocument);
    }

    /**
     * Busca clientes por parte do nome (case-insensitive).
     * @param name A parte do nome a ser buscada.
     * @return Uma lista de {@link CustomerView} que correspondem à busca.
     */
    public List<CustomerView> findByName(String name) {
        return customerGateway.findByName(name);
    }
}