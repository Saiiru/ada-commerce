package com.ada.commerce.service;

import com.ada.commerce.model.Order;
import com.ada.commerce.model.OrderItem;
import com.ada.commerce.model.OrderStatus;
import com.ada.commerce.repository.OrderRepository;

import java.math.BigDecimal;

public class ChangeOrderItemQuantityServiceImpl implements ChangeOrderItemQuantityService {

  private final OrderRepository repository;

  public ChangeOrderItemQuantityServiceImpl(OrderRepository repository) {
    this.repository = repository;
  }

  @Override
  public Order execute(OrderItem orderItem, Integer newQuantity) {
    if (orderItem == null) {
      throw new IllegalArgumentException("O referido item não foi encontrado");
    }
    if (newQuantity == null || newQuantity <= 0) {
      throw new IllegalArgumentException("A quantidade tem que ser maior que zero");
    }

    Order order = orderItem.getOrder();

    if (order.getStatus() != OrderStatus.ABERTO) {
      throw new IllegalStateException("Não é possível alterar itens em um pedido " + order.getStatus());
    }

    Order newOrder = order.changeQuantity(orderItem, newQuantity);
    repository.update(newOrder);
    return newOrder;
  }
}
