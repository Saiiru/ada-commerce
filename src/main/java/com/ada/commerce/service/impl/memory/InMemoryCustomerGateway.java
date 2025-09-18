package com.ada.commerce.service.impl.memory;

import com.ada.commerce.service.ports.CustomerGateway;
import com.ada.commerce.service.ports.CustomerView;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryCustomerGateway implements CustomerGateway {
  private final Map<UUID, CustomerView> byId = new ConcurrentHashMap<>();
  private final Map<String, UUID> byDocument = new ConcurrentHashMap<>();

  @Override
  public UUID createCustomer(String name, String document, String emailOrNull) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(document);
    var doc = document.replaceAll("\\D+", "");
    if (doc.isEmpty()) throw new IllegalArgumentException("Documento invalido");
    if (byDocument.containsKey(doc)) throw new IllegalStateException("Documento duplicado");

    var id = UUID.randomUUID();
    var view = new CustomerView(id, name, doc, emailOrNull, Instant.now(), true);
    byId.put(id, view);
    byDocument.put(doc, id);
    return id;
  }

  @Override
  public void updateCustomer(UUID id, String name, String emailOrNull) {
    var cur = byId.get(id);
    if (cur == null) throw new NoSuchElementException("Cliente nao encontrado");
    var updated = new CustomerView(cur.id(), name, cur.document(), emailOrNull, cur.createdAt(), cur.active());
    byId.put(id, updated);
  }

  @Override
  public List<CustomerView> listCustomers() {
    return new ArrayList<>(byId.values());
  }

  @Override
  public Optional<CustomerView> getCustomer(UUID id) {
    return Optional.ofNullable(byId.get(id));
  }
}
