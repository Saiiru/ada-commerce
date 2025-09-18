package com.ada.commerce.service;

import com.ada.commerce.controller.dto.CustomerDTO;
import com.ada.commerce.repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ListCustomerService {
  private final CustomerRepository repo;
  public ListCustomerService(CustomerRepository repo){ this.repo = repo; }

  public List<CustomerDTO> execute() {
    return repo.findAll().stream()
      .map(CustomerDTO::fromEntity)
      .collect(Collectors.toList());
  }
}
