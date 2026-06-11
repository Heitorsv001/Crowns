package utils;

import model.Carta;
import model.FabricaCartas;
import java.util.List;
import java.util.Random;

/**
 * Utilitários de aleatoriedade do jogo.
 */
public class RandomUtils {

    private static final Random random = new Random();

    /** Retorna true com a probabilidade informada (0.0 a 1.0). */
    public static boolean chance(double probabilidade) {
        return random.nextDouble() < probabilidade;
    }

    /** Retorna um inteiro aleatório entre min e max (inclusive). */
    public static int entre(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    /** Escolhe um elemento aleatório de uma lista. Retorna null se vazia. */
    public static <T> T escolher(List<T> lista) {
        if (lista == null || lista.isEmpty()) return null;
        return lista.get(random.nextInt(lista.size()));
    }

    /**
     * Sorteia uma carta bônus para recompensa com base na arena.
     * Arena 1 → Morcego ou Goblin
     * Arena 2 → Curandeira ou Dragão
     * Arena 3 → PEKA ou Golem
     */
    public static Carta sortearCartaBonus(int arenaNumero) {
        switch (arenaNumero) {
            case 1: return chance(0.5) ? FabricaCartas.criarMorcego()    : FabricaCartas.criarGoblin();
            case 2: return chance(0.5) ? FabricaCartas.criarCurandeira() : FabricaCartas.criarDragao();
            case 3: return chance(0.5) ? FabricaCartas.criarPEKA()       : FabricaCartas.criarGolem();
            default: return FabricaCartas.criarEsqueleto();
        }
    }
}
