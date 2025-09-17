package com.ada.commerce.repository;

import com.ada.commerce.model.Customer;
import com.ada.commerce.model.vo.Document;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
  void save(Customer customer);
  Optional<Customer> findById(String id);
  Optional<Customer> findByDocument(Document document);
  List<Customer> findAll();
  void update(Customer customer);
}
