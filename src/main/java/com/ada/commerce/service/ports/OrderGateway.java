package com.ada.commerce.service.ports;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderGateway {
  UUID createOrder(UUID customerId);
  void addItem(UUID orderId, UUID productId, String productNameSnapshot, BigDecimal unitPriceSnapshot, int quantity);
  void changeItemQuantity(UUID orderId, UUID productId, int quantity);
  void removeItem(UUID orderId, UUID productId);
  void finalizeOrder(UUID orderId);
  void pay(UUID orderId);
  void deliver(UUID orderId);
  Optional<OrderView> getOrder(UUID orderId);
  List<OrderView> listOrdersByCustomer(UUID customerId);
  List<OrderView> listAllOrders();
}

