CREATE SCHEMA IF NOT EXISTS aventura;

CREATE TABLE IF NOT EXISTS aventura.aventureiros (
    id               BIGSERIAL PRIMARY KEY,
    organizacao_id   BIGINT NOT NULL REFERENCES audit.organizacoes(id),
    cadastrado_por   BIGINT NOT NULL REFERENCES audit.usuarios(id),
    nome             VARCHAR(120) NOT NULL,
    classe           VARCHAR(30)  NOT NULL,
    nivel            INTEGER      NOT NULL CHECK (nivel >= 1),
    ativo            BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS aventura.companheiros (
    aventureiro_id   BIGINT PRIMARY KEY REFERENCES aventura.aventureiros(id) ON DELETE CASCADE,
    nome             VARCHAR(120) NOT NULL,
    especie          VARCHAR(30)  NOT NULL,
    lealdade         INTEGER      NOT NULL CHECK (lealdade BETWEEN 0 AND 100)
);

CREATE TABLE IF NOT EXISTS aventura.missoes (
    id               BIGSERIAL PRIMARY KEY,
    organizacao_id   BIGINT      NOT NULL REFERENCES audit.organizacoes(id),
    titulo           VARCHAR(150) NOT NULL,
    nivel_perigo     VARCHAR(20)  NOT NULL,
    status           VARCHAR(20)  NOT NULL DEFAULT 'PLANEJADA',
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    data_inicio      DATE,
    data_termino     DATE
);

CREATE TABLE IF NOT EXISTS aventura.participacoes_missao (
    missao_id        BIGINT      NOT NULL REFERENCES aventura.missoes(id) ON DELETE CASCADE,
    aventureiro_id   BIGINT      NOT NULL REFERENCES aventura.aventureiros(id) ON DELETE CASCADE,
    papel            VARCHAR(20)  NOT NULL,
    recompensa_ouro  NUMERIC(12,2) CHECK (recompensa_ouro >= 0),
    mvp              BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    PRIMARY KEY (missao_id, aventureiro_id)
);
