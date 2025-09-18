package com.ada.commerce.service.ports;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductGateway {
  UUID createProduct(String name, BigDecimal basePrice);
  void updateProduct(UUID id, String name, BigDecimal basePrice);
  List<ProductView> listProducts();
  Optional<ProductView> getProduct(UUID id);
}

