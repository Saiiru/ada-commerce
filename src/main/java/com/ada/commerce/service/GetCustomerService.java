package com.ada.commerce.service;

import com.ada.commerce.controller.dto.CustomerDTO;
import com.ada.commerce.model.exception.NotFoundException;
import com.ada.commerce.repository.CustomerRepository;

public class GetCustomerService {
  private final CustomerRepository repo;
  public GetCustomerService(CustomerRepository repo){ this.repo = repo; }

  public CustomerDTO execute(String id) {
    return repo.findById(id).map(CustomerDTO::fromEntity)
      .orElseThrow(() -> new NotFoundException("Customer não encontrado: " + id));
  }
}
