package com.ada.commerce.service.ports;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductView(UUID id, String name, BigDecimal basePrice, Instant createdAt, boolean active) {}

