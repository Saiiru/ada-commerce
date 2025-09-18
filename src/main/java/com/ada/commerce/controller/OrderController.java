package com.ada.commerce.controller;

import com.ada.commerce.service.ports.*;
import com.ada.commerce.cli.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller responsável pelas operações relacionadas a Pedidos.
 * <p>
 * Orquestra as chamadas aos gateways de pedido, produto e cliente,
 * encapsulando a lógica de negócio e validações para o fluxo de pedidos.
 * Retorna mensagens ou objetos de visualização para a camada de apresentação.
 */
public class OrderController {
    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;
    private final CustomerGateway customerGateway;

    /**
     * Construtor do OrderController.
     * @param orderGateway O gateway para operações de persistência e consulta de pedidos.
     * @param productGateway O gateway para operações de persistência e consulta de produtos.
     * @param customerGateway O gateway para operações de persistência e consulta de clientes.
     */
    public OrderController(OrderGateway orderGateway, ProductGateway productGateway, CustomerGateway customerGateway) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
        this.customerGateway = customerGateway;
    }

    /**
     * Lista todos os clientes disponíveis para seleção de pedido.
     * @return Uma lista de {@link CustomerView} contendo os clientes.
     */
    public List<CustomerView> listCustomers() {
        return customerGateway.listCustomers();
    }

    /**
     * Lista todos os produtos disponíveis para seleção de itens de pedido.
     * @return Uma lista de {@link ProductView} contendo os produtos.
     */
    public List<ProductView> listProducts() {
        return productGateway.listProducts();
    }

    /**
     * Executa a criação de um novo pedido para um cliente específico.
     * @param customerId O ID do cliente para o qual o pedido será criado.
     * @return Um {@link Optional} contendo o ID do novo pedido se o cliente for encontrado, ou vazio caso contrário.
     */
    public Optional<UUID> executeCreateOrder(UUID customerId) {
        if (customerGateway.getCustomer(customerId).isEmpty()) return Optional.empty();
        return Optional.of(orderGateway.createOrder(customerId));
    }

    /**
     * Adiciona um item a um pedido existente.
     * @param orderId O ID do pedido.
     * @param productId O ID do produto a ser adicionado.
     * @param unitPrice O preço unitário do item no momento da adição (snapshot).
     * @param quantity A quantidade do item.
     * @return Uma mensagem de feedback sobre o resultado da operação.
     */
    public String executeAddItem(UUID orderId, UUID productId, BigDecimal unitPrice, int quantity) {
        Optional<ProductView> pv = productGateway.getProduct(productId);
        if (pv.isEmpty()) return "Produto não encontrado";
        if (quantity <= 0) return "Quantidade inválida";
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) return "Preço inválido";
        try {
            orderGateway.addItem(orderId, productId, pv.get().name(), unitPrice, quantity);
            return "Item adicionado ao pedido.";
        } catch (Exception e) {
            return "Erro ao adicionar item: " + e.getMessage();
        }
    }

    /**
     * Lista todos os pedidos de um cliente específico.
     * @param customerId O ID do cliente.
     * @return Uma lista de {@link OrderView} contendo os pedidos do cliente.
     */
    public List<OrderView> listOrdersByCustomer(UUID customerId) {
        return orderGateway.listOrdersByCustomer(customerId);
    }

    /**
     * Obtém a visualização de um pedido específico.
     * @param orderId O ID do pedido.
     * @return Um {@link Optional} contendo o {@link OrderView} se encontrado, ou vazio caso contrário.
     */
    public Optional<OrderView> getOrder(UUID orderId) {
        return orderGateway.getOrder(orderId);
    }

    /**
     * Altera a quantidade de um item em um pedido existente.
     * @param orderId O ID do pedido.
     * @param productId O ID do produto do item a ser alterado.
     * @param quantity A nova quantidade do item.
     * @return Uma mensagem de feedback sobre o resultado da operação.
     */
    public String executeChangeItemQuantity(UUID orderId, UUID productId, int quantity) {
        if (quantity <= 0) return "Quantidade inválida";
        try {
            orderGateway.changeItemQuantity(orderId, productId, quantity);
            return "Quantidade alterada.";
        } catch (Exception e) {
            return "Erro ao alterar quantidade: " + e.getMessage();
        }
    }

    /**
     * Remove um item de um pedido existente.
     * @param orderId O ID do pedido.
     * @param productId O ID do produto do item a ser removido.
     * @return Uma mensagem de feedback sobre o resultado da operação.
     */
    public String executeRemoveItem(UUID orderId, UUID productId) {
        try {
            orderGateway.removeItem(orderId, productId);
            return "Item removido.";
        } catch (Exception e) {
            return "Erro ao remover item: " + e.getMessage();
        }
    }

    /**
     * Finaliza um pedido, alterando seu status para aguardando pagamento.
     * @param orderId O ID do pedido a ser finalizado.
     * @return Uma mensagem de feedback sobre o resultado da operação.
     */
    public String executeFinalizeOrder(UUID orderId) {
        try {
            orderGateway.finalizeOrder(orderId);
            return "Pedido finalizado, aguardando pagamento.";
        } catch (Exception e) {
            return "Erro ao finalizar pedido: " + e.getMessage();
        }
    }

    /**
     * Processa o pagamento de um pedido.
     * @param orderId O ID do pedido a ser pago.
     * @return Uma mensagem de feedback sobre o resultado da operação.
     */
    public String executePayOrder(UUID orderId) {
        try {
            orderGateway.pay(orderId);
            return "Pagamento confirmado.";
        } catch (Exception e) {
            return "Erro ao pagar pedido: " + e.getMessage();
        }
    }

    /**
     * Marca um pedido como entregue.
     * @param orderId O ID do pedido a ser marcado como entregue.
     * @return Uma mensagem de feedback sobre o resultado da operação.
     */
    public String executeDeliverOrder(UUID orderId) {
        try {
            orderGateway.deliver(orderId);
            return "Pedido marcado como entregue.";
        } catch (Exception e) {
            return "Erro ao entregar pedido: " + e.getMessage();
        }
    }

    /**
     * Lista todos os pedidos registrados no sistema.
     * @return Uma lista de {@link OrderView} contendo todos os pedidos.
     */
    public List<OrderView> listAllOrders() {
        return orderGateway.listAllOrders();
    }

    /**
     * Lista todos os pedidos de um cliente específico.
     * @param customerId O ID do cliente.
     * @return Uma lista de {@link OrderView} contendo os pedidos do cliente.
     */
    @Override
    public List<OrderView> listOrdersByCustomer(UUID customerId) {
        return orderGateway.listOrdersByCustomer(customerId);
    }