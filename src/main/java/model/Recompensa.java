package model;

/**
 * Representa a recompensa recebida ao vencer uma batalha.
 *
 * Regras aplicadas:
 *  - RN12: jogador recebe moedas ao vencer qualquer batalha
 *  - RF4:  ao vencer, jogador recebe moedas e pode escolher uma carta bônus
 *  - RF5:  progressão: inimigos ficam mais difíceis
 */
public class Recompensa {

    // =========================================================
    // Atributos
    // =========================================================
    private int    moedas;
    private Carta  cartaBonus;    // pode ser null (nem toda vitória dá carta bônus)
    private String mensagem;      // texto exibido na tela de recompensas
    private boolean temCartaBonus;

    // =========================================================
    // Construtores
    // =========================================================

    /** Recompensa com moedas e carta bônus (RF4). */
    public Recompensa(int moedas, Carta cartaBonus, String mensagem) {
        this.moedas       = moedas;
        this.cartaBonus   = cartaBonus;
        this.mensagem     = mensagem;
        this.temCartaBonus = cartaBonus != null;
    }

    /** Recompensa apenas com moedas (sem carta bônus). */
    public Recompensa(int moedas, String mensagem) {
        this(moedas, null, mensagem);
    }

    // =========================================================
    // Fábricas por arena
    // =========================================================

    /**
     * Recompensa da Arena 1 (Ringo).
     * 50 moedas + carta bônus: Morcego ou Goblin (sorteado em CalcularDano/RandomUtils).
     */
    public static Recompensa recompensaArena1(boolean darCartaBonus) {
        Carta bonus = darCartaBonus ? FabricaCartas.criarMorcego() : null;
        return new Recompensa(
            50,
            bonus,
            "Ringo foi derrotado! Você provou que tem potencial, aventureiro."
        );
    }

    /**
     * Recompensa da Arena 2 (Diego).
     * 100 moedas + carta bônus: Dragão ou Curandeira.
     */
    public static Recompensa recompensaArena2(boolean darCartaBonus) {
        Carta bonus = darCartaBonus ? FabricaCartas.criarCurandeira() : null;
        return new Recompensa(
            100,
            bonus,
            "Diego caiu! Sua estratégia foi superior. As sombras recuam."
        );
    }

    /**
     * Recompensa da Arena 3 — Boss Final (Valentine).
     * 200 moedas + carta bônus: PEKA ou Golem.
     */
    public static Recompensa recompensaArena3(boolean darCartaBonus) {
        Carta bonus = darCartaBonus ? FabricaCartas.criarPEKA() : null;
        return new Recompensa(
            200,
            bonus,
            "Valentine foi derrotado! O Trono de Ferro é seu. Você completou o Crowns!"
        );
    }

    // =========================================================
    // Aplica a recompensa ao jogador
    // =========================================================

    /**
     * Aplica moedas e carta bônus ao jogador (se houver).
     */
    public void aplicar(Jogador jogador) {
        jogador.adicionarMoedas(moedas);
        if (temCartaBonus && cartaBonus != null) {
            jogador.getColecao().add(cartaBonus);
        }
    }

    // =========================================================
    // Getters
    // =========================================================
    public int    getMoedas()       { return moedas; }
    public Carta  getCartaBonus()   { return cartaBonus; }
    public String getMensagem()     { return mensagem; }
    public boolean isTemCartaBonus(){ return temCartaBonus; }

    @Override
    public String toString() {
        return String.format("Recompensa{moedas=%d, cartaBonus=%s, mensagem='%s'}",
                moedas,
                temCartaBonus ? cartaBonus.getNome() : "nenhuma",
                mensagem);
    }
}
