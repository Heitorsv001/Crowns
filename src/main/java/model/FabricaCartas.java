package model;
public class FabricaCartas {

    public static Carta criarEsqueleto() {
        Carta c = new Carta("Esqueleto", TipoCarta.HORDA, 90, 70, "esqueletos.png");
        return c;
    }

    public static Carta criarArqueira() {
        Carta c = new Carta("Arqueira", TipoCarta.DISTANCIA, 150, 120, "arqueira.png");
        return c;
    }

    public static Carta criarGoblinLanceiro() {
        Carta c = new Carta("Goblin Lanceiro", TipoCarta.HORDA, 120, 100, "goblin.png");
        return c;
    }

    public static Carta criarCavaleiro() {
        Carta c = new Carta("Cavaleiro", TipoCarta.TANQUE, 420, 140, "Cavaleiro.png");
        return c;
    }

    public static Carta criarMago() {
        Carta c = new Carta("Mago", TipoCarta.DISTANCIA, 220, 170, "mago.png");
        return c;
    }

    public static Carta criarMorcego() {
        Carta c = new Carta("Morcego", TipoCarta.HORDA, 70, 55, "morcego.png");
        return c;
    }

    public static Carta criarGoblin() {
        Carta c = new Carta("Goblin", TipoCarta.HORDA, 140, 115, "goblin.png");
        return c;
    }

    public static Carta criarDragao() {
        Carta c = new Carta("Dragão", TipoCarta.DISTANCIA, 320, 180, "dragao.png");
        return c;
    }

    public static Carta criarPrincepe() {
        Carta c = new Carta("Príncipe", TipoCarta.DESTRUIDOR, 450, 220, "principe.png");
        c.setChanceCritico(0.25);
        return c;
    }

    public static Carta criarCurandeira() {
        Carta c = new Carta("Curandeira", TipoCarta.SUPORTE, 280, 90, "curandeira.png");
        c.setChanceCura(0.20);
        c.setPercentualCura(0.40);
        return c;
    }

    public static Carta criarPEKA() {
        Carta c = new Carta("PEKA", TipoCarta.DESTRUIDOR, 650, 320, "peka.png");
        return c;
    }

    public static Carta criarGolem() {
        Carta c = new Carta("Golem", TipoCarta.TANQUE, 900, 120, "golem.png");
        return c;
    }

    public static Carta criarLavaHound() {
        Carta c = new Carta("Lava Hound", TipoCarta.TANQUE, 700, 40, "lavahound.png");
        return c;
    }

    public static Carta criarMegaCavaleiro() {
        Carta c = new Carta("Mega Cavaleiro", TipoCarta.ESPECIAL, 800, 280, "megacavaleiro.png");
        c.setChanceCritico(0.30);
        return c;
    }
}
