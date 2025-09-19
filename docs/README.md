# Documentação do Ada Commerce

Este diretório contém toda a documentação técnica e de negócio do projeto Ada Commerce. A documentação está organizada de forma profissional seguindo padrões da indústria para projetos Java com Clean Architecture.

## 📁 Estrutura da Documentação

```
docs/
├── README.md                    # Este arquivo - índice geral
├── diagrams/                    # Diagramas técnicos e de negócio
│   ├── casos-de-uso.md         # Descrição dos casos de uso
│   ├── classes.md              # Documentação do diagrama de classes
│   ├── sequencia.md            # Documentação dos diagramas de sequência
│   └── arquitetura.md          # Documentação da arquitetura
├── specs/                       # Especificações e requisitos
│   ├── requisitos-funcionais.md     # RF001-RF999
│   ├── requisitos-nao-funcionais.md # RNF001-RNF999  
│   ├── regras-de-negocio.md         # RN001-RN999
│   └── glossario.md                 # Definições e termos técnicos
└── exemplos/                    # Exemplos práticos de uso
    ├── cli-comandos.md         # Comandos CLI detalhados
    ├── fluxos-completos.md     # Fluxos de ponta a ponta
    └── casos-de-teste.md       # Cenários de teste manuais
```

## 🎯 Navegação Rápida

### Para Desenvolvedores
- **[📊 Diagramas](diagrams/)** - Visualização da arquitetura e fluxos
- **[📋 Especificações](specs/)** - Requisitos técnicos e de negócio
- **[💡 Exemplos](exemplos/)** - Casos práticos e comandos

### Para Product Owners
- **[Requisitos Funcionais](specs/requisitos-funcionais.md)** - O que o sistema faz
- **[Regras de Negócio](specs/regras-de-negocio.md)** - Como o sistema deve se comportar
- **[Casos de Uso](diagrams/casos-de-uso.md)** - Interações do usuário

### Para Arquitetos
- **[Arquitetura](diagrams/arquitetura.md)** - Visão geral da solução
- **[Diagrama de Classes](diagrams/classes.md)** - Estrutura do código
- **[Diagramas de Sequência](diagrams/sequencia.md)** - Fluxos de execução

### Para Testadores
- **[Casos de Teste](exemplos/casos-de-teste.md)** - Cenários de validação
- **[Fluxos Completos](exemplos/fluxos-completos.md)** - Jornadas do usuário
- **[Comandos CLI](exemplos/cli-comandos.md)** - Interface de teste

## 📐 Orientações para Diagramas

### Tipos de Diagramas Disponíveis

1. **Diagrama de Arquitetura** (`architecture.svg`)
   - Visão de camadas da Clean Architecture
   - Dependências entre módulos
   - Separação de responsabilidades

2. **Diagrama de Fluxo de Dados** (`data-flow.svg`)
   - Movimento de informações no sistema
   - Transformações de dados
   - Pontos de integração

3. **Diagrama de Classes** (`ClassDiagram-Ada-Commerce.svg`)
   - Entidades de domínio
   - Value Objects
   - Relacionamentos e cardinalidades

4. **Diagrama de Casos de Uso** (`UseCase-Ada-Commerce.svg`)
   - Atores do sistema
   - Funcionalidades principais
   - Relacionamentos entre casos de uso

5. **Diagramas de Sequência**
   - **Pedido** (`SequenceDiagram-Pedido-Ada-Commerce.svg`)
   - **Entrega e Pagamento** (`SequenceDiagram-Entrega-Pagamento-Ada-Commerce.svg`)

### Como Interpretar os Diagramas

- **Cores**: Cada camada arquitetural tem sua cor
- **Setas**: Indicam direção de dependência
- **Estereótipos**: `<<Entity>>`, `<<Value Object>>`, `<<Service>>`, etc.
- **Multiplicidade**: `1..*`, `0..1` nos relacionamentos

## 📝 Especificações Técnicas

### Categorização de Requisitos

- **RF (Requisitos Funcionais)**: O que o sistema deve fazer
- **RNF (Requisitos Não Funcionais)**: Como o sistema deve se comportar
- **RN (Regras de Negócio)**: Restrições e validações específicas

### Padrão de Numeração
- RF001, RF002... para requisitos funcionais
- RNF001, RNF002... para requisitos não funcionais  
- RN001, RN002... para regras de negócio

### Rastreabilidade
Cada requisito é rastreável até:
- Casos de uso que o implementam
- Classes que o realizam
- Testes que o validam

## 💻 Exemplos de Uso

### Organização dos Exemplos

1. **Comandos CLI**: Referência completa de comandos
2. **Fluxos Completos**: Cenários de ponta a ponta
3. **Casos de Teste**: Validações manuais e automáticas

### Convenções nos Exemplos

```bash
# Comando a ser executado
$ comando

# Saída esperada
Resultado do comando

# Explicação
Descrição do que aconteceu
```

## 🤝 Como Contribuir com a Documentação

### Adicionando Novos Diagramas

1. Criar arquivo SVG ou PNG em `diagrams/`
2. Adicionar documentação markdown explicativa
3. Referenciar no README principal quando relevante
4. Usar nomes descritivos e padrão de nomenclatura

### Atualizando Especificações

1. Seguir numeração sequencial
2. Manter rastreabilidade com código
3. Atualizar glossário quando necessário
4. Revisar impactos em outros documentos

### Criando Exemplos

1. Testar todos os comandos antes de documentar
2. Incluir saídas reais do sistema
3. Explicar contexto e objetivos
4. Organizar por nível de complexidade

## 🔍 Glossário Rápido

- **Clean Architecture**: Padrão arquitetural com separação em camadas
- **Value Object**: Objeto imutável identificado por seus valores
- **Aggregate**: Conjunto de entidades tratadas como unidade
- **Port**: Interface que define contratos entre camadas
- **Adapter**: Implementação que adapta tecnologias externas
- **Use Case**: Funcionalidade específica do sistema
- **Domain Event**: Evento que representa algo importante no domínio

## 📧 Contato

Para dúvidas sobre a documentação:
- Abrir issue no repositório com label `documentation`
- Seguir templates de issue disponíveis
- Consultar documentação existente antes de perguntar

---

**Última atualização**: Automaticamente mantida via CI/CD
**Versão da documentação**: Sempre alinhada com a versão do código