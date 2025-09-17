package TEST.java.com.ada.commerce.domain;

import java.util.*;

public class InMemoryProductRepository implements ProductRepository {
  private final Map<Integer, Product> storage = new HashMap<>();

  public void save(Product product) {
    storage.put(product.getSKU(), product);
  }

  public Product findBySKU(int SKU) {
    return storage.get(SKU);
  }

  public List<Product> findAll() {
    return new ArrayList<>(storage.values());
  }
}
