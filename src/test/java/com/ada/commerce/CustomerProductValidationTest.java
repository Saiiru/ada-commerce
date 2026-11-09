package com.ada.commerce;

import com.ada.commerce.controller.CustomerController;
import com.ada.commerce.model.vo.ValidarEmail;
import com.ada.commerce.service.impl.memory.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CustomerProductValidationTest {
    @Test void acceptsValidCpfCnpjAndOptionalEmailAndFindsNormalizedDocuments() {
        var customers = new InMemoryCustomerGateway();
        var cpf = customers.createCustomer(" Pessoa ", "529.982.247-25", "Pessoa@EXAMPLE.TEST");
        var cnpj = customers.createCustomer("Empresa", "11.222.333/0001-81", null);
        assertEquals(cpf, customers.findByDocument("52998224725").orElseThrow().id());
        assertEquals(cnpj, customers.findByDocument("11222333000181").orElseThrow().id());
        assertEquals("Pessoa", customers.getCustomer(cpf).orElseThrow().name());
        assertEquals("pessoa@example.test", customers.getCustomer(cpf).orElseThrow().email());
        assertEquals("normal@example.technology", new ValidarEmail("normal@example.technology").value());
    }

    @Test void rejectsInvalidDocumentsAndDuplicatesWithoutCreatingCustomers() {
        var customers = new InMemoryCustomerGateway();
        for (String doc : new String[]{"123", "11111111111", "52998224724", "11222333000182", "abc52998224725"}) {
            assertThrows(IllegalArgumentException.class, () -> customers.createCustomer("Teste", doc, null), doc);
        }
        customers.createCustomer("Teste", "52998224725", null);
        assertThrows(IllegalStateException.class, () -> customers.createCustomer("Outro", "529.982.247-25", null));
        assertEquals(1, customers.listCustomers().size());
    }

    @Test void invalidCustomerUpdateIsAtomicAndNullFieldsPreserveValues() {
        var customers = new InMemoryCustomerGateway();
        var id = customers.createCustomer("Original", "52998224725", "original@example.test");
        assertThrows(IllegalArgumentException.class, () -> customers.updateCustomer(id, "Alterado", "ruim"));
        assertEquals("Original", customers.getCustomer(id).orElseThrow().name());
        customers.updateCustomer(id, null, null);
        assertEquals("original@example.test", customers.getCustomer(id).orElseThrow().email());
        customers.updateCustomer(id, "Novo", "");
        assertNull(customers.getCustomer(id).orElseThrow().email());
        assertTrue(customers.findByName("ORIGINAL").isEmpty());
        assertEquals(1, customers.findByName("novo").size());
        assertEquals("52998224725", customers.getCustomer(id).orElseThrow().document());
    }

    @Test void controllerAcceptsNullOptionalEmailAndRejectsBlankNameOrBadEmail() {
        var customers = new InMemoryCustomerGateway();
        var controller = new CustomerController(customers);
        assertTrue(controller.executeCreateCustomer("Teste", "52998224725", null).isSuccess());
        assertFalse(controller.executeCreateCustomer(" ", "11222333000181", "").isSuccess());
        assertFalse(controller.executeCreateCustomer("Empresa", "11222333000181", "sem-email").isSuccess());
        var id = customers.listCustomers().get(0).id();
        assertTrue(controller.executeUpdateCustomer(id, null, null).isSuccess());
    }

    @Test void productPricesArePositiveExactCentsAndFailedUpdatePreservesProduct() {
        var products = new InMemoryProductGateway();
        var id = products.createProduct("Produto", new BigDecimal("12.5"), 3);
        assertEquals(new BigDecimal("12.50"), products.getProduct(id).orElseThrow().basePrice());
        for (String price : new String[]{"0", "-1", "0.001", "12.345"}) {
            assertThrows(IllegalArgumentException.class, () -> products.createProduct("Produto", new BigDecimal(price), 1));
        }
        assertThrows(IllegalArgumentException.class, () -> products.updateProduct(id, "Novo", new BigDecimal("1.001"), 9));
        assertEquals("Produto", products.getProduct(id).orElseThrow().name());
        assertEquals(3, products.getProduct(id).orElseThrow().stock());
    }
}
