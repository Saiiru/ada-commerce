package com.ada.commerce.service;

public interface CreateOrderService {

  Order execute(Client owner, List<OrderItem> items);
}
