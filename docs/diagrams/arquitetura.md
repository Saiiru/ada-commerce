# Documentação da Arquitetura

> **Diagrama**: `architecture.svg`

## Visão Geral

O Ada Commerce implementa a **Clean Architecture** proposta por Robert C. Martin, organizando o código em camadas concêntricas com dependências unidirecionais sempre apontando para o centro (domínio).

## Camadas da Arquitetura

### 🎯 Domain Layer (Núcleo)
**Localização**: `src/main/java/com/ada/commerce/model/`

**Responsabilidades**:
- Entidades de negócio (`Customer`, `Product`, `Order`)
- Value Objects (`Money`, `Document`, `Email`)  
- Enumerações de domínio (`OrderStatus`, `PaymentStatus`)
- Eventos de domínio (`OrderAwaitingPayment`, `OrderPaid`, `OrderDelivered`)
- Exceções de domínio (`InvalidDocumentException`)

**Princípios**:
- ✅ Não depende de nenhuma camada externa
- ✅ Contém toda a lógica de negócio
- ✅ É testável de forma isolada
- ✅ Representa o vocabulário ubíquo do domínio

### 🔧 Application Layer (Casos de Uso)
**Localização**: `src/main/java/com/ada/commerce/service/`

**Responsabilidades**:
- Orquestração de casos de uso
- Definição de portas (interfaces) para infraestrutura
- DTOs para transferência de dados entre camadas
- Validações específicas de casos de uso
- Coordenação de transações

**Principais Componentes**:
- `CustomerService`: Gestão de clientes
- `ProductService`: Gestão de produtos  
- `OrderService`: Gestão de pedidos
- `PaymentService`: Processamento de pagamentos
- `DeliveryService`: Gestão de entregas

### 🔌 Interface Adapters (Controladores e Apresentação)
**Localização**: `src/main/java/com/ada/commerce/controller/` e `cli/`

**Responsabilidades**:
- Adaptação entre protocolos externos e casos de uso
- Conversão de dados de entrada/saída
- Validações de formato e protocolo
- Tratamento de erros para usuários finais

**Principais Componentes**:
- `CustomerController`: Interface para gestão de clientes
- `ProductController`: Interface para gestão de produtos
- `OrderController`: Interface para gestão de pedidos
- `MainMenuHandler`: CLI de demonstração

### 🏗️ Infrastructure Layer (Detalhes Externos)
**Localização**: `src/main/java/com/ada/commerce/repository/` e `service/impl/`

**Responsabilidades**:
- Implementação de repositórios
- Adaptadores para tecnologias específicas
- Configurações de frameworks
- Detalhes de persistência

**Principais Componentes**:
- `InMemoryCustomerGateway`: Repositório de clientes em memória
- `InMemoryProductGateway`: Repositório de produtos em memória
- `InMemoryOrderGateway`: Repositório de pedidos em memória
- `ConsoleEmailNotifier`: Notificador via console
- `SystemClockProvider`: Provedor de tempo do sistema

## Fluxo de Dependências

```
CLI/Controllers → Services → Domain ← Repositories
     ↓              ↓         ↑           ↑
   Framework    Use Cases  Entities   Database
```

### Regra de Dependência

**✅ Permitido**: Camadas externas dependem de camadas internas
**❌ Proibido**: Camadas internas dependem de camadas externas

### Inversão de Dependência

A camada de aplicação define **interfaces (portas)** para os serviços que precisa:
- `CustomerRepository` → Implementada por `InMemoryCustomerGateway`
- `NotificationService` → Implementada por `ConsoleEmailNotifier`
- `ClockProvider` → Implementada por `SystemClockProvider`

## Benefícios da Arquitetura

### 🧪 Testabilidade
- Domínio testável sem dependências externas
- Mocks fáceis via interfaces
- Testes unitários rápidos e confiáveis

### 🔄 Flexibilidade
- Fácil troca de implementações (memória → banco)
- Adição de novos protocolos (CLI → REST)
- Evolução independente de camadas

### 📦 Separação de Responsabilidades
- Cada camada tem responsabilidade clara
- Baixo acoplamento entre módulos
- Alta coesão dentro de cada camada

### 🛡️ Proteção do Domínio
- Regras de negócio isoladas de frameworks
- Domínio independente de tecnologias
- Evolução do negócio sem quebrar código

## Padrões Aplicados

### Repository Pattern
Interface no domínio, implementação na infraestrutura:
```java
// Domain
public interface CustomerRepository {
    void save(Customer customer);
    Optional<Customer> findById(UUID id);
}

// Infrastructure  
public class InMemoryCustomerGateway implements CustomerRepository {
    // Implementação específica
}
```

### Domain Events
Comunicação assíncrona entre módulos:
```java
public record OrderPaid(UUID orderId, Instant when) implements DomainEvent {}
```

### Value Objects
Conceitos de domínio encapsulados:
```java
public final class Money {
    private final BigDecimal amount;
    // Validações e comportamentos
}
```

## Evolução da Arquitetura

### Fase Atual (MVP)
- Repositórios em memória
- CLI para demonstração
- Eventos síncronos

### Próximas Fases
- REST API como nova camada de interface
- Banco de dados como nova implementação de repositório
- Message broker para eventos assíncronos
- Métricas e observabilidade

## Validação da Arquitetura

### ArchUnit Tests (Futuro)
```java
@Test
void domainShouldNotDependOnInfrastructure() {
    classes()
        .that().resideInAPackage("..model..")
        .should().onlyDependOnClassesIn("..model..", "java..", "javax..")
        .check(importedClasses);
}
```

### Dependency Analysis
- Verificação automática de dependências
- Detecção de violações arquiteturais
- Métricas de acoplamento

---

**Esta arquitetura garante que o Ada Commerce seja mantível, testável e evolutivo, seguindo as melhores práticas da indústria para sistemas Java corporativos.**