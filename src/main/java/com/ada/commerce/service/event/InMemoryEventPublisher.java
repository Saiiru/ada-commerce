package com.ada.commerce.service.event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class InMemoryEventPublisher implements EventPublisher {
  private final Map<Class<?>, List<Consumer<?>>> handlers = new ConcurrentHashMap<>();

  @Override public <E> void publish(E event) {
    if (event == null) return;
    var list = handlers.getOrDefault(event.getClass(), List.of());
    for (Consumer<?> h : list) {
      @SuppressWarnings("unchecked") Consumer<E> typed = (Consumer<E>) h;
      typed.accept(event);
    }
  }

  @Override public <E> void subscribe(Class<E> type, Consumer<E> handler) {
    handlers.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(handler);
  }
}

