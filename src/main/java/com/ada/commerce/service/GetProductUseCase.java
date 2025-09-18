package com.ada.commerce.service;

import com.ada.commerce.model.Product;
import com.ada.commerce.repository.ProductRepository;
import java.util.List;

public class GetProductUseCase {
  private final ProductRepository repository;

  public GetProductUseCase(ProductRepository repository) {
    this.repository = repository;
  }

  public Product execute(int sku) {
    return repository.findBySKU(sku);
  }
}