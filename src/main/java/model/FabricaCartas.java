package model;

/**
 * Fábrica responsável por criar instâncias das cartas do jogo com
 * os atributos exatos definidos no documento de requisitos.
 *
 * Todos os valores de vida, dano, custo e habilidades seguem a tabela
 * de cartas definida em RF/RN. O custo é derivado por RN10.
 *
 * Uso: Carta c = FabricaCartas.criarCavaleiro();
 */
public class FabricaCartas {

    // =========================================================
    // Cartas do jogo (14 no total)
    // =========================================================

    public static Carta criarEsqueleto() {
        // Vida 90, Dano 70, Custo 1 (vida < 100), Tipo HORDA — morrem fácil
        Carta c = new Carta("Esqueleto", TipoCarta.HORDA, 90, 70, "esqueleto.png");
        return c;
    }

    public static Carta criarArqueira() {
        // Vida 150, Dano 120, Custo 2 (vida < 200), Tipo DISTANCIA
        Carta c = new Carta("Arqueira", TipoCarta.DISTANCIA, 150, 120, "arqueira.png");
        return c;
    }

    public static Carta criarGoblinLanceiro() {
        // Vida 120, Dano 100, Custo 2 (vida < 200), Tipo HORDA
        Carta c = new Carta("Goblin Lanceiro", TipoCarta.HORDA, 120, 100, "goblin.png");
        return c;
    }

    public static Carta criarCavaleiro() {
        // Vida 420, Dano 140, Custo 4 (vida >= 400), Tipo TANQUE — equilibrado
        Carta c = new Carta("Cavaleiro", TipoCarta.TANQUE, 420, 140, "cavaleiro.png");
        return c;
    }

    public static Carta criarMago() {
        // Vida 220, Dano 170, Custo 3 (vida < 400), Tipo DISTANCIA
        Carta c = new Carta("Mago", TipoCarta.DISTANCIA, 220, 170, "mago.png");
        return c;
    }

    public static Carta criarMorcego() {
        // Vida 70, Dano 55, Custo 1 (vida < 100), Tipo HORDA — muito rápido
        Carta c = new Carta("Morcego", TipoCarta.HORDA, 70, 55, "morcego.png");
        return c;
    }

    public static Carta criarGoblin() {
        // Vida 140, Dano 115, Custo 2 (vida < 200), Tipo HORDA — ataque veloz
        Carta c = new Carta("Goblin", TipoCarta.HORDA, 140, 115, "goblin.png");
        return c;
    }

    public static Carta criarDragao() {
        // Vida 320, Dano 180, Custo 3 (vida < 400), Tipo DISTANCIA
        Carta c = new Carta("Dragão", TipoCarta.DISTANCIA, 320, 180, "dragao.png");
        return c;
    }

    public static Carta criarPrincepe() {
        // Vida 450, Dano 220, Custo 4 (vida >= 400), Tipo DESTRUIDOR — 25% crítico
        Carta c = new Carta("Príncipe", TipoCarta.DESTRUIDOR, 450, 220, "principe.png");
        c.setChanceCritico(0.25);
        return c;
    }

    public static Carta criarCurandeira() {
        // Vida 280, Dano 90, Custo 3 (vida < 400), Tipo SUPORTE
        // 20% de chance de curar 40% da vida máxima ao atacar (RN4)
        Carta c = new Carta("Curandeira", TipoCarta.SUPORTE, 280, 90, "curandeira.png");
        c.setChanceCura(0.20);
        c.setPercentualCura(0.40);
        return c;
    }

    public static Carta criarPEKA() {
        // Vida 650, Dano 320, Custo 4 (vida >= 400), Tipo DESTRUIDOR — destruidor pesado
        Carta c = new Carta("PEKA", TipoCarta.DESTRUIDOR, 650, 320, "peka.png");
        return c;
    }

    public static Carta criarGolem() {
        // Vida 900, Dano 120, Custo 4 (vida >= 400), Tipo TANQUE — super tank
        Carta c = new Carta("Golem", TipoCarta.TANQUE, 900, 120, "golem.png");
        return c;
    }

    public static Carta criarLavaHound() {
        // Vida 700, Dano 40, Custo 4 (vida >= 400), Tipo TANQUE — tank
        Carta c = new Carta("Lava Hound", TipoCarta.TANQUE, 700, 40, "lavahound.png");
        return c;
    }

    public static Carta criarMegaCavaleiro() {
        // Vida 800, Dano 280, Custo 4 (vida >= 400), Tipo ESPECIAL — 30% crítico, boss
        Carta c = new Carta("Mega Cavaleiro", TipoCarta.ESPECIAL, 800, 280, "megacavaleiro.png");
        c.setChanceCritico(0.30);
        return c;
    }
}
