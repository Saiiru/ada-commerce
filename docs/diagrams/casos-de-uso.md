# Casos de Uso do Ada Commerce

> **Diagrama**: `UseCase-Ada-Commerce.svg`

## Visão Geral

Os casos de uso representam as funcionalidades que o sistema oferece aos seus usuários (atores). O Ada Commerce é projetado para suportar operações essenciais de e-commerce, desde cadastros básicos até processamento completo de pedidos.

## Atores do Sistema

### 👤 Cliente Final
**Descrição**: Pessoa física ou jurídica que realiza compras
**Responsabilidades**:
- Fornecer dados para cadastro
- Realizar pedidos
- Acompanhar status de compras

### 👨‍💼 Operador do Sistema
**Descrição**: Funcionário que administra o e-commerce
**Responsabilidades**:
- Cadastrar e manter clientes
- Gerenciar catálogo de produtos
- Processar pagamentos e entregas
- Monitorar operações

### 🤖 Sistema Externo
**Descrição**: Integrações automáticas (futuras)
**Responsabilidades**:
- Validar pagamentos
- Confirmar entregas  
- Sincronizar estoques

## Módulo: Gestão de Clientes

### UC001 - Cadastrar Cliente
**Ator Principal**: Operador do Sistema
**Pré-condições**: Sistema inicializado
**Fluxo Principal**:
1. Operador informa nome do cliente
2. Operador informa documento (CPF ou CNPJ)
3. Operador informa email (opcional)
4. Sistema valida formato do documento
5. Sistema verifica unicidade do documento
6. Sistema persiste cliente com status ativo
7. Sistema exibe confirmação de cadastro

**Fluxos Alternativos**:
- **FA001**: Documento inválido → Sistema exibe erro e solicita correção
- **FA002**: Documento duplicado → Sistema informa que cliente já existe

**Pós-condições**: Cliente cadastrado e disponível para uso

### UC002 - Listar Clientes
**Ator Principal**: Operador do Sistema
**Pré-condições**: Sistema inicializado
**Fluxo Principal**:
1. Operador solicita listagem de clientes
2. Sistema recupera todos os clientes ativos
3. Sistema exibe lista com ID, nome e documento
4. Operador visualiza informações

### UC003 - Atualizar Cliente
**Ator Principal**: Operador do Sistema
**Pré-condições**: Cliente existe no sistema
**Fluxo Principal**:
1. Operador seleciona cliente a atualizar
2. Sistema exibe dados atuais
3. Operador altera nome e/ou email
4. Sistema valida novos dados
5. Sistema persiste alterações
6. Sistema confirma atualização

**Restrições**: Documento não pode ser alterado

### UC004 - Consultar Cliente
**Ator Principal**: Operador do Sistema
**Pré-condições**: Cliente existe no sistema
**Fluxo Principal**:
1. Operador informa ID ou documento do cliente
2. Sistema localiza cliente
3. Sistema exibe todos os dados do cliente
4. Sistema exibe histórico de pedidos (se houver)

## Módulo: Gestão de Produtos

### UC005 - Cadastrar Produto
**Ator Principal**: Operador do Sistema
**Pré-condições**: Sistema inicializado
**Fluxo Principal**:
1. Operador informa nome do produto
2. Operador informa preço base
3. Sistema valida preço positivo
4. Sistema gera ID único para produto
5. Sistema persiste produto
6. Sistema confirma cadastro

**Fluxos Alternativos**:
- **FA001**: Preço inválido → Sistema exibe erro de validação

### UC006 - Listar Produtos
**Ator Principal**: Operador do Sistema
**Pré-condições**: Sistema inicializado
**Fluxo Principal**:
1. Operador solicita listagem de produtos
2. Sistema recupera todos os produtos
3. Sistema exibe lista com ID, nome e preço
4. Operador visualiza catálogo

### UC007 - Atualizar Produto
**Ator Principal**: Operador do Sistema
**Pré-condições**: Produto existe no sistema
**Fluxo Principal**:
1. Operador seleciona produto a atualizar
2. Sistema exibe dados atuais
3. Operador altera nome e/ou preço
4. Sistema valida novos dados
5. Sistema persiste alterações
6. Sistema confirma atualização

## Módulo: Gestão de Pedidos

### UC008 - Criar Pedido
**Ator Principal**: Operador do Sistema
**Pré-condições**: Cliente existe no sistema
**Fluxo Principal**:
1. Operador seleciona cliente
2. Sistema cria pedido em status OPEN
3. Sistema associa pedido ao cliente
4. Sistema registra timestamp de criação
5. Sistema disponibiliza pedido para adição de itens

### UC009 - Adicionar Item ao Pedido
**Ator Principal**: Operador do Sistema
**Pré-condições**: Pedido em status OPEN, produto existe
**Fluxo Principal**:
1. Operador seleciona produto
2. Operador informa quantidade
3. Sistema captura preço atual do produto (snapshot)
4. Sistema valida quantidade positiva
5. Sistema adiciona item ao pedido
6. Sistema recalcula total do pedido
7. Sistema confirma adição

**Fluxos Alternativos**:
- **FA001**: Produto já no pedido → Sistema atualiza quantidade
- **FA002**: Quantidade inválida → Sistema exibe erro

### UC010 - Alterar Quantidade de Item
**Ator Principal**: Operador do Sistema
**Pré-condições**: Pedido em status OPEN, item existe no pedido
**Fluxo Principal**:
1. Operador seleciona item do pedido
2. Operador informa nova quantidade
3. Sistema valida quantidade positiva
4. Sistema atualiza quantidade do item
5. Sistema recalcula total do pedido
6. Sistema confirma alteração

### UC011 - Remover Item do Pedido
**Ator Principal**: Operador do Sistema
**Pré-condições**: Pedido em status OPEN, item existe no pedido
**Fluxo Principal**:
1. Operador seleciona item a remover
2. Sistema remove item do pedido
3. Sistema recalcula total do pedido
4. Sistema confirma remoção

### UC012 - Finalizar Pedido
**Ator Principal**: Operador do Sistema
**Pré-condições**: Pedido em status OPEN com pelo menos um item
**Fluxo Principal**:
1. Operador solicita finalização do pedido
2. Sistema valida que pedido tem itens
3. Sistema altera status para AWAITING_PAYMENT
4. Sistema publica evento OrderAwaitingPayment
5. Sistema envia notificação de aguardando pagamento
6. Sistema confirma finalização

**Fluxos Alternativos**:
- **FA001**: Pedido vazio → Sistema impede finalização

## Módulo: Processamento de Pagamentos

### UC013 - Processar Pagamento
**Ator Principal**: Operador do Sistema
**Pré-condições**: Pedido em status AWAITING_PAYMENT
**Fluxo Principal**:
1. Operador seleciona pedido a pagar
2. Sistema valida status do pedido
3. Sistema altera status para PAID
4. Sistema registra timestamp do pagamento
5. Sistema publica evento OrderPaid
6. Sistema envia notificação de pagamento confirmado
7. Sistema confirma processamento

**Fluxos Alternativos**:
- **FA001**: Status inválido → Sistema impede pagamento

## Módulo: Gestão de Entregas

### UC014 - Marcar como Entregue
**Ator Principal**: Operador do Sistema
**Pré-condições**: Pedido em status PAID
**Fluxo Principal**:
1. Operador seleciona pedido a entregar
2. Sistema valida status do pedido
3. Sistema altera status para FINISHED
4. Sistema registra timestamp da entrega
5. Sistema publica evento OrderDelivered
6. Sistema envia notificação de entrega realizada
7. Sistema confirma entrega

**Fluxos Alternativos**:
- **FA001**: Status inválido → Sistema impede marcação

## Módulo: Consultas e Relatórios

### UC015 - Visualizar Pedido
**Ator Principal**: Operador do Sistema
**Pré-condições**: Pedido existe no sistema
**Fluxo Principal**:
1. Operador informa ID do pedido
2. Sistema localiza pedido
3. Sistema exibe dados do pedido (cliente, status, itens, total)
4. Sistema exibe histórico de alterações
5. Operador visualiza informações completas

### UC016 - Listar Pedidos por Cliente
**Ator Principal**: Operador do Sistema
**Pré-condições**: Cliente existe no sistema
**Fluxo Principal**:
1. Operador seleciona cliente
2. Sistema recupera todos os pedidos do cliente
3. Sistema ordena por data de criação (mais recente primeiro)
4. Sistema exibe lista com ID, status, total e data
5. Operador visualiza histórico do cliente

## Relacionamentos entre Casos de Uso

### Dependências
- UC008 **requer** UC001 (cliente deve existir)
- UC009 **requer** UC008 + UC005 (pedido e produto devem existir)
- UC012 **requer** UC009 (pedido deve ter itens)
- UC013 **requer** UC012 (pedido deve estar finalizado)
- UC014 **requer** UC013 (pedido deve estar pago)

### Extensões
- UC009 **estende** UC010 (quando produto já existe no pedido)
- UC015 **inclui** visualização de UC016 (pedidos do cliente)

## Validações Transversais

### Regras de Negócio Aplicadas
- **RN001**: Documento único por cliente
- **RN002**: Preço sempre positivo
- **RN003**: Quantidade sempre positiva
- **RN004**: Status de pedido segue fluxo linear
- **RN005**: Itens só podem ser alterados em pedidos OPEN

### Eventos Publicados
- `OrderAwaitingPayment` → Dispara notificação
- `OrderPaid` → Dispara notificação + habilita entrega
- `OrderDelivered` → Dispara notificação + fecha pedido

---

**Os casos de uso garantem que todas as funcionalidades essenciais do e-commerce sejam cobertas de forma consistente e rastreável.**