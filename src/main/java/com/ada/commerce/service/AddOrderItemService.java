package com.ada.commerce.service;

import com.ada.commerce.model.Order;
import com.ada.commerce.model.OrderItem;

import java.math.BigDecimal;

public interface AddOrderItemService {

  OrderItem execute(Order order, Product product, Integer quantity, BigDecimal sellingPrice);
}
