package com.ada.commerce.service.delivery;

import com.ada.commerce.service.event.EventPublisher;
import com.ada.commerce.service.ports.OrderEvents;
import com.ada.commerce.service.ports.OrderGateway;

import java.util.Objects;
import java.util.UUID;

/**
 * Caso de uso para marcar um pedido como entregue.
 * Valida o estado do pedido, marca-o como entregue e publica um evento de notificação.
 */
public final class MarkAsDeliveredUseCase {
  private final OrderGateway orders;
  private final EventPublisher events;

  public MarkAsDeliveredUseCase(OrderGateway orders, EventPublisher events) {
    this.orders = Objects.requireNonNull(orders);
    this.events = Objects.requireNonNull(events);
  }

  /**
   * Executa o processo de entrega para um dado ID de pedido.
   * @param orderId O ID do pedido a ser marcado como entregue.
   */
  public void execute(UUID orderId) {
    orders.deliver(orderId);
    events.publish(new OrderEvents.Delivered(orderId));
  }
}