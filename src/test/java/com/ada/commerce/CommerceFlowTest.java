package com.ada.commerce;

import com.ada.commerce.controller.*;
import com.ada.commerce.service.event.InMemoryEventPublisher;
import com.ada.commerce.service.impl.memory.*;
import com.ada.commerce.service.ports.*;
import com.ada.commerce.service.payment.ProcessPaymentUseCase;
import com.ada.commerce.service.delivery.MarkAsDeliveredUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CommerceFlowTest {
    InMemoryEventPublisher bus;
    InMemoryCustomerGateway customers;
    InMemoryProductGateway products;
    InMemoryOrderGateway orders;
    OrderController controller;
    UUID customer, product, order;
    List<String> events;
    List<UUID> eventOrderIds;

    @BeforeEach void setup() {
        bus = new InMemoryEventPublisher();
        customers = new InMemoryCustomerGateway();
        products = new InMemoryProductGateway();
        orders = new InMemoryOrderGateway(bus);
        controller = new OrderController(orders, products, customers);
        customer = customers.createCustomer("Cliente de teste", "52998224725", "teste@example.test");
        product = products.createProduct("Produto de teste", new BigDecimal("12.50"), 10);
        order = controller.executeCreateOrder(customer).orElseThrow();
        events = new ArrayList<>();
        eventOrderIds = new ArrayList<>();
        bus.subscribe(OrderEvents.AwaitingPayment.class, e -> { events.add("awaiting"); eventOrderIds.add(e.orderId()); });
        bus.subscribe(OrderEvents.Paid.class, e -> { events.add("paid"); eventOrderIds.add(e.orderId()); });
        bus.subscribe(OrderEvents.Delivered.class, e -> { events.add("delivered"); eventOrderIds.add(e.orderId()); });
    }

    @Test void completeLifecyclePreservesSnapshotAndEmitsEachTransitionOnce() {
        assertEquals("Item adicionado ao pedido.", controller.executeAddItem(order, product, new BigDecimal("12.50"), 2));
        products.updateProduct(product, "Nome novo", new BigDecimal("99.00"), 8);
        assertEquals(new BigDecimal("25.00"), view().total());
        assertEquals("Produto de teste", view().items().get(0).name());
        assertEquals("Pedido finalizado, aguardando pagamento.", controller.executeFinalizeOrder(order));
        assertEquals("AWAITING_PAYMENT", view().orderStatus());
        assertEquals("Pagamento confirmado.", controller.executePayOrder(order));
        assertEquals("PAID", view().orderStatus());
        assertEquals("PAID", view().paymentStatus());
        assertEquals("Pedido marcado como entregue.", controller.executeDeliverOrder(order));
        assertEquals("DELIVERED", view().orderStatus());
        assertEquals(List.of("awaiting", "paid", "delivered"), events);
        assertEquals(List.of(order, order, order), eventOrderIds);
        assertTrue(controller.executeDeliverOrder(order).startsWith("Erro"));
        assertEquals(3, events.size());
    }

    @Test void stockTracksAddIncreaseDecreaseRemoveAndDuplicateAdd() {
        controller.executeAddItem(order, product, new BigDecimal("12.50"), 2);
        assertEquals(8, stock());
        controller.executeAddItem(order, product, new BigDecimal("99.00"), 1);
        assertEquals(3, view().items().get(0).quantity());
        assertEquals(new BigDecimal("37.50"), view().total());
        assertEquals(7, stock());
        controller.executeChangeItemQuantity(order, product, 5);
        assertEquals(5, stock());
        controller.executeChangeItemQuantity(order, product, 1);
        assertEquals(9, stock());
        controller.executeRemoveItem(order, product);
        assertEquals(10, stock());
        assertTrue(view().items().isEmpty());
    }

    @Test void twoOrdersShareStockAndCanReserveReleasedItems() {
        UUID otherOrder = controller.executeCreateOrder(customer).orElseThrow();
        assertEquals("Item adicionado ao pedido.", controller.executeAddItem(order, product, new BigDecimal("12.50"), 6));
        assertTrue(controller.executeAddItem(otherOrder, product, new BigDecimal("12.50"), 5).startsWith("Erro"));
        assertTrue(orders.getOrder(otherOrder).orElseThrow().items().isEmpty());
        assertEquals(4, stock());
        assertEquals("Item removido.", controller.executeRemoveItem(order, product));
        assertEquals("Item adicionado ao pedido.", controller.executeAddItem(otherOrder, product, new BigDecimal("12.50"), 5));
        assertEquals(5, stock());
        assertTrue(view().items().isEmpty());
        assertEquals(new BigDecimal("62.50"), orders.getOrder(otherOrder).orElseThrow().total());
    }

    @Test void insufficientStockDoesNotMutateOrderOrInventory() {
        assertTrue(controller.executeAddItem(order, product, new BigDecimal("12.50"), 11).startsWith("Erro"));
        assertEquals(10, stock());
        assertTrue(view().items().isEmpty());
        controller.executeAddItem(order, product, new BigDecimal("12.50"), 2);
        assertTrue(controller.executeChangeItemQuantity(order, product, 11).startsWith("Erro"));
        assertEquals(8, stock());
        assertEquals(2, view().items().get(0).quantity());
    }

    @Test void invalidTransitionsAndClosedItemEditsLeaveStateUnchanged() {
        assertThrows(IllegalStateException.class, () -> orders.finalizeOrder(order));
        assertThrows(IllegalStateException.class, () -> orders.pay(order));
        assertThrows(IllegalStateException.class, () -> orders.deliver(order));
        controller.executeAddItem(order, product, new BigDecimal("12.50"), 1);
        orders.finalizeOrder(order);
        assertThrows(IllegalStateException.class, () -> orders.finalizeOrder(order));
        assertTrue(controller.executeAddItem(order, product, new BigDecimal("12.50"), 1).startsWith("Erro"));
        assertTrue(controller.executeChangeItemQuantity(order, product, 2).startsWith("Erro"));
        assertTrue(controller.executeRemoveItem(order, product).startsWith("Erro"));
        assertEquals(9, stock());
        assertEquals(1, view().items().get(0).quantity());
        orders.pay(order);
        assertThrows(IllegalStateException.class, () -> orders.pay(order));
        orders.deliver(order);
        assertThrows(IllegalStateException.class, () -> orders.deliver(order));
    }

    @Test void useCasesDoNotDuplicateGatewayEvents() {
        controller.executeAddItem(order, product, new BigDecimal("12.50"), 1);
        orders.finalizeOrder(order);
        new ProcessPaymentUseCase(orders, bus).execute(order);
        new MarkAsDeliveredUseCase(orders, bus).execute(order);
        assertEquals(List.of("awaiting", "paid", "delivered"), events);
        assertEquals(List.of(order, order, order), eventOrderIds);
    }

    @Test void rejectsUnknownCustomersOrdersAndInvalidAmounts() {
        assertTrue(controller.executeCreateOrder(UUID.randomUUID()).isEmpty());
        assertTrue(controller.executeAddItem(UUID.randomUUID(), product, BigDecimal.ONE, 1).startsWith("Erro"));
        assertThrows(IllegalArgumentException.class, () -> orders.addItem(order, product, "Produto", new BigDecimal("0.001"), 1));
        assertThrows(IllegalArgumentException.class, () -> orders.addItem(order, product, "Produto", BigDecimal.ONE, 0));
        assertTrue(view().items().isEmpty());
        assertEquals(10, stock());
    }

    @Test void stockArithmeticCannotOverflow() {
        assertThrows(IllegalArgumentException.class, () -> products.updateStock(product, Integer.MAX_VALUE));
        assertEquals(10, stock());
    }

    OrderView view() { return orders.getOrder(order).orElseThrow(); }
    int stock() { return products.getProduct(product).orElseThrow().stock(); }
}
