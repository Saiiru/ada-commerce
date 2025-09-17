package com.ada.commerce.utils;

import com.ada.commerce.model.Order;
import com.ada.commerce.model.OrderItem;

import java.math.BigDecimal;

public class PricingServiceImpl implements PricingService {

  @Override
  public BigDecimal calculateTotal(Order order) {
    return order.getItems().stream()
      .map(OrderItem::subTotal)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
