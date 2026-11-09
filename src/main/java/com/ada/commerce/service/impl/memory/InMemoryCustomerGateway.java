package com.ada.commerce.service.impl.memory;

import com.ada.commerce.model.vo.Document;
import com.ada.commerce.model.vo.ValidarEmail;
import com.ada.commerce.service.ports.CustomerGateway;
import com.ada.commerce.service.ports.CustomerView;
import java.time.Instant;
import java.util.*;

public final class InMemoryCustomerGateway implements CustomerGateway {
  private final Map<UUID, CustomerView> byId = new LinkedHashMap<>();
  private final Map<String, UUID> byDocument = new HashMap<>();

  @Override
  public UUID createCustomer(String name, String document, String emailOrNull) {
    String validName = validateName(name);
    if (document == null) throw new IllegalArgumentException("Documento obrigatório");
    String digits = document.replaceAll("\\D", "");
    Document.Type type = digits.length() == 11 ? Document.Type.CPF : Document.Type.CNPJ;
    String doc = new Document(document, type).value();
    String email = validateEmail(emailOrNull);
    if (byDocument.containsKey(doc)) throw new IllegalStateException("Documento duplicado");
    UUID id = UUID.randomUUID();
    byId.put(id, new CustomerView(id, validName, doc, email, Instant.now(), true));
    byDocument.put(doc, id);
    return id;
  }

  @Override
  public void updateCustomer(UUID id, String name, String emailOrNull) {
    var current = byId.get(id);
    if (current == null) throw new NoSuchElementException("Cliente não encontrado");
    String validName = name == null ? current.name() : validateName(name);
    String email = emailOrNull == null ? current.email() : validateEmail(emailOrNull);
    byId.put(id, new CustomerView(id, validName, current.document(), email, current.createdAt(), current.active()));
  }

  private static String validateName(String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Nome obrigatório");
    return name.trim();
  }

  private static String validateEmail(String email) {
    return email == null || email.isBlank() ? null : new ValidarEmail(email.trim()).value();
  }

  @Override public List<CustomerView> listCustomers() { return new ArrayList<>(byId.values()); }
  @Override public Optional<CustomerView> getCustomer(UUID id) { return Optional.ofNullable(byId.get(id)); }

  @Override public Optional<CustomerView> findByDocument(String document) {
    if (document == null) return Optional.empty();
    UUID id = byDocument.get(document.replaceAll("\\D", ""));
    return id == null ? Optional.empty() : getCustomer(id);
  }

  @Override public List<CustomerView> findByName(String name) {
    if (name == null || name.isBlank()) return List.of();
    String key = name.trim().toLowerCase(Locale.ROOT);
    return byId.values().stream().filter(c -> c.name().toLowerCase(Locale.ROOT).contains(key)).toList();
  }
}
