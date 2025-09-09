#!/usr/bin/env bash
set -e
# Requer GitHub CLI autenticado e remoto 'origin' apontando para o GitHub.

create_issue () {
  local title="$1"; shift
  gh issue create --title "$title" --body "$*" --label "$LABELS"
}

LABELS="task,domain"
create_issue "Modelagem de domínio: entidades e VOs" "
- [ ] Customer, Product, Order, OrderItem
- [ ] Money (BigDecimal, HALF_EVEN), Document (CPF/CNPJ), Email
"

LABELS="task,infrastructure"
create_issue "Repositórios em memória e adaptadores" "
- [ ] Customer/Product/Order repositories thread-safe
- [ ] Notificador fake (console), EventPublisher simples, Clock/ID
"

LABELS="task,application"
create_issue "Casos de uso essenciais" "
- [ ] Clientes: Create/List/Update/Get
- [ ] Produtos: Create/List/Update/Get
- [ ] Pedidos: Create/Add/UpdateQty/Remove/Finalize/Pay/Deliver/Get/ListByCustomer
"

LABELS="task,cli"
create_issue "CLI de demonstração (fluxo feliz)" "
- [ ] Script interativo ou main simples para rodar o fluxo
"

LABELS="task,test"
create_issue "Testes mínimos" "
- [ ] VO e entidades
- [ ] Fluxos e transições inválidas
"

echo "Issues criadas."
