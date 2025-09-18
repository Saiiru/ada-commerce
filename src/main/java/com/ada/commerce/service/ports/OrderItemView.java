package com.ada.commerce.service.ports;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemView(UUID productId, String name, BigDecimal unitPrice, int quantity) {}

