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
  public UUID createProduct(String name, BigDecimal basePrice, int initialStock) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Nome invalido");
    if (basePrice == null || basePrice.signum() <= 0) throw new IllegalArgumentException("Preco base deve ser > 0");
    if (initialStock < 0) throw new IllegalArgumentException("Estoque inicial nao pode ser negativo");
    var id = UUID.randomUUID();
    var view = new ProductView(id, name, basePrice, initialStock, Instant.now(), true);
    byId.put(id, view);
    return id;
  }

  @Override
  public void updateProduct(UUID id, String name, BigDecimal basePrice, int stock) {
    var cur = byId.get(id);
    if (cur == null) throw new NoSuchElementException("Produto nao encontrado");
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Nome invalido");
    if (basePrice == null || basePrice.signum() <= 0) throw new IllegalArgumentException("Preco base deve ser > 0");
    if (stock < 0) throw new IllegalArgumentException("Estoque nao pode ser negativo");
    byId.put(id, new ProductView(id, name, basePrice, stock, cur.createdAt(), cur.active()));
  }

  @Override
  public List<ProductView> listProducts() {
    return new ArrayList<>(byId.values());
  }

  @Override
  public Optional<ProductView> getProduct(UUID id) {
    return Optional.ofNullable(byId.get(id));
  }

  @Override
  public List<ProductView> findByName(String name) {
    var key = name.toLowerCase();
    var out = new ArrayList<ProductView>();
    byId.values().forEach(p -> {
      if (p.name() != null && p.name().toLowerCase().contains(key)) out.add(p);
    });
    return out;
  }

  @Override
  public void updateStock(UUID id, int quantityChange) {
    var cur = byId.get(id);
    if (cur == null) throw new NoSuchElementException("Produto nao encontrado");
    int newStock = cur.stock() + quantityChange;
    if (newStock < 0) throw new IllegalArgumentException("Estoque insuficiente");
    byId.put(id, new ProductView(cur.id(), cur.name(), cur.basePrice(), newStock, cur.createdAt(), cur.active()));
  }
}
