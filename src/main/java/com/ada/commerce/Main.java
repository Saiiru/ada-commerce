package com.ada.commerce;

import com.ada.commerce.cli.MainMenuHandler;
import com.ada.commerce.controller.CustomerController;
import com.ada.commerce.controller.OrderController;
import com.ada.commerce.controller.ProductController;
import com.ada.commerce.service.event.EventPublisher;
import com.ada.commerce.service.event.InMemoryEventPublisher;
import com.ada.commerce.service.impl.memory.InMemoryCustomerGateway;
import com.ada.commerce.service.impl.memory.InMemoryOrderGateway;
import com.ada.commerce.service.impl.memory.InMemoryProductGateway;
import com.ada.commerce.service.notification.ConsoleEmailNotifier;
import com.ada.commerce.service.ports.NotificationService;
import com.ada.commerce.service.ports.OrderEvents;
import com.ada.commerce.service.registry.ServiceRegistry;
import com.ada.commerce.service.time.ClockProvider;
import com.ada.commerce.service.time.SystemClockProvider;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Inicialização da infraestrutura
        EventPublisher bus = new InMemoryEventPublisher();
        ClockProvider clock = new SystemClockProvider();
        NotificationService notifier = new ConsoleEmailNotifier();

        ServiceRegistry.init(bus, clock, notifier);

        // Registra os gateways em memória para permitir o teste de fluxo completo.
        ServiceRegistry.register(new InMemoryCustomerGateway());
        ServiceRegistry.register(new InMemoryProductGateway());
        ServiceRegistry.register(new InMemoryOrderGateway(ServiceRegistry.bus()));

        // Subscrições de notificação para desacoplar os casos de uso do notificador.
        bus.subscribe(OrderEvents.AwaitingPayment.class, e -> notifier.onAwaitingPayment(e.orderId()));
        bus.subscribe(OrderEvents.Paid.class,           e -> notifier.onPaid(e.orderId()));
        bus.subscribe(OrderEvents.Delivered.class,      e -> notifier.onDelivered(e.orderId()));

        // Populando o sistema com dados de teste
        seedData();

        // Inicialização dos Controllers
        ProductController productController = new ProductController(ServiceRegistry.product());
        CustomerController customerController = new CustomerController(ServiceRegistry.customer());
        OrderController orderController = new OrderController(
                ServiceRegistry.order(),
                ServiceRegistry.product(),
                ServiceRegistry.customer()
        );

        // Execução do MainMenuHandler
        try (Scanner sc = new Scanner(System.in)) {
            MainMenuHandler mainMenuHandler = new MainMenuHandler(sc, productController, customerController, orderController);
            mainMenuHandler.run();
        }
    }

    private static void seedData() {
        System.out.println("\n[INFO] Populando o sistema com dados de teste...");
        try {
            // Clientes
            ServiceRegistry.customer().createCustomer("Ada Lovelace", "11122233344", "ada@lovelace.com");
            ServiceRegistry.customer().createCustomer("Grace Hopper", "22233344455", "grace@hopper.com");

            // Produtos
            ServiceRegistry.product().createProduct("Teclado Mecanico", new BigDecimal("350.99"), 10);
            ServiceRegistry.product().createProduct("Mouse Gamer RGB", new BigDecimal("220.50"), 15);
            ServiceRegistry.product().createProduct("Monitor Ultrawide 34\"", new BigDecimal("2800.00"), 5);
            System.out.println("[INFO] Dados de teste carregados.");
        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao carregar dados de teste: " + e.getMessage());
        }
    }
}
