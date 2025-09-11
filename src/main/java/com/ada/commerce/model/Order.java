package com.ada.commerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
  private Integer id;
  private Client owner;
  private ArrayList<OrderItem> items;
  private LocalDateTime createdAt;
  private OrderStatus status;

  Order(Client owner, ArrayList<OrderItem> items) {
    this.owner = owner;
    this.items = items;
    this.createdAt = LocalDateTime.now();
    this.status = OrderStatus.ABERTO;
  }

  public BigDecimal total() {
    return items.stream()
      .map(OrderItem::subTotal)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void add(OrderItem item) {
    items.add(item);
  }

  public void remove(OrderItem item) {
    items.remove(item);
  }

  public void changeQuantity(OrderItem item, Integer newQuantity) {
    item.setQuantity(newQuantity);
  }

  public Integer getId() {
    return id;
  }

  public Client getOwner() {
    return owner;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public ArrayList<OrderItem> getItems() {
    return items;
  }
}
