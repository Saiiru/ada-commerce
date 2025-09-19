# Diagramas de Sequência do Ada Commerce

> **Diagramas**: `SequenceDiagram-Pedido-Ada-Commerce.svg` e `SequenceDiagram-Entrega-Pagamento-Ada-Commerce.svg`

## Visão Geral

Os diagramas de sequência mostram a interação temporal entre objetos durante a execução de casos de uso específicos. Eles ilustram como as mensagens fluem entre as camadas da Clean Architecture.

## Diagrama 1: Fluxo de Criação e Gestão de Pedido

### Participantes
- **CLI**: Interface de linha de comando
- **OrderController**: Controlador da camada de interface
- **OrderService**: Serviço da camada de aplicação
- **CustomerService**: Serviço para validação de cliente
- **ProductService**: Serviço para validação de produto
- **OrderRepository**: Repositório de pedidos
- **EventPublisher**: Publicador de eventos

### Sequência 1: Criar Pedido

```
CLI -> OrderController: executeCreateOrder(customerId)
OrderController -> OrderService: create(customerId)
OrderService -> CustomerService: findById(customerId)
CustomerService -> CustomerRepository: findById(customerId)
CustomerRepository --> CustomerService: Optional<Customer>
CustomerService --> OrderService: Response<CustomerView>

alt Customer exists
    OrderService -> Order: new Order(customerId)
    Order --> OrderService: order
    OrderService -> OrderRepository: save(order)
    OrderRepository --> OrderService: void
    OrderService --> OrderController: Response<OrderView>
    OrderController --> CLI: Response (success)
else Customer not found
    OrderService --> OrderController: Response (error: "Cliente não encontrado")
    OrderController --> CLI: Response (error)
end
```

**Pontos Importantes**:
1. Validação de cliente antes da criação
2. Criação do pedido com status OPEN padrão
3. Persistência imediata
4. Retorno padronizado via Response

### Sequência 2: Adicionar Item ao Pedido

```
CLI -> OrderController: executeAddItem(orderId, productId, quantity)
OrderController -> OrderService: addItem(orderId, productId, quantity)
OrderService -> OrderRepository: findById(orderId)
OrderRepository --> OrderService: Optional<Order>

alt Order exists and is OPEN
    OrderService -> ProductService: findById(productId)
    ProductService -> ProductRepository: findById(productId)
    ProductRepository --> ProductService: Optional<Product>
    ProductService --> OrderService: Response<ProductView>
    
    alt Product exists
        OrderService -> Order: addItem(productId, productName, unitPrice, quantity)
        Order -> Order: validateStatus() // Must be OPEN
        Order -> Order: createOrUpdateItem()
        Order -> Order: calculateTotal()
        Order --> OrderService: void
        OrderService -> OrderRepository: save(order)
        OrderRepository --> OrderService: void
        OrderService --> OrderController: Response<OrderView>
        OrderController --> CLI: Response (success)
    else Product not found
        OrderService --> OrderController: Response (error: "Produto não encontrado")
        OrderController --> CLI: Response (error)
    end
else Order not found or not OPEN
    OrderService --> OrderController: Response (error: "Pedido inválido")
    OrderController --> CLI: Response (error)
end
```

**Pontos Importantes**:
1. Validação de existência do pedido
2. Verificação de status OPEN
3. Validação de produto
4. Snapshot de preço do produto
5. Recálculo automático do total

### Sequência 3: Finalizar Pedido

```
CLI -> OrderController: executeFinalizeOrder(orderId)
OrderController -> OrderService: finalize(orderId)
OrderService -> OrderRepository: findById(orderId)
OrderRepository --> OrderService: Optional<Order>

alt Order exists and is OPEN
    OrderService -> Order: finalize()
    Order -> Order: validateHasItems()
    
    alt Has items
        Order -> Order: setStatus(AWAITING_PAYMENT)
        Order -> Order: setPaymentStatus(AWAITING_PAYMENT)
        Order --> OrderService: void
        OrderService -> OrderRepository: save(order)
        OrderRepository --> OrderService: void
        OrderService -> EventPublisher: publish(OrderAwaitingPayment)
        EventPublisher -> NotificationService: onAwaitingPayment(orderId)
        NotificationService -> Console: "Pedido aguardando pagamento: " + orderId
        OrderService --> OrderController: Response<OrderView>
        OrderController --> CLI: Response (success)
    else No items
        OrderService --> OrderController: Response (error: "Pedido sem itens")
        OrderController --> CLI: Response (error)
    end
else Order not found or not OPEN
    OrderService --> OrderController: Response (error: "Pedido inválido")
    OrderController --> CLI: Response (error)
end
```

**Pontos Importantes**:
1. Validação de pedido com itens
2. Mudança de status sincronizada
3. Publicação de evento de domínio
4. Notificação automática
5. Desacoplamento via eventos

## Diagrama 2: Fluxo de Pagamento e Entrega

### Participantes
- **CLI**: Interface de linha de comando
- **OrderController**: Controlador da camada de interface
- **OrderService**: Serviço da camada de aplicação
- **OrderRepository**: Repositório de pedidos
- **EventPublisher**: Publicador de eventos
- **NotificationService**: Serviço de notificações

### Sequência 4: Processar Pagamento

```
CLI -> OrderController: executeProcessPayment(orderId)
OrderController -> OrderService: processPayment(orderId)
OrderService -> OrderRepository: findById(orderId)
OrderRepository --> OrderService: Optional<Order>

alt Order exists and AWAITING_PAYMENT
    OrderService -> Order: markAsPaid()
    Order -> Order: validateStatus() // Must be AWAITING_PAYMENT
    Order -> Order: setStatus(PAID)
    Order -> Order: setPaymentStatus(PAID)
    Order -> Order: recordPaymentTimestamp()
    Order --> OrderService: void
    OrderService -> OrderRepository: save(order)
    OrderRepository --> OrderService: void
    OrderService -> EventPublisher: publish(OrderPaid(orderId, now()))
    EventPublisher -> NotificationService: onPaid(orderId)
    NotificationService -> Console: "Pagamento confirmado para pedido: " + orderId
    OrderService --> OrderController: Response<OrderView>
    OrderController --> CLI: Response (success)
else Order not found or wrong status
    OrderService --> OrderController: Response (error: "Pedido não pode ser pago")
    OrderController --> CLI: Response (error)
end
```

**Pontos Importantes**:
1. Validação rigorosa de status
2. Timestamp de pagamento
3. Evento específico de pagamento
4. Notificação diferenciada

### Sequência 5: Marcar como Entregue

```
CLI -> OrderController: executeMarkAsDelivered(orderId)
OrderController -> OrderService: markAsDelivered(orderId)
OrderService -> OrderRepository: findById(orderId)
OrderRepository --> OrderService: Optional<Order>

alt Order exists and PAID
    OrderService -> Order: markAsDelivered()
    Order -> Order: validateStatus() // Must be PAID
    Order -> Order: setStatus(FINISHED)
    Order -> Order: recordDeliveryTimestamp()
    Order --> OrderService: void
    OrderService -> OrderRepository: save(order)
    OrderRepository --> OrderService: void
    OrderService -> EventPublisher: publish(OrderDelivered(orderId, now()))
    EventPublisher -> NotificationService: onDelivered(orderId)
    NotificationService -> Console: "Pedido entregue: " + orderId
    OrderService --> OrderController: Response<OrderView>
    OrderController --> CLI: Response (success)
else Order not found or wrong status
    OrderService --> OrderController: Response (error: "Pedido não pode ser entregue")
    OrderController --> CLI: Response (error)
end
```

**Pontos Importantes**:
1. Fluxo linear de status
2. Finalização do ciclo de vida
3. Timestamp de entrega
4. Evento de conclusão

## Fluxo Completo Integrado

### Visão End-to-End

```
[OPEN] -> addItem() -> addItem() -> finalize() -> [AWAITING_PAYMENT]
         ↓               ↓            ↓
    Snapshot Price  Calculate Total  Publish Event
    
[AWAITING_PAYMENT] -> processPayment() -> [PAID] -> markAsDelivered() -> [FINISHED]
         ↓                    ↓          ↓              ↓
    Notify Customer      Record Time   Notify Payment  Notify Delivery
```

### Estados e Transições Válidas

1. **OPEN**: 
   - Permite: addItem, updateQuantity, removeItem, finalize
   - Próximo: AWAITING_PAYMENT

2. **AWAITING_PAYMENT**: 
   - Permite: processPayment
   - Próximo: PAID

3. **PAID**: 
   - Permite: markAsDelivered
   - Próximo: FINISHED

4. **FINISHED**: 
   - Estado final
   - Permite: apenas consultas

## Padrões de Interação

### Request-Response Pattern
- Toda interação retorna Response<T>
- Padronização de sucesso/erro
- Mensagens user-friendly

### Event-Driven Architecture
- Eventos de domínio para mudanças importantes
- Desacoplamento temporal
- Extensibilidade para novos handlers

### Validation Chain
- Validação em múltiplas camadas
- Fail-fast principle
- Mensagens específicas por contexto

### Repository Abstraction
- Interfaces para persistência
- Testabilidade via mocks
- Independência de tecnologia

## Tratamento de Erros

### Cenários de Erro Comuns

1. **Entidade não encontrada**
   ```
   OrderRepository --> OrderService: Optional.empty()
   OrderService --> Controller: Response.error("Pedido não encontrado")
   ```

2. **Estado inválido**
   ```
   Order -> Order: validateStatus() throws InvalidStatusException
   OrderService --> Controller: Response.error("Operação não permitida")
   ```

3. **Dados inválidos**
   ```
   Order -> Order: validateQuantity() throws ValidationException
   OrderService --> Controller: Response.error("Quantidade inválida")
   ```

### Propagação de Erros
- Exceções capturadas nos Services
- Conversão para Response.error()
- Logging nos pontos apropriados
- Não exposição de detalhes internos

## Observabilidade

### Pontos de Log
- Entrada em cada Service method
- Publicação de eventos
- Persistência de entidades
- Erros e exceções

### Métricas Potenciais
- Tempo de resposta por operação
- Taxa de sucesso/erro
- Volume de eventos publicados
- Distribuição de status de pedidos

---

**Os diagramas de sequência garantem que o fluxo de dados seja claro, consistente e auditável, facilitando manutenção e debugging do sistema.**