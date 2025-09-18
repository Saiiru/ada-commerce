package com.ada.commerce.service.delivery;

import com.ada.commerce.service.event.EventPublisher;
import com.ada.commerce.service.ports.OrderEvents;
import com.ada.commerce.service.ports.OrderGateway;

import java.util.Objects;
import java.util.UUID;

/** Marca como entregue: valida no gateway, muda para DELIVERED e publica evento. */
public final class MarkAsDeliveredUseCase {
  private final OrderGateway orders;
  private final EventPublisher events;

  public MarkAsDeliveredUseCase(OrderGateway orders, EventPublisher events) {
    this.orders = Objects.requireNonNull(orders);
    this.events = Objects.requireNonNull(events);
  }

  public void execute(UUID orderId) {
    orders.deliver(orderId);
    events.publish(new OrderEvents.Delivered(orderId));
  }
}

