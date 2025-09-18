package com.ada.commerce.repository;

import com.ada.commerce.model.Customer;
import com.ada.commerce.model.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OrderRepository implements Repository<Order, UUID> {

  private Map<UUID, Order> orders = new ConcurrentHashMap<>();
  private final File file;

  public OrderRepository() {
    this(new File("build/data/orders.dat"));
  }

  public OrderRepository(File file) {
    this.file = file;
    loadFromFile();
  }

  @Override
  public Order save(Order order) {
    if (orders.containsKey(order.getId())) {
      throw new IllegalArgumentException("Order com este ID já existe.");
    }
    orders.put(order.getId(), order);
    persistToFile();
    return order;
  }

  @Override
  public Order update(Order order) {
    if (!orders.containsKey(order.getId())) {
      throw new IllegalArgumentException("Order não encontrado para atualização.");
    }
    orders.put(order.getId(), order);
    persistToFile();
    return order;
  }

  @Override
  public List<Order> findAll() {
    return new ArrayList<>(orders.values());
  }

  @Override
  public Order findById(UUID id) {
    return orders.get(id);
  }

  public List<Order> findByCustomer(Customer owner) {
    return orders.values().stream()
      .filter(order -> order.getOwner().equals(owner))
      .collect(Collectors.toList());
  }

  private void persistToFile() {
    try {
      File parentDir = file.getParentFile();
      if (parentDir != null && !parentDir.exists()) {
        parentDir.mkdirs();
      }

      try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
        out.writeObject(orders);
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar orders no arquivo", e);
    }
  }

  @SuppressWarnings("unchecked")
  private void loadFromFile() {
    if (!file.exists()) {
      return;
    }
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
      orders = (Map<UUID, Order>) in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      // Silenciosamente ignora se o arquivo estiver corrompido ou em formato antigo, começando do zero.
      System.err.println("Aviso: Não foi possível carregar os pedidos do arquivo. Começando com um repositório vazio.");
      orders = new ConcurrentHashMap<>();
    }
  }
}