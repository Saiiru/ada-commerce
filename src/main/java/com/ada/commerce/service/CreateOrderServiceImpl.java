package com.ada.commerce.service;

import com.ada.commerce.model.Order;
import com.ada.commerce.model.OrderItem;
import com.ada.commerce.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CreateOrderServiceImpl implements CreateOrderService{

  @Override
  public Order execute(Client owner, ArrayList<OrderItem> items) {
    if (owner ==null) {
      throw new IllegalArgumentException("Cliente não pode ser nulo");
    }
    if (items.isEmpty()) {
      throw new IllegalArgumentException("Não é possível criar um pedido sem itens");
    }

    Order order = new Order(owner, items, LocalDateTime.now() , OrderStatus.ABERTO);
    return null;
  }
}
