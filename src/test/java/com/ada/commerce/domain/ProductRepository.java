package TEST.java.com.ada.commerce.domain;

import java.util.List;

public interface ProductRepository {
  void save(Product product);
  Product findBySKU(int SKU);
  List<Product> findAll();
}
