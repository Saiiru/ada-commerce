package com.ada.commerce.service;

import com.ada.commerce.controller.dto.CustomerDTO;
import com.ada.commerce.model.Customer;
import com.ada.commerce.model.exception.InvalidDocumentException;
import com.ada.commerce.model.exception.NotFoundException;
import com.ada.commerce.model.vo.ValidarEmail;
import com.ada.commerce.repository.CustomerRepository;

import java.util.Optional;

public class UpdateCustomerService {
  private final CustomerRepository repo;
  public UpdateCustomerService(CustomerRepository repo){ this.repo = repo; }

  public CustomerDTO execute(String id, String newName, String newEmail) throws InvalidDocumentException {
    Optional<Customer> opt = repo.findById(id);
    Customer c = opt.orElseThrow(() -> new NotFoundException("Customer não encontrado: " + id));
    if (newName != null && !newName.isBlank()) c.updateName(newName);
    if (newEmail != null) {
      if (newEmail.isBlank()) c.updateEmail(null);
      else c.updateEmail(new ValidarEmail(newEmail));
    }
    repo.update(c);
    return CustomerDTO.fromEntity(c);
  }
}
