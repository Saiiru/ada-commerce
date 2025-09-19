# Ada Commerce — Núcleo de E-commerce Educacional

## Visão Geral do Projeto

**Ada Commerce** é um projeto educacional que implementa o núcleo de um sistema de e-commerce utilizando **Clean Architecture**. O foco está na criação de uma base sólida, testável e extensível para operações de cadastro de clientes/produtos e gestão completa do fluxo de pedidos.

**Problema**: Implementar o núcleo de um e‑commerce educacional para cadastro de clientes/produtos e fluxo de pedidos (itens, finalização, pagamento, entrega), sem REST nesta etapa.

**Meta**: Base limpa, testável e extensível com separação clara de responsabilidades seguindo os princípios da Clean Architecture.

## Funcionalidades Principais

### 🧑‍💼 Gestão de Clientes
- ✅ Cadastro de clientes com validação de documento (CPF/CNPJ)
- ✅ Listagem e consulta de clientes
- ✅ Atualização de dados (nome e email)
- ✅ Validação de documentos únicos no sistema

### 📦 Gestão de Produtos  
- ✅ Cadastro de produtos com informações básicas
- ✅ Listagem e consulta de produtos
- ✅ Atualização de informações e preços
- ✅ Controle de preços com BigDecimal (2 casas decimais)

### 🛒 Gestão de Pedidos
- ✅ Criação de pedidos para clientes cadastrados
- ✅ Adição/remoção/alteração de itens (apenas em status OPEN)
- ✅ Cálculo automático de totais com snapshot de preços
- ✅ Fluxo completo: OPEN → AWAITING_PAYMENT → PAID → FINISHED
- ✅ Notificações em cada transição de status

### 🔔 Sistema de Notificações
- ✅ Notificações via console (simulação de email)
- ✅ Eventos assíncronos para desacoplamento
- ✅ Notificações automáticas em mudanças de status

## Regras de Negócio

### Clientes
- **Documento obrigatório e único**: Cada cliente deve ter um documento válido (CPF ou CNPJ) único no sistema
- **Validação rigorosa**: CPF e CNPJ são validados algoritmicamente
- **Imutabilidade do documento**: Uma vez cadastrado, o documento não pode ser alterado
- **Soft delete**: Clientes são marcados como inativos, nunca removidos

### Produtos
- **Preço obrigatório**: Todo produto deve ter um preço base maior que zero
- **Informações básicas**: ID, nome e preço são suficientes para o MVP
- **Atualizações permitidas**: Nome e preço podem ser atualizados a qualquer momento

### Pedidos
- **Estado inicial**: Todo pedido inicia no status OPEN
- **Modificações restritas**: Itens só podem ser alterados em pedidos OPEN
- **Snapshot de preços**: Preço do item é capturado no momento da adição
- **Fluxo linear**: OPEN → AWAITING_PAYMENT → PAID → FINISHED
- **Cliente obrigatório**: Todo pedido deve estar associado a um cliente válido

### Pagamentos e Entregas
- **Pagamento necessário**: Pedidos devem ser pagos antes da entrega
- **Notificação automática**: Cada transição gera evento e notificação
- **Status final**: FINISHED indica pedido completamente processado

## Tecnologias

- **Java 17** - Linguagem principal com recursos modernos
- **Gradle 8.8** - Build tool e gerenciamento de dependências  
- **JUnit 5** - Framework de testes unitários
- **Clean Architecture** - Padrão arquitetural para separação de responsabilidades
- **GitHub Actions** - CI/CD e automações
- **Console CLI** - Interface de demonstração das funcionalidades

## Estrutura de Pastas

```
ada-commerce/
├── src/
│   ├── main/java/com/ada/commerce/
│   │   ├── cli/                     # Interface CLI de demonstração
│   │   ├── controller/              # Controllers da camada de interface
│   │   ├── model/                   # Entidades, VOs, Enums, Portas, Eventos
│   │   │   ├── entity/              # Entidades de domínio (Customer, Product, Order)
│   │   │   ├── vo/                  # Value Objects (Money, Document, Email)
│   │   │   ├── enums/               # Enumerações (OrderStatus, PaymentStatus)
│   │   │   ├── ports/               # Interfaces de repositórios
│   │   │   └── events/              # Eventos de domínio
│   │   ├── service/                 # Casos de uso e serviços da aplicação
│   │   │   ├── impl/                # Implementações de casos de uso
│   │   │   ├── ports/               # Portas da camada de aplicação
│   │   │   └── registry/            # Registry de serviços
│   │   ├── repository/              # Implementações de repositórios
│   │   └── utils/                   # Utilitários e helpers
│   └── test/java/                   # Testes unitários espelhando a estrutura
├── docs/                            # Documentação completa do projeto
│   ├── diagrams/                    # Diagramas de caso de uso, classe e sequência
│   ├── specs/                       # Especificações e requisitos detalhados
│   ├── exemplos/                    # Exemplos de uso e fluxos CLI
│   └── README.md                    # Índice da documentação
├── .github/                         # Templates e workflows GitHub
│   ├── ISSUE_TEMPLATE/              # Templates para issues
│   ├── workflows/                   # CI/CD pipelines
│   └── PULL_REQUEST_TEMPLATE.md     # Template para pull requests
├── scripts/                         # Scripts de automação
├── gradle/                          # Wrapper e configurações Gradle
├── build.gradle.kts                 # Configuração de build
├── settings.gradle.kts              # Configurações do projeto
└── README.md                        # Este arquivo
```

### Visão de Camadas (Clean Architecture)

```
┌─────────────────────────────────────────────────────────────┐
│                    INTERFACE ADAPTERS                       │
│                   (cli, controllers)                        │
├─────────────────────────────────────────────────────────────┤
│                   APPLICATION LAYER                         │
│              (services, use cases, ports)                   │
├─────────────────────────────────────────────────────────────┤
│                    DOMAIN LAYER                             │
│           (entities, value objects, events)                 │
├─────────────────────────────────────────────────────────────┤
│                  INFRASTRUCTURE LAYER                       │
│              (repositories, external services)              │
└─────────────────────────────────────────────────────────────┘
```
### Arquitetura e Diagramas

![Arquitetura](docs/diagrams/architecture.svg)

### Fluxo de Dados de Alto Nível

![Fluxo](docs/diagrams/data-flow.svg)

### Diagrama de Classes

![Diagrama de Classes](docs/diagrams/ClassDiagram-Ada-Commerce.svg)

### Casos de Uso

![Casos de Uso](docs/diagrams/UseCase-Ada-Commerce.svg)

### Diagrama de Sequência - Pedido

![Sequencia - Pedido](docs/diagrams/SequenceDiagram-Pedido-Ada-Commerce.svg)

### Diagrama de Sequência - Entrega e Pagamento

![Sequencia - Entrega e Pagamento](docs/diagrams/SequenceDiagram-Entrega-Pagamento-Ada-Commerce.svg)

## Como Executar

### Pré-requisitos
- **JDK 17** ou superior
- **Git** instalado
- **GitHub CLI (`gh`)** para automações (opcional)

### Configuração Inicial

```bash
# 1. Clonar o repositório
git clone https://github.com/Saiiru/ada-commerce.git
cd ada-commerce

# 2. Verificar instalação do Java
java -version

# 3. Executar build e testes
./gradlew build

# 4. Executar a aplicação CLI
./gradlew run
```

### Comandos Úteis

```bash
# Executar apenas os testes
./gradlew test

# Executar build limpo
./gradlew clean build

# Verificar dependências
./gradlew dependencies

# Gerar relatório de testes
./gradlew test jacocoTestReport
```

## Demonstração do CLI

O sistema inclui uma interface de linha de comando completa para demonstrar todas as funcionalidades. Ao executar `./gradlew run`, você terá acesso a um menu interativo:

### Menu Principal
```
Ada Commerce CLI

--- CONTEXTO ATUAL ---
Cliente Selecionado: Nenhum
Pedido Atual: Nenhum
----------------------

=== MENU PRINCIPAL ===
 1) Clientes
 2) Produtos  
 3) Pedidos
 0) Sair
```

### Fluxo de Demonstração Completo

1. **Cadastro de Cliente**
   - Navegar para menu Clientes → Cadastrar
   - Informar nome, documento (CPF/CNPJ) e email
   - Sistema valida documento e verifica unicidade

2. **Cadastro de Produtos**
   - Navegar para menu Produtos → Cadastrar
   - Informar nome e preço base
   - Sistema valida preço positivo

3. **Criação e Gestão de Pedido**
   - Selecionar cliente cadastrado
   - Criar novo pedido (status OPEN)
   - Adicionar produtos com quantidades
   - Visualizar total calculado automaticamente

4. **Processamento do Pedido**
   - Finalizar pedido → Status AWAITING_PAYMENT
   - Processar pagamento → Status PAID  
   - Marcar como entregue → Status FINISHED
   - Observar notificações em cada etapa

### Exemplo de Sessão

```bash
$ ./gradlew run

[INFO] Populando o sistema com dados de teste...
[INFO] Dados de teste carregados.

# Menu permite navegar pelas funcionalidades
# Sistema exibe contexto atual (cliente/pedido selecionado)
# Validações são aplicadas em tempo real
# Notificações aparecem automaticamente
```

### Dados de Teste

O sistema é populado automaticamente com:
- **Clientes**: Exemplos com CPF e CNPJ válidos
- **Produtos**: Itens diversos com preços variados
- **Estrutura pronta**: Para testar fluxos completos

Para mais exemplos detalhados, consulte [docs/exemplos/](docs/exemplos/).

## Colaboradores

Este projeto foi desenvolvido como parte do programa educacional da Ada Tech, com contribuições de:

- **[Antonio Carlos]** - Módulo Customer e validações de documento
- **[Thiago]** - Módulo Product e gestão de preços
- **[Carlúcio]** - Módulo Order e fluxo de pedidos
- **[Urias]** - Infraestrutura, Payment, Delivery, Notification e CLI

Cada colaborador foi responsável por módulos específicos seguindo os princípios da Clean Architecture e práticas de desenvolvimento colaborativo.

## Referências

### Arquitetura e Design
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design - Eric Evans](https://domainlanguage.com/ddd/)
- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)

### Tecnologias e Ferramentas
- [Java 17 Documentation](https://docs.oracle.com/en/java/javase/17/)
- [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

### Boas Práticas
- [Conventional Commits](https://www.conventionalcommits.org/)
- [GitHub Flow](https://guides.github.com/introduction/flow/)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)

### Documentação Adicional
- [docs/README.md](docs/README.md) - Índice completo da documentação
- [docs/specs/](docs/specs/) - Especificações técnicas detalhadas
- [docs/exemplos/](docs/exemplos/) - Exemplos práticos de uso

## Desenvolvimento e Contribuição

### Issues e Pull Requests

**Issues**: São cartões de trabalho organizados por templates:
- `Task`: Tarefa técnica granular
- `Story`: História funcional com critérios de aceite  
- `Bug`: Defeito com passos de reprodução

**Fluxo de Desenvolvimento**:
1. Criar issue definindo escopo e critérios de aceite
2. Criar branch: `feat/<escopo>` ou `fix/<escopo>`
3. Commits seguindo **Conventional Commits** (`feat:`, `fix:`, `test:`, `docs:`, `refactor:`, `chore:`)
4. Abrir PR pequeno e focado, preenchendo o template
5. Solicitar pelo menos 1 review
6. Merge via **Squash** (branch `main` protegida)

### Padrões de Código
- **Clean Architecture**: Separação clara de responsabilidades por camadas
- **SOLID Principles**: Aplicados em todo o design de classes
- **Value Objects**: Para conceitos de domínio como Money, Document, Email
- **Domain Events**: Para comunicação assíncrona entre módulos
- **Repository Pattern**: Para abstração de persistência

### Testes
- **Testes unitários**: Para todas as regras de negócio
- **Testes de integração**: Para fluxos completos
- **Coverage**: Objetivo de 80%+ nas camadas de domínio e aplicação

## Roadmap

### ✅ Fase 1: Núcleo sem API (Atual)
- Domínio completo com entidades e regras de negócio
- Casos de uso para todos os módulos
- Repositórios em memória
- CLI funcional para demonstração
- Documentação completa

### 🔄 Fase 2: API REST
- Controllers REST mapeando casos de uso
- Serialização JSON
- Validação de entrada
- Documentação OpenAPI/Swagger
- Postman collections

### 📋 Fase 3: Persistência e Estoque
- Banco de dados relacional (PostgreSQL)
- JPA/Hibernate para persistência
- Controle de estoque
- Transações ACID
- Migrações de schema

### 🚀 Fase 4: Integrações Externas
- Gateway de pagamento real
- Serviços de entrega
- Notificações por email/SMS
- Métricas e monitoramento
- Deploy em cloud

## Licença

MIT License - veja [LICENSE](LICENSE) para detalhes.

---

**Ada Commerce** - Desenvolvido com 💙 pela turma da Ada Tech
