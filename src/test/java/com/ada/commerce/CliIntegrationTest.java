package com.ada.commerce;

import com.ada.commerce.cli.MainMenuHandler;
import com.ada.commerce.controller.*;
import com.ada.commerce.service.event.InMemoryEventPublisher;
import com.ada.commerce.service.impl.memory.*;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class CliIntegrationTest {
    @Test void menuCompletesRealCustomerProductOrderLifecycle() {
        var customers = new InMemoryCustomerGateway();
        var products = new InMemoryProductGateway();
        var orders = new InMemoryOrderGateway(new InMemoryEventPublisher());
        String input = String.join("\n", "1", "1", "Cliente CLI", "52998224725", "cli@example.test",
                "3", "52998224725", "0", "2", "1", "Produto CLI", "12.50", "5", "0",
                "3", "1", "2", "Produto CLI", "", "2", "5", "6", "7", "8", "0", "0", "");
        String output = run(input, customers, products, orders);
        assertEquals(1, customers.listCustomers().size());
        assertEquals(1, products.listProducts().size());
        var order = orders.listAllOrders().get(0);
        assertEquals("DELIVERED", order.orderStatus());
        assertEquals(new BigDecimal("25.00"), order.total());
        assertEquals(3, products.listProducts().get(0).stock());
        assertTrue(output.contains("Pagamento confirmado."));
        assertTrue(output.contains("Fim."));
        assertFalse(output.contains("Erro:"));
    }

    @Test void eofAtMainMenuSubmenuAndDuringRegistrationExitsCleanly() {
        for (String input : new String[]{"", "1\n", "1\n1\nNome\n", "2\n1\nProduto\n", "3\n"}) {
            assertDoesNotThrow(() -> run(input, new InMemoryCustomerGateway(), new InMemoryProductGateway(),
                    new InMemoryOrderGateway(new InMemoryEventPublisher())));
        }
    }

    @Test void cliCanUpdateCustomerAndProduct() {
        var customers = new InMemoryCustomerGateway();
        var products = new InMemoryProductGateway();
        var orders = new InMemoryOrderGateway(new InMemoryEventPublisher());
        var customer = customers.createCustomer("Original", "52998224725", "old@example.test");
        var product = products.createProduct("Produto", new BigDecimal("12.50"), 5);
        run("1\n3\n52998224725\n4\nNovo nome\nnew@example.test\n0\n2\n4\nProduto\nNovo produto\n15.00\n0\n0\n", customers, products, orders);
        assertEquals("Novo nome", customers.getCustomer(customer).orElseThrow().name());
        assertEquals("new@example.test", customers.getCustomer(customer).orElseThrow().email());
        assertEquals("Novo produto", products.getProduct(product).orElseThrow().name());
        assertEquals(new BigDecimal("15.00"), products.getProduct(product).orElseThrow().basePrice());
        assertEquals(5, products.getProduct(product).orElseThrow().stock());
    }

    @Test void mainStartsWithValidDemoDataAndNotificationsWorkThroughMenu() {
        var originalIn = System.in;
        var originalOut = System.out;
        var originalErr = System.err;
        var captured = new ByteArrayOutputStream();
        String input = "1\n3\n52998224725\n0\n3\n1\n2\nTeclado\n\n1\n5\n6\n7\n0\n0\n";
        try (var output = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(output);
            System.setErr(output);
            assertDoesNotThrow(() -> Main.main(new String[0]));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
        String output = captured.toString(StandardCharsets.UTF_8);
        assertFalse(output.contains("[ERRO]"), output);
        assertEquals(3, output.lines().filter(line -> line.contains("[EMAIL]")).count(), output);
        assertTrue(output.contains("Pagamento confirmado."), output);
        assertTrue(output.contains("Pedido marcado como entregue."), output);
        assertEquals(2, com.ada.commerce.service.registry.ServiceRegistry.customer().listCustomers().size());
        assertEquals(3, com.ada.commerce.service.registry.ServiceRegistry.product().listProducts().size());
    }

    @Test void badNumericInputRecoversToMenu() {
        String output = run("2\n1\nProduto\nabc\n0\n0\n", new InMemoryCustomerGateway(),
                new InMemoryProductGateway(), new InMemoryOrderGateway(new InMemoryEventPublisher()));
        assertTrue(output.contains("Erro:"));
        assertTrue(output.contains("Fim."));
    }

    static String run(String input, InMemoryCustomerGateway customers, InMemoryProductGateway products, InMemoryOrderGateway orders) {
        var captured = new ByteArrayOutputStream();
        var original = System.out;
        try (var output = new PrintStream(captured, true, StandardCharsets.UTF_8);
             var scanner = new Scanner(input)) {
            System.setOut(output);
            new MainMenuHandler(scanner, new ProductController(products), new CustomerController(customers),
                    new OrderController(orders, products, customers)).run();
        } finally {
            System.setOut(original);
        }
        return captured.toString(StandardCharsets.UTF_8);
    }
}
