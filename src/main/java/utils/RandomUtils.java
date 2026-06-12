package utils;

import model.Carta;
import model.FabricaCartas;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    private static final Random random = new Random();

    public static boolean chance(double probabilidade) {
        return random.nextDouble() < probabilidade;
    }
    public static int entre(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public static <T> T escolher(List<T> lista) {
        if (lista == null || lista.isEmpty()) return null;
        return lista.get(random.nextInt(lista.size()));
    }

    public static Carta sortearCartaBonus(int arenaNumero) {
        switch (arenaNumero) {
            case 1: return chance(0.5) ? FabricaCartas.criarMorcego()    : FabricaCartas.criarGoblin();
            case 2: return chance(0.5) ? FabricaCartas.criarCurandeira() : FabricaCartas.criarDragao();
            case 3: return chance(0.5) ? FabricaCartas.criarPEKA()       : FabricaCartas.criarGolem();
            default: return FabricaCartas.criarEsqueleto();
        }
    }
}
