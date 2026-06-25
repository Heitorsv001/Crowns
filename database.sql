CREATE TABLE cartas (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    tipo VARCHAR(20) NOT NULL,
    vida_base INTEGER NOT NULL CHECK (vida_base > 0),
    dano_base INTEGER NOT NULL CHECK (dano_base >= 0),
    custo INTEGER NOT NULL CHECK (custo > 0),
    imagem_path VARCHAR(100) NOT NULL,
    chance_critico NUMERIC(4,2) DEFAULT 0.00,
    chance_cura NUMERIC(4,2) DEFAULT 0.00,
    percentual_cura NUMERIC(4,2) DEFAULT 0.00
);

INSERT INTO cartas (
    nome,
    tipo,
    vida_base,
    dano_base,
    custo,
    imagem_path,
    chance_critico,
    chance_cura,
    percentual_cura
)
VALUES
    ('Esqueleto',       'HORDA',       90,  70, 1, 'esqueletos.png',       0.00, 0.00, 0.00),
    ('Arqueira',        'DISTANCIA',  150, 120, 2, 'arqueira.png',         0.00, 0.00, 0.00),
    ('Goblin Lanceiro', 'HORDA',      120, 100, 2, 'goblin_lanceiro.png',  0.00, 0.00, 0.00),
    ('Cavaleiro',       'TANQUE',     420, 140, 4, 'Cavaleiro.png',        0.00, 0.00, 0.00),
    ('Mago',            'DISTANCIA',  220, 170, 3, 'mago.png',             0.00, 0.00, 0.00),
    ('Morcego',         'HORDA',       70,  55, 1, 'morcegos.png',         0.00, 0.00, 0.00),
    ('Goblin',          'HORDA',      140, 115, 2, 'goblin.png',           0.00, 0.00, 0.00),
    ('Dragao',          'DISTANCIA',  320, 180, 3, 'dragao.png',           0.00, 0.00, 0.00),
    ('Principe',        'DESTRUIDOR', 450, 220, 4, 'principe.png',         0.25, 0.00, 0.00),
    ('Curandeira',      'SUPORTE',    280,  90, 3, 'curandeira.png',       0.00, 0.20, 0.40),
    ('PEKA',            'DESTRUIDOR', 650, 320, 4, 'pekka.png',            0.00, 0.00, 0.00),
    ('Golem',           'TANQUE',     900, 120, 4, 'golem.png',            0.00, 0.00, 0.00),
    ('Lava Hound',      'TANQUE',     700,  40, 4, 'lava-hound.png',       0.00, 0.00, 0.00),
    ('Mega Cavaleiro',  'ESPECIAL',   800, 280, 4, 'mega-cavaleiro.png',   0.30, 0.00, 0.00);

CREATE TABLE jogadores (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(80) NOT NULL UNIQUE,
    moedas INTEGER NOT NULL DEFAULT 0,
    total_vitorias INTEGER NOT NULL DEFAULT 0,
    total_derrotas INTEGER NOT NULL DEFAULT 0,
    arena_atual INTEGER NOT NULL DEFAULT 0,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE jogador_cartas (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    jogador_id INTEGER NOT NULL,
    carta_id INTEGER NOT NULL,
    nivel INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_jogador_cartas_jogador
        FOREIGN KEY (jogador_id)
        REFERENCES jogadores(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_jogador_cartas_carta
        FOREIGN KEY (carta_id)
        REFERENCES cartas(id),

    CONSTRAINT uq_jogador_carta
        UNIQUE (jogador_id, carta_id)
);

CREATE TABLE deck_jogador (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    jogador_id INTEGER NOT NULL,
    carta_id INTEGER NOT NULL,
    posicao INTEGER NOT NULL CHECK (posicao BETWEEN 1 AND 4),

    CONSTRAINT fk_deck_jogador_jogador
        FOREIGN KEY (jogador_id)
        REFERENCES jogadores(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_deck_jogador_carta
        FOREIGN KEY (carta_id)
        REFERENCES cartas(id),

    CONSTRAINT uq_deck_posicao
        UNIQUE (jogador_id, posicao)
);

CREATE TABLE historico_runs (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    jogador_id INTEGER NOT NULL,
    data_run TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    arenas_vencidas INTEGER NOT NULL DEFAULT 0,
    resultado VARCHAR(10) NOT NULL
        CHECK (resultado IN ('VITORIA', 'DERROTA')),
    moedas_ganhas INTEGER NOT NULL DEFAULT 0,
    carta_bonus VARCHAR(50),

    CONSTRAINT fk_historico_runs_jogador
        FOREIGN KEY (jogador_id)
        REFERENCES jogadores(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_jogador_cartas_jogador
    ON jogador_cartas(jogador_id);

CREATE INDEX idx_deck_jogador_jogador
    ON deck_jogador(jogador_id);

CREATE INDEX idx_historico_jogador
    ON historico_runs(jogador_id);

CREATE INDEX idx_historico_data
    ON historico_runs(data_run DESC);

DROP VIEW IF EXISTS ranking;

CREATE VIEW ranking AS
SELECT
    ROW_NUMBER() OVER (
        ORDER BY total_vitorias DESC,
                 total_derrotas ASC
    ) AS posicao,
    nome,
    total_vitorias AS vitorias,
    total_derrotas AS derrotas,
    moedas,
    criado_em
FROM jogadores;
