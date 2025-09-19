# Glossário - Ada Commerce

## A

**Aggregate**
- Conjunto de entidades de domínio tratadas como uma unidade para mudanças de dados
- Exemplo: Order (raiz) + OrderItem (entidades filhas)
- Sempre tem uma raiz que controla acesso às entidades internas

**Application Layer**
- Camada da Clean Architecture que contém casos de uso
- Orquestra operações entre domínio e infraestrutura
- Não contém lógica de negócio, apenas coordenação

**Ator**
- Entidade externa que interage com o sistema
- Exemplos: Cliente Final, Operador do Sistema, Sistema Externo

## B

**BigDecimal**
- Tipo de dados Java para valores monetários
- Garante precisão aritmética sem perda de precisão
- Usado para todos os valores de Money no sistema

**Business Rule**
- Ver Regra de Negócio

## C

**Clean Architecture**
- Padrão arquitetural que organiza código em camadas concêntricas
- Dependências sempre apontam para o centro (domínio)
- Promove testabilidade e independência de frameworks

**CLI (Command Line Interface)**
- Interface de linha de comando para demonstração do sistema
- Permite interação via terminal/console
- Menu-driven com navegação hierárquica

**CNPJ (Cadastro Nacional da Pessoa Jurídica)**
- Documento de identificação de empresas no Brasil
- 14 dígitos com validação algorítmica específica
- Formato: XX.XXX.XXX/XXXX-XX

**CPF (Cadastro de Pessoas Físicas)**
- Documento de identificação de pessoas físicas no Brasil
- 11 dígitos com validação algorítmica específica
- Formato: XXX.XXX.XXX-XX

**Customer (Cliente)**
- Entidade de domínio representando uma pessoa física ou jurídica
- Possui documento único, nome e email opcional
- Pode realizar pedidos no sistema

## D

**DDD (Domain-Driven Design)**
- Abordagem para desenvolvimento focada no domínio de negócio
- Conceitos: Entities, Value Objects, Aggregates, Repositories
- Vocabulário ubíquo entre desenvolvedores e especialistas do domínio

**Document (Documento)**
- Value Object que encapsula CPF ou CNPJ
- Fornece validação algorítmica e formatação
- Imutável após criação

**Domain Event**
- Evento que representa algo importante que aconteceu no domínio
- Exemplos: OrderAwaitingPayment, OrderPaid, OrderDelivered
- Permite comunicação assíncrona entre módulos

**Domain Layer**
- Camada central da Clean Architecture
- Contém entidades, value objects, eventos e regras de negócio
- Independente de frameworks e tecnologias externas

**DTO (Data Transfer Object)**
- Objeto usado para transferir dados entre camadas
- Não contém lógica de negócio
- Exemplos: CustomerView, ProductView, OrderView

## E

**Email**
- Value Object para endereços de email
- Valida formato e normaliza para lowercase
- Campo opcional no cadastro de clientes

**Entity (Entidade)**
- Objeto de domínio com identidade única
- Exemplos: Customer, Product, Order
- Identidade persiste durante todo ciclo de vida

**Event Publisher**
- Componente responsável por publicar e distribuir eventos
- Implementa padrão Publisher/Subscriber
- Permite registro de handlers para tipos específicos de eventos

## F

**Functional Requirement**
- Ver Requisito Funcional

## G

**Gateway**
- Padrão para abstrair acesso a recursos externos
- Implementação específica de Repository
- Exemplo: InMemoryCustomerGateway

## H

**HALF_EVEN**
- Estratégia de arredondamento para valores monetários
- Também conhecida como "banker's rounding"
- Minimiza bias em cálculos com muitas operações

## I

**Infrastructure Layer**
- Camada externa da Clean Architecture
- Contém detalhes de implementação (banco, frameworks, etc.)
- Implementa as interfaces definidas nas camadas internas

**Interface Adapters**
- Camada da Clean Architecture entre aplicação e infraestrutura
- Converte dados entre formatos interno e externo
- Inclui controllers, presenters, gateways

**Invariant (Invariante)**
- Condição que deve sempre ser verdadeira para um objeto
- Exemplo: Money sempre positivo, Document sempre válido
- Protegida por encapsulamento e validação

## J

**JUnit**
- Framework de testes unitários para Java
- Versão 5 usada no projeto
- Suporte a anotações e assertions modernas

## L

**Lifecycle (Ciclo de Vida)**
- Estados pelos quais uma entidade passa durante sua existência
- Exemplo Order: OPEN → AWAITING_PAYMENT → PAID → FINISHED
- Transições controladas por regras de negócio

## M

**Money (Dinheiro)**
- Value Object para valores monetários
- Encapsula BigDecimal com validações de negócio
- Garante precisão e operações matemáticas seguras

**MVP (Minimum Viable Product)**
- Versão mínima do produto com funcionalidades essenciais
- Foco em validar conceitos principais
- Base para iterações futuras

## N

**Non-Functional Requirement**
- Ver Requisito Não Funcional

**Notification Service**
- Serviço para envio de notificações aos usuários
- Implementação atual via console (simulação)
- Interface permite futuras implementações (email, SMS)

## O

**Order (Pedido)**
- Aggregate root representando um pedido de compra
- Contém itens, cliente, status e timestamps
- Controla seu próprio ciclo de vida

**Order Item**
- Entidade filha de Order
- Representa um produto específico no pedido
- Mantém snapshot de preço e nome do produto

**Order Status**
- Enumeração representando estado atual do pedido
- Valores: OPEN, AWAITING_PAYMENT, PAID, FINISHED
- Fluxo linear obrigatório

## P

**Payment Status**
- Enumeração representando estado do pagamento
- Valores: NONE, AWAITING_PAYMENT, PAID
- Sincronizado com Order Status

**Port**
- Interface que define contrato entre camadas
- Permite inversão de dependência
- Exemplos: CustomerRepository, NotificationService

**Product (Produto)**
- Entidade representando item do catálogo
- Possui nome, preço base e timestamps
- Pode ser adicionado a pedidos

## Q

**Quantity (Quantidade)**
- Número inteiro positivo representando quantidade de itens
- Sempre ≥ 1 em OrderItem
- Validada em todas as operações

## R

**Repository**
- Padrão para abstrair persistência de aggregates
- Define interface na camada de aplicação
- Implementado na camada de infraestrutura

**Requisito Funcional (RF)**
- Especifica o que o sistema deve fazer
- Exemplo: RF001 - Cadastrar Cliente
- Numeração sequencial para rastreabilidade

**Requisito Não Funcional (RNF)**
- Especifica como o sistema deve se comportar
- Exemplo: RNF001 - Performance
- Qualidade, segurança, usabilidade, etc.

**Regra de Negócio (RN)**
- Política ou restrição específica do domínio
- Exemplo: RN001 - Documento único por cliente
- Implementada no código, não configuração

**Response**
- Objeto padrão para retorno de operações
- Encapsula sucesso/erro de forma consistente
- Usado em todos os services e controllers

## S

**Service**
- Classe que implementa casos de uso específicos
- Orquestra operações entre repositories e entities
- Stateless e focado em uma responsabilidade

**Service Registry**
- Padrão para centralizar acesso a serviços
- Facilita injeção de dependências
- Configuração inicial da aplicação

**Snapshot**
- Cópia imutável de dados em momento específico
- Usado em OrderItem para preço e nome do produto
- Garante consistência histórica

**SOLID**
- Conjunto de princípios de design orientado a objetos
- Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion
- Aplicado em todo o design do sistema

## T

**Thread Safety**
- Propriedade de código que funciona corretamente em ambiente multi-thread
- Repositórios em memória são thread-safe
- Uso de ConcurrentHashMap para estruturas compartilhadas

**Timestamp**
- Marcação temporal de quando evento ocorreu
- Tipo Instant do Java para precisão
- Usado em createdAt, paymentAt, deliveredAt

## U

**Ubiquitous Language**
- Vocabulário comum entre desenvolvedores e especialistas do domínio
- Refletido no código através de nomes de classes e métodos
- Facilita comunicação e manutenção

**Use Case**
- Caso de uso específico que o sistema suporta
- Exemplo: UC001 - Cadastrar Cliente
- Implementado como método em Service classes

**UUID (Universally Unique Identifier)**
- Identificador único de 128 bits
- Usado como ID para todas as entidades
- Gerado automaticamente pelo sistema

## V

**Validation**
- Processo de verificar se dados atendem a critérios específicos
- Aplicada em múltiplas camadas (Value Objects, Services, Controllers)
- Fail-fast principle para feedback rápido

**Value Object**
- Objeto identificado por seus valores, não por identidade
- Imutável após criação
- Exemplos: Money, Document, Email

**View**
- Representação de dados otimizada para apresentação
- DTOs que expõem apenas dados necessários
- Exemplos: CustomerView, ProductView, OrderView

## W

**Workflow**
- Sequência de passos para completar um processo de negócio
- Exemplo: Criar pedido → Adicionar itens → Finalizar → Pagar → Entregar
- Implementado através de múltiplos use cases

---

## Convenções de Nomenclatura

### Pacotes
- `com.ada.commerce.model.*` - Domínio
- `com.ada.commerce.service.*` - Aplicação
- `com.ada.commerce.repository.*` - Infraestrutura
- `com.ada.commerce.controller.*` - Interface

### Classes
- **Entidades**: Substantivos (Customer, Product, Order)
- **Value Objects**: Substantivos (Money, Document, Email)
- **Services**: [Entidade]Service (CustomerService)
- **Repositories**: [Entidade]Repository (CustomerRepository)
- **Events**: [Ação]Event (OrderAwaitingPayment)

### Métodos
- **Commands**: Verbos (create, update, delete)
- **Queries**: find*, get*, list*, exists*
- **Business Actions**: Verbos de domínio (finalize, markAsPaid)

### Constantes
- UPPER_SNAKE_CASE para enums e constantes
- Prefixos descritivos (DEFAULT_, MAX_, MIN_)

---

**Este glossário é mantido atualizado conforme evolução do sistema e deve ser consultado para esclarecimentos sobre terminologia do projeto.**