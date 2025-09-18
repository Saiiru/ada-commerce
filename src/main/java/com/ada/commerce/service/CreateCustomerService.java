package com.ada.commerce.service;

import com.ada.commerce.controller.dto.CustomerDTO;
import com.ada.commerce.model.Customer;
import com.ada.commerce.model.exception.InvalidDocumentException;
import com.ada.commerce.model.vo.Document;
import com.ada.commerce.model.vo.ValidarEmail;
import com.ada.commerce.repository.CustomerRepository;

public class CreateCustomerService {
  private final CustomerRepository repo;
  public CreateCustomerService(CustomerRepository repo){ this.repo = repo; }

  public CustomerDTO execute(String name, String rawDocument, Document.Type type, String email) throws InvalidDocumentException {
    Document doc = new Document(rawDocument, type);
    ValidarEmail e = (email == null || email.isBlank()) ? null : new ValidarEmail(email);
    Customer c = new Customer(name, doc, e);
    repo.save(c);
    return CustomerDTO.fromEntity(c);
  }
}
