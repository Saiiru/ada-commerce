package com.ada.commerce.repository;

import com.ada.commerce.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepository implements Repository<Order, Integer> {

  private List<Order> orders = new ArrayList<>();

  @Override
  public Order save(Order order) {
    orders.add(order);
    return order;
  }

  @Override
  public Order update(Order order) {
    orders.set(order.getId(), order);
    return order;
  }

  @Override
  public List<Order> findAll() {
    return orders;
  }

  @Override
  public Order findById(Integer id) {
    return orders.get(id);
  }
}
