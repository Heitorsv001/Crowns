package model;

/**
 * Define os tipos de carta disponíveis no jogo.
 * Cada tipo influencia o comportamento e a estratégia de uso.
 */
public enum TipoCarta {
    TANQUE,     // Alta vida, dano moderado (Cavaleiro, Golem, PEKA)
    DESTRUIDOR, // Alto dano, vida moderada (PEKA, Dragão, Príncipe)
    SUPORTE,    // Habilidades especiais (Curandeira)
    HORDA,      // Baixo custo, múltiplos usos rápidos (Esqueletos, Morcego, Goblin)
    DISTANCIA,  // Ataque à distância (Arqueira, Mago, Dragão)
    ESPECIAL    // Boss / inimigos únicos (Mega Cavaleiro)
}
