package com.ada.commerce.service.registry;

import com.ada.commerce.service.event.EventPublisher;
import com.ada.commerce.service.ports.*;
import com.ada.commerce.service.ports.nulls.NullCustomerGateway;
import com.ada.commerce.service.ports.nulls.NullOrderGateway;
import com.ada.commerce.service.ports.nulls.NullProductGateway;
import com.ada.commerce.service.time.ClockProvider;

import java.util.Objects;

public final class ServiceRegistry {
  private static EventPublisher bus;
  private static ClockProvider clock;
  private static NotificationService notifier;

  private static CustomerGateway customer = new NullCustomerGateway();
  private static ProductGateway  product  = new NullProductGateway();
  private static OrderGateway    order    = new NullOrderGateway();

  private ServiceRegistry() {}

  public static void init(EventPublisher eventBus, ClockProvider systemClock, NotificationService notification) {
    bus = Objects.requireNonNull(eventBus);
    clock = Objects.requireNonNull(systemClock);
    notifier = Objects.requireNonNull(notification);
  }

  public static void register(CustomerGateway g) { customer = Objects.requireNonNull(g); }
  public static void register(ProductGateway g)  { product  = Objects.requireNonNull(g); }
  public static void register(OrderGateway g)    { order    = Objects.requireNonNull(g); }

  public static CustomerGateway customer() { return customer; }
  public static ProductGateway  product()  { return product; }
  public static OrderGateway    order()    { return order; }

  public static EventPublisher bus() { return bus; }
  public static ClockProvider  clock() { return clock; }
  public static NotificationService notifier() { return notifier; }
}

