package com.ada.commerce.service.payment;

import com.ada.commerce.service.event.EventPublisher;
import com.ada.commerce.service.ports.OrderGateway;

import java.util.Objects;
import java.util.UUID;

/**
 * Caso de uso para processar o pagamento de um pedido.
 * Valida o estado do pedido através do gateway, efetua o pagamento e delega a publicação do evento ao gateway.
 */
public final class ProcessPaymentUseCase {
  private final OrderGateway orders;

  public ProcessPaymentUseCase(OrderGateway orders, EventPublisher events) {
    this.orders = Objects.requireNonNull(orders);
    Objects.requireNonNull(events);
  }

  /**
   * Executa o processo de pagamento para um dado ID de pedido.
   * @param orderId O ID do pedido a ser pago.
   */
  public void execute(UUID orderId) {
    orders.pay(orderId);
  }
}