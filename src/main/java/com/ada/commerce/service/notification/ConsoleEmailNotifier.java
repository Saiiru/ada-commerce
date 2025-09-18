package com.ada.commerce.service.notification;

import com.ada.commerce.service.ports.NotificationService;
import java.util.UUID;

public final class ConsoleEmailNotifier implements NotificationService {
  @Override public void onAwaitingPayment(UUID orderId) {
  System.out.println("[EMAIL] Pedido " + orderId + " aguardando pagamento.");
  }
  @Override public void onPaid(UUID orderId)            {
  System.out.println("[EMAIL] Pedido " + orderId + " pago.");
  }
  @Override public void onDelivered(UUID orderId)       {
  System.out.println("[EMAIL] Pedido " + orderId + " entregue.");
  }
}

