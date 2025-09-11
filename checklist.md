## O que é DoD

**DoD = Definition of Done**. Critérios objetivos para considerar a tarefa concluída:

- Compila e roda (`./gradlew run`) sem erros.
- Regras atendidas e bloqueios corretos (não permite o que não pode).
- Integra com os demais via interfaces previstas.
- Mensagens no console nas transições exigidas (quando aplicável).
- Sem dependências externas além do Java.

---

## Regras fixas do MVP (valem para todos)

- **Cliente**: documento (CPF/CNPJ) **válido e único**.
- **Produto**: apenas `id`, `name`, `basePrice>0` (sem SKU no MVP).
- **Pedido**: nasce `OPEN` com `createdAt`.
- **Itens**: só mudam em `OPEN`. Preço do item é **snapshot** informado no item.
- **Fluxo**: `OPEN → AWAITING_PAYMENT → PAID → FINISHED`.
- **Notificações**: console ao **finalizar**, **pagar**, **entregar**.

---

## Todos — base comum (não tem “dono”, qualquer um pode abrir PR)

- [ ] **VOs**: `Money` (2 casas, HALF_EVEN), `Document` (CPF/CNPJ sanitizado e válido), `Email` (válido).
- [ ] **Enums**: `OrderStatus {OPEN, FINISHED}`, `PaymentStatus {NONE, AWAITING_PAYMENT, PAID}`.
- [ ] **Eventos**: `OrderAwaitingPayment`, `OrderPaid`, `OrderDelivered`.
- [ ] **Ports**: `CustomerRepository`, `ProductRepository`, `OrderRepository`.
- [ ] **Repos in-memory**: estruturas thread-safe; índice por **document** (cliente).

**DoD base**: compila; VOs validam; enums/eventos disponíveis; ports/repo funcionam em memória.

---

## Antonio Carlos — Customer

**Objetivo**: CRUD sem delete, documento obrigatório e único.

**Entregar**

- [ ] **Entity**: `Customer { id, name, document, email?, createdAt, active=true }`.
- [ ] **Port**: `CustomerRepository { save, findById, findByDocument, findAll, existsByDocument }`.
- [ ] **Repo memória**: índice por `document` (bloqueio de duplicados).
- [ ] **Use cases (execute)**:
  - `CreateCustomerUseCase` — rejeita documento inválido/duplicado.
  - `UpdateCustomerUseCase` — altera `name/email`; **não** altera `document`.
  - `ListCustomersUseCase`
  - `GetCustomerUseCase`

**DoD Customer**

- Criação falha sem documento válido ou se duplicado.
- Atualização só muda `name/email`.
- List/Get retornam dados consistentes.

---

## Thiago — Product

**Objetivo**: CRUD sem delete, sem SKU, `basePrice > 0`.

**Entregar**

- [ ] **Entity**: `Product { id, name, basePrice>0, createdAt, active=true }`.
- [ ] **Port**: `ProductRepository { save, findById, findAll }`.
- [ ] **Repo memória**.
- [ ] **Use cases (execute)**:
  - `CreateProductUseCase` — rejeita `basePrice <= 0`.
  - `UpdateProductUseCase` — altera `name/basePrice` (mantém `> 0`).
  - `ListProductsUseCase`
  - `GetProductUseCase`

**DoD Product**

- Criação/atualização respeitam `basePrice > 0`.
- List/Get funcionam.

---

## Carlúcio — Order

**Objetivo**: itens em `OPEN`, transições corretas, total por snapshot.

**Entregar**

- [ ] **Entities**:
  - `Order { id, customerId, status=OPEN, payment=NONE, createdAt, items[] }`
  - `OrderItem { productId, nameSnapshot, unitPriceSnapshot>0, quantity≥1 }`
- [ ] **Port**: `OrderRepository { save, findById, findAllByCustomerId }`.
- [ ] **Repo memória**.
- [ ] **Use cases (execute)**:
  - `CreateOrderUseCase` — cria `OPEN` com `createdAt`.
  - `AddOrderItemUseCase` — `qty≥1`, `price>0`, grava snapshot nome/preço.
  - `ChangeOrderItemQuantityUseCase` — **somente `OPEN`**, `qty≥1`.
  - `RemoveOrderItemUseCase` — **somente `OPEN`**.
  - `FinalizeOrderUseCase` — **≥1 item** e **total>0** → `AWAITING_PAYMENT` + `OrderAwaitingPayment`.
  - `PayOrderUseCase` — **somente `AWAITING_PAYMENT`** → `PAID` + `OrderPaid`.
  - `DeliverOrderUseCase` — **somente `PAID`** → `FINISHED` + `OrderDelivered`.
  - `GetOrderUseCase`, `ListOrdersByCustomerUseCase`.

**DoD Order**

- Não permite alterar itens fora de `OPEN`.
- Total = soma(`unitPriceSnapshot * quantity`).
- Transições obedecem o fluxo; dispara eventos corretos.

---

## Urias — Infra + Payment + Delivery + Notification + CLI

**Objetivo**: ligar os módulos e mostrar o fluxo no console.

**Entregar**

- [ ] **Publisher** in-memory: `publish(event)` chama assinantes síncronos.
- [ ] **EmailNotifier**: imprime mensagens no console nos 3 eventos.
- [ ] **Payment**
  - `ProcessPaymentUseCase` — valida `AWAITING_PAYMENT` → muda para `PAID` → publica `OrderPaid`.
    _(Se já coberto por `PayOrderUseCase` do Order, não duplicar. Apenas orquestrar via publisher/notifier.)_
- [ ] **Delivery**
  - `MarkAsDeliveredUseCase` — valida `PAID` → muda para `FINISHED` → publica `OrderDelivered`.
    _(Se já coberto por `DeliverOrderUseCase` do Order, só orquestrar.)_
- [ ] **Clock/UUID**: usar `Clock.systemUTC()` e `UUID.randomUUID()` (Java padrão).
- [] **CLI** (fluxo feliz):
  - Cria 1 cliente e 2 produtos.
  - Cria pedido, adiciona/altera/remove itens em `OPEN`.
  - Finaliza → console “Aguardando pagamento”.
  - Paga → “Pagamento confirmado”.
  - Entrega → “Pedido entregue”.
  - Exibe status final = `FINISHED` e total.

**DoD Infra/CLI**

- Eventos publicados chamam o notifier no console.
- `./gradlew run` executa o fluxo feliz do início ao fim.

---
