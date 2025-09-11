package com.ada.commerce.service;

import java.math.BigDecimal;

public interface AddOrderItemService {

  OrderItem execute(Order order, Product product, Integer quantity, BigDecimal sellingPrice);
}
