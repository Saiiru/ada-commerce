package com.ada.commerce.service;

public interface ChangeOrderItemQuantityService {

  OrderItem execute(OrderItem orderItem, Integer newQuantity);
}
