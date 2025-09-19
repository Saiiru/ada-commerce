# Requisitos Funcionais - Ada Commerce

## RF001 - Cadastrar Cliente

**Descrição**: O sistema deve permitir o cadastro de novos clientes com validação de dados obrigatórios.

**Critérios de Aceite**:
- Nome é obrigatório e não pode estar vazio
- Documento (CPF ou CNPJ) é obrigatório, deve ser válido e único no sistema
- Email é opcional, mas quando informado deve ter formato válido
- Sistema deve gerar ID único automaticamente
- Data de criação deve ser registrada automaticamente
- Cliente é criado com status ativo por padrão

**Prioridade**: Alta
**Módulo**: Customer
**Implementado em**: `CustomerService.create()`

---

## RF002 - Listar Clientes

**Descrição**: O sistema deve permitir a listagem de todos os clientes cadastrados.

**Critérios de Aceite**:
- Exibir todos os clientes ativos
- Mostrar ID, nome, documento e email
- Ordenar por data de criação (mais recente primeiro)
- Não exibir dados sensíveis desnecessários

**Prioridade**: Média
**Módulo**: Customer
**Implementado em**: `CustomerService.listAll()`

---

## RF003 - Consultar Cliente

**Descrição**: O sistema deve permitir a consulta de um cliente específico por ID.

**Critérios de Aceite**:
- Buscar cliente por ID único
- Exibir todos os dados do cliente
- Retornar erro se cliente não existir
- Incluir histórico de pedidos se aplicável

**Prioridade**: Média
**Módulo**: Customer
**Implementado em**: `CustomerService.findById()`

---

## RF004 - Atualizar Cliente

**Descrição**: O sistema deve permitir a atualização de dados de clientes existentes.

**Critérios de Aceite**:
- Permitir alteração de nome e email
- NÃO permitir alteração de documento
- Validar novos dados antes de salvar
- Manter histórico de alterações
- Atualizar timestamp de modificação

**Prioridade**: Baixa
**Módulo**: Customer
**Implementado em**: `CustomerService.update()`

---

## RF005 - Cadastrar Produto

**Descrição**: O sistema deve permitir o cadastro de novos produtos no catálogo.

**Critérios de Aceite**:
- Nome é obrigatório e não pode estar vazio
- Preço base é obrigatório e deve ser positivo
- Sistema deve gerar ID único automaticamente
- Data de criação deve ser registrada automaticamente

**Prioridade**: Alta
**Módulo**: Product
**Implementado em**: `ProductService.create()`

---

## RF006 - Listar Produtos

**Descrição**: O sistema deve permitir a listagem de todos os produtos cadastrados.

**Critérios de Aceite**:
- Exibir todos os produtos do catálogo
- Mostrar ID, nome e preço base
- Ordenar por nome alfabeticamente
- Incluir data de criação

**Prioridade**: Média
**Módulo**: Product
**Implementado em**: `ProductService.listAll()`

---

## RF007 - Consultar Produto

**Descrição**: O sistema deve permitir a consulta de um produto específico por ID.

**Critérios de Aceite**:
- Buscar produto por ID único
- Exibir todos os dados do produto
- Retornar erro se produto não existir

**Prioridade**: Média
**Módulo**: Product
**Implementado em**: `ProductService.findById()`

---

## RF008 - Atualizar Produto

**Descrição**: O sistema deve permitir a atualização de dados de produtos existentes.

**Critérios de Aceite**:
- Permitir alteração de nome e preço
- Validar novos dados antes de salvar
- Preço deve continuar sendo positivo
- Atualizar timestamp de modificação

**Prioridade**: Baixa
**Módulo**: Product
**Implementado em**: `ProductService.update()`

---

## RF009 - Criar Pedido

**Descrição**: O sistema deve permitir a criação de novos pedidos para clientes cadastrados.

**Critérios de Aceite**:
- Cliente deve existir no sistema
- Pedido é criado com status OPEN
- Sistema gera ID único automaticamente
- Data de criação é registrada automaticamente
- Lista de itens inicia vazia

**Prioridade**: Alta
**Módulo**: Order
**Implementado em**: `OrderService.create()`

---

## RF010 - Adicionar Item ao Pedido

**Descrição**: O sistema deve permitir a adição de produtos a pedidos em status OPEN.

**Critérios de Aceite**:
- Pedido deve estar em status OPEN
- Produto deve existir no sistema
- Quantidade deve ser positiva
- Sistema captura snapshot do preço atual
- Se produto já existe no pedido, atualizar quantidade
- Recalcular total do pedido automaticamente

**Prioridade**: Alta
**Módulo**: Order
**Implementado em**: `OrderService.addItem()`

---

## RF011 - Alterar Quantidade de Item

**Descrição**: O sistema deve permitir a alteração da quantidade de itens em pedidos OPEN.

**Critérios de Aceite**:
- Pedido deve estar em status OPEN
- Item deve existir no pedido
- Nova quantidade deve ser positiva
- Recalcular total do pedido automaticamente

**Prioridade**: Média
**Módulo**: Order
**Implementado em**: `OrderService.updateItemQuantity()`

---

## RF012 - Remover Item do Pedido

**Descrição**: O sistema deve permitir a remoção de itens de pedidos em status OPEN.

**Critérios de Aceite**:
- Pedido deve estar em status OPEN
- Item deve existir no pedido
- Remover item completamente da lista
- Recalcular total do pedido automaticamente

**Prioridade**: Média
**Módulo**: Order
**Implementado em**: `OrderService.removeItem()`

---

## RF013 - Finalizar Pedido

**Descrição**: O sistema deve permitir a finalização de pedidos que possuem itens.

**Critérios de Aceite**:
- Pedido deve estar em status OPEN
- Pedido deve ter pelo menos um item
- Alterar status para AWAITING_PAYMENT
- Publicar evento OrderAwaitingPayment
- Enviar notificação de aguardando pagamento

**Prioridade**: Alta
**Módulo**: Order
**Implementado em**: `OrderService.finalize()`

---

## RF014 - Processar Pagamento

**Descrição**: O sistema deve permitir o processamento de pagamentos para pedidos finalizados.

**Critérios de Aceite**:
- Pedido deve estar em status AWAITING_PAYMENT
- Alterar status para PAID
- Registrar timestamp do pagamento
- Publicar evento OrderPaid
- Enviar notificação de pagamento confirmado

**Prioridade**: Alta
**Módulo**: Order
**Implementado em**: `OrderService.processPayment()`

---

## RF015 - Marcar como Entregue

**Descrição**: O sistema deve permitir marcar pedidos pagos como entregues.

**Critérios de Aceite**:
- Pedido deve estar em status PAID
- Alterar status para FINISHED
- Registrar timestamp da entrega
- Publicar evento OrderDelivered
- Enviar notificação de entrega realizada

**Prioridade**: Alta
**Módulo**: Order
**Implementado em**: `OrderService.markAsDelivered()`

---

## RF016 - Consultar Pedido

**Descrição**: O sistema deve permitir a consulta de pedidos específicos por ID.

**Critérios de Aceite**:
- Buscar pedido por ID único
- Exibir dados completos do pedido
- Incluir lista de itens com subtotais
- Mostrar total calculado
- Exibir histórico de status

**Prioridade**: Média
**Módulo**: Order
**Implementado em**: `OrderService.findById()`

---

## RF017 - Listar Pedidos por Cliente

**Descrição**: O sistema deve permitir a listagem de todos os pedidos de um cliente específico.

**Critérios de Aceite**:
- Cliente deve existir no sistema
- Exibir todos os pedidos do cliente
- Ordenar por data de criação (mais recente primeiro)
- Mostrar status e total de cada pedido

**Prioridade**: Baixa
**Módulo**: Order
**Implementado em**: `OrderService.findAllByCustomerId()`

---

## RF018 - Notificar Mudanças de Status

**Descrição**: O sistema deve notificar automaticamente sobre mudanças importantes nos pedidos.

**Critérios de Aceite**:
- Notificar quando pedido aguarda pagamento
- Notificar quando pagamento é confirmado
- Notificar quando pedido é entregue
- Notificações devem ser via console (simulação de email)
- Notificações são automáticas e assíncronas

**Prioridade**: Média
**Módulo**: Notification
**Implementado em**: `ConsoleEmailNotifier`

---

## RF019 - Interface CLI de Demonstração

**Descrição**: O sistema deve fornecer uma interface de linha de comando para demonstrar todas as funcionalidades.

**Critérios de Aceite**:
- Menu principal com navegação entre módulos
- Submenus para cada tipo de operação
- Validação de entrada do usuário
- Exibição clara de resultados e erros
- Contexto atual (cliente/pedido selecionado)

**Prioridade**: Média
**Módulo**: CLI
**Implementado em**: `MainMenuHandler`

---

## RF020 - Validação de Documentos

**Descrição**: O sistema deve validar algoritimicamente documentos CPF e CNPJ.

**Critérios de Aceite**:
- Validar CPF com 11 dígitos
- Validar CNPJ com 14 dígitos
- Aplicar algoritmo de validação oficial
- Sanitizar entrada (remover caracteres especiais)
- Formatar para exibição quando necessário

**Prioridade**: Alta
**Módulo**: ValueObject
**Implementado em**: `Document`

---

## Matriz de Rastreabilidade

| Requisito | Caso de Uso | Classe Principal | Teste |
|-----------|-------------|------------------|-------|
| RF001 | UC001 | CustomerService | CustomerServiceTest |
| RF002 | UC002 | CustomerService | CustomerServiceTest |
| RF003 | UC004 | CustomerService | CustomerServiceTest |
| RF004 | UC003 | CustomerService | CustomerServiceTest |
| RF005 | UC005 | ProductService | ProductServiceTest |
| RF006 | UC006 | ProductService | ProductServiceTest |
| RF007 | UC007 | ProductService | ProductServiceTest |
| RF008 | UC008 | ProductService | ProductServiceTest |
| RF009 | UC008 | OrderService | OrderServiceTest |
| RF010 | UC009 | OrderService | OrderServiceTest |
| RF011 | UC010 | OrderService | OrderServiceTest |
| RF012 | UC011 | OrderService | OrderServiceTest |
| RF013 | UC012 | OrderService | OrderServiceTest |
| RF014 | UC013 | OrderService | OrderServiceTest |
| RF015 | UC014 | OrderService | OrderServiceTest |
| RF016 | UC015 | OrderService | OrderServiceTest |
| RF017 | UC016 | OrderService | OrderServiceTest |
| RF018 | - | ConsoleEmailNotifier | NotificationTest |
| RF019 | - | MainMenuHandler | CLITest |
| RF020 | - | Document | DocumentTest |

---

**Total**: 20 Requisitos Funcionais
**Status**: ✅ Implementados conforme especificação
**Cobertura**: 100% dos casos de uso mapeados