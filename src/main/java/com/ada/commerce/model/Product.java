package com.ada.commerce.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Product {
  private String name;
  private BigDecimal basePrice;
  private int SKU;
  private final Instant createdAt;
  private boolean active;

  public Product(String name, BigDecimal basePrice, int SKU){
    this.name = name;
    this.basePrice = basePrice;
    this.SKU = SKU;
    this.createdAt = Instant.now();
    this.active = true;
  }

  @Override
  public String toString() {
    return "Produto{" +
      "SKU=" + getSKU() +
      ", Nome='" + getName() + '\'' +
      ", Preço=R$ " + getBasePrice() +
      '}';
  }

  public String getName() {
    return name;
  }

  public BigDecimal getBasePrice() {
    return basePrice;
  }

  public int getSKU() {
    return SKU;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setBasePrice(BigDecimal basePrice) {
    this.basePrice = basePrice;
  }
}
