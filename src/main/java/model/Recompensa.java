package model;


public class Recompensa {

    private int    moedas;
    private Carta  cartaBonus;   
    private String mensagem;      
    private boolean temCartaBonus;

    

   
    public Recompensa(int moedas, Carta cartaBonus, String mensagem) {
        this.moedas       = moedas;
        this.cartaBonus   = cartaBonus;
        this.mensagem     = mensagem;
        this.temCartaBonus = cartaBonus != null;
    }

 
    public Recompensa(int moedas, String mensagem) {
        this(moedas, null, mensagem);
    }

    public static Recompensa recompensaArena1(boolean darCartaBonus) {
        Carta bonus = darCartaBonus ? FabricaCartas.criarMorcego() : null;
        return new Recompensa(
            50,
            bonus,
            "Ringo foi derrotado"
        );
    }

   
    public static Recompensa recompensaArena2(boolean darCartaBonus) {
        Carta bonus = darCartaBonus ? FabricaCartas.criarCurandeira() : null;
        return new Recompensa(
            100,
            bonus,
            "Diego foi derrotado."
        );
    }

   
    public static Recompensa recompensaArena3(boolean darCartaBonus) {
        Carta bonus = darCartaBonus ? FabricaCartas.criarPEKA() : null;
        return new Recompensa(
            200,
            bonus,
            "Valentine foi derrotado. parabens!!!Você completou o Crowns."
        );
    }

    public void aplicar(Jogador jogador) {
        jogador.adicionarMoedas(moedas);
        if (temCartaBonus && cartaBonus != null) {
            jogador.getColecao().add(cartaBonus);
        }
    }

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
