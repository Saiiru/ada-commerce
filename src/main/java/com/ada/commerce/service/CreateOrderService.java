package com.ada.commerce.service;

import com.ada.commerce.model.Order;
import com.ada.commerce.model.OrderItem;

import java.util.ArrayList;

public interface CreateOrderService {

  Order execute(Client owner, ArrayList<OrderItem> items);
}
