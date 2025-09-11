package com.ada.commerce.service;

import com.ada.commerce.model.Order;

import java.util.ArrayList;

public interface ListServicesByCustomerService {

  ArrayList<Order> execute(Client owner);
}
