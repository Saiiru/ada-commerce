package com.ada.commerce.service.ports;

import java.util.UUID;

public interface NotificationService {
  void onAwaitingPayment(UUID orderId);
  void onPaid(UUID orderId);
  void onDelivered(UUID orderId);
}

