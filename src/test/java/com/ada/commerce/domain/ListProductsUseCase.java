package TEST.java.com.ada.commerce.domain;

import TEST.java.com.ada.commerce.domain.Product;
import TEST.java.com.ada.commerce.domain.ProductRepository;
import java.util.List;

public class ListProductsUseCase {
  private final ProductRepository repository;

  public ListProductsUseCase(ProductRepository repository) {
    this.repository = repository;
  }

  public List<Product> execute() {
    return repository.findAll();
  }
}

