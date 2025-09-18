package com.ada.commerce.service.ports;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductGateway {
  UUID createProduct(String name, BigDecimal basePrice, int initialStock);
  void updateProduct(UUID id, String name, BigDecimal basePrice, int stock);
  void updateStock(UUID id, int quantityChange); // quantityChange pode ser positivo (adicionar) ou negativo (remover)
  List<ProductView> listProducts();
  Optional<ProductView> getProduct(UUID id);
  List<ProductView> findByName(String name);
}

