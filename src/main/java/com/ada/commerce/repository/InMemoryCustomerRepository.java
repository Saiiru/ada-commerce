package com.ada.commerce.repository;

import com.ada.commerce.model.Customer;
import com.ada.commerce.model.exception.DuplicateDocumentException;
import com.ada.commerce.model.exception.NotFoundException;
import com.ada.commerce.model.vo.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCustomerRepository implements CustomerRepository{
  private final Map<String, Customer> byId = new ConcurrentHashMap<>();
  private final Map<String, String> docToId = new ConcurrentHashMap<>();

  @Override
  public synchronized void save(Customer customer){
    String docKey = key(customer.getDocument());
    if (docToId.containsKey(docKey)) throw new DuplicateDocumentException("Documento já cadastrado: " + docKey);
    byId.put(customer.getId(), customer);
    docToId.put(docKey, customer.getId());
  }

  @Override
  public Optional<Customer> findById(String id){ return Optional.ofNullable(byId.get(id)); }

  @Override
  public Optional<Customer> findByDocument(Document document){
    String id = String.valueOf(docToId.get(key(document)));
    return id == null ? Optional.empty() : Optional.ofNullable(byId.get(id));
  }

  @Override
  public List<Customer> findAll(){ return new ArrayList<>(byId.values()); }

  @Override
  public synchronized void update(Customer customer) {
    if (!byId.containsKey(customer.getId())) throw new NotFoundException("Customer não encontrado: " + customer.getId());
    byId.put(customer.getId(), customer);
  }

  private String key(Document d){ return d.type().name() + ":" + d.value(); }

}
