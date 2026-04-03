# Customer API REST

API RESTful completa para gerenciamento de clientes, construída com **Spring Boot**, **Spring Data JPA**, **Hibernate**,
**MapStruct** e **Flyway**. A API implementa **CRUD**, **exclusão lógica** e **reativação de clientes**, seguindo boas
práticas de desenvolvimento backend.

## Sumário

* [Tecnologias](#tecnologias)
* [Funcionalidades](#funcionalidades)
* [Customer Entity](#customer-entity)
* [Endpoints REST](#endpoints-rest)
* [Validações e Exceções](#validações-e-exceções)
* [Configuração](#configuração)
* [Executando a Aplicação](#executando-a-aplicação)
* [Exemplos de Payload](#exemplos-de-payload)
* [Exemplos de Response Body](#exemplos-de-response-body)
* [Licença](#licença)
* [Autora & Desenvolvedora](#autora--desenvolvedora)

## Tecnologias

* Java 25 LTS (set/2025)
* Spring Framework 7 (nov/2025)
* Spring Boot 4.0.5 (nov/2025)
* Spring Data JPA + Hibernate
* MapStruct
* Lombok
* Flyway (versionamento de banco)
* MySQL
* Spring Actuator
* Jakarta Bean Validation
* H2 Database

## Funcionalidades

* Cadastro de clientes (`POST /customers`)
* Consulta de cliente por ID (`GET /customers/{id}`)
* Listagem de clientes ativos (`GET /customers`)
* Atualização total (`PUT /customers/{id}`)
* Atualização parcial (`PATCH /customers/{id}`)
* Exclusão lógica (`DELETE /customers/{id}` → `status = FALSE`)
* Reativação de clientes (`PATCH /customers/{id}/reactivate` → `status = TRUE`)
* Validação de dados com mensagens claras
* Controle de concorrência otimista (`@Version`)
* Timestamps automáticos (`registrationDate`, `lastUpdate`)

## Customer Entity

Modelo de persistência do cliente:

| Campo              | Tipo          | Descrição                                                   | Validação                     | Exemplo                                | Obrigatório |
|--------------------|---------------|-------------------------------------------------------------|-------------------------------|----------------------------------------|-------------|
| `id`               | UUID          | Identificador único, gerado automaticamente pelo Hibernate. | Não informado pelo usuário    | `3fa85f64-5717-4562-b3fc-2c963f66afa6` | Sim         |
| `version`          | Integer       | Controle de concorrência otimista.                          | Gerenciado pelo JPA/Hibernate | `1`                                    | Não         |
| `fullName`         | String        | Nome completo do cliente.                                   | 3–150 caracteres              | `Juliane Maran`                        | Sim         |
| `email`            | String        | E-mail do cliente.                                          | Único, formato válido         | `juliane@example.com`                  | Sim         |
| `phone`            | String        | Número de telefone internacional E.164.                     | 11–15 dígitos, '+' opcional   | `+5511998765432`                       | Não         |
| `registrationDate` | LocalDateTime | Data de cadastro, gerada automaticamente.                   | Não editável                  | `2026-04-02T10:30:00`                  | Sim         |
| `lastUpdate`       | LocalDateTime | Data da última atualização, atualizada automaticamente.     | Automático                    | `2026-04-02T15:45:00`                  | Sim         |
| `status`           | Boolean       | Status ativo (`true`) ou inativo (`false`).                 | Default `true`                | `true`                                 | Sim         |

## Endpoints REST

Base URL: `http://localhost:8081/api/v1/customers`

| Método | Endpoint           | Descrição                        | Status HTTP                   |
|--------|--------------------|----------------------------------|-------------------------------|
| POST   | `/`                | Criar cliente                    | 201 Created + Location header |
| GET    | `/`                | Listar clientes ativos           | 200 OK                        |
| GET    | `/{id}`            | Buscar cliente por ID            | 200 OK                        |
| PUT    | `/{id}`            | Atualização total                | 204 No Content                |
| PATCH  | `/{id}`            | Atualização parcial              | 204 No Content                |
| DELETE | `/{id}`            | Exclusão lógica (status = FALSE) | 204 No Content                |
| PATCH  | `/{id}/reactivate` | Reativar cliente (status = TRUE) | 204 No Content                |

## Validações e Exceções

* **Email já cadastrado** → `EmailAlreadyExistsException` (HTTP 409 Conflict)
* **Cliente não encontrado** → `CustomerNotFoundException` (HTTP 404 Not Found)
* **DTO inválido** → `MethodArgumentNotValidException` (HTTP 400 Bad Request)
* **Validações de path/params** → `ConstraintViolationException` (HTTP 400)
* **Exceção genérica** → HTTP 500 Internal Server Error

Exceções tratadas globalmente via `@RestControllerAdvice` com **logs estruturados**:

* `WARN` → erros 4xx
* `ERROR` → erros 5xx
* `log.info` → ações importantes
* `log.debug` → leitura
* `log.warn` → fallback / comportamento inesperado

## Configuração

Arquivo `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/customer_db
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate.format_sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration
server:
  port: 8081
logging:
  level:
    root: INFO
    com.juhmaran.customerapi: DEBUG
```

## Executando a Aplicação

1. Clonar o repositório:

    ```bash
    git clone <repo-url>
    cd customer-api-spring
    ```

2. Configurar o banco de dados MySQL e atualizar `application.yaml`.

3. Executar com Maven:

    ```bash
    ./mvnw spring-boot:run
    ```

4. Endpoints disponíveis em: `http://localhost:8081/api/v1/customers`

## Exemplos de Payload

### Get Customer By ID

```bash
curl --location 'http://localhost:8081/api/v1/customers/91dfd2b2-0ec3-4381-95d5-2f54900944bf'
```

### Register New Customer (Body Vazio + Location Header)

```bash
curl --location 'http://localhost:8081/api/v1/customers' \
--header 'Content-Type: application/json' \
--data-raw '{
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "phone": "11998765432"
}' \
--include
```

> **Observação:** O POST retorna **status 201**, **body vazio** e o header `Location` aponta para o recurso criado, por
> exemplo:
>
> ```
> HTTP/1.1 201 Created
> Location: /api/v1/customers/91dfd2b2-0ec3-4381-95d5-2f54900944bf
> ```

## Exemplos de Response Body

### Sucesso – GET Customer

```json
{
  "id": "91dfd2b2-0ec3-4381-95d5-2f54900944bf",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "phone": "11998765432",
  "registrationDate": "2026-04-02T12:17:58",
  "lastUpdate": "2026-04-02T12:17:58",
  "status": true
}
```

### Erro – 400 Bad Request (DTO inválido)

```json
{
  "timestamp": "2026-04-02T13:25:02.9131373",
  "status": 400,
  "error": "Bad Request",
  "message": "email: Email is required",
  "path": "/api/v1/customers"
}
```

### Erro – 404 Not Found

```json
{
  "timestamp": "2026-04-02T13:10:42.6037993",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente não encontrado",
  "path": "/api/v1/customers/91dfd2b2-0ec3-4381-95d5-2f54900944bf"
}
```

### Erro – 409 Conflict (Email já cadastrado)

```json
{
  "timestamp": "2026-04-02T13:23:29.8127067",
  "status": 409,
  "error": "Conflict",
  "message": "Email já cadastrado",
  "path": "/api/v1/customers"
}
```

### Erro – 500 Internal Server Error

```json
{
  "timestamp": "2026-04-02T13:11:06.6135247",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Ocorreu um erro inesperado",
  "path": "/api/v1/customers/:customerId"
}
```

## Licença

Este projeto é destinado para **fins educacionais**

[Apache License 2.0](LICENSE)

## Autora & Desenvolvedora

### Juliane Maran

_Software Architecture & Development_  

