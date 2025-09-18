package com.ada.commerce.controller;

import com.ada.commerce.service.ports.*;
import com.ada.commerce.cli.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller responsável pelas operações relacionadas a Produtos.
 * <p>
 * Atua como uma camada de aplicação, orquestrando as chamadas aos gateways
 * e encapsulando a lógica de negócio e validações para operações de produto.
 * Retorna objetos {@link Response} para feedback padronizado.
 */
public class ProductController {
    private final ProductGateway productGateway;

    /**
     * Construtor do ProductController.
     * @param productGateway O gateway para operações de persistência e consulta de produtos.
     */
    public ProductController(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    /**
     * Executa a criação de um novo produto.
     * Realiza validações básicas e delega a criação ao gateway.
     * @param name O nome do produto.
     * @param price O preço base do produto.
     * @return Um objeto {@link Response} indicando o sucesso ou falha da operação e uma mensagem.
     */
    public Response executeCreateProduct(String name, BigDecimal price) {
        if (name == null || name.isBlank()) return new Response(false, "Nome obrigatório.");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) return new Response(false, "Preço inválido.");
        try {
            UUID id = productGateway.createProduct(name, price);
            return new Response(true, "Produto criado: " + id);
        } catch (Exception e) {
            return new Response(false, "Erro: " + e.getMessage());
        }
    }

    /**
     * Lista todos os produtos disponíveis.
     * @return Uma lista de {@link ProductView} contendo os produtos.
     */
    public List<ProductView> listProducts() {
        return productGateway.listProducts();
    }

    /**
     * Executa a atualização de um produto existente.
     * @param id O ID do produto a ser atualizado.
     * @param name O novo nome do produto (pode ser null para não alterar).
     * @param price O novo preço base do produto (pode ser null para não alterar).
     * @return Um objeto {@link Response} indicando o sucesso ou falha da operação e uma mensagem.
     */
    public Response executeUpdateProduct(UUID id, String name, BigDecimal price) {
        try {
            productGateway.updateProduct(id, name, price);
            return new Response(true, "Produto atualizado: " + id);
        } catch (Exception e) {
            return new Response(false, "Erro ao atualizar produto: " + e.getMessage());
        }
    }

    /**
     * Obtém um produto pelo seu ID.
     * @param id O ID do produto.
     * @return Um {@link Optional} contendo o {@link ProductView} se encontrado, ou vazio caso contrário.
     */
    public Optional<ProductView> getProduct(UUID id) {
        return productGateway.getProduct(id);
    }
}