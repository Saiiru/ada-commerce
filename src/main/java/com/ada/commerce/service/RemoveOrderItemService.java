package com.ada.commerce.service;

import com.ada.commerce.model.Order;
import com.ada.commerce.model.OrderItem;

public interface RemoveOrderItemService {

  Order execute(Order order, OrderItem orderItem);
}
