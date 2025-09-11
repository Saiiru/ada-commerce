package com.ada.commerce.service;

import com.ada.commerce.model.Order;

public interface FinalizeOrderService {

  Order execute(Order order);
}
