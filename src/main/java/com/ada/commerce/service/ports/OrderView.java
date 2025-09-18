package com.ada.commerce.service.ports;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderView(UUID id, UUID customerId, String orderStatus, String paymentStatus, List<OrderItemView> items, BigDecimal total) {}

