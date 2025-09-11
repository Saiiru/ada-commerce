package com.ada.commerce.service;

import com.ada.commerce.model.OrderItem;

public interface ChangeOrderItemQuantityService {

  OrderItem execute(OrderItem orderItem, Integer newQuantity);
}
