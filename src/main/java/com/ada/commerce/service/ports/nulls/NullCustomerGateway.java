package com.ada.commerce.service.ports.nulls;

import com.ada.commerce.service.ports.CustomerGateway;
import com.ada.commerce.service.ports.CustomerView;
import java.util.*;

public final class NullCustomerGateway implements CustomerGateway {
  private static UnsupportedOperationException ex() { return new UnsupportedOperationException("Customer module nao conectado"); }
  @Override public UUID createCustomer(String n, String d, String e){ throw ex(); }
  @Override public void updateCustomer(UUID id, String n, String e){ throw ex(); }
  @Override public List<CustomerView> listCustomers(){ throw ex(); }
  @Override public Optional<CustomerView> getCustomer(UUID id){ throw ex(); }
  @Override public Optional<CustomerView> findByDocument(String numericDocument) { throw ex(); }
  @Override public List<CustomerView> findByName(String name) { throw ex(); }
}

