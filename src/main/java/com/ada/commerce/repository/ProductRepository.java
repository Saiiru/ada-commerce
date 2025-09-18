package com.ada.commerce.repository;

import com.ada.commerce.model.Product;
import java.util.List;

public interface ProductRepository {
  void save(Product product);
  Product findBySKU(int SKU);
  List<Product> findAll();
}