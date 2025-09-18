package com.ada.commerce.service;

import com.ada.commerce.model.Customer;
import com.ada.commerce.model.Order;
import com.ada.commerce.model.OrderStatus;
import com.ada.commerce.repository.OrderRepository;
import com.ada.commerce.service.ports.CreateChargeService;
import com.ada.commerce.service.ports.NotificationService;
import com.ada.commerce.utils.PricingService;

import java.math.BigDecimal;

public class FinalizeOrderServiceImpl implements FinalizeOrderService {

  private OrderRepository repository;
  private NotificationService notificationService;
  private PricingService pricingService;
  private CreateChargeService createChargeService;

  public FinalizeOrderServiceImpl(OrderRepository repository,
                                  NotificationService notificationService,
                                  PricingService pricingService,
                                  CreateChargeService createChargeService
                                  ) {
    this.repository = repository;
    this.notificationService = notificationService;
    this.pricingService = pricingService;
    this.createChargeService = createChargeService;
  }

  @Override
  public Order execute(Order order) {
    if (order.getStatus() != OrderStatus.ABERTO) {
      throw new IllegalStateException("Não é possível finalizar um pedido " + order.getStatus());
    }
    if (order.getItems().isEmpty()) {
      throw new IllegalStateException("Não é possível finalizar um pedido sem itens");
    }
    if (pricingService.calculateTotal(order).compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalStateException("Só é possível finalizar pedidos com total maior que zero");
    }

    createChargeService.createCharge(order.getId());

    Order finalizedOrder = order.changeStatus(OrderStatus.AGUARDANDO_PAGAMENTO);
    repository.update(finalizedOrder);

    Customer owner = finalizedOrder.getOwner();
    notificationService.onAwaitingPayment(order.getId());

    return finalizedOrder;
  }
}