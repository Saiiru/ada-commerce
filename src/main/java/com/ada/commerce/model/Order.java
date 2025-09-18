package com.ada.commerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order implements Serializable {
  private final UUID id;
  private Customer owner;
  private List<OrderItem> items;
  private LocalDateTime createdAt;
  private OrderStatus status;

  public Order(Customer owner, List<OrderItem> items, LocalDateTime createdAt, OrderStatus status) {
    this.id = UUID.randomUUID();
    this.owner = owner;
    this.createdAt = createdAt;
    this.status = status;
    this.items = new ArrayList<>();
    items.forEach(this::add);
  }

  public Order add(OrderItem item) {
    items.add(item);
    item.setOrder(this);
    return this;
  }

  public Order changeQuantity(OrderItem item, Integer newQuantity) {
    item.setQuantity(newQuantity);
    return this;
  }

  public Order changeStatus(OrderStatus newStatus) {
    this.status = newStatus;
    return this;
  }

  public Order remove(OrderItem item) {
    items.remove(item);
    item.setOrder(null);
    return this;
  }

  public UUID getId() {
    return id;
  }

  public Customer getOwner() {
    return owner;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public List<OrderItem> getItems() {
    return List.copyOf(items);
  }

  @Override
  public String toString() {
    return "Order{" +
      "id=" + id +
      ", owner=" + owner +
      ", items=" + items +
      ", createdAt=" + createdAt +
      ", status=" + status +
      '}';
  }
}