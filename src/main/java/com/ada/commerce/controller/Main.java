package com.ada.commerce.controller;

import com.ada.commerce.model.Product;
import com.ada.commerce.repository.InMemoryProductRepository;
import com.ada.commerce.repository.ProductRepository;
import com.ada.commerce.service.CreateProductUseCase;
import com.ada.commerce.service.GetProductUseCase;
import com.ada.commerce.service.ListProductsUseCase;
import com.ada.commerce.service.UpdateProductUseCase;

import java.math.BigDecimal;

public class Main {
  public static void main(String[] args) {
    ProductRepository repo = new InMemoryProductRepository();

    // Criar casos de uso
    CreateProductUseCase createUC = new CreateProductUseCase(repo);
    ListProductsUseCase listUC = new ListProductsUseCase(repo);
    GetProductUseCase getUC = new GetProductUseCase(repo);
    UpdateProductUseCase updateUC = new UpdateProductUseCase(repo);

    // Criar produtos
    createUC.execute("Teclado", new BigDecimal("130.50"), 1);
    createUC.execute("Mouse", new BigDecimal("80.00"), 2);

    // Listar produtos
    System.out.println("Produtos cadastrados:");
    listUC.execute().forEach(p -> System.out.println(p));

    // Buscar produto específico
    System.out.println("\nProduto com SKU 1:");
    System.out.println(getUC.execute(1));

    // Atualizar produto
    updateUC.execute(2, "Mouse Gamer", new BigDecimal("120.00"));

    System.out.println("\nDepois da atualização:");
    listUC.execute().forEach(p -> System.out.println(p));
  }
}