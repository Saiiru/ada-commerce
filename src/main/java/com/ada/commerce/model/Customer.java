package com.ada.commerce.model;

import com.ada.commerce.model.vo.Document;
import com.ada.commerce.model.vo.Email;

import java.util.UUID;
import java.time.Instant;
import java.util.Objects;

public class Customer {

  private final String id;
  private String name;
  private final Document document;
  private Email email;
  private final Instant createdAt;
  private boolean active;

  public Customer(String name, Document document, Email email) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.document = document;
    this.email = email;
    this.createdAt = Instant.now();
    this.active = true;
  }

  public Customer(String id, String name, Document document, Email email, Instant createdAt, boolean active) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Nome inválido");
    this.id = id;
    this.name = name;
    this.document = document;
    this.email = email;
    this.createdAt = createdAt;
    this.active = active;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Document getDocument() {
    return document;
  }

  public Email getEmail() {
    return email;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public boolean isActive() {
    return active;
  }

  public void updateName(String newName) {
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException("Nome inválido");
        this.name = newName;
    }

  public void updateEmail(Email email) { this.email = email; }
  public void deactivate() { this.active = false; }
  public void activate() { this.active = true; }

  @Override
  public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer c)) return false;
    return id.equals(c.id);
  }

  @Override
  public int hashCode(){ return Objects.hash(id);}
}
