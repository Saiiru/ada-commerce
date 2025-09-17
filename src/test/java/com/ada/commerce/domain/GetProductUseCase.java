package TEST.java.com.ada.commerce.domain;

import TEST.java.com.ada.commerce.domain.Product;
import TEST.java.com.ada.commerce.domain.ProductRepository;
import java.util.List;

public class GetProductUseCase {
  private final ProductRepository repository;

  public GetProductUseCase(ProductRepository repository) {
    this.repository = repository;
  }

  public Product execute(int sku) {
    return repository.findBySKU(sku);
  }
}

