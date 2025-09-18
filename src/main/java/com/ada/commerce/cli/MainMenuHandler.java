package com.ada.commerce.cli;

import com.ada.commerce.controller.*;
import com.ada.commerce.service.ports.CustomerView;
import com.ada.commerce.service.ports.ProductView;
import com.ada.commerce.service.ports.OrderView;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

/**
 * Manipulador do menu principal da CLI.
 * <p>
 * Responsável por interagir diretamente com o usuário (entrada/saída),
 * exibir o menu, ler as opções e delegar as chamadas aos controllers apropriados.
 * Mantém o estado do pedido atual para facilitar o fluxo.
 */
public class MainMenuHandler {
    private final Scanner sc;
    private final ProductController productController;
    private final CustomerController customerController;
    private final OrderController orderController;
    private UUID currentOrder = null;

    /**
     * Construtor do MainMenuHandler.
     * @param sc O Scanner para leitura da entrada do usuário.
     * @param pc O ProductController para operações de produto.
     * @param cc O CustomerController para operações de cliente.
     * @param oc O OrderController para operações de pedido.
     */
    public MainMenuHandler(Scanner sc, ProductController pc, CustomerController cc, OrderController oc) {
        this.sc = sc;
        this.productController = pc;
        this.customerController = cc;
        this.orderController = oc;
    }

    /**
     * Inicia o loop principal do menu da CLI, exibindo opções e processando a entrada do usuário.
     */
    public void run() {
        System.out.println("Ada Commerce CLI");
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
            System.out.println("13) Listar todos os pedidos");
            System.out.println("14) Listar pedidos por cliente");
            System.out.println("15) Selecionar pedido atual");
            System.out.println("0) Sair");
            System.out.print("Escolha: ");

            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1" -> cadastrarCliente();
                    case "2" -> listarClientes();
                    case "3" -> cadastrarProduto();
                    case "4" -> listarProdutos();
                    case "5" -> criarPedido();
                    case "6" -> adicionarItemAoPedido();
                    case "7" -> alterarQuantidadeItem();
                    case "8" -> removerItemDoPedido();
                    case "9" -> finalizarPedido();
                    case "10" -> pagarPedido();
                    case "11" -> entregarPedido();
                    case "12" -> verPedidoAtual();
                    case "13" -> listarTodosPedidos();
                    case "14" -> listarPedidosPorCliente();
                    case "15" -> selecionarPedidoAtual();
                    case "0" -> { System.out.println("Fim."); return; }
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (UnsupportedOperationException e) {
                System.out.println("Funcao indisponivel: " + e.getMessage());
            } catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
            }
        }
    }

    private void cadastrarCliente() {
        System.out.print("Nome: "); String name = sc.nextLine().trim();
        System.out.print("Documento (CPF/CNPJ): "); String doc = sc.nextLine().trim();
        System.out.print("Email (opcional): "); String email = sc.nextLine().trim();
        Response res = customerController.executeCreateCustomer(name, doc, email);
        System.out.println(res.getMessage());
    }

    private void listarClientes() {
        List<CustomerView> all = customerController.listCustomers();
        if (all.isEmpty()) System.out.println("(vazio)");
        else all.forEach(c -> System.out.println(c.id()+" | "+c.name()+" | "+c.document()+" | "+(c.email()==null?"-":c.email())));
    }

    private void cadastrarProduto() {
        System.out.print("Nome do produto: "); String name = sc.nextLine().trim();
        System.out.print("Preco base (ex 19.90): "); BigDecimal price = new BigDecimal(sc.nextLine().trim());
        Response res = productController.executeCreateProduct(name, price);
        System.out.println(res.getMessage());
    }

    private void listarProdutos() {
        List<ProductView> all = productController.listProducts();
        if (all.isEmpty()) System.out.println("(vazio)");
        else all.forEach(p -> System.out.println(p.id()+" | "+p.name()+" | base="+p.basePrice()));
    }

    private void criarPedido() {
        UUID customerId = resolveCustomerId();
        if (customerId == null) return;
        Optional<UUID> orderIdOpt = orderController.executeCreateOrder(customerId);
        if (orderIdOpt.isPresent()) {
            currentOrder = orderIdOpt.get();
            System.out.println("Pedido criado: " + currentOrder);
        } else {
            System.out.println("Erro ao criar pedido. Cliente não encontrado.");
        }
    }

    private void adicionarItemAoPedido() {
        if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); return; }
        listarProdutos();
        System.out.print("Produto ID: "); UUID pid = UUID.fromString(sc.nextLine().trim());
        System.out.print("Preco de venda (snapshot): "); BigDecimal unit = new BigDecimal(sc.nextLine().trim());
        System.out.print("Quantidade: "); int qty = Integer.parseInt(sc.nextLine().trim());
        String msg = orderController.executeAddItem(currentOrder, pid, unit, qty);
        System.out.println(msg);
    }

    private void alterarQuantidadeItem() {
        if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); return; }
        verPedidoAtual();
        System.out.print("Produto ID do item a alterar: "); UUID pid = UUID.fromString(sc.nextLine().trim());
        System.out.print("Nova quantidade: "); int qty = Integer.parseInt(sc.nextLine().trim());
        String msg = orderController.executeChangeItemQuantity(currentOrder, pid, qty);
        System.out.println(msg);
    }

    private void removerItemDoPedido() {
        if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); return; }
        verPedidoAtual();
        System.out.print("Produto ID do item a remover: "); UUID pid = UUID.fromString(sc.nextLine().trim());
        String msg = orderController.executeRemoveItem(currentOrder, pid);
        System.out.println(msg);
    }

    private void finalizarPedido() {
        if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); return; }
        String msg = orderController.executeFinalizeOrder(currentOrder);
        System.out.println(msg);
    }

    private void pagarPedido() {
        if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); return; }
        String msg = orderController.executePayOrder(currentOrder);
        System.out.println(msg);
    }

    private void entregarPedido() {
        if (currentOrder == null) { System.out.println("Crie um pedido primeiro."); return; }
        String msg = orderController.executeDeliverOrder(currentOrder);
        System.out.println(msg);
    }

    private void verPedidoAtual() {
        if (currentOrder == null) { System.out.println("Sem pedido atual."); return; }
        Optional<OrderView> ov = orderController.getOrder(currentOrder);
        if (ov.isEmpty()) { System.out.println("Pedido nao encontrado."); return; }
        var o = ov.get();
        Optional<CustomerView> customerOpt = customerController.getCustomer(o.customerId());

        System.out.println("\n--- DETALHES DO PEDIDO ---");
        System.out.println("ID do Pedido: " + o.id());
        System.out.println("Cliente: " + customerOpt.map(CustomerView::name).orElse("Desconhecido") + " (" + customerOpt.map(CustomerView::document).orElse("N/A") + ")");
        System.out.println("Status: " + o.orderStatus());
        System.out.println("Status Pagamento: " + o.paymentStatus());
        System.out.println("Total: R$ " + o.total().setScale(2, BigDecimal.ROUND_HALF_EVEN));

        if (o.items().isEmpty()) {
            System.out.println("  (Nenhum item no pedido)");
        } else {
            System.out.println("Itens:");
            o.items().forEach(item -> {
                System.out.printf("  - %s (ID: %s) | Qtd: %d | Preço Unit.: R$ %.2f | Subtotal: R$ %.2f%n",
                        item.name(), item.productId(), item.quantity(), item.unitPrice(), item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            });
        }
        System.out.println("--------------------------");
    }

    private void listarTodosPedidos() {
        List<OrderView> allOrders = orderController.listAllOrders();
        if (allOrders.isEmpty()) {
            System.out.println("(Nenhum pedido encontrado)");
            return;
        }
        System.out.println("\n--- TODOS OS PEDIDOS ---");
        allOrders.forEach(o -> {
            Optional<CustomerView> customerOpt = customerController.getCustomer(o.customerId());
            System.out.printf("ID: %s | Cliente: %s | Status: %s | Total: R$ %.2f%n",
                    o.id(), customerOpt.map(CustomerView::name).orElse("Desconhecido"), o.orderStatus(), o.total().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        });
        System.out.println("------------------------");
    }

    private void listarPedidosPorCliente() {
        UUID customerId = resolveCustomerId();
        if (customerId == null) return;

        List<OrderView> customerOrders = orderController.listOrdersByCustomer(customerId);
        if (customerOrders.isEmpty()) {
            System.out.println("(Nenhum pedido encontrado para este cliente)");
            return;
        }
        Optional<CustomerView> customerOpt = customerController.getCustomer(customerId);
        System.out.printf("\n--- PEDIDOS DO CLIENTE: %s (%s) ---\n", customerOpt.map(CustomerView::name).orElse("Desconhecido"), customerId);
        customerOrders.forEach(o -> {
            System.out.printf("ID: %s | Status: %s | Total: R$ %.2f%n",
                    o.id(), o.orderStatus(), o.total().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        });
        System.out.println("----------------------------------");
    }

    private void selecionarPedidoAtual() {
        System.out.print("Digite o ID do pedido que deseja tornar o atual: ");
        String input = sc.nextLine().trim();
        try {
            UUID orderId = UUID.fromString(input);
            Optional<OrderView> orderOpt = orderController.getOrder(orderId);
            if (orderOpt.isPresent()) {
                currentOrder = orderId;
                System.out.println("Pedido " + currentOrder + " selecionado como atual.");
            } else {
                System.out.println("Pedido não encontrado.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ID de pedido inválido.");
        }
    }

    private UUID resolveCustomerId() {
        System.out.print("Cliente (UUID | documento | parte do nome): ");
        String input = sc.nextLine().trim();

        // tenta UUID
        try { return UUID.fromString(input); } catch (Exception ignore) {}

        // tenta documento
        var byDoc = customerController.findByDocument(input);
        if (byDoc.isPresent()) return byDoc.get().id();

        // tenta nome (lista se múltiplos)
        var matches = customerController.findByName(input);
        if (matches.isEmpty()) {
            System.out.println("Cliente não encontrado.");
            return null;
        }

        if (matches.size() == 1) return matches.get(0).id();

        System.out.println("Varios clientes encontrados:");
        for (int i=0;i<matches.size();i++) {
            var c = matches.get(i);
            System.out.printf(" %d) %s | %s | %s%n", i+1, c.name(), c.document(), c.id());
        }
        System.out.print("Escolha [1-" + matches.size() + "]: ");
        int idx = Integer.parseInt(sc.nextLine().trim());
        if (idx < 1 || idx > matches.size()) {
            System.out.println("Indice inválido.");
            return null;
        }
        return matches.get(idx-1).id();
    }
}