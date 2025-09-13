package com.ada.commerce.service;

import com.ada.commerce.model.Order;
import com.ada.commerce.repository.OrderRepository;

import java.util.List;

public class ListAllOrdersServiceImpl implements ListAllOrdersService {

  private final OrderRepository repository;

  public ListAllOrdersServiceImpl(OrderRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Order> execute() {
    return repository.findAll();
  }
}
