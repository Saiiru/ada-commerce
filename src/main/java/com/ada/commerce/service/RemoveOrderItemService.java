package com.ada.commerce.service;

public interface RemoveOrderItemService {

  Order execute(Order order, OrderItem orderItem);
}
