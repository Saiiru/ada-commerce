package com.ada.commerce.service.payment;

import com.ada.commerce.service.event.EventPublisher;
import com.ada.commerce.service.ports.OrderEvents;
import com.ada.commerce.service.ports.OrderGateway;

import java.util.Objects;
import java.util.UUID;

/** Processa pagamento: valida no gateway, muda para PAID e publica evento. */
public final class ProcessPaymentUseCase {
  private final OrderGateway orders;
  private final EventPublisher events;

  public ProcessPaymentUseCase(OrderGateway orders, EventPublisher events) {
    this.orders = Objects.requireNonNull(orders);
    this.events = Objects.requireNonNull(events);
  }

  public void execute(UUID orderId) {
    orders.pay(orderId);
    events.publish(new OrderEvents.Paid(orderId));
  }
}

