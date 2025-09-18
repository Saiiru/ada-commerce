package com.ada.commerce.service.ports;

import java.util.UUID;

public final class OrderEvents {
  private OrderEvents() {}
  public record AwaitingPayment(UUID orderId) {}
  public record Paid(UUID orderId) {}
  public record Delivered(UUID orderId) {}
}

