package com.ada.commerce.service;

import com.ada.commerce.model.Product;
import com.ada.commerce.repository.ProductRepository;
import java.math.BigDecimal;

public class UpdateProductUseCase {
  private final ProductRepository repository;

  public UpdateProductUseCase(ProductRepository repository) {
    this.repository = repository;
  }

  public Product execute(int sku, String newName, BigDecimal newPrice) {
    Product product = repository.findBySKU(sku);
    if (product == null) {
      throw new IllegalArgumentException("Produto não encontrado: " + sku);
    }

    if (newName != null && !newName.isBlank()) {
      product.setName(newName);
    }
    if (newPrice != null && newPrice.compareTo(BigDecimal.ZERO) > 0) {
      product.setBasePrice(newPrice);
    }

    repository.save(product);
    return product;
  }
}