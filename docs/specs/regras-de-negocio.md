# Regras de Negócio - Ada Commerce

## RN001 - Unicidade de Documento de Cliente

**Descrição**: Cada cliente deve ter um documento único no sistema.

**Detalhamento**:
- Não podem existir dois clientes com o mesmo documento (CPF ou CNPJ)
- Validação deve ocorrer no momento do cadastro
- Sistema deve rejeitar tentativas de cadastro com documento duplicado
- Documentos são case-insensitive e sanitizados

**Implementação**: 
- `CustomerService.create()` verifica `existsByDocument()`
- `InMemoryCustomerGateway` mantém índice por documento
- Exceção lançada: `DuplicateDocumentException`

**Testes**:
- Cadastro de segundo cliente com mesmo CPF deve falhar
- Cadastro de segundo cliente com mesmo CNPJ deve falhar
- Documentos com formatações diferentes mas mesmo número são considerados iguais

---

## RN002 - Validação Algorítmica de Documentos

**Descrição**: CPF e CNPJ devem ser algoritmicamente válidos.

**Detalhamento**:
- CPF deve ter 11 dígitos e passar na validação de dígitos verificadores
- CNPJ deve ter 14 dígitos e passar na validação de dígitos verificadores
- Algoritmos oficiais da Receita Federal devem ser aplicados
- Documentos com todos os dígitos iguais são considerados inválidos

**Implementação**:
- `Document` value object com `ValidarCPF` e `ValidarCNPJ`
- Sanitização automática remove caracteres especiais
- Validação ocorre no construtor do `Document`

**Testes**:
- CPF válido: "123.456.789-09" deve passar
- CPF inválido: "111.111.111-11" deve falhar
- CNPJ válido: "11.222.333/0001-81" deve passar
- CNPJ inválido: "11.111.111/1111-11" deve falhar

---

## RN003 - Preços Sempre Positivos

**Descrição**: Todos os valores monetários no sistema devem ser positivos.

**Detalhamento**:
- Preço base de produtos deve ser > 0
- Preço unitário de itens de pedido deve ser > 0
- Valores zero ou negativos são rejeitados
- Precisão de 2 casas decimais com arredondamento HALF_EVEN

**Implementação**:
- `Money` value object valida valores positivos
- `Product.updatePrice()` rejeita valores inválidos
- `OrderItem` snapshot sempre com preço positivo

**Testes**:
- Produto com preço R$ 0,00 deve ser rejeitado
- Produto com preço R$ -10,00 deve ser rejeitado
- Produto com preço R$ 0,01 deve ser aceito
- Cálculos mantêm precisão de 2 casas decimais

---

## RN004 - Quantidades Sempre Positivas

**Descrição**: Quantidades de itens em pedidos devem ser números inteiros positivos.

**Detalhamento**:
- Quantidade mínima é 1 unidade
- Não são aceitas quantidades zero ou negativas
- Quantidades devem ser números inteiros (sem frações)
- Limite máximo prático de 999.999 unidades por item

**Implementação**:
- `OrderItem.updateQuantity()` valida entrada
- `Order.addItem()` rejeita quantidades inválidas
- Controllers validam entrada antes de chamar services

**Testes**:
- Adicionar item com quantidade 0 deve falhar
- Adicionar item com quantidade -5 deve falhar
- Adicionar item com quantidade 1 deve passar
- Atualizar quantidade para 100 deve passar

---

## RN005 - Fluxo Linear de Status de Pedido

**Descrição**: Status de pedidos seguem fluxo sequencial obrigatório.

**Detalhamento**:
- Sequência: OPEN → AWAITING_PAYMENT → PAID → FINISHED
- Não é possível pular etapas do fluxo
- Não é possível retroceder no fluxo
- Cada transição tem validações específicas

**Implementação**:
- `Order.finalize()` só funciona se status = OPEN
- `Order.markAsPaid()` só funciona se status = AWAITING_PAYMENT
- `Order.markAsDelivered()` só funciona se status = PAID

**Testes**:
- Tentar pagar pedido OPEN diretamente deve falhar
- Tentar entregar pedido AWAITING_PAYMENT deve falhar
- Fluxo completo OPEN→AWAITING_PAYMENT→PAID→FINISHED deve funcionar

---

## RN006 - Modificações Apenas em Pedidos OPEN

**Descrição**: Itens de pedidos só podem ser alterados em status OPEN.

**Detalhamento**:
- Adicionar itens só é permitido em pedidos OPEN
- Remover itens só é permitido em pedidos OPEN
- Alterar quantidades só é permitido em pedidos OPEN
- Pedidos finalizados não podem ser modificados

**Implementação**:
- `Order.addItem()` verifica status antes de executar
- `Order.removeItem()` verifica status antes de executar
- `Order.updateItemQuantity()` verifica status antes de executar

**Testes**:
- Adicionar item a pedido AWAITING_PAYMENT deve falhar
- Remover item de pedido PAID deve falhar
- Alterar quantidade em pedido FINISHED deve falhar
- Modificações em pedido OPEN devem funcionar

---

## RN007 - Pedido Deve Ter Itens Para Finalizar

**Descrição**: Pedidos só podem ser finalizados se tiverem pelo menos um item.

**Detalhamento**:
- Lista de itens não pode estar vazia
- Pelo menos 1 item com quantidade ≥ 1
- Validação ocorre no momento da finalização
- Pedidos vazios permanecem em OPEN

**Implementação**:
- `Order.finalize()` valida se `items.isEmpty()`
- Exception lançada se tentar finalizar pedido vazio
- CLI exibe mensagem apropriada

**Testes**:
- Finalizar pedido sem itens deve falhar
- Finalizar pedido com 1 item deve funcionar
- Remover último item e tentar finalizar deve falhar

---

## RN008 - Snapshot de Preço no Momento da Adição

**Descrição**: Preço de itens é capturado no momento da adição ao pedido.

**Detalhamento**:
- Preço do produto pode mudar após adição ao pedido
- Item mantém preço original (snapshot)
- Total do pedido não muda se preço do produto mudar
- Garante consistência histórica de pedidos

**Implementação**:
- `OrderItem` armazena `unitPriceSnapshot`
- `Order.addItem()` captura preço atual do produto
- Preço do snapshot nunca é alterado após criação

**Testes**:
- Adicionar produto por R$ 10,00 ao pedido
- Alterar preço do produto para R$ 15,00
- Item no pedido deve manter R$ 10,00
- Total do pedido deve refletir preço original

---

## RN009 - Cálculo Automático de Totais

**Descrição**: Totais de pedidos são calculados automaticamente sempre que necessário.

**Detalhamento**:
- Total = Σ(quantidade × preço_unitário) para todos os itens
- Recálculo automático ao adicionar/remover/alterar itens
- Precisão de 2 casas decimais
- Arredondamento HALF_EVEN para valores intermediários

**Implementação**:
- `Order.calculateTotal()` soma todos os subtotais
- Chamado automaticamente após modificações nos itens
- `OrderItem.getSubtotal()` calcula quantidade × preço

**Testes**:
- Pedido com 2 itens (R$ 10,00 x 2) + (R$ 5,00 x 3) = R$ 35,00
- Remover um item deve recalcular total automaticamente
- Precisão mantida em cálculos com muitas casas decimais

---

## RN010 - Cliente Deve Existir Para Criar Pedido

**Descrição**: Pedidos só podem ser criados para clientes válidos e existentes.

**Detalhamento**:
- Cliente deve estar cadastrado no sistema
- Cliente deve estar ativo (não desabilitado)
- Referência ao cliente via ID único
- Validação ocorre antes da criação do pedido

**Implementação**:
- `OrderService.create()` valida existência do cliente
- `CustomerService.findById()` é chamado antes de criar pedido
- Exception lançada se cliente não existir

**Testes**:
- Criar pedido com ID de cliente inexistente deve falhar
- Criar pedido com cliente válido deve funcionar
- Criar pedido com cliente inativo deve falhar

---

## RN011 - Produto Deve Existir Para Adicionar ao Pedido

**Descrição**: Itens só podem ser adicionados se o produto existir no catálogo.

**Detalhamento**:
- Produto deve estar cadastrado no sistema
- Produto deve ter preço válido (> 0)
- Validação ocorre no momento da adição
- Snapshot de dados é capturado do produto válido

**Implementação**:
- `OrderService.addItem()` valida existência do produto
- `ProductService.findById()` é chamado antes de adicionar item
- Dados do produto são copiados para o snapshot

**Testes**:
- Adicionar produto inexistente deve falhar
- Adicionar produto válido deve funcionar
- Dados do snapshot devem refletir produto no momento da adição

---

## RN012 - Imutabilidade de Documento de Cliente

**Descrição**: Documento de cliente não pode ser alterado após cadastro.

**Detalhamento**:
- CPF ou CNPJ é imutável após criação do cliente
- Operações de update não podem alterar documento
- Apenas nome e email podem ser modificados
- Documento é identificador de negócio do cliente

**Implementação**:
- `Customer.updateDocument()` não existe
- `CustomerService.update()` não aceita documento
- `Document` é final e imutável

**Testes**:
- Atualizar cliente sem alterar documento deve funcionar
- Tentar alterar documento via API deve ser impossível
- Cliente mantém mesmo documento durante toda vida útil

---

## RN013 - Eventos de Domínio Para Mudanças Críticas

**Descrição**: Mudanças importantes de status geram eventos de domínio.

**Detalhamento**:
- Finalização de pedido gera `OrderAwaitingPayment`
- Confirmação de pagamento gera `OrderPaid`
- Conclusão de entrega gera `OrderDelivered`
- Eventos são assíncronos e desacoplados

**Implementação**:
- `Order` publica eventos via `EventPublisher`
- Handlers registrados para cada tipo de evento
- Notificações automáticas via eventos

**Testes**:
- Finalizar pedido deve gerar evento `OrderAwaitingPayment`
- Pagar pedido deve gerar evento `OrderPaid`
- Entregar pedido deve gerar evento `OrderDelivered`
- Eventos devem chegar aos handlers registrados

---

## RN014 - Notificações Automáticas

**Descrição**: Sistema notifica automaticamente sobre mudanças relevantes.

**Detalhamento**:
- Notificação quando pedido aguarda pagamento
- Notificação quando pagamento é confirmado
- Notificação quando pedido é entregue
- Notificações via console (simulação de email)

**Implementação**:
- `ConsoleEmailNotifier` implementa `NotificationService`
- Subscrições automáticas via `EventPublisher`
- Mensagens padronizadas e informativas

**Testes**:
- Finalizar pedido deve exibir notificação no console
- Pagar pedido deve exibir notificação de pagamento
- Entregar pedido deve exibir notificação de entrega

---

## RN015 - Validação de Email Quando Informado

**Descrição**: Email de cliente deve ter formato válido quando fornecido.

**Detalhamento**:
- Email é campo opcional no cadastro
- Quando informado, deve ter formato válido (regex)
- Normalização para lowercase
- Validação via `Email` value object

**Implementação**:
- `Email` value object valida formato
- Regex padrão para validação de email
- Normalização automática no construtor

**Testes**:
- Email "usuario@dominio.com" deve ser válido
- Email "email-inválido" deve ser rejeitado
- Email null ou vazio deve ser aceito (opcional)
- Normalização: "USER@DOMAIN.COM" → "user@domain.com"

---

## Matriz de Impacto das Regras

| Regra | Módulo Afetado | Criticidade | Validation Layer | Status |
|-------|----------------|-------------|------------------|--------|
| RN001 | Customer | Alta | Application + Domain | ✅ |
| RN002 | Customer | Alta | Domain (Value Object) | ✅ |
| RN003 | Product, Order | Alta | Domain (Value Object) | ✅ |
| RN004 | Order | Média | Domain + Application | ✅ |
| RN005 | Order | Alta | Domain | ✅ |
| RN006 | Order | Alta | Domain | ✅ |
| RN007 | Order | Média | Domain | ✅ |
| RN008 | Order | Alta | Domain | ✅ |
| RN009 | Order | Média | Domain | ✅ |
| RN010 | Order | Alta | Application | ✅ |
| RN011 | Order | Alta | Application | ✅ |
| RN012 | Customer | Média | Domain | ✅ |
| RN013 | Order | Média | Domain | ✅ |
| RN014 | Notification | Baixa | Infrastructure | ✅ |
| RN015 | Customer | Baixa | Domain (Value Object) | ✅ |

## Enforcement Strategy

### Domain Layer
- Value Objects garantem invariantes (RN002, RN003, RN015)
- Entities protegem estado interno (RN005, RN006, RN012)
- Business logic encapsulada (RN008, RN009)

### Application Layer  
- Use cases validam pré-condições (RN001, RN010, RN011)
- Orchestration de validações (RN007)
- Transaction boundaries

### Infrastructure Layer
- Repository constraints (RN001)
- Event publishing (RN013, RN014)
- Persistence integrity

---

**Total**: 15 Regras de Negócio
**Enforcement**: 100% via código (não configuração)
**Cobertura de Testes**: Todas as regras têm testes específicos
**Documentação**: Rastreável até requisitos funcionais