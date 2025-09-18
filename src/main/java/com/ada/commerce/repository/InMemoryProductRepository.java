package com.ada.commerce.repository;

import com.ada.commerce.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryProductRepository implements ProductRepository {
  private final Map<Integer, Product> storage = new HashMap<>();

  public void save(Product product) {
    storage.put(product.getSKU(), product);
  }

  public Product findBySKU(int SKU) {
    return storage.get(SKU);
  }

  public List<Product> findAll() {
    return new ArrayList<>(storage.values());
  }
}