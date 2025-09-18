package com.ada.commerce.service;

import com.ada.commerce.model.Product;
import com.ada.commerce.repository.ProductRepository;
import java.math.BigDecimal;

public class CreateProductUseCase {
  private final ProductRepository repository;

  public CreateProductUseCase(ProductRepository repository) {
    this.repository = repository;
  }

  public Product execute(String name, BigDecimal basePrice, int sku, int initialStock) {
    if (basePrice.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Preço deve ser maior do que zero!");
    }

    Product product = new Product(name, basePrice, sku, initialStock);
    repository.save(product);
    return product;
  }
}