package com.ada.commerce.model;

import java.math.BigDecimal;

public class OrderItem {
  private Product product;
  private Order order;
  private Integer quantity;
  private BigDecimal sellingPrice;

  public OrderItem(Product product, Order order, Integer quantity, BigDecimal sellingPrice) {
    this.product = product;
    this.order = order;
    this.quantity = quantity;
    this.sellingPrice = sellingPrice;
  }

  public BigDecimal subTotal() {
    return sellingPrice.multiply(BigDecimal.valueOf(quantity));
  }

  public Product getProduct() {
    return product;
  }

  protected void setProduct(Product product) {
    this.product = product;
  }

  public Order getOrder() {
    return order;
  }

  protected void setOrder(Order order) {
    this.order = order;
  }

  public Integer getQuantity() {
    return quantity;
  }

  protected void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getSellingPrice() {
    return sellingPrice;
  }

  protected void setSellingPrice(BigDecimal sellingPrice) {
    this.sellingPrice = sellingPrice;
  }

  @Override
  public String toString() {
    return "OrderItem{" +
      "product=" + product +
      ", quantity=" + quantity +
      ", sellingPrice=" + sellingPrice +
      '}';
  }
}
