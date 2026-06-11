package utils;

import model.Carta;
import java.util.Random;

/**
 * Utilitário de cálculo de dano.
 * Centraliza toda lógica matemática de batalha para manter
 * GameplayController limpo.
 */
public class CalcularDano {

    private static final Random random = new Random();

    /**
     * Calcula o dano que uma carta causa, aplicando crítico se houver.
     * Príncipe: 25% chance de dano dobrado.
     * Mega Cavaleiro: 30% chance de dano dobrado.
     */
    public static int calcularDanoAtaque(Carta atacante) {
        int danoBase = atacante.getDano();
        double chance = atacante.getChanceCritico();
        if (chance > 0 && random.nextDouble() < chance) {
            return danoBase * 2; // crítico
        }
        return danoBase;
    }

    /**
     * Calcula cura da Curandeira: 20% de chance de curar 40% da vida máxima.
     * Retorna 0 se não ativou.
     */
    public static int calcularCura(Carta curandeira) {
        if (curandeira.getChanceCura() > 0
                && random.nextDouble() < curandeira.getChanceCura()) {
            return (int) (curandeira.getVidaMaxima() * curandeira.getPercentualCura());
        }
        return 0;
    }

    /**
     * Calcula recompensa em moedas pela vitória numa arena.
     * Fórmula: recompensaBase + (arenaNumero * 20)
     */
    public static int calcularRecompensa(int recompensaBase, int arenaNumero) {
        return recompensaBase + (arenaNumero * 20);
    }
}
