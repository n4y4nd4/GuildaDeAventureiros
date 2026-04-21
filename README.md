# Guilda de Aventureiros

API REST em Java 17 + Spring Boot 3.2.5 com JPA/PostgreSQL e Elasticsearch.

## Pré-requisitos

- Java 17+
- Maven 3.8+
- Docker

## Como rodar

### 1. Suba o PostgreSQL

```bash
# Windows
docker run -d -p 5432:5432 leogloriainfnet/postgres-tp2-spring:2.0-win

# Mac
docker run -d -p 5432:5432 leogloriainfnet/postgres-tp2-spring:2.0-mac
```

### 2. Suba o Elasticsearch

```bash
# Windows
docker run -d --name guilda-es -p 9200:9200 -e ES_JAVA_OPTS="-Xms512m -Xmx512m" leogloriainfnet/elastic-tp2-spring:1.0-windows

# Mac
docker run -d --name guilda-es -p 9200:9200 -e ES_JAVA_OPTS="-Xms512m -Xmx512m" leogloriainfnet/elastic-tp2-spring:1.0-mac
```

### 3. Rode a aplicação

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

Na inicialização, o `DatabaseSeeder` insere automaticamente 100+ aventureiros no banco caso o schema `operacoes` esteja vazio.

### 4. Rode os testes

```bash
mvn test
```

---

## Estrutura do Projeto

```
src/main/java/com/guilda/
├── GuildaApplication.java
├── audit/                          → mapeamento do schema audit (banco legado)
│   ├── entity/                     → Organizacao, Usuario, Role, Permission, ApiKey, AuditEntry
│   └── repository/                 → OrganizacaoRepository, UsuarioRepository, RoleRepository
├── aventura/                       → domínio principal (tabelas no schema operacoes)
│   ├── dto/                        → AventureiroResponse, MissaoResponse, CompanheiroResponse,
│   │                                  ParticipacaoResponse
│   ├── entity/                     → Aventureiro, Companheiro, Missao, ParticipacaoMissao
│   ├── enums/                      → ClasseAventureiro, EspecieCompanheiro, NivelPerigo,
│   │                                  StatusMissao, PapelMissao
│   ├── projection/                 → AventureiroDetalheView, RankingAventureiroView,
│   │                                  RelatorioMissaoView
│   ├── repository/                 → AventureiroRepository, MissaoRepository,
│   │                                  ParticipacaoMissaoRepository
│   ├── service/                    → AventureiroQueryService, MissaoQueryService,
│   │                                  ParticipacaoService
│   └── controller/                 → AventureiroQueryController (/aventureiros),
│                                      MissaoQueryController (/missoes)
├── operacoes/                      → painel tático (view somente leitura)
│   ├── dto/                        → PainelTaticoResponse
│   ├── entity/                     → PainelTaticoMissao (@Immutable)
│   ├── repository/                 → PainelTaticoRepository
│   ├── service/                    → PainelTaticoService (@Cacheable)
│   └── controller/                 → PainelTaticoController (GET /missoes/top15dias)
├── marketplace/                    → integração Elasticsearch
│   ├── dto/                        → ProdutoDTO
│   ├── service/                    → ElasticsearchService
│   └── controller/                 → ProdutoController (/produtos)
├── common/                         → infraestrutura compartilhada
│   ├── dto/                        → ErroResponse
│   ├── exception/                  → RequisicaoInvalidaException
│   └── handler/                    → GlobalExceptionHandler
└── config/                         → configurações de infraestrutura
    ├── CacheConfig.java            → Caffeine TTL 5min
    ├── ElasticsearchConfig.java    → cliente ES 8.x
    └── DatabaseSeeder.java         → seed de 100+ aventureiros
```

---

## Endpoints

### Aventureiros (`/aventureiros`)

| Método | Endpoint | Descrição | Retorno |
|--------|----------|-----------|---------|
| GET | `/aventureiros?orgId=1` | Listar com filtros e paginação | `List<AventureiroResponse>` |
| GET | `/aventureiros/busca?orgId=1&termo=al` | Busca textual parcial por nome | `List<AventureiroResponse>` |
| GET | `/aventureiros/{id}` | Detalhe com companheiro e participações | `AventureiroDetalheView` |
| GET | `/aventureiros/ranking?orgId=1` | Ranking por participações e recompensas | `List<RankingAventureiroView>` |

Filtros disponíveis na listagem: `ativo`, `classe`, `nivelMinimo`, `page`, `size`, `sort`.

### Missões (`/missoes`)

| Método | Endpoint | Descrição | Retorno |
|--------|----------|-----------|---------|
| GET | `/missoes/top15dias` | Top 10 dos últimos 15 dias (com cache) | `List<PainelTaticoResponse>` |
| GET | `/missoes?orgId=1` | Listar com filtros e paginação | `List<MissaoResponse>` |
| GET | `/missoes/{id}` | Detalhe com lista de participantes | `MissaoResponse` |
| GET | `/missoes/relatorio?orgId=1` | Relatório com métricas por missão | `List<RelatorioMissaoView>` |

Filtros disponíveis na listagem: `status`, `nivelPerigo`, `dataInicio`, `dataFim`.

### Marketplace — Elasticsearch (`/produtos`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/produtos/busca/nome?termo=espada` | Busca por nome (match) |
| GET | `/produtos/busca/descricao?termo=cura` | Busca por descrição (match) |
| GET | `/produtos/busca/frase?termo=cura superior` | Busca por frase exata (match_phrase) |
| GET | `/produtos/busca/fuzzy?termo=espdaa` | Busca tolerante a erros (fuzzy) |
| GET | `/produtos/busca/multicampos?termo=dragao` | Busca em nome e descrição (multi_match) |
| GET | `/produtos/busca/com-filtro?termo=pocao&categoria=pocoes` | Busca + filtro por categoria |
| GET | `/produtos/busca/faixa-preco?min=50&max=300` | Filtro por faixa de preço |
| GET | `/produtos/busca/avancada?categoria=armas&raridade=raro&min=200&max=1000` | Busca avançada combinada |
| GET | `/produtos/agregacoes/por-categoria` | Quantidade por categoria |
| GET | `/produtos/agregacoes/por-raridade` | Quantidade por raridade |
| GET | `/produtos/agregacoes/preco-medio` | Preço médio dos produtos |
| GET | `/produtos/agregacoes/faixas-preco` | Distribuição por faixas de preço |

---

## Banco de Dados

O banco PostgreSQL possui 3 schemas relevantes:

- `audit` — núcleo legado: organizações, usuários, papéis, permissões, auditoria
- `operacoes` — domínio de negócio: aventureiros, companheiros, missões, participações e a view `vw_painel_tatico_missao`
- `aventura` — schema existente no banco mas sem tabelas utilizadas pela aplicação

O código no pacote `aventura/` mapeia tabelas do schema `operacoes` — essa separação entre pacote e schema é intencional.

---

## Cache

O endpoint `GET /missoes/top15dias` consulta a view `vw_painel_tatico_missao`, que possui um `pg_sleep(4)` embutido para simular uma consulta pesada. O resultado é cacheado por 5 minutos via Caffeine.

```properties
spring.cache.type=caffeine
spring.cache.caffeine.spec=expireAfterWrite=5m,maximumSize=10
```

---

## DTOs de Resposta

Nenhuma entidade JPA é exposta diretamente na API. Todos os endpoints retornam DTOs:

| DTO | Usado em |
|-----|----------|
| `AventureiroResponse` | Listagem e busca de aventureiros |
| `MissaoResponse` | Listagem e detalhe de missões |
| `ParticipacaoResponse` | Dentro de `MissaoResponse` (detalhe) |
| `CompanheiroResponse` | Disponível para composição |
| `PainelTaticoResponse` | Endpoint top15dias |
| `ProdutoDTO` | Todos os endpoints de busca ES |
| `AventureiroDetalheView` | Detalhe do aventureiro (projeção JPQL) |
| `RankingAventureiroView` | Ranking (projeção JPQL) |
| `RelatorioMissaoView` | Relatório de missões (projeção JPQL) |

---

## Testes

```
src/test/java/com/guilda/
├── audit/AuditMappingTest.java            → 3 testes: mapeamento do schema audit
├── aventura/AventuraQueryTest.java        → 10 testes: consultas operacionais
└── operacoes/PainelTaticoServiceTest.java → 3 testes: service com mock + evidência no console
```
