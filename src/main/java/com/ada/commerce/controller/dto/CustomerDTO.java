package com.ada.commerce.controller.dto;

import com.ada.commerce.model.Customer;

import java.time.Instant;

public record CustomerDTO(String id, String name, String document, String email, Instant createdAt, boolean active) {
  public static CustomerDTO fromEntity(Customer c) {
    return new CustomerDTO(
      c.getId(),
      c.getName(),
      c.getDocument().value(),
      c.getEmail() == null ? null : c.getEmail().value(),
      c.getCreatedAt(),
      c.isActive()
    );
  }
}
