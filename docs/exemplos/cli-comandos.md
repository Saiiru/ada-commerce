# Comandos CLI - Ada Commerce

## Iniciando a Aplicação

### Comando de Execução
```bash
# Executar a aplicação CLI
./gradlew run

# Ou com logs de debug
./gradlew run --debug

# Ou com configuração específica de JVM
./gradlew run -Dorg.gradle.jvmargs="-Xmx512m"
```

### Saída Inicial
```
Starting a Gradle Daemon (subsequent builds will be faster)

> Task :run

[INFO] Populando o sistema com dados de teste...
[INFO] Dados de teste carregados.
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
Escolha:
```

## Módulo: Gestão de Clientes

### 1.1 Acessar Menu de Clientes
```
Escolha: 1

=== MENU CLIENTES ===
 1) Cadastrar Cliente
 2) Listar Clientes
 3) Consultar Cliente
 4) Atualizar Cliente
 5) Selecionar Cliente
 0) Voltar ao Menu Principal
Escolha:
```

### 1.2 Cadastrar Novo Cliente
```
Escolha: 1

=== CADASTRAR CLIENTE ===
Nome: João Silva
Documento (CPF/CNPJ): 123.456.789-09
Email (opcional): joao.silva@email.com

✅ Cliente cadastrado com sucesso!
ID: a1b2c3d4-e5f6-7890-abcd-123456789012
Nome: João Silva
Documento: 123.456.789-09 (CPF)
Email: joao.silva@email.com
```

### 1.3 Cadastrar Cliente com CNPJ
```
Escolha: 1

=== CADASTRAR CLIENTE ===
Nome: Empresa LTDA
Documento (CPF/CNPJ): 11.222.333/0001-81
Email (opcional): contato@empresa.com

✅ Cliente cadastrado com sucesso!
ID: b2c3d4e5-f6g7-8901-bcde-234567890123
Nome: Empresa LTDA
Documento: 11.222.333/0001-81 (CNPJ)
Email: contato@empresa.com
```

### 1.4 Erro - Documento Inválido
```
Escolha: 1

=== CADASTRAR CLIENTE ===
Nome: Cliente Teste
Documento (CPF/CNPJ): 111.111.111-11
Email (opcional): teste@email.com

❌ Erro: Documento inválido para tipo CPF
```

### 1.5 Erro - Documento Duplicado
```
Escolha: 1

=== CADASTRAR CLIENTE ===
Nome: Outro Cliente
Documento (CPF/CNPJ): 123.456.789-09
Email (opcional): outro@email.com

❌ Erro: Já existe um cliente com este documento
```

### 1.6 Listar Todos os Clientes
```
Escolha: 2

=== LISTA DE CLIENTES ===
1. João Silva
   ID: a1b2c3d4-e5f6-7890-abcd-123456789012
   Documento: 123.456.789-09 (CPF)
   Email: joao.silva@email.com
   Criado em: 2025-01-19 10:30:45

2. Empresa LTDA
   ID: b2c3d4e5-f6g7-8901-bcde-234567890123
   Documento: 11.222.333/0001-81 (CNPJ)
   Email: contato@empresa.com
   Criado em: 2025-01-19 10:32:15

Total: 2 clientes encontrados
```

### 1.7 Consultar Cliente Específico
```
Escolha: 3

=== CONSULTAR CLIENTE ===
Digite o ID do cliente: a1b2c3d4-e5f6-7890-abcd-123456789012

✅ Cliente encontrado:
ID: a1b2c3d4-e5f6-7890-abcd-123456789012
Nome: João Silva
Documento: 123.456.789-09 (CPF)
Email: joao.silva@email.com
Status: Ativo
Criado em: 2025-01-19 10:30:45

Histórico de Pedidos: 0 pedidos encontrados
```

### 1.8 Atualizar Cliente
```
Escolha: 4

=== ATUALIZAR CLIENTE ===
Digite o ID do cliente: a1b2c3d4-e5f6-7890-abcd-123456789012

Cliente atual:
Nome: João Silva
Email: joao.silva@email.com

Novo nome (Enter para manter): João Silva Santos
Novo email (Enter para manter): joao.santos@email.com

✅ Cliente atualizado com sucesso!
Nome: João Silva Santos
Email: joao.santos@email.com
```

### 1.9 Selecionar Cliente para Contexto
```
Escolha: 5

=== SELECIONAR CLIENTE ===
1. João Silva Santos
2. Empresa LTDA

Digite o número do cliente: 1

✅ Cliente selecionado: João Silva Santos

--- CONTEXTO ATUAL ---
Cliente Selecionado: João Silva Santos (123.456.789-09)
Pedido Atual: Nenhum
----------------------
```

## Módulo: Gestão de Produtos

### 2.1 Acessar Menu de Produtos
```
Escolha: 2

=== MENU PRODUTOS ===
 1) Cadastrar Produto
 2) Listar Produtos
 3) Consultar Produto
 4) Atualizar Produto
 0) Voltar ao Menu Principal
Escolha:
```

### 2.2 Cadastrar Produto
```
Escolha: 1

=== CADASTRAR PRODUTO ===
Nome do produto: Notebook Dell Inspiron
Preço base (R$): 2499.99

✅ Produto cadastrado com sucesso!
ID: c3d4e5f6-g7h8-9012-cdef-345678901234
Nome: Notebook Dell Inspiron
Preço: R$ 2.499,99
Criado em: 2025-01-19 10:45:30
```

### 2.3 Erro - Preço Inválido
```
Escolha: 1

=== CADASTRAR PRODUTO ===
Nome do produto: Produto Grátis
Preço base (R$): 0

❌ Erro: Preço deve ser maior que zero
```

### 2.4 Listar Produtos
```
Escolha: 2

=== LISTA DE PRODUTOS ===
1. Notebook Dell Inspiron
   ID: c3d4e5f6-g7h8-9012-cdef-345678901234
   Preço: R$ 2.499,99
   Criado em: 2025-01-19 10:45:30

2. Mouse Logitech
   ID: d4e5f6g7-h8i9-0123-defg-456789012345
   Preço: R$ 89,90
   Criado em: 2025-01-19 10:47:15

3. Teclado Mecânico
   ID: e5f6g7h8-i9j0-1234-efgh-567890123456
   Preço: R$ 299,99
   Criado em: 2025-01-19 10:48:45

Total: 3 produtos encontrados
```

### 2.5 Atualizar Produto
```
Escolha: 4

=== ATUALIZAR PRODUTO ===
Digite o ID do produto: c3d4e5f6-g7h8-9012-cdef-345678901234

Produto atual:
Nome: Notebook Dell Inspiron
Preço: R$ 2.499,99

Novo nome (Enter para manter): Notebook Dell Inspiron 15
Novo preço (Enter para manter): 2399.99

✅ Produto atualizado com sucesso!
Nome: Notebook Dell Inspiron 15
Preço: R$ 2.399,99
```

## Módulo: Gestão de Pedidos

### 3.1 Acessar Menu de Pedidos
```
Escolha: 3

=== MENU PEDIDOS ===
 1) Criar Pedido
 2) Ver Pedido Atual
 3) Adicionar Item
 4) Alterar Quantidade
 5) Remover Item
 6) Finalizar Pedido
 7) Processar Pagamento
 8) Marcar como Entregue
 9) Consultar Pedido
10) Listar Pedidos do Cliente
11) Selecionar Pedido
 0) Voltar ao Menu Principal
Escolha:
```

### 3.2 Criar Pedido (sem cliente selecionado)
```
Escolha: 1

❌ Erro: Nenhum cliente selecionado. 
Acesse o menu Clientes > Selecionar Cliente primeiro.
```

### 3.3 Criar Pedido (com cliente selecionado)
```
--- CONTEXTO ATUAL ---
Cliente Selecionado: João Silva Santos (123.456.789-09)
Pedido Atual: Nenhum
----------------------

Escolha: 1

✅ Pedido criado com sucesso!
ID: f6g7h8i9-j0k1-2345-fghi-678901234567
Cliente: João Silva Santos
Status: OPEN
Total: R$ 0,00
Criado em: 2025-01-19 11:00:00

--- CONTEXTO ATUAL ---
Cliente Selecionado: João Silva Santos (123.456.789-09)
Pedido Atual: f6g7h8i9... (OPEN) - R$ 0,00
----------------------
```

### 3.4 Adicionar Item ao Pedido
```
Escolha: 3

=== ADICIONAR ITEM ===
Produtos disponíveis:
1. Notebook Dell Inspiron 15 - R$ 2.399,99
2. Mouse Logitech - R$ 89,90
3. Teclado Mecânico - R$ 299,99

Digite o número do produto: 1
Quantidade: 1

✅ Item adicionado com sucesso!
Produto: Notebook Dell Inspiron 15
Quantidade: 1
Preço unitário: R$ 2.399,99
Subtotal: R$ 2.399,99

Total do pedido: R$ 2.399,99
```

### 3.5 Adicionar Mais Itens
```
Escolha: 3

=== ADICIONAR ITEM ===
Produtos disponíveis:
1. Notebook Dell Inspiron 15 - R$ 2.399,99
2. Mouse Logitech - R$ 89,90
3. Teclado Mecânico - R$ 299,99

Digite o número do produto: 2
Quantidade: 2

✅ Item adicionado com sucesso!
Produto: Mouse Logitech
Quantidade: 2
Preço unitário: R$ 89,90
Subtotal: R$ 179,80

Total do pedido: R$ 2.579,79
```

### 3.6 Ver Pedido Atual
```
Escolha: 2

=== PEDIDO ATUAL ===
ID: f6g7h8i9-j0k1-2345-fghi-678901234567
Cliente: João Silva Santos (123.456.789-09)
Status: OPEN
Criado em: 2025-01-19 11:00:00

Itens:
  - Notebook Dell Inspiron 15 (ID: c3d4e5f6...) | Qtd: 1 | Preço Unit.: R$ 2.399,99 | Subtotal: R$ 2.399,99
  - Mouse Logitech (ID: d4e5f6g7...) | Qtd: 2 | Preço Unit.: R$ 89,90 | Subtotal: R$ 179,80

TOTAL: R$ 2.579,79
--------------------------
```

### 3.7 Alterar Quantidade de Item
```
Escolha: 4

=== ALTERAR QUANTIDADE ===
Itens do pedido:
1. Notebook Dell Inspiron 15 - Qtd: 1
2. Mouse Logitech - Qtd: 2

Digite o número do item: 2
Nova quantidade: 1

✅ Quantidade alterada com sucesso!
Item: Mouse Logitech
Nova quantidade: 1
Novo subtotal: R$ 89,90

Total do pedido: R$ 2.489,89
```

### 3.8 Finalizar Pedido
```
Escolha: 6

=== FINALIZAR PEDIDO ===
Pedido atual:
Total: R$ 2.489,89
Itens: 2

Confirma finalização? (s/n): s

✅ Pedido finalizado com sucesso!
Status: AWAITING_PAYMENT

[NOTIFICAÇÃO] Pedido f6g7h8i9-j0k1-2345-fghi-678901234567 aguardando pagamento
```

### 3.9 Processar Pagamento
```
Escolha: 7

=== PROCESSAR PAGAMENTO ===
Pedido: f6g7h8i9-j0k1-2345-fghi-678901234567
Status atual: AWAITING_PAYMENT
Total: R$ 2.489,89

Confirma pagamento? (s/n): s

✅ Pagamento processado com sucesso!
Status: PAID

[NOTIFICAÇÃO] Pagamento confirmado para pedido f6g7h8i9-j0k1-2345-fghi-678901234567
```

### 3.10 Marcar como Entregue
```
Escolha: 8

=== MARCAR COMO ENTREGUE ===
Pedido: f6g7h8i9-j0k1-2345-fghi-678901234567
Status atual: PAID
Total: R$ 2.489,89

Confirma entrega? (s/n): s

✅ Pedido marcado como entregue!
Status: FINISHED

[NOTIFICAÇÃO] Pedido f6g7h8i9-j0k1-2345-fghi-678901234567 foi entregue
```

## Cenários de Erro

### Tentativa de Modificar Pedido Finalizado
```
Escolha: 3

❌ Erro: Não é possível adicionar itens a um pedido com status AWAITING_PAYMENT
```

### Produto Inexistente
```
Escolha: 3

=== ADICIONAR ITEM ===
Digite o ID do produto: inexistente-123

❌ Erro: Produto não encontrado
```

### Quantidade Inválida
```
Escolha: 3

=== ADICIONAR ITEM ===
Digite o número do produto: 1
Quantidade: 0

❌ Erro: Quantidade deve ser maior que zero
```

### Finalizar Pedido Vazio
```
Escolha: 6

❌ Erro: Não é possível finalizar um pedido sem itens
```

## Comandos de Saída

### Sair da Aplicação
```
Escolha: 0

Fim.

BUILD SUCCESSFUL in 45s
2 actionable tasks: 1 executed, 1 up-to-date
```

### Sair com Ctrl+C
```
^C
BUILD CANCELLED
Cancelled build.
```

## Dicas de Uso

### Navegação Eficiente
1. Use números para navegação rápida
2. Use 0 para voltar ao menu anterior
3. Mantenha contexto (cliente/pedido selecionado)

### Validação de Entrada
- Sistema valida todos os inputs automaticamente
- Mensagens de erro são específicas e orientativas
- Retry automático em caso de entrada inválida

### Dados de Teste
- Sistema inicia com dados pré-carregados
- Clientes e produtos de exemplo disponíveis
- Facilita testes de fluxos completos

---

**Esta referência de comandos cobre todos os cenários principais e de erro do sistema CLI. Use como guia para testes manuais e demonstrações.**