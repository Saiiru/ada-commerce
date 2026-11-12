# Order Management CLI

**Objetivo:** Simular no terminal a evolução de pedidos e estoque, desde cadastros até pagamento e entrega fictícios.

**Estado:** Trabalho em equipe, dados em memória; testes JUnit e fluxo CLI executados localmente.

**Documentação técnica, origem e próximos passos:** [docs/PROJECT_BRIEF.md](docs/PROJECT_BRIEF.md)

---

## Material original preservado

# Ada Commerce — CLI educacional em Java

Projeto de estudo para cadastrar clientes e produtos e acompanhar pedidos pelo terminal.
O fluxo executado por `Main` usa controllers, portas (`service/ports`) e gateways em memória
(`service/impl/memory`). As entidades e serviços anteriores continuam no repositório como
material do desenvolvimento em equipe; não são o caminho usado pela CLI atual.

## Executar

Requisitos: **JDK 17** instalado, Git e acesso à internet na primeira execução
para baixar Gradle 8.8 e as dependências de teste. O wrapper usa um toolchain Java 17;
ter somente um JDK mais recente não garante a execução. Não é necessário instalar Gradle
globalmente. No Windows, substitua `./gradlew` por `gradlew.bat`.

```bash
git clone https://github.com/Saiiru/java-order-management-cli.git
cd java-order-management-cli
java -version
./gradlew clean build
./gradlew run --console=plain --quiet
```

Se o Gradle não localizar o JDK 17, configure `JAVA_HOME` para sua instalação antes
de executar o wrapper. Não há banco de dados, credenciais ou servidor para configurar.

```bash
./gradlew test                         # testes JUnit 5
./gradlew installDist                  # distribuição local
./build/install/ada-commerce/bin/ada-commerce
```

O relatório de testes fica em `build/reports/tests/test/index.html`.
A aplicação inicia com dois clientes de demonstração (documentos de exemplo com
dígitos verificadores válidos) e três produtos. Não use dados pessoais reais.
Todos os dados e UUIDs são recriados a cada execução.

## Menus e fluxo

No menu principal: **1 Clientes**, **2 Produtos**, **3 Pedidos**, **0 Sair**.
Nos submenus, **0** volta. Fim da entrada (por exemplo, Ctrl+D no Linux)
encerra a sessão sem stack trace.

| Menu | Opções |
|---|---|
| Clientes | 1 cadastrar; 2 listar; 3 selecionar por UUID, documento ou parte do nome; 4 atualizar cliente atual |
| Produtos | 1 cadastrar; 2 listar; 3 ajustar estoque com quantidade positiva/negativa; 4 atualizar nome e preço |
| Pedidos | 1 criar para cliente selecionado; 2 adicionar item; 3 alterar quantidade; 4 remover item; 5 finalizar; 6 pagar; 7 entregar; 8 ver atual; 9 listar todos; 10 listar por cliente; 11 selecionar por UUID |

As listagens mostram o UUID completo para copiar e selecionar.
Ao atualizar um cliente, nome/email vazios mantêm o valor e `-` remove o email.
Ao atualizar um produto, nome/preço vazios mantêm o valor. Preços usam ponto decimal,
como `19.90`, e devem ser positivos com até duas casas decimais.

Para demonstrar um pedido:

1. Abra **Clientes → 3** e selecione `52998224725` (Cliente Exemplo); volte.
2. Abra **Pedidos → 1** para criar um pedido.
3. Escolha **2**, procure `Teclado`, deixe o preço vazio para usar o preço base e informe quantidade `1`.
4. Escolha **5**, **6**, **7** para finalizar, simular pagamento e registrar entrega.
5. Escolha **8** para conferir o pedido entregue, total e snapshot do produto.

Uma sessão reproduzível em shell Unix:

```bash
printf '1\n3\n52998224725\n0\n3\n1\n2\nTeclado\n\n1\n5\n6\n7\n8\n0\n0\n' | ./gradlew run --console=plain --quiet
```

## Regras do fluxo ativo

- Clientes exigem nome e CPF/CNPJ válidos pelo algoritmo dos dígitos verificadores.
  O documento é normalizado, único e imutável. Email é opcional e recebe validação sintática;
  isso não verifica existência, titularidade ou situação cadastral.
- Produtos exigem nome, preço positivo em centavos e estoque inteiro não negativo.
- Um pedido é criado para um cliente existente. Itens só podem ser alterados em `OPEN`.
- Adicionar um produto reserva estoque imediatamente. Adições repetidas somam quantidades
  e preservam o nome/preço capturados na primeira adição. Alterar quantidade ajusta a reserva;
  remover o item devolve a quantidade reservada ao estoque.
- Estoque insuficiente e entradas inválidas são rejeitados antes de alterar pedido/estoque.
  Atualizar o cadastro ou preço do produto não altera os snapshots dos pedidos existentes.
- Finalizar exige ao menos um item e total positivo. O fluxo é
  **OPEN → AWAITING_PAYMENT → PAID → DELIVERED**.
  Pagamento ou entrega fora da sequência, inclusive repetições, são rejeitados.
- Cada transição bem-sucedida publica um evento síncrono em memória e uma mensagem
  `[EMAIL]` no console. Não há envio de email, cobrança financeira ou serviço de entrega real.

## Testes e limites

Os testes automatizados exercitam validações, transições inválidas, totais/snapshots,
reserva/devolução de estoque, eventos, inicialização e sessões completas dos menus.
Os arquivos em `src/test/java/com/ada/commerce/domain` são exemplos antigos,
não testes JUnit nem evidência de cobertura.

Esta aplicação tem um único usuário por processo e armazenamento volátil.
Não oferece API REST, persistência, transações entre gateways, concorrência,
autenticação, cancelamento/expiração de reservas ou integrações externas.
Reservas de pedidos abertos duram até a remoção do item ou o encerramento da sessão.
A validação de documentos implementada cobre CPF e CNPJ numéricos; outros formatos
não estão implementados. Não há plugin JaCoCo nem meta de cobertura comprovada.

Os [diagramas e especificações](docs/README.md) registram o desenho educacional e
podem incluir propostas ainda não implementadas. Este README e os testes descrevem
o comportamento executável atual.

## Colaboração

Projeto desenvolvido no programa educacional Ada Tech, com contribuições de
Antonio Carlos (clientes/documentos), Thiago (produtos), Carlúcio (pedidos)
e Urias (infraestrutura, pagamento, entrega, notificações e CLI).

Licença MIT: consulte [LICENSE](LICENSE).


## Verificação do código em 12/11/2026

Gradle 8.8 offline JUnit: BUILD SUCCESSFUL; demo de pedido entregue PAID/DELIVERED, total R$ 350,99

**Limitações:** Pagamentos, notificações e entregas são simulações; banco em memória, sem persistência real.
