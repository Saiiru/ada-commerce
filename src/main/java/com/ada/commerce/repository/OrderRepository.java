package com.ada.commerce.repository;

import com.ada.commerce.model.Customer;
import com.ada.commerce.model.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements Repository<Order, Integer> {

  private List<Order> orders = new ArrayList<>();
  private int nextId = 0;
  private File file;

  public OrderRepository() {
    this(new File("build/data/orders.dat"));
  }

  public OrderRepository(File file) {
    this.file = file;
    loadFromFile();
  }

  @Override
  public Order save(Order order) {
    if (order.getId() != null) throw new IllegalArgumentException("Order já tem ID");

    order.setId(nextId++);
    orders.add(order);
    persistToFile();

    return order;
  }

  @Override
  public Order update(Order order) {
    orders.set(order.getId(), order);
    return order;
  }

  @Override
  public List<Order> findAll() {
    return orders;
  }

  @Override
  public Order findById(Integer id) {
    return orders.get(id);
  }

  public List<Order> findByCustomer(Customer owner) {
    return orders.stream()
      .filter(order -> order.getOwner().equals(owner))
      .toList();
  }

  private void persistToFile() {
    try {
      File parentDir = file.getParentFile();
      if (parentDir != null && !parentDir.exists()) {
        parentDir.mkdirs();
      }

      try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
        out.writeInt(nextId);
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
      nextId = in.readInt();
      orders = (List<Order>) in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException("Erro ao carregar orders do arquivo", e);
    }
  }
}
