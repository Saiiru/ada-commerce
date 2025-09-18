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
    private UUID currentCustomer = null;

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
            displayCurrentContext();
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println(" 1) Clientes");
            System.out.println(" 2) Produtos");
            System.out.println(" 3) Pedidos");
            System.out.println(" 0) Sair");
            System.out.print("Escolha: ");

            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1" -> handleCustomerMenu();
                    case "2" -> handleProductMenu();
                    case "3" -> handleOrderMenu();
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

    private void displayCurrentContext() {
        System.out.println("\n--- CONTEXTO ATUAL ---");
        String customerName = "Nenhum";
        if (currentCustomer != null) {
            Optional<CustomerView> custOpt = customerController.getCustomer(currentCustomer);
            customerName = custOpt.map(CustomerView::name).orElse("Desconhecido");
        }
        System.out.println("Cliente Selecionado: " + customerName + (currentCustomer != null ? " (ID: " + currentCustomer.toString().substring(0,8) + "...)" : ""));

        String orderInfo = "Nenhum";
        if (currentOrder != null) {
            Optional<OrderView> orderOpt = orderController.getOrder(currentOrder);
            orderInfo = orderOpt.map(o -> "ID: " + o.id().toString().substring(0,8) + "... | Status: " + o.orderStatus() + " | Total: R$ " + o.total().setScale(2, BigDecimal.ROUND_HALF_EVEN)).orElse("Desconhecido");
        }
        System.out.println("Pedido Atual: " + orderInfo);
        System.out.println("----------------------");
    }

    private void handleCustomerMenu() {
        while (true) {
            displayCurrentContext();
            System.out.println("\n=== MENU CLIENTES ===");
            System.out.println(" 1) Cadastrar cliente");
            System.out.println(" 2) Listar clientes");
            System.out.println(" 3) Selecionar cliente atual");
            System.out.println(" 0) Voltar");
            System.out.print("Escolha: ");

            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1" -> cadastrarCliente();
                    case "2" -> listarClientes();
                    case "3" -> selecionarClienteAtual();
                    case "0" -> { return; }
                    default -> System.out.println("Opcao invalida.");
                }
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
        else all.forEach(c -> System.out.printf("ID: %s | Nome: %s | Documento: %s | Email: %s%n", c.id().toString().substring(0,8), c.name(), c.document(), (c.email()==null?"-":c.email())));
    }

    private void selecionarClienteAtual() {
        System.out.print("Cliente (UUID | documento | parte do nome): ");
        String input = sc.nextLine().trim();
        UUID resolvedId = resolveCustomerId(input);
        if (resolvedId != null) {
            currentCustomer = resolvedId;
            System.out.println("Cliente " + customerController.getCustomer(currentCustomer).map(CustomerView::name).orElse("Desconhecido") + " selecionado.");
        }
    }

    private void handleProductMenu() {
        while (true) {
            displayCurrentContext();
            System.out.println("\n=== MENU PRODUTOS ===");
            System.out.println(" 1) Cadastrar produto");
            System.out.println(" 2) Listar produtos");
            System.out.println(" 3) Atualizar estoque");
            System.out.println(" 0) Voltar");
            System.out.print("Escolha: ");

            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1" -> cadastrarProduto();
                    case "2" -> listarProdutos();
                    case "3" -> atualizarEstoque();
                    case "0" -> { return; }
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
            }
        }
    }

    private void cadastrarProduto() {
        System.out.print("Nome do produto: "); String name = sc.nextLine().trim();
        System.out.print("Preco base (ex 19.90): "); BigDecimal price = new BigDecimal(sc.nextLine().trim());
        System.out.print("Estoque inicial: "); int stock = Integer.parseInt(sc.nextLine().trim());
        Response res = productController.executeCreateProduct(name, price, stock);
        System.out.println(res.getMessage());
    }

    private void listarProdutos() {
        List<ProductView> all = productController.listProducts();
        if (all.isEmpty()) System.out.println("(vazio)");
        else all.forEach(p -> System.out.printf("ID: %s | Nome: %s | Preço Base: %.2f | Estoque: %d%n", p.id().toString().substring(0,8), p.name(), p.basePrice(), p.stock()));
    }

    private void atualizarEstoque() {
        listarProdutos();
        System.out.print("Produto (UUID | parte do nome) para atualizar estoque: ");
        UUID pid = resolveProductId();
        if (pid == null) return;
        System.out.print("Quantidade a adicionar/remover (ex: 10 para adicionar, -5 para remover): ");
        int quantityChange = Integer.parseInt(sc.nextLine().trim());
        Response res = productController.executeUpdateStock(pid, quantityChange);
        System.out.println(res.getMessage());
    }

    private void handleOrderMenu() {
        while (true) {
            displayCurrentContext();
            System.out.println("\n=== MENU PEDIDOS ===");
            System.out.println(" 1) Criar pedido");
            System.out.println(" 2) Adicionar item ao pedido atual");
            System.out.println(" 3) Alterar quantidade de item no pedido atual");
            System.out.println(" 4) Remover item do pedido atual");
            System.out.println(" 5) Finalizar pedido atual");
            System.out.println(" 6) Pagar pedido atual");
            System.out.println(" 7) Entregar pedido atual");
            System.out.println(" 8) Ver pedido atual");
            System.out.println(" 9) Listar todos os pedidos");
            System.out.println("10) Listar pedidos por cliente");
            System.out.println("11) Selecionar pedido atual");
            System.out.println(" 0) Voltar");
            System.out.print("Escolha: ");

            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1" -> criarPedido();
                    case "2" -> adicionarItemAoPedido();
                    case "3" -> alterarQuantidadeItem();
                    case "4" -> removerItemDoPedido();
                    case "5" -> finalizarPedido();
                    case "6" -> pagarPedido();
                    case "7" -> entregarPedido();
                    case "8" -> verPedidoAtual();
                    case "9" -> listarTodosPedidos();
                    case "10" -> listarPedidosPorCliente();
                    case "11" -> selecionarPedidoAtual();
                    case "0" -> { return; }
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
            }
        }
    }

    private void criarPedido() {
        if (currentCustomer == null) {
            System.out.println("Selecione um cliente primeiro (Menu Clientes -> Selecionar cliente atual).");
            return;
        }
        Optional<UUID> orderIdOpt = orderController.executeCreateOrder(currentCustomer);
        if (orderIdOpt.isPresent()) {
            currentOrder = orderIdOpt.get();
            System.out.println("Pedido criado: " + currentOrder);
        } else {
            System.out.println("Erro ao criar pedido. Cliente não encontrado ou erro interno.");
        }
    }

    private void adicionarItemAoPedido() {
        if (currentOrder == null) { System.out.println("Selecione um pedido primeiro (Menu Pedidos -> Selecionar pedido atual ou Criar pedido)."); return; }
        listarProdutos();
        UUID pid = resolveProductId();
        if (pid == null) return;
        Optional<ProductView> pv = productController.getProduct(pid);
        if (pv.isEmpty()) { System.out.println("Produto nao encontrado."); return; }
        System.out.printf("Preco de venda (snapshot) [sugestão: %.2f]: ", pv.get().basePrice());
        String unitInput = sc.nextLine().trim();
        BigDecimal unit = unitInput.isEmpty() ? pv.get().basePrice() : new BigDecimal(unitInput);
        System.out.print("Quantidade (Estoque disponível: " + pv.get().stock() + "): "); int qty = Integer.parseInt(sc.nextLine().trim());

        if (qty > pv.get().stock()) {
            System.out.println("Erro: Quantidade solicitada excede o estoque disponível.");
            return;
        }

        String msg = orderController.executeAddItem(currentOrder, pid, unit, qty);
        System.out.println(msg);

        if (msg.equals("Item adicionado ao pedido.")) {
            productController.executeUpdateStock(pid, -qty); // Diminui o estoque
        }
    }

    private void alterarQuantidadeItem() {
        if (currentOrder == null) { System.out.println("Selecione um pedido primeiro."); return; }
        verPedidoAtual();
        UUID pid = resolveProductId();
        if (pid == null) return;
        System.out.print("Nova quantidade: "); int qty = Integer.parseInt(sc.nextLine().trim());
        String msg = orderController.executeChangeItemQuantity(currentOrder, pid, qty);
        System.out.println(msg);
    }

    private void removerItemDoPedido() {
        if (currentOrder == null) { System.out.println("Selecione um pedido primeiro."); return; }
        verPedidoAtual();
        UUID pid = resolveProductId();
        if (pid == null) return;
        String msg = orderController.executeRemoveItem(currentOrder, pid);
        System.out.println(msg);
    }

    private void finalizarPedido() {
        if (currentOrder == null) { System.out.println("Selecione um pedido primeiro."); return; }
        String msg = orderController.executeFinalizeOrder(currentOrder);
        System.out.println(msg);
    }

    private void pagarPedido() {
        if (currentOrder == null) { System.out.println("Selecione um pedido primeiro."); return; }
        String msg = orderController.executePayOrder(currentOrder);
        System.out.println(msg);
    }

    private void entregarPedido() {
        if (currentOrder == null) { System.out.println("Selecione um pedido primeiro."); return; }
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
        System.out.println("ID do Pedido: " + o.id().toString().substring(0,8) + "...");
        System.out.println("Cliente: " + customerOpt.map(CustomerView::name).orElse("Desconhecido") + " (" + customerOpt.map(CustomerView::document).orElse("N/A") + ")");
        System.out.println("Status: " + o.orderStatus());
        System.out.println("Status Pagamento: " + o.paymentStatus());
        System.out.println("Total: R$ " + o.total().setScale(2, BigDecimal.ROUND_HALF_EVEN));

        if (o.items().isEmpty()) {
            System.out.println("  (Nenhum item no pedido)");
        } else {
            System.out.println("Itens:");
            o.items().forEach(item -> {
                System.out.printf("  - %s (ID: %s...) | Qtd: %d | Preço Unit.: R$ %.2f | Subtotal: R$ %.2f%n",
                        item.name(), item.productId().toString().substring(0,8), item.quantity(), item.unitPrice(), item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())).setScale(2, BigDecimal.ROUND_HALF_EVEN));
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
            System.out.printf("ID: %s... | Cliente: %s | Status: %s | Total: R$ %.2f%n",
                    o.id().toString().substring(0,8), customerOpt.map(CustomerView::name).orElse("Desconhecido"), o.orderStatus(), o.total().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        });
        System.out.println("------------------------");
    }

    private void listarPedidosPorCliente() {
        System.out.println("\n--- LISTAR PEDIDOS POR CLIENTE ---");
        UUID customerId = resolveCustomerId(sc.nextLine().trim()); // Reutiliza resolveCustomerId
        if (customerId == null) return;

        List<OrderView> customerOrders = orderController.listOrdersByCustomer(customerId);
        if (customerOrders.isEmpty()) {
            System.out.println("(Nenhum pedido encontrado para este cliente)");
            return;
        }
        Optional<CustomerView> customerOpt = customerController.getCustomer(customerId);
        System.out.printf("\n--- PEDIDOS DO CLIENTE: %s (%s) ---\n", customerOpt.map(CustomerView::name).orElse("Desconhecido"), customerId);
        customerOrders.forEach(o -> {
            System.out.printf("ID: %s... | Status: %s | Total: R$ %.2f%n",
                    o.id().toString().substring(0,8), o.orderStatus(), o.total().setScale(2, BigDecimal.ROUND_HALF_EVEN));
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

    private UUID resolveCustomerId(String input) {
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
            System.out.printf(" %d) %s | %s | ID: %s...%n", i+1, c.name(), c.document(), c.id().toString().substring(0,8));
        }
        System.out.print("Escolha [1-" + matches.size() + "]: ");
        int idx = Integer.parseInt(sc.nextLine().trim());
        if (idx < 1 || idx > matches.size()) {
            System.out.println("Indice inválido.");
            return null;
        }
        return matches.get(idx-1).id();
    }

    private UUID resolveProductId() {
        System.out.print("Produto (UUID | parte do nome): ");
        String input = sc.nextLine().trim();

        // tenta UUID
        try { return UUID.fromString(input); } catch (Exception ignore) {}

        // tenta nome (lista se múltiplos)
        var matches = productController.findByName(input);
        if (matches.isEmpty()) {
            System.out.println("Produto não encontrado.");
            return null;
        }

        if (matches.size() == 1) return matches.get(0).id();

        System.out.println("Varios produtos encontrados:");
        for (int i=0;i<matches.size();i++) {
            var p = matches.get(i);
            System.out.printf(" %d) %s | Preço Base: %.2f | ID: %s...%n", i+1, p.name(), p.basePrice(), p.id().toString().substring(0,8));
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
