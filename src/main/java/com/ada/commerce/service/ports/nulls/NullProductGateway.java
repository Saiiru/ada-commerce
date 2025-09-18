package com.ada.commerce.service.ports.nulls;

import com.ada.commerce.service.ports.ProductGateway;
import com.ada.commerce.service.ports.ProductView;
import java.math.BigDecimal;
import java.util.*;

public final class NullProductGateway implements ProductGateway {
  private static UnsupportedOperationException ex() {
    return new UnsupportedOperationException("Product module nao conectado");
  }
  @Override public UUID createProduct(String n, BigDecimal p, int s){ throw ex(); }
  @Override public void updateProduct(UUID id, String n, BigDecimal p, int s){ throw ex(); }
  @Override public List<ProductView> listProducts(){ throw ex(); }
  @Override public Optional<ProductView> getProduct(UUID id){ throw ex(); }
  @Override public List<ProductView> findByName(String name){ throw ex(); }
  @Override public void updateStock(UUID id, int quantityChange){ throw ex(); }
}

