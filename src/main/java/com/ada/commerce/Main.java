package com.ada.commerce;

import com.ada.commerce.service.delivery.MarkAsDeliveredUseCase;
import com.ada.commerce.service.event.EventPublisher;
import com.ada.commerce.service.event.InMemoryEventPublisher;
import com.ada.commerce.service.notification.ConsoleEmailNotifier;
import com.ada.commerce.service.ports.*;
import com.ada.commerce.service.registry.ServiceRegistry;
import com.ada.commerce.service.time.ClockProvider;
import com.ada.commerce.service.time.SystemClockProvider;
import com.ada.commerce.service.impl.memory.InMemoryCustomerGateway;
import com.ada.commerce.service.impl.memory.InMemoryOrderGateway;
import com.ada.commerce.service.impl.memory.InMemoryProductGateway;
import com.ada.commerce.service.payment.ProcessPaymentUseCase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

/**
 * Ponto de entrada principal para a aplicação Ada Commerce CLI.
 * <p>
 * Esta classe inicializa os serviços de infraestrutura (eventos, clock, notificação),
 * configura o registro de serviços e executa o menu interativo da CLI.
 */
public class Main {
  public static void main(String[] args) {
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

    runMenu();
  }

  private static void runMenu() {
    try (Scanner sc = new Scanner(System.in)) {
      System.out.println("Ada Commerce CLI");
      UUID currentOrder = null;

      var paymentUC = new ProcessPaymentUseCase(ServiceRegistry.order(), ServiceRegistry.bus());
      var deliveryUC = new MarkAsDeliveredUseCase(ServiceRegistry.order(), ServiceRegistry.bus());

      loop:
      while (true) {
        System.out.println("\n=== MENU ===");
        System.out.println("CLIENTE");
        System.out.println(" 1) Cadastrar cliente");
        System.out.println(" 2) Listar clientes");
        System.out.println("PRODUTO");
        System.out.println(" 3) Cadastrar produto");
        System.out.println(" 4) Listar produtos");
        System.out.println("PEDIDO");
        System.out.println(" 5) Criar pedido");
        System.out.println(" 6) Adicionar item");
        System.out.println(" 7) Alterar quantidade de item");
        System.out.println(" 8) Remover item");
        System.out.println(" 9) Finalizar");
        System.out.println("10) Pagar");
        System.out.println("11) Entregar");
        System.out.println("12) Ver pedido atual");
        System.out.println("0) Sair");
        System.out.print("Escolha: ");

        String opt = sc.nextLine().trim();
        try {
          switch (opt) {
            case "1" -> {
              System.out.print("Nome: "); String name = sc.nextLine().trim();
              System.out.print("Documento (CPF/CNPJ): "); String doc = sc.nextLine().trim();
              System.out.print("Email (opcional): "); String email = sc.nextLine().trim();
              UUID id = ServiceRegistry.customer().createCustomer(name, doc, email.isBlank()? null: email);
              System.out.println("Cliente criado: " + id);
            }
            case "2" -> {
              List<CustomerView> all = ServiceRegistry.customer().listCustomers();
              if (all.isEmpty()) System.out.println("(vazio)");
              else all.forEach(c -> System.out.println(c.id()+" | "+c.name()+" | "+c.document()+" | "+(c.email()==null?"-":c.email())));
            }
            case "3" -> {
              System.out.print("Nome do produto: "); String name = sc.nextLine().trim();
              System.out.print("Preco base (ex 19.90): "); BigDecimal price = new BigDecimal(sc.nextLine().trim());
              UUID id = ServiceRegistry.product().createProduct(name, price);
              System.out.println("Produto criado: " + id);
            }
            case "4" -> {
              List<ProductView> all = ServiceRegistry.product().listProducts();
              if (all.isEmpty()) System.out.println("(vazio)");
              else all.forEach(p -> System.out.println(p.id()+" | "+p.name()+" | base="+p.basePrice()));
            }
            case "5" -> {
              System.out.print("ID do cliente: "); UUID cid = UUID.fromString(sc.nextLine().trim());
              currentOrder = ServiceRegistry.order().createOrder(cid);
              System.out.println("Pedido criado: " + currentOrder);
            }
            case "6" -> {
              if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); break; }
              System.out.print("Produto ID: "); UUID pid = UUID.fromString(sc.nextLine().trim());
              Optional<ProductView> pv = ServiceRegistry.product().getProduct(pid);
              if (pv.isEmpty()) { System.out.println("Produto nao encontrado."); break; }
              System.out.print("Preco de venda (snapshot): "); BigDecimal unit = new BigDecimal(sc.nextLine().trim());
              System.out.print("Quantidade: "); int qty = Integer.parseInt(sc.nextLine().trim());
              ServiceRegistry.order().addItem(currentOrder, pid, pv.get().name(), unit, qty);
              System.out.println("Item adicionado.");
            }
            case "7" -> {
              if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); break; }
              System.out.print("Produto ID: "); UUID pid = UUID.fromString(sc.nextLine().trim());
              System.out.print("Nova quantidade: "); int qty = Integer.parseInt(sc.nextLine().trim());
              ServiceRegistry.order().changeItemQuantity(currentOrder, pid, qty);
              System.out.println("Quantidade alterada.");
            }
            case "8" -> {
              if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); break; }
              System.out.print("Produto ID: "); UUID pid = UUID.fromString(sc.nextLine().trim());
              ServiceRegistry.order().removeItem(currentOrder, pid);
              System.out.println("Item removido.");
            }
            case "9" -> {
              if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); break; }
              ServiceRegistry.order().finalizeOrder(currentOrder);
              System.out.println("Pedido finalizado → aguardando pagamento.");
            }
            case "10" -> {
              if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); break; }
              paymentUC.execute(currentOrder);
              System.out.println("Pagamento confirmado.");
            }
            case "11" -> {
              if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); break; }
              deliveryUC.execute(currentOrder);
              System.out.println("Pedido entregue.");
            }
            case "12" -> {
              if (currentOrder == null) { System.out.println("Sem pedido atual."); break; }
              Optional<OrderView> ov = ServiceRegistry.order().getOrder(currentOrder);
              if (ov.isEmpty()) { System.out.println("Pedido nao encontrado."); break; }
              var o = ov.get();
              System.out.println("Pedido " + o.id() + " | status=" + o.orderStatus() + " | payment=" + o.paymentStatus() + " | total=" + o.total());
              o.items().forEach(i -> System.out.println("  - "+i.productId()+" "+i.name()+" x"+i.quantity()+" @"+i.unitPrice()));
            }
            case "0" -> { System.out.println("Fim."); break loop; }
            default -> System.out.println("Opcao invalida.");
          }
        } catch (UnsupportedOperationException e) {
          System.out.println("Funcao indisponivel: " + e.getMessage());
        } catch (Exception ex) {
          System.out.println("Erro: " + ex.getMessage());
        }
      }
    }
  }
}