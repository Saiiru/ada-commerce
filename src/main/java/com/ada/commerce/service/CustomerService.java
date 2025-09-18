package com.ada.commerce.service;

import com.ada.commerce.controller.dto.CustomerDTO;
import com.ada.commerce.model.exception.InvalidDocumentException;

import java.util.List;

public class CustomerService {
  private final CreateCustomerService create;
  private final UpdateCustomerService update;
  private final GetCustomerService get;
  private final ListCustomerService list;

  public CustomerService(CreateCustomerService create, UpdateCustomerService update, GetCustomerService get, ListCustomerService list) {
    this.create = create;
    this.update = update;
    this.get = get;
    this.list = list;
  }

  public CustomerDTO create(String name, String rawDocument, com.ada.commerce.model.vo.Document.Type type, String email) throws InvalidDocumentException {
    return create.execute(name, rawDocument, type, email);
  }

  public CustomerDTO update(String id, String newName, String newEmail) throws InvalidDocumentException {
    return update.execute(id, newName, newEmail);
  }

  public CustomerDTO get(String id) {
    return get.execute(id);
  }

  public List<CustomerDTO> list() {
    return list.execute();
  }
}
