package com.ada.commerce.service;

import com.ada.commerce.model.Product;
import com.ada.commerce.repository.ProductRepository;
import java.util.List;

public class ListProductsUseCase {
  private final ProductRepository repository;

  public ListProductsUseCase(ProductRepository repository) {
    this.repository = repository;
  }

  public List<Product> execute() {
    return repository.findAll();
  }
}