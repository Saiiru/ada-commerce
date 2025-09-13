package com.ada.commerce.service;

import com.ada.commerce.model.Customer;
import com.ada.commerce.model.Order;
import com.ada.commerce.model.OrderItem;

import java.util.ArrayList;

public interface CreateOrderService {

  Order execute(Customer owner, ArrayList<OrderItem> items);
}
