package com.ada.commerce.service;

import com.ada.commerce.model.Order;
import com.ada.commerce.model.Product;

import java.math.BigDecimal;

public interface AddOrderItemService {

  Order execute(Order order, Product product, Integer quantity, BigDecimal sellingPrice);
}
