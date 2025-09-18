package com.ada.commerce.service.impl.memory;

import com.ada.commerce.service.ports.ProductGateway;
import com.ada.commerce.service.ports.ProductView;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryProductGateway implements ProductGateway {
  private final Map<UUID, ProductView> byId = new ConcurrentHashMap<>();

  @Override
  public UUID createProduct(String name, BigDecimal basePrice) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Nome invalido");
    if (basePrice == null || basePrice.signum() <= 0) throw new IllegalArgumentException("Preco base deve ser > 0");
    var id = UUID.randomUUID();
    var view = new ProductView(id, name, basePrice, Instant.now(), true);
    byId.put(id, view);
    return id;
  }

  @Override
  public void updateProduct(UUID id, String name, BigDecimal basePrice) {
    var cur = byId.get(id);
    if (cur == null) throw new NoSuchElementException("Produto nao encontrado");
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Nome invalido");
    if (basePrice == null || basePrice.signum() <= 0) throw new IllegalArgumentException("Preco base deve ser > 0");
    byId.put(id, new ProductView(id, name, basePrice, cur.createdAt(), cur.active()));
  }

  @Override
  public List<ProductView> listProducts() {
    return new ArrayList<>(byId.values());
  }

  @Override
  public Optional<ProductView> getProduct(UUID id) {
    return Optional.ofNullable(byId.get(id));
  }
}
