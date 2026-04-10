# Guilda de Aventureiros

API REST em Java 17 + Spring Boot 3.2.5 com JPA/PostgreSQL.

## Pré-requisitos

- Java 17+
- Maven 3.8+
- Docker

## Subir o banco

```bash
# Windows/amd64
docker run -d -p 5432:5432 leogloriainfnet/postgres-tp2-spring:2.0-win

# Mac/arm64
docker run -d -p 5432:5432 leogloriainfnet/postgres-tp2-spring:2.0-mac
```

## Criar o schema aventura

Após o banco subir, execute o script:

```bash
docker exec -i $(docker ps -q --filter ancestor=leogloriainfnet/postgres-tp2-spring:2.0-win) \
  psql -U postgres -d postgres -f - < src/main/resources/db/aventura-schema.sql
```

## Rodar a aplicação

```bash
mvn spring-boot:run
```

## Rodar os testes

```bash
mvn test
```

## Endpoints principais (Parte 1 — em memória)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | /aventureiros | Registrar aventureiro |
| GET | /aventureiros | Listar com filtros e paginação |
| GET | /aventureiros/{id} | Consultar por id |
| PUT | /aventureiros/{id} | Atualizar nome/classe/nível |
| PATCH | /aventureiros/{id}/desativar | Encerrar vínculo |
| PATCH | /aventureiros/{id}/reativar | Recrutar novamente |
| PUT | /aventureiros/{id}/companheiro | Definir/substituir companheiro |
| DELETE | /aventureiros/{id}/companheiro | Remover companheiro |
