package com.ada.commerce.service.ports.nulls;

import com.ada.commerce.service.ports.*;
import java.math.BigDecimal;
import java.util.*;

public final class NullOrderGateway implements OrderGateway {
  private static UnsupportedOperationException ex() {
    return new UnsupportedOperationException("Order module nao conectado");
  }
  @Override public UUID createOrder(UUID c){ throw ex(); }
  @Override public void addItem(UUID o, UUID p, String n, BigDecimal up, int q){ throw ex(); }
  @Override public void changeItemQuantity(UUID o, UUID p, int q){ throw ex(); }
  @Override public void removeItem(UUID o, UUID p){ throw ex(); }
  @Override public void finalizeOrder(UUID o){ throw ex(); }
  @Override public void pay(UUID o){ throw ex(); }
  @Override public void deliver(UUID o){ throw ex(); }
  @Override public Optional<OrderView> getOrder(UUID o){ throw ex(); }
  @Override public List<OrderView> listOrdersByCustomer(UUID c){ throw ex(); }
}

