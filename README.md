# AEP — Controle de Validade e Doação de Alimentos

Projeto do 4º semestre de Engenharia de Software (Esoft) que ajuda estabelecimentos de alimentos a controlar a validade dos produtos e a destinar itens próximos do vencimento para **doação** ou **descarte**, em vez de deixar vencer.

Relacionado aos ODS 12 (Consumo e Produção Responsáveis) e ODS 2 (Fome Zero).

## Integrantes

- Angelo Gabriel Albonetti — Sprint 1 (cadastro de produtos com validade) e Sprint 4 (relatórios e métricas)
- Leonardo — Sprint 2 (filtro de lotes por validade)
- Lênon — Sprint 3 (baixa de estoque por doação ou descarte)

## Tecnologias

- Java + Spring Boot
- PostgreSQL
- Interface via console (menu no terminal)

## Funcionalidades (RF01 a RF05)

| Requisito | Descrição |
|---|---|
| RF01 | Cadastro de produtos com nome, categoria, quantidade, unidade de medida, preço e validade |
| RF02 | Filtro de lotes por situação: vencido, próximo do vencimento ou dentro do prazo |
| RF03 | Baixa de desperdício com quantidade perdida, data da ocorrência e justificativa detalhada|
| RF04 |Doação para entidades parceiras ou consumo interno |
| RF05 | Relatórios de perdas e resgates, com exportação dos indicadores de impacto financeiro e volume reaproveitado|

## Estrutura do repositório

```
AEP/
├── src/        # código-fonte Java (Spring Boot)
├── database/   # script.sql com a criação das tabelas
└── docs/       # documentação do projeto (diagramas, PDF de especificação)
```

## Entidades

- **Usuario**: Administrador ou Operador (herança por `nivel_do_usuario`)
- **Estabelecimento**: pertence a um Administrador e possui vários Lotes
- **Lote**: produto com nome, categoria, quantidade, unidade de medida, preço e data de validade
- **Movimentacao**: Doação ou Descarte de um Lote, feito por um Usuario

## Como rodar o projeto

### Pré-requisitos
- Java 27+
- PostgreSQL rodando na porta padrão (5432)
- Maven (ou use o `mvnw` incluído no projeto)

### Passo a passo

1. Clone o repositório:
   ```
   git clone https://github.com/AngeloGabrielAlbonetti/AEP.git
   cd AEP
   ```

2. Crie o banco de dados no PostgreSQL:
   ```sql
   CREATE DATABASE aep;
   ```

3. Rode o script `database/script.sql` para criar as tabelas.

4. Confira as configurações em `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/aep
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```

5. Rode a aplicação:
   ```
   ./mvnw spring-boot:run
   ```

6. O menu aparece direto no terminal. Use as opções numeradas para cadastrar usuários, estabelecimentos, lotes e registrar doações ou descartes.

## Entrega

Trabalho da disciplina de Engenharia de Software — entrega em 13/11/2026.