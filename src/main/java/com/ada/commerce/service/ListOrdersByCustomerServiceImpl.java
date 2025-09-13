package com.ada.commerce.service;

import com.ada.commerce.model.Customer;
import com.ada.commerce.model.Order;
import com.ada.commerce.repository.OrderRepository;

import java.util.List;

public class ListOrdersByCustomerServiceImpl implements ListOrdersByCustomerService {

  private final OrderRepository repository;

  public ListOrdersByCustomerServiceImpl(OrderRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Order> execute(Customer owner) {
    if (owner == null) {
      throw new IllegalArgumentException("O cliente é um dado obrigatório");
    }

    return repository.findByCustomer(owner);
  }
}
