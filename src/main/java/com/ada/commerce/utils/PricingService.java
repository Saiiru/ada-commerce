package com.ada.commerce.utils;

import com.ada.commerce.model.Order;

import java.math.BigDecimal;

public interface PricingService {

  BigDecimal calculateTotal(Order order);
}
