# Customer API REST

## Tecnologias

* Java 25 LTS (set/2025)
* Spring Framework 7 (nov/2025)
* Spring Boot 4.0.5 (nov/2025)
* Spring Web
* Spring Data JPA
* MySQL Driver
* Flyway Migration
* Validation
* Project Lombok
* MapStruct
* Actuator

# Customer Entity

Representa o modelo de persistência do cliente na API.

| Campo              | Tipo          | Descrição                                                                   | Validação                                                         | Exemplo                                | Obrigatório |
|--------------------|---------------|-----------------------------------------------------------------------------|-------------------------------------------------------------------|----------------------------------------|-------------|
| `id`               | UUID          | Identificador único do cliente. Gerado automaticamente pelo Hibernate.      | Não é necessário informar, gerado automaticamente.                | `3fa85f64-5717-4562-b3fc-2c963f66afa6` | Sim         |
| `version`          | Integer       | Versão do registro para controle de concorrência otimista.                  | Valor gerenciado pelo JPA/Hibernate.                              | `1`                                    | Não         |
| `fullName`         | String        | Nome completo do cliente.                                                   | Mínimo 3 caracteres, máximo 150 caracteres.                       | `Juliane Maran`                        | Sim         |
| `email`            | String        | E-mail do cliente.                                                          | Formato válido de e-mail. Deve ser único.                         | `juliane@example.com`                  | Sim         |
| `phone`            | String        | Número de telefone no formato internacional E.164.                          | 11 a 15 dígitos. '+' opcional no início. Código do país opcional. | `+5511998765432`                       | Não         |
| `registrationDate` | LocalDateTime | Data de cadastro do cliente. Gerada automaticamente na criação do registro. | Não pode ser atualizada.                                          | `2026-04-02T10:30:00`                  | Sim         |
| `lastUpdate`       | LocalDateTime | Data da última atualização do registro. Atualizada automaticamente.         | Não precisa de validação manual.                                  | `2026-04-02T15:45:00`                  | Sim         |
| `status`           | Boolean       | Status do cliente: ativo (`true`) ou inativo (`false`).                     | Default `true`.                                                   | `true`                                 | Sim         |

## Observações de Validação

- **fullName**: mínimo 3 e máximo 150 caracteres.
- **email**: deve ser único e em formato válido.
- **phone**: suporta formatos como:
    - `+5511998765432` → Brasil com código do país
    - `11998765432` → Brasil sem código do país
- **registrationDate**: gerado automaticamente, não editável.
- **lastUpdate**: atualizado automaticamente sempre que o registro é modificado.
- **status**: padrão `true`, indica se o cliente está ativo.

## Licença

Este projeto é destinado para **fins educacionais**

[Apache License 2.0](LICENSE)

## Autora & Desenvolvedora

<h1 style="text-align: center;">Juliane Maran</h1>

<p style="text-align: center;">
  Software Architecture & Development
</p>
