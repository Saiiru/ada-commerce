package com.ada.commerce.service.impl.memory;

import com.ada.commerce.service.event.EventPublisher;
import com.ada.commerce.service.ports.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryOrderGateway implements OrderGateway {
  private static final String OPEN = "OPEN";
  private static final String AWAITING = "AWAITING_PAYMENT";
  private static final String PAID = "PAID";
  private static final String DELIVERED = "DELIVERED";

  private final Map<UUID, OrderState> byId = new ConcurrentHashMap<>();
  private final EventPublisher events;

  public InMemoryOrderGateway(EventPublisher events) {
    this.events = Objects.requireNonNull(events);
  }

  @Override
  public UUID createOrder(UUID customerId) {
    var id = UUID.randomUUID();
    byId.put(id, new OrderState(id, customerId));
    return id;
  }

  @Override
  public void addItem(UUID orderId, UUID productId, String productNameSnapshot, BigDecimal unitPriceSnapshot, int quantity) {
    var o = getState(orderId);
    ensureOpen(o);
    if (unitPriceSnapshot == null || unitPriceSnapshot.signum() <= 0) throw new IllegalArgumentException("Preco unitario deve ser > 0");
    if (quantity < 1) throw new IllegalArgumentException("Quantidade deve ser >= 1");
    o.items.put(productId, new Item(productId, productNameSnapshot, unitPriceSnapshot, quantity));
  }

  @Override
  public void changeItemQuantity(UUID orderId, UUID productId, int quantity) {
    var o = getState(orderId);
    ensureOpen(o);
    var it = o.items.get(productId);
    if (it == null) throw new NoSuchElementException("Item nao encontrado");
    if (quantity < 1) throw new IllegalArgumentException("Quantidade deve ser >= 1");
    o.items.put(productId, new Item(it.productId, it.name, it.unitPrice, quantity));
  }

  @Override
  public void removeItem(UUID orderId, UUID productId) {
    var o = getState(orderId);
    ensureOpen(o);
    if (o.items.remove(productId) == null) throw new NoSuchElementException("Item nao encontrado");
  }

  @Override
  public void finalizeOrder(UUID orderId) {
    var o = getState(orderId);
    ensureOpen(o);
    if (o.items.isEmpty()) throw new IllegalStateException("Pedido sem itens");
    if (o.total().signum() <= 0) throw new IllegalStateException("Total deve ser > 0");
    o.orderStatus = AWAITING; // Define o status do pedido como aguardando pagamento
    o.paymentStatus = AWAITING;
    events.publish(new OrderEvents.AwaitingPayment(orderId));
  }

  @Override
  public void pay(UUID orderId) {
    var o = getState(orderId);
    if (!AWAITING.equals(o.paymentStatus)) throw new IllegalStateException("Pedido nao esta aguardando pagamento");
    o.paymentStatus = PAID;
  }

  @Override
  public void deliver(UUID orderId) {
    var o = getState(orderId);
    if (!PAID.equals(o.paymentStatus)) throw new IllegalStateException("Pedido nao esta pago");
    o.orderStatus = DELIVERED; // seguindo enum unico do MVP
  }

  @Override
  public Optional<OrderView> getOrder(UUID orderId) {
    var o = byId.get(orderId);
    return Optional.ofNullable(o).map(OrderState::toView);
  }

  @Override
  public List<OrderView> listOrdersByCustomer(UUID customerId) {
    var out = new ArrayList<OrderView>();
    for (var st : byId.values()) {
      if (Objects.equals(st.customerId, customerId)) out.add(st.toView());
    }
    return out;
  }

  @Override
  public List<OrderView> listAllOrders() {
    var out = new ArrayList<OrderView>();
    for (var st : byId.values()) {
      out.add(st.toView());
    }
    return out;
  }

  // ---------- helpers ----------

  private OrderState getState(UUID id) {
    var o = byId.get(id);
    if (o == null) throw new NoSuchElementException("Pedido nao encontrado");
    return o;
  }

  private static void ensureOpen(OrderState o) {
    if (!OPEN.equals(o.orderStatus)) throw new IllegalStateException("Pedido nao esta OPEN");
  }

  private static final class OrderState {
    final UUID id;
    final UUID customerId;
    String orderStatus = OPEN;
    String paymentStatus = "NONE";
    final Map<UUID, Item> items = new LinkedHashMap<>();

    OrderState(UUID id, UUID customerId) {
      this.id = id;
      this.customerId = customerId;
    }

    BigDecimal total() {
      return items.values().stream()
        .map(i -> i.unitPrice.multiply(BigDecimal.valueOf(i.quantity)))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    OrderView toView() {
      var list = new ArrayList<OrderItemView>();
      for (var it : items.values()) {
        list.add(new OrderItemView(it.productId, it.name, it.unitPrice, it.quantity));
      }
      return new OrderView(id, customerId, orderStatus, paymentStatus, list, total());
    }
  }

  private record Item(UUID productId, String name, BigDecimal unitPrice, int quantity) {}
}
