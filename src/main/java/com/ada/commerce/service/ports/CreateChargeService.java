package com.ada.commerce.service.ports;

import java.util.UUID;

/** Porta para geração de cobrança quando pedido é finalizado. */
public interface CreateChargeService {
  UUID createCharge(UUID orderId);
}

