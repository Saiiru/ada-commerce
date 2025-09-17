package TEST.java.com.ada.commerce.domain;

import TEST.java.com.ada.commerce.domain.Product;
import TEST.java.com.ada.commerce.domain.ProductRepository;
import java.math.BigDecimal;

public class CreateProductUseCase {
  private final ProductRepository repository;

  public CreateProductUseCase(ProductRepository repository) {
    this.repository = repository;
  }

  public Product execute(String name, BigDecimal basePrice, int sku) {
    if (basePrice.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Preço deve ser maior do que zero!");
    }

    Product product = new Product(name, basePrice, sku);
    repository.save(product);
    return product;
  }
}
