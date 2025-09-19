# Diagrama de Classes do Ada Commerce

> **Diagrama**: `ClassDiagram-Ada-Commerce.svg`

## Visão Geral

O diagrama de classes mostra a estrutura estática do sistema, organizando as classes por camadas da Clean Architecture. Cada classe tem responsabilidades bem definidas e relacionamentos claros com outras classes.

## Camada de Domínio (Model)

### 🏢 Entidades Principais

#### Customer (Cliente)
```java
public class Customer {
    - UUID id
    - String name  
    - Document document
    - Email email
    - Instant createdAt
    - boolean active
    
    + Customer(String name, Document document, Email email)
    + void updateName(String name)
    + void updateEmail(Email email)
    + void deactivate()
    // getters...
}
```

**Responsabilidades**:
- Representar cliente do sistema
- Validar regras de negócio de cliente
- Controlar estado ativo/inativo
- Garantir imutabilidade do documento

**Invariantes**:
- `id` não pode ser nulo
- `name` não pode ser vazio
- `document` deve ser válido e único
- `createdAt` não pode ser alterado

#### Product (Produto)
```java
public class Product {
    - UUID id
    - String name
    - Money basePrice
    - Instant createdAt
    
    + Product(String name, Money basePrice)
    + void updateName(String name)
    + void updatePrice(Money basePrice)
    // getters...
}
```

**Responsabilidades**:
- Representar produto do catálogo
- Manter preço base do produto
- Validar preços positivos

**Invariantes**:
- `name` não pode ser vazio
- `basePrice` deve ser positivo
- `id` é imutável após criação

#### Order (Pedido)
```java
public class Order {
    - UUID id
    - UUID customerId
    - OrderStatus status
    - PaymentStatus paymentStatus
    - List<OrderItem> items
    - Instant createdAt
    
    + Order(UUID customerId)
    + void addItem(UUID productId, String productName, Money unitPrice, int quantity)
    + void updateItemQuantity(UUID productId, int quantity)
    + void removeItem(UUID productId)
    + void finalize()
    + void markAsPaid()
    + void markAsDelivered()
    + Money calculateTotal()
    // getters...
}
```

**Responsabilidades**:
- Gerenciar ciclo de vida do pedido
- Controlar itens e quantidades
- Calcular totais automaticamente
- Validar transições de status

**Invariantes**:
- Status segue fluxo: OPEN → AWAITING_PAYMENT → PAID → FINISHED
- Itens só podem ser alterados em status OPEN
- Deve ter pelo menos um item para finalizar

#### OrderItem (Item do Pedido)
```java
public class OrderItem {
    - UUID productId
    - String nameSnapshot
    - Money unitPriceSnapshot
    - int quantity
    
    + OrderItem(UUID productId, String nameSnapshot, Money unitPrice, int quantity)
    + void updateQuantity(int quantity)
    + Money getSubtotal()
    // getters...
}
```

**Responsabilidades**:
- Representar item específico do pedido
- Manter snapshot de dados do produto
- Calcular subtotais

**Invariantes**:
- `quantity` deve ser positivo
- `unitPriceSnapshot` deve ser positivo
- Snapshots são imutáveis após criação

### 💎 Value Objects

#### Money (Dinheiro)
```java
public final class Money {
    - BigDecimal amount
    
    + Money(BigDecimal amount)
    + Money(String amount)
    + Money add(Money other)
    + Money multiply(BigDecimal factor)
    + Money multiply(int factor)
    + boolean isPositive()
    + boolean equals(Object other)
    + int hashCode()
    + String toString()
}
```

**Características**:
- Imutável
- Precisão de 2 casas decimais
- Arredondamento HALF_EVEN
- Operações matemáticas seguras

#### Document (Documento)
```java
public final class Document {
    - String value
    - Type type
    
    public enum Type { CPF, CNPJ }
    
    + Document(String raw, Type type)
    + String value()
    + Type type() 
    + String formatted()
    + boolean equals(Object other)
    + int hashCode()
    + String toString()
}
```

**Características**:
- Validação algorítmica de CPF/CNPJ
- Sanitização automática (remove caracteres)
- Formatação para exibição
- Imutável após criação

#### Email (E-mail)
```java
public final class Email {
    - String address
    
    + Email(String address)
    + String address()
    + boolean equals(Object other)  
    + int hashCode()
    + String toString()
}
```

**Características**:
- Validação de formato
- Normalização (lowercase)
- Imutável

### 📊 Enumerações

#### OrderStatus (Status do Pedido)
```java
public enum OrderStatus {
    OPEN,              // Pedido criado, aceita modificações
    AWAITING_PAYMENT,  // Finalizado, aguardando pagamento
    PAID,              // Pago, pronto para entrega
    FINISHED           // Entregue, processo completo
}
```

#### PaymentStatus (Status do Pagamento)
```java
public enum PaymentStatus {
    NONE,              // Nenhum pagamento processado
    AWAITING_PAYMENT,  // Aguardando processamento
    PAID               // Pagamento confirmado
}
```

### 🎭 Eventos de Domínio

#### OrderAwaitingPayment
```java
public record OrderAwaitingPayment(UUID orderId, Instant when) implements DomainEvent {}
```

#### OrderPaid
```java
public record OrderPaid(UUID orderId, Instant when) implements DomainEvent {}
```

#### OrderDelivered
```java
public record OrderDelivered(UUID orderId, Instant when) implements DomainEvent {}
```

## Camada de Aplicação (Service)

### 🔧 Serviços de Aplicação

#### CustomerService
```java
public class CustomerService {
    - CustomerRepository repository
    
    + Response<CustomerView> create(String name, String document, String email)
    + Response<List<CustomerView>> listAll()
    + Response<CustomerView> findById(UUID id)
    + Response<CustomerView> update(UUID id, String name, String email)
}
```

#### ProductService
```java
public class ProductService {
    - ProductRepository repository
    
    + Response<ProductView> create(String name, BigDecimal price)
    + Response<List<ProductView>> listAll()
    + Response<ProductView> findById(UUID id)
    + Response<ProductView> update(UUID id, String name, BigDecimal price)
}
```

#### OrderService
```java
public class OrderService {
    - OrderRepository orderRepository
    - CustomerRepository customerRepository
    - ProductRepository productRepository
    - EventPublisher eventPublisher
    
    + Response<OrderView> create(UUID customerId)
    + Response<OrderView> addItem(UUID orderId, UUID productId, int quantity)
    + Response<OrderView> updateItemQuantity(UUID orderId, UUID productId, int quantity)
    + Response<OrderView> removeItem(UUID orderId, UUID productId)
    + Response<OrderView> finalize(UUID orderId)
    + Response<OrderView> processPayment(UUID orderId)
    + Response<OrderView> markAsDelivered(UUID orderId)
}
```

### 🚪 Portas (Interfaces)

#### Repositórios
```java
public interface CustomerRepository {
    void save(Customer customer);
    Optional<Customer> findById(UUID id);
    Optional<Customer> findByDocument(Document document);
    List<Customer> findAll();
    boolean existsByDocument(Document document);
}

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(UUID id);
    List<Product> findAll();
}

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(UUID id);
    List<Order> findAllByCustomerId(UUID customerId);
}
```

#### Serviços de Infraestrutura
```java
public interface NotificationService {
    void onAwaitingPayment(UUID orderId);
    void onPaid(UUID orderId);
    void onDelivered(UUID orderId);
}

public interface EventPublisher {
    <T extends DomainEvent> void publish(T event);
    <T extends DomainEvent> void subscribe(Class<T> eventType, Consumer<T> handler);
}

public interface ClockProvider {
    Instant now();
}
```

### 📋 DTOs e Views

#### CustomerView
```java
public record CustomerView(
    UUID id,
    String name,
    String document,
    String email,
    Instant createdAt,
    boolean active
) {}
```

#### ProductView
```java
public record ProductView(
    UUID id,
    String name,
    BigDecimal basePrice,
    Instant createdAt
) {}
```

#### OrderView
```java
public record OrderView(
    UUID id,
    UUID customerId,
    String customerName,
    OrderStatus status,
    PaymentStatus paymentStatus,
    List<OrderItemView> items,
    BigDecimal total,
    Instant createdAt
) {}
```

## Camada de Interface (Controllers)

### 🎮 Controllers

#### CustomerController
```java
public class CustomerController {
    - CustomerService service
    
    + Response executeCreateCustomer(String name, String document, String email)
    + Response executeListCustomers()
    + Response executeGetCustomer(String id)
    + Response executeUpdateCustomer(String id, String name, String email)
}
```

#### ProductController
```java
public class ProductController {
    - ProductService service
    
    + Response executeCreateProduct(String name, String price)
    + Response executeListProducts()
    + Response executeGetProduct(String id)
    + Response executeUpdateProduct(String id, String name, String price)
}
```

#### OrderController
```java
public class OrderController {
    - OrderService orderService
    - ProductService productService
    - CustomerService customerService
    
    + Response executeCreateOrder(String customerId)
    + Response executeAddItem(String orderId, String productId, String quantity)
    + Response executeUpdateItemQuantity(String orderId, String productId, String quantity)
    + Response executeRemoveItem(String orderId, String productId)
    + Response executeFinalizeOrder(String orderId)
    + Response executeProcessPayment(String orderId)
    + Response executeMarkAsDelivered(String orderId)
}
```

## Camada de Infraestrutura

### 🗄️ Implementações de Repositório

#### InMemoryCustomerGateway
```java
public class InMemoryCustomerGateway implements CustomerRepository {
    - Map<UUID, Customer> customers
    - Map<String, UUID> documentIndex
    
    // Implementações thread-safe
}
```

#### InMemoryProductGateway
```java
public class InMemoryProductGateway implements ProductRepository {
    - Map<UUID, Product> products
    
    // Implementações thread-safe
}
```

#### InMemoryOrderGateway
```java
public class InMemoryOrderGateway implements OrderRepository {
    - Map<UUID, Order> orders
    - Map<UUID, List<UUID>> customerOrdersIndex
    - EventPublisher eventPublisher
    
    // Implementações thread-safe
}
```

## Relacionamentos entre Classes

### Agregados
- **Customer**: Agregado simples
- **Product**: Agregado simples  
- **Order + OrderItem**: Agregado complexo (Order é a raiz)

### Dependências
- Controllers → Services
- Services → Repositories (interfaces)
- Services → Domain Entities
- Repositories → Domain Entities
- Entities → Value Objects

### Composição
- Order contém List<OrderItem>
- Customer contém Document e Email
- OrderItem contém Money (unitPriceSnapshot)

### Associação
- Order referencia Customer via customerId
- OrderItem referencia Product via productId

## Padrões de Design Aplicados

### Repository Pattern
- Abstração de persistência
- Interfaces na camada de aplicação
- Implementações na infraestrutura

### Value Object Pattern
- Objetos imutáveis para conceitos de domínio
- Validação encapsulada
- Igualdade por valor

### Domain Events
- Comunicação assíncrona
- Desacoplamento entre módulos
- Publisher/Subscriber pattern

### Service Layer
- Orquestração de casos de uso
- Transações e validações
- Adaptação entre camadas

---

**O diagrama de classes reflete uma arquitetura limpa, com separação clara de responsabilidades e baixo acoplamento entre os módulos.**