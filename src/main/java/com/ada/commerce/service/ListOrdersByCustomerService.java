package com.ada.commerce.service;

import com.ada.commerce.model.Customer;
import com.ada.commerce.model.Order;

import java.util.List;

public interface ListOrdersByCustomerService {

  List<Order> execute(Customer owner);
}
