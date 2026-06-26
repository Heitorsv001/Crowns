package utils;

import model.Carta;
import java.util.Random;


public class CalcularDano {

    private static final Random random = new Random();
    public static int calcularDanoAtaque(Carta atacante) {
        int danoBase = atacante.getDano();
        double chance = atacante.getChanceCritico();
        if (chance > 0 && random.nextDouble() < chance) {
            return danoBase * 2; 
        }
        return danoBase;
    }

  
    public static int calcularCura(Carta curandeira) {
        if (curandeira.getChanceCura() > 0
                && random.nextDouble() < curandeira.getChanceCura()) {
            return (int) (curandeira.getVidaMaxima() * curandeira.getPercentualCura());
        }
        return 0;
    }

    
    public static int calcularRecompensa(int recompensaBase, int arenaNumero) {
        return recompensaBase + (arenaNumero * 20);
    }
}
