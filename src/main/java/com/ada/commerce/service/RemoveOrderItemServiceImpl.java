package com.ada.commerce.service;

import com.ada.commerce.model.Order;
import com.ada.commerce.model.OrderItem;
import com.ada.commerce.model.OrderStatus;
import com.ada.commerce.repository.OrderRepository;

public class RemoveOrderItemServiceImpl implements RemoveOrderItemService {

  private final OrderRepository repository;

  public RemoveOrderItemServiceImpl(OrderRepository repository) {
    this.repository = repository;
  }

  @Override
  public Order execute(OrderItem orderItem) {
    if (orderItem == null) {
      throw new IllegalArgumentException("O referido item não foi encontrado");
    }

    Order order = orderItem.getOrder();

    if (order.getStatus() != OrderStatus.ABERTO) {
      throw new IllegalStateException("Não é possível remover itens em um pedido " + order.getStatus());
    }

    order = order.remove(orderItem);
    repository.update(order);
    return order;
  }
}
