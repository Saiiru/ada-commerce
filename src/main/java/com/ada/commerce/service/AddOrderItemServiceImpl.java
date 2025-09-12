package com.ada.commerce.service;

import com.ada.commerce.model.Order;
import com.ada.commerce.model.OrderItem;
import com.ada.commerce.model.OrderStatus;
import com.ada.commerce.repository.OrderRepository;

import java.math.BigDecimal;

public class AddOrderItemServiceImpl implements AddOrderItemService {

  OrderRepository repository = new OrderRepository();

  @Override
  public Order execute(Order order, Product product, Integer quantity, BigDecimal sellingPrice) {
    if (order == null) {
      throw new IllegalArgumentException("O pedido não pode ser nulo");
    }
    if (product == null) {
      throw new IllegalArgumentException("O produto não pode ser nulo");
    }
    if (quantity == null || quantity <= 0) {
      throw new IllegalArgumentException("A quantidade tem que ser maior que zero");
    }
    if (sellingPrice == null || sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("O preço de venda tem que ser maior que zero");
    }
    if (order.getStatus() != OrderStatus.ABERTO) {
      throw new IllegalStateException("Não é possível adicionar itens em um pedido " + order.getStatus());
    }

    OrderItem newItem = new OrderItem(product, order, quantity, sellingPrice);
    return new order.add(newItem);
  }
}
