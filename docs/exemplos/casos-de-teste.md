# Casos de Teste - Ada Commerce

## CT001 - Cadastro de Cliente com CPF Válido

**Objetivo**: Validar cadastro de cliente pessoa física com CPF válido

**Pré-condições**: 
- Sistema iniciado
- Menu de clientes acessível

**Dados de Teste**:
- Nome: "Ana Silva"
- Documento: "123.456.789-09" (CPF válido)
- Email: "ana.silva@email.com"

**Passos**:
1. Acessar Menu Principal > Clientes > Cadastrar Cliente
2. Informar nome: "Ana Silva"
3. Informar documento: "123.456.789-09"
4. Informar email: "ana.silva@email.com"

**Resultado Esperado**:
- ✅ Cliente cadastrado com sucesso
- ID único gerado automaticamente
- Documento formatado como "123.456.789-09 (CPF)"
- Status ativo por padrão
- Data de criação registrada

**Resultado Real**: [ ]

---

## CT002 - Rejeição de CPF Inválido

**Objetivo**: Validar rejeição de CPF com dígitos verificadores inválidos

**Pré-condições**: Sistema iniciado

**Dados de Teste**:
- Nome: "Cliente Teste"
- Documento: "111.111.111-11" (CPF inválido)
- Email: "teste@email.com"

**Passos**:
1. Acessar Menu Clientes > Cadastrar Cliente
2. Informar dados de teste
3. Confirmar cadastro

**Resultado Esperado**:
- ❌ Erro: "Documento inválido para tipo CPF"
- Cliente não é cadastrado
- Sistema permite nova tentativa

**Resultado Real**: [ ]

---

## CT003 - Cadastro de Cliente com CNPJ Válido

**Objetivo**: Validar cadastro de cliente pessoa jurídica

**Dados de Teste**:
- Nome: "Tech Solutions LTDA"
- Documento: "11.222.333/0001-81" (CNPJ válido)
- Email: "contato@tech.com"

**Passos**:
1. Cadastrar cliente com dados de teste
2. Verificar formatação do documento
3. Confirmar tipo de documento

**Resultado Esperado**:
- ✅ Cliente cadastrado com sucesso
- Documento formatado como "11.222.333/0001-81 (CNPJ)"
- Todas as validações aplicadas

**Resultado Real**: [ ]

---

## CT004 - Prevenção de Documento Duplicado

**Objetivo**: Validar que não é possível cadastrar dois clientes com mesmo documento

**Pré-condições**: Cliente com CPF "123.456.789-09" já cadastrado

**Dados de Teste**:
- Nome: "Outro Cliente"
- Documento: "123.456.789-09" (duplicado)
- Email: "outro@email.com"

**Passos**:
1. Tentar cadastrar cliente com documento já existente
2. Verificar resposta do sistema

**Resultado Esperado**:
- ❌ Erro: "Já existe um cliente com este documento"
- Segundo cliente não é cadastrado
- Primeiro cliente permanece inalterado

**Resultado Real**: [ ]

---

## CT005 - Cadastro de Produto com Preço Válido

**Objetivo**: Validar cadastro de produto com preço positivo

**Dados de Teste**:
- Nome: "Notebook Gamer"
- Preço: "2999.99"

**Passos**:
1. Acessar Menu Produtos > Cadastrar Produto
2. Informar dados de teste
3. Confirmar cadastro

**Resultado Esperado**:
- ✅ Produto cadastrado com sucesso
- Preço formatado como "R$ 2.999,99"
- ID único gerado
- Data de criação registrada

**Resultado Real**: [ ]

---

## CT006 - Rejeição de Preço Zero ou Negativo

**Objetivo**: Validar que produtos devem ter preço positivo

**Dados de Teste**:
- Caso 1: Nome "Produto Grátis", Preço "0"
- Caso 2: Nome "Produto Negativo", Preço "-10.50"

**Passos**:
1. Tentar cadastrar produto com preço zero
2. Tentar cadastrar produto com preço negativo
3. Verificar mensagens de erro

**Resultado Esperado**:
- ❌ Ambos os casos devem falhar
- Erro: "Preço deve ser maior que zero"
- Nenhum produto é cadastrado

**Resultado Real**: [ ]

---

## CT007 - Criação de Pedido para Cliente Válido

**Objetivo**: Validar criação de pedido associado a cliente existente

**Pré-condições**: 
- Cliente "Ana Silva" cadastrado e selecionado

**Passos**:
1. Acessar Menu Pedidos > Criar Pedido
2. Verificar status inicial
3. Confirmar associação com cliente

**Resultado Esperado**:
- ✅ Pedido criado com status OPEN
- Associado ao cliente selecionado
- Lista de itens vazia
- Total R$ 0,00
- ID único gerado

**Resultado Real**: [ ]

---

## CT008 - Falha ao Criar Pedido sem Cliente

**Objetivo**: Validar que pedidos requerem cliente selecionado

**Pré-condições**: Nenhum cliente selecionado

**Passos**:
1. Tentar criar pedido sem cliente selecionado
2. Verificar mensagem de erro

**Resultado Esperado**:
- ❌ Erro: "Nenhum cliente selecionado"
- Orientação para selecionar cliente primeiro
- Nenhum pedido é criado

**Resultado Real**: [ ]

---

## CT009 - Adição de Item a Pedido OPEN

**Objetivo**: Validar adição de produtos a pedidos em status OPEN

**Pré-condições**:
- Pedido criado em status OPEN
- Produto "Notebook Gamer" disponível

**Dados de Teste**:
- Produto: "Notebook Gamer" (R$ 2.999,99)
- Quantidade: 2

**Passos**:
1. Acessar Menu Pedidos > Adicionar Item
2. Selecionar produto
3. Informar quantidade
4. Verificar cálculo do total

**Resultado Esperado**:
- ✅ Item adicionado com sucesso
- Snapshot de preço capturado (R$ 2.999,99)
- Subtotal: R$ 5.999,98
- Total do pedido atualizado

**Resultado Real**: [ ]

---

## CT010 - Cálculo Correto de Totais

**Objetivo**: Validar precisão dos cálculos de subtotais e total

**Pré-condições**: Pedido OPEN criado

**Dados de Teste**:
- Item 1: Notebook (R$ 2.999,99 x 1) = R$ 2.999,99
- Item 2: Mouse (R$ 89,90 x 3) = R$ 269,70
- Item 3: Teclado (R$ 299,99 x 2) = R$ 599,98

**Passos**:
1. Adicionar os três itens
2. Verificar cada subtotal
3. Verificar total geral

**Resultado Esperado**:
- Subtotais corretos para cada item
- Total geral: R$ 3.869,67
- Precisão de 2 casas decimais mantida

**Resultado Real**: [ ]

---

## CT011 - Snapshot de Preço Funcional

**Objetivo**: Validar que mudanças no preço do produto não afetam itens já adicionados

**Pré-condições**:
- Produto "Mouse" com preço R$ 89,90
- Item adicionado ao pedido por R$ 89,90

**Passos**:
1. Adicionar Mouse ao pedido (preço atual R$ 89,90)
2. Atualizar preço do produto para R$ 99,90
3. Verificar preço do item no pedido
4. Adicionar novo item do mesmo produto

**Resultado Esperado**:
- Item antigo mantém R$ 89,90 (snapshot)
- Item novo usa R$ 99,90 (preço atual)
- Total reflete ambos os preços

**Resultado Real**: [ ]

---

## CT012 - Finalização de Pedido com Itens

**Objetivo**: Validar finalização de pedido que contém itens

**Pré-condições**: Pedido OPEN com pelo menos um item

**Passos**:
1. Acessar Menu Pedidos > Finalizar Pedido
2. Confirmar finalização
3. Verificar mudança de status
4. Verificar notificação

**Resultado Esperado**:
- ✅ Status alterado para AWAITING_PAYMENT
- Notificação: "Pedido aguardando pagamento"
- Pedido não pode mais ser modificado

**Resultado Real**: [ ]

---

## CT013 - Bloqueio de Finalização de Pedido Vazio

**Objetivo**: Validar que pedidos vazios não podem ser finalizados

**Pré-condições**: Pedido OPEN sem itens

**Passos**:
1. Tentar finalizar pedido vazio
2. Verificar mensagem de erro

**Resultado Esperado**:
- ❌ Erro: "Não é possível finalizar um pedido sem itens"
- Status permanece OPEN
- Nenhuma notificação enviada

**Resultado Real**: [ ]

---

## CT014 - Processamento de Pagamento

**Objetivo**: Validar transição de AWAITING_PAYMENT para PAID

**Pré-condições**: Pedido em status AWAITING_PAYMENT

**Passos**:
1. Acessar Menu Pedidos > Processar Pagamento
2. Confirmar pagamento
3. Verificar mudança de status
4. Verificar notificação

**Resultado Esperado**:
- ✅ Status alterado para PAID
- Timestamp de pagamento registrado
- Notificação: "Pagamento confirmado"

**Resultado Real**: [ ]

---

## CT015 - Entrega de Pedido Pago

**Objetivo**: Validar transição de PAID para FINISHED

**Pré-condições**: Pedido em status PAID

**Passos**:
1. Acessar Menu Pedidos > Marcar como Entregue
2. Confirmar entrega
3. Verificar status final
4. Verificar notificação

**Resultado Esperado**:
- ✅ Status alterado para FINISHED
- Timestamp de entrega registrado
- Notificação: "Pedido entregue"
- Fluxo completo finalizado

**Resultado Real**: [ ]

---

## CT016 - Bloqueio de Transições Inválidas

**Objetivo**: Validar que status seguem fluxo linear obrigatório

**Dados de Teste**:
- Cenário 1: OPEN → PAID (pular AWAITING_PAYMENT)
- Cenário 2: AWAITING_PAYMENT → FINISHED (pular PAID)

**Passos**:
1. Tentar pagar pedido OPEN diretamente
2. Tentar entregar pedido AWAITING_PAYMENT
3. Verificar mensagens de erro

**Resultado Esperado**:
- ❌ Ambas operações devem falhar
- Mensagens específicas para cada caso
- Status não é alterado

**Resultado Real**: [ ]

---

## CT017 - Modificação de Pedido Não-OPEN

**Objetivo**: Validar que apenas pedidos OPEN podem ser modificados

**Pré-condições**: Pedido em status AWAITING_PAYMENT

**Passos**:
1. Tentar adicionar item
2. Tentar remover item
3. Tentar alterar quantidade
4. Verificar mensagens de erro

**Resultado Esperado**:
- ❌ Todas operações devem falhar
- Erro: "Operação não permitida para pedido em status X"
- Itens permanecem inalterados

**Resultado Real**: [ ]

---

## CT018 - Validação de Email Opcional

**Objetivo**: Validar que email é opcional mas deve ser válido quando informado

**Dados de Teste**:
- Caso 1: Nome "Cliente 1", CPF válido, Email vazio
- Caso 2: Nome "Cliente 2", CPF válido, Email "inválido"
- Caso 3: Nome "Cliente 3", CPF válido, Email "valido@domain.com"

**Resultado Esperado**:
- Caso 1: ✅ Aceito (email opcional)
- Caso 2: ❌ Rejeitado (email inválido)
- Caso 3: ✅ Aceito (email válido)

**Resultado Real**: [ ]

---

## CT019 - Atualização de Cliente

**Objetivo**: Validar alteração de dados de cliente existente

**Pré-condições**: Cliente "Ana Silva" cadastrado

**Dados de Teste**:
- Novo nome: "Ana Silva Santos"
- Novo email: "ana.santos@email.com"

**Passos**:
1. Selecionar cliente para atualização
2. Alterar nome e email
3. Verificar que documento não pode ser alterado
4. Confirmar alterações

**Resultado Esperado**:
- ✅ Nome e email atualizados
- Documento permanece inalterado
- Timestamp de modificação atualizado

**Resultado Real**: [ ]

---

## CT020 - Consulta de Histórico de Pedidos

**Objetivo**: Validar listagem de pedidos por cliente

**Pré-condições**: 
- Cliente com múltiplos pedidos criados
- Pedidos em diferentes status

**Passos**:
1. Selecionar cliente
2. Acessar Menu Pedidos > Listar Pedidos do Cliente
3. Verificar ordenação e informações

**Resultado Esperado**:
- Lista ordenada por data (mais recente primeiro)
- Status atual de cada pedido
- Totais corretos
- Contadores de pedidos por status

**Resultado Real**: [ ]

---

## Matriz de Cobertura de Testes

| Módulo | Casos de Teste | Cobertura |
|--------|----------------|-----------|
| Customer | CT001, CT002, CT003, CT004, CT018, CT019 | 6/6 ✅ |
| Product | CT005, CT006 | 2/2 ✅ |
| Order | CT007, CT008, CT009, CT010, CT011, CT012, CT013, CT014, CT015, CT016, CT017, CT020 | 12/12 ✅ |
| **Total** | **20 casos** | **100%** |

## Checklist de Execução

### Preparação
- [ ] Sistema compilado sem erros
- [ ] CLI executando corretamente
- [ ] Dados de teste carregados

### Execução
- [ ] Executar CT001-CT006 (Cadastros básicos)
- [ ] Executar CT007-CT008 (Criação de pedidos)
- [ ] Executar CT009-CT011 (Gestão de itens)
- [ ] Executar CT012-CT017 (Fluxo de status)
- [ ] Executar CT018-CT020 (Casos especiais)

### Validação
- [ ] Todos os casos PASSOU
- [ ] Logs sem erros críticos
- [ ] Notificações funcionando
- [ ] Performance adequada

### Relatório
- [ ] Documentar resultados
- [ ] Identificar bugs (se houver)
- [ ] Atualizar documentação

---

**Estes casos de teste cobrem todos os requisitos funcionais e regras de negócio do sistema. Execute-os sequencialmente para validação completa.**