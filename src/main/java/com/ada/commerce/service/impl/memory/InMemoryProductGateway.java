package com.ada.commerce.service.impl.memory;

import com.ada.commerce.service.ports.ProductGateway;
import com.ada.commerce.service.ports.ProductView;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public final class InMemoryProductGateway implements ProductGateway {
  private final Map<UUID, ProductView> byId = new LinkedHashMap<>();

  @Override
  public UUID createProduct(String name, BigDecimal basePrice, int initialStock) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Nome invalido");
    if (basePrice == null || basePrice.signum() <= 0) throw new IllegalArgumentException("Preco base deve ser > 0");
    basePrice = currency(basePrice);
    if (initialStock < 0) throw new IllegalArgumentException("Estoque inicial nao pode ser negativo");
    var id = UUID.randomUUID();
    var view = new ProductView(id, name.trim(), basePrice, initialStock, Instant.now(), true);
    byId.put(id, view);
    return id;
  }

  @Override
  public void updateProduct(UUID id, String name, BigDecimal basePrice, int stock) {
    var cur = byId.get(id);
    if (cur == null) throw new NoSuchElementException("Produto nao encontrado");
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Nome invalido");
    if (basePrice == null || basePrice.signum() <= 0) throw new IllegalArgumentException("Preco base deve ser > 0");
    basePrice = currency(basePrice);
    if (stock < 0) throw new IllegalArgumentException("Estoque nao pode ser negativo");
    byId.put(id, new ProductView(id, name.trim(), basePrice, stock, cur.createdAt(), cur.active()));
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

  static BigDecimal currency(BigDecimal amount) {
    if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("Preço deve ser > 0");
    try { return amount.setScale(2, java.math.RoundingMode.UNNECESSARY); }
    catch (ArithmeticException e) { throw new IllegalArgumentException("Preço deve ter no máximo 2 casas decimais", e); }
  }

  @Override
  public void updateStock(UUID id, int quantityChange) {
    var cur = byId.get(id);
    if (cur == null) throw new NoSuchElementException("Produto nao encontrado");
    long candidate = (long) cur.stock() + quantityChange;
    if (candidate > Integer.MAX_VALUE) throw new IllegalArgumentException("Estoque excede o limite");
    int newStock = (int) candidate;
    if (newStock < 0) throw new IllegalArgumentException("Estoque insuficiente");
    byId.put(id, new ProductView(cur.id(), cur.name(), cur.basePrice(), newStock, cur.createdAt(), cur.active()));
  }
}
