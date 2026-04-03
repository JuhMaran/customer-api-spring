# Customer API REST

API REST para gerenciamento de clientes (**Customer**), com foco em consistência de dados, busca avançada e boas
práticas de arquitetura backend.

## Sumário

- [Objetivo](#objetivo)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Modelo de Domínio](#modelo-de-domínio)
- [Regras de Negócio](#regras-de-negócio)
- [Busca Avançada](#busca-avançada)
- [Endpoints](#endpoints)
- [Paginação](#paginação)
- [Cache](#cache)
- [Tratamento de Erros](#tratamento-de-erros)
- [Banco de Dados](#banco-de-dados)
- [Observabilidade](#observabilidade)
- [Pontos Importantes](#pontos-importantes)
- [Licença](#licença)
- [Autora & Desenvolvedora](#autora--desenvolvedora)

## Objetivo

Fornecer uma API robusta para:

- Cadastro e manutenção de clientes
- Controle de ativação/desativação (*soft delete*)
- Garantia de unicidade de e-mail
- Busca avançada baseada em parâmetros explícitos
- Observabilidade e cache para melhor performance

## Tecnologias

- Java 25 LTS (set/2025)
- Spring Framework 7 (nov/2025)
- Spring Boot 4.0.5 (nov/2025)
- Spring Data JPA + Hibernate
- MapStruct
- Lombok
- Flyway (versionamento de banco)
- MySQL
- H2 Database (ambiente de desenvolvimento/testes)
- Spring Actuator (monitoramento)
- Jakarta Bean Validation

## Funcionalidades

- CRUD completo de clientes
- Soft delete (desativação sem remoção física)
- Reativação com validação de integridade
- Busca avançada com múltiplos filtros combinados (AND)
- Paginação padrão
- Cache com invalidação automática
- Controle de concorrência (*optimistic locking*)
- Tratamento padronizado de erros
- Observabilidade com logs estruturados

## Modelo de Domínio

### Customer

| Campo            | Tipo     | Descrição                        |
|------------------|----------|----------------------------------|
| id               | UUID     | Identificador único              |
| fullName         | String   | Nome completo (3–150 caracteres) |
| email            | String   | Único globalmente                |
| phone            | String   | Formato E.164                    |
| phoneSearch      | String   | Apenas números (uso interno)     |
| status           | Boolean  | Ativo/Inativo                    |
| registrationDate | DateTime | Data de criação                  |
| lastUpdate       | DateTime | Última atualização               |
| version          | Long     | Controle de concorrência         |

## Regras de Negócio

### Criação

- Email deve ser único (mesmo para clientes inativos)
- Cliente inicia como ativo (`status = true`)
- Telefone é normalizado automaticamente

### Atualização

- `PUT`: atualização completa
- `PATCH`: atualização parcial (manual, sem mapper)
- Validação de email em caso de alteração

### Desativação

- Não remove do banco
- Apenas define `status = false`

### Reativação

- Permitida apenas para clientes inativos
- Bloqueada se houver outro cliente ativo com o mesmo email

## Busca Avançada

Endpoint:

```
GET /api/v1/customers/search
```

### Parâmetros

| Parâmetro | Descrição                        |
|-----------|----------------------------------|
| fullName  | Busca por nome (`LIKE`)          |
| email     | Busca por email (`LIKE`)         |
| phone     | Busca por telefone (normalizado) |
| status    | Ativo/Inativo                    |
| page      | Página                           |
| size      | Tamanho                          |

### Regras

- Combinação de filtros via **AND**
- Sem ambiguidade (campos explícitos)
- Busca vazia:
    - retorna ativos por padrão
- Sem resultados:
    - retorna `404`

## Endpoints

Base URL:

```
/api/v1/customers
````

| Método | Endpoint                   | Descrição            |
|--------|----------------------------|----------------------|
| POST   | `/`                        | Criar cliente        |
| GET    | `/{customerId}`            | Buscar por ID        |
| GET    | `/`                        | Listar ativos        |
| PUT    | `/{customerId}`            | Atualização completa |
| PATCH  | `/{customerId}`            | Atualização parcial  |
| PATCH  | `/{customerId}/deactivate` | Desativar            |
| PATCH  | `/{customerId}/reactivate` | Reativar             |
| GET    | `/search`                  | Busca avançada       |

## Paginação

- `page = 0` (default)
- `size = 10` (default)

## Cache

### Tipos

- `customers` → busca por ID
- `customersPage` → listagem
- `customersSearch` → busca

### Invalidação

- Executada automaticamente em operações de escrita

## Tratamento de Erros

Formato padrão:

```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "...",
  "path": "/api/..."
}
```

### Principais códigos

| Status | Descrição                  |
|--------|----------------------------|
| 400    | Validação inválida         |
| 404    | Não encontrado             |
| 409    | Conflito (email duplicado) |
| 500    | Erro interno               |

## Banco de Dados

- MySQL
- Versionamento com Flyway
- `ddl-auto: validate`

## Observabilidade

- Logs estruturados
- Níveis:
    - INFO → operações principais
    - DEBUG → consultas
    - TRACE → SQL detalhado
- Integração com Logbook + Logstash
- Endpoint `/health` monitorado

## Pontos Importantes

- Email é único globalmente
- Cliente nunca é removido fisicamente
- Telefone é normalizado automaticamente
- Busca não utiliza parsing genérico (sem ambiguidade)
- Cache é sempre invalidado em escrita

## Licença

Este projeto é destinado para **fins educacionais**

[Apache License 2.0](LICENSE)

## Autora & Desenvolvedora

### Juliane Maran

_Software Architecture & Development_  

