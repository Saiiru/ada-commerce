package com.ada.commerce.service.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerGateway {
  UUID createCustomer(String name, String document, String emailOrNull);
  void updateCustomer(UUID id, String name, String emailOrNull);
  List<CustomerView> listCustomers();
  Optional<CustomerView> getCustomer(UUID id);

  // novo
  Optional<CustomerView> findByDocument(String numericDocument);
  List<CustomerView> findByName(String name); // match exato ou “contém”, a critério do adapter
}

