# Fluxos Completos - Ada Commerce

## Fluxo 1: Jornada Completa do Cliente (Happy Path)

### Objetivo
Demonstrar o fluxo completo desde cadastro de cliente até entrega do pedido.

### Pré-condições
- Sistema iniciado com `./gradlew run`
- Dados de teste carregados

### Passos Detalhados

#### Passo 1: Cadastrar Novo Cliente
```
Menu Principal > 1 (Clientes) > 1 (Cadastrar Cliente)

Nome: Maria da Silva
Documento: 987.654.321-00
Email: maria.silva@email.com

✅ Cliente cadastrado: Maria da Silva
```

#### Passo 2: Cadastrar Produtos (se necessário)
```
Menu Principal > 2 (Produtos) > 1 (Cadastrar Produto)

Nome: Smartphone Samsung Galaxy
Preço: 1299.99

✅ Produto cadastrado: Smartphone Samsung Galaxy
```

#### Passo 3: Selecionar Cliente
```
Menu Principal > 1 (Clientes) > 5 (Selecionar Cliente)

1. Maria da Silva
Escolha: 1

✅ Cliente selecionado: Maria da Silva
```

#### Passo 4: Criar Pedido
```
Menu Principal > 3 (Pedidos) > 1 (Criar Pedido)

✅ Pedido criado para Maria da Silva
ID: abc123...
Status: OPEN
```

#### Passo 5: Adicionar Itens ao Pedido
```
Menu Pedidos > 3 (Adicionar Item)

Produto: Smartphone Samsung Galaxy
Quantidade: 1

✅ Item adicionado - Total: R$ 1.299,99

Menu Pedidos > 3 (Adicionar Item)

Produto: Capinha Protetora
Quantidade: 2

✅ Item adicionado - Total: R$ 1.349,97
```

#### Passo 6: Revisar Pedido
```
Menu Pedidos > 2 (Ver Pedido Atual)

Itens:
- Smartphone Samsung Galaxy: 1x R$ 1.299,99
- Capinha Protetora: 2x R$ 24,99
Total: R$ 1.349,97
```

#### Passo 7: Finalizar Pedido
```
Menu Pedidos > 6 (Finalizar Pedido)

✅ Pedido finalizado
Status: AWAITING_PAYMENT
[NOTIFICAÇÃO] Pedido aguardando pagamento
```

#### Passo 8: Processar Pagamento
```
Menu Pedidos > 7 (Processar Pagamento)

✅ Pagamento processado
Status: PAID
[NOTIFICAÇÃO] Pagamento confirmado
```

#### Passo 9: Marcar como Entregue
```
Menu Pedidos > 8 (Marcar como Entregue)

✅ Pedido entregue
Status: FINISHED
[NOTIFICAÇÃO] Pedido entregue
```

#### Resultado Final
- Cliente cadastrado e ativo
- Pedido completo com status FINISHED
- Notificações enviadas em cada etapa
- Histórico preservado para consultas futuras

---

## Fluxo 2: Gestão de Catálogo de Produtos

### Objetivo
Demonstrar operações completas de gestão de produtos.

### Cenário
Loja de eletrônicos precisa atualizar seu catálogo.

#### Passo 1: Listar Produtos Existentes
```
Menu Principal > 2 (Produtos) > 2 (Listar Produtos)

Produtos encontrados:
1. Notebook Dell - R$ 2.499,99
2. Mouse Logitech - R$ 89,90
```

#### Passo 2: Cadastrar Novos Produtos
```
Menu Produtos > 1 (Cadastrar Produto)

Nome: Tablet iPad Air
Preço: 3999.99
✅ Produto cadastrado

Nome: Fone de Ouvido Bluetooth
Preço: 299.90
✅ Produto cadastrado

Nome: Carregador Wireless
Preço: 149.99
✅ Produto cadastrado
```

#### Passo 3: Atualizar Preços
```
Menu Produtos > 4 (Atualizar Produto)

Produto: Notebook Dell
Novo preço: 2299.99
✅ Preço atualizado

Produto: Mouse Logitech
Novo nome: Mouse Logitech MX Master
Novo preço: 99.90
✅ Produto atualizado
```

#### Passo 4: Verificar Catálogo Final
```
Menu Produtos > 2 (Listar Produtos)

Catálogo atualizado:
1. Notebook Dell - R$ 2.299,99
2. Mouse Logitech MX Master - R$ 99,90
3. Tablet iPad Air - R$ 3.999,99
4. Fone de Ouvido Bluetooth - R$ 299,90
5. Carregador Wireless - R$ 149,99
```

---

## Fluxo 3: Múltiplos Pedidos para o Mesmo Cliente

### Objetivo
Demonstrar histórico de compras e gestão de múltiplos pedidos.

### Cenário
Cliente corporativo realiza várias compras ao longo do tempo.

#### Passo 1: Cadastrar Cliente Corporativo
```
Menu Clientes > 1 (Cadastrar Cliente)

Nome: Tech Solutions LTDA
Documento: 12.345.678/0001-90
Email: compras@techsolutions.com

✅ Cliente corporativo cadastrado
```

#### Passo 2: Primeiro Pedido - Equipamentos Básicos
```
Menu Clientes > 5 (Selecionar Cliente)
Escolha: Tech Solutions LTDA

Menu Pedidos > 1 (Criar Pedido)
Menu Pedidos > 3 (Adicionar Item)
- 5x Mouse Logitech = R$ 499,50
- 5x Teclado USB = R$ 374,95

Menu Pedidos > 6 (Finalizar)
Menu Pedidos > 7 (Pagar)
Menu Pedidos > 8 (Entregar)

✅ Primeiro pedido finalizado: R$ 874,45
```

#### Passo 3: Segundo Pedido - Notebooks
```
Menu Pedidos > 1 (Criar Pedido) [novo pedido]
Menu Pedidos > 3 (Adicionar Item)
- 3x Notebook Dell = R$ 6.899,97

Menu Pedidos > 6 (Finalizar)
Menu Pedidos > 7 (Pagar)
Menu Pedidos > 8 (Entregar)

✅ Segundo pedido finalizado: R$ 6.899,97
```

#### Passo 4: Terceiro Pedido - Acessórios
```
Menu Pedidos > 1 (Criar Pedido) [novo pedido]
Menu Pedidos > 3 (Adicionar Item)
- 3x Fone de Ouvido = R$ 899,70
- 10x Cabo USB-C = R$ 299,90

Menu Pedidos > 6 (Finalizar)
Menu Pedidos > 7 (Pagar)
# Simular problema na entrega - não marcar como entregue

✅ Terceiro pedido pago, aguardando entrega
```

#### Passo 5: Consultar Histórico do Cliente
```
Menu Pedidos > 10 (Listar Pedidos do Cliente)

Histórico de Tech Solutions LTDA:
1. Pedido #001 - FINISHED - R$ 874,45 - 19/01/2025
2. Pedido #002 - FINISHED - R$ 6.899,97 - 19/01/2025  
3. Pedido #003 - PAID - R$ 1.199,60 - 19/01/2025

Total gasto: R$ 8.974,02
Pedidos finalizados: 2
Pedidos pendentes entrega: 1
```

---

## Fluxo 4: Cenários de Erro e Recuperação

### Objetivo
Demonstrar tratamento de erros e como o usuário pode se recuperar.

### Cenário 1: Documento Inválido
```
Menu Clientes > 1 (Cadastrar Cliente)

Nome: Cliente Teste
Documento: 123.456.789-00 [CPF inválido]

❌ Erro: Documento inválido para tipo CPF

Recuperação:
Documento: 123.456.789-09 [CPF válido]
✅ Cliente cadastrado com sucesso
```

### Cenário 2: Produto com Preço Zero
```
Menu Produtos > 1 (Cadastrar Produto)

Nome: Produto Promocional
Preço: 0.00

❌ Erro: Preço deve ser maior que zero

Recuperação:
Preço: 1.00
✅ Produto cadastrado
```

### Cenário 3: Tentativa de Modificar Pedido Finalizado
```
Contexto: Pedido com status AWAITING_PAYMENT

Menu Pedidos > 3 (Adicionar Item)

❌ Erro: Não é possível adicionar itens. Pedido não está em status OPEN

Recuperação:
1. Criar novo pedido, OU
2. Cancelar pedido atual e criar novo (funcionalidade futura)
```

### Cenário 4: Fluxo de Status Inválido
```
Contexto: Pedido com status OPEN

Menu Pedidos > 7 (Processar Pagamento)

❌ Erro: Pedido deve estar em status AWAITING_PAYMENT para ser pago

Recuperação:
Menu Pedidos > 6 (Finalizar Pedido)
Menu Pedidos > 7 (Processar Pagamento)
✅ Pagamento processado
```

---

## Fluxo 5: Operações Avançadas

### Objetivo
Demonstrar recursos avançados e casos de uso específicos.

### Cenário: Loja com Alto Volume de Transações

#### Batch de Cadastros
```
# Cadastrar múltiplos clientes rapidamente
Menu Clientes > 1 (Cadastrar)
Cliente 1: João Santos - 111.222.333-44
Cliente 2: Maria Oliveira - 555.666.777-88  
Cliente 3: Empresa A LTDA - 11.111.111/0001-11
Cliente 4: Empresa B LTDA - 22.222.222/0001-22

# Cadastrar linha completa de produtos
Menu Produtos > 1 (Cadastrar)
Smartphone Basic - R$ 599,99
Smartphone Pro - R$ 1.199,99
Smartphone Premium - R$ 1.999,99
Cabo USB-C - R$ 29,99
Carregador Rápido - R$ 79,99
Película Protetora - R$ 19,99
```

#### Pedidos Simultâneos (Simulação)
```
# Pedido Cliente 1
Cliente: João Santos
Itens: 1x Smartphone Basic + 1x Cabo USB-C
Total: R$ 629,98
Status: FINISHED

# Pedido Cliente 2  
Cliente: Maria Oliveira
Itens: 1x Smartphone Pro + 2x Película Protetora
Total: R$ 1.239,97
Status: FINISHED

# Pedido Empresa A
Cliente: Empresa A LTDA
Itens: 10x Smartphone Basic + 10x Carregador Rápido
Total: R$ 6.799,90
Status: PAID (aguardando entrega)

# Pedido Empresa B
Cliente: Empresa B LTDA  
Itens: 5x Smartphone Premium + 5x Cabo USB-C
Total: R$ 10.149,90
Status: AWAITING_PAYMENT
```

#### Relatório de Vendas (via consultas)
```
# Total de vendas finalizadas: R$ 18.819,75
# Pedidos pendentes: R$ 10.149,90
# Cliente com maior volume: Empresa B LTDA
# Produto mais vendido: Smartphone Basic (11 unidades)
```

---

## Fluxo 6: Teste de Performance

### Objetivo
Validar performance do sistema com volume moderado de dados.

### Metodologia
Criar dados em volume para testar responsividade.

#### Setup de Dados
```bash
# Executar com timer
time ./gradlew run

# Criar via CLI:
# 50 clientes (mix de CPF/CNPJ)
# 20 produtos (preços variados)
# 100 pedidos (diferentes status)
# 500 itens de pedido no total
```

#### Métricas Esperadas
```
Operação               | Tempo Esperado
--------------------- | ---------------
Cadastrar cliente     | < 50ms
Listar 50 clientes   | < 100ms
Criar pedido          | < 30ms
Adicionar item        | < 40ms
Calcular total        | < 10ms
Finalizar pedido      | < 60ms
Consultar histórico   | < 80ms
```

#### Validação
- Menu responsivo (< 1s para qualquer operação)
- Cálculos precisos mesmo com muitos itens
- Memória estável (< 512MB com 1000+ objetos)
- Notificações funcionando corretamente

---

## Checklist de Validação

### Funcionalidades Básicas
- [ ] Cadastro de cliente com CPF válido
- [ ] Cadastro de cliente com CNPJ válido
- [ ] Validação de documento inválido
- [ ] Prevenção de documento duplicado
- [ ] Cadastro de produto com preço válido
- [ ] Validação de preço inválido
- [ ] Criação de pedido para cliente válido
- [ ] Adição de itens a pedido OPEN
- [ ] Cálculo correto de totais
- [ ] Finalização de pedido com itens

### Fluxo de Status
- [ ] OPEN → AWAITING_PAYMENT (finalizar)
- [ ] AWAITING_PAYMENT → PAID (pagar)
- [ ] PAID → FINISHED (entregar)
- [ ] Bloqueio de transições inválidas

### Notificações
- [ ] Notificação ao finalizar pedido
- [ ] Notificação ao confirmar pagamento
- [ ] Notificação ao entregar pedido

### Interface
- [ ] Navegação entre menus
- [ ] Contexto cliente/pedido funcionando
- [ ] Mensagens de erro claras
- [ ] Formatação de valores monetários
- [ ] Saída limpa da aplicação

---

**Estes fluxos cobrem todos os cenários críticos do sistema e devem ser executados para validação completa da funcionalidade.**