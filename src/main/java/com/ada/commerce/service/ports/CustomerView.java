package com.ada.commerce.service.ports;

import java.time.Instant;
import java.util.UUID;

public record CustomerView(UUID id, String name, String document, String email, Instant createdAt, boolean active) {}

