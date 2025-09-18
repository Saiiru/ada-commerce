package com.ada.commerce.service.event;

import java.util.function.Consumer;

public interface EventPublisher {
  <E> void publish(E event);
  <E> void subscribe(Class<E> type, Consumer<E> handler);
}

