package com.ada.commerce.service;

import com.ada.commerce.model.Order;
import com.ada.commerce.model.OrderItem;

public interface ChangeOrderItemQuantityService {

  Order execute(OrderItem orderItem, Integer newQuantity);
}
