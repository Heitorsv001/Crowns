package model;

import java.util.ArrayList;
import java.util.List;


public class Inimigo {
    private String nome;
    private String descricao;
    private Deck deck;
    private int turnoAtual;
    private boolean ehBoss;

  
    private List<List<Integer>> padraoAtaque;
    public Inimigo(String nome, String descricao, Deck deck,
                   List<List<Integer>> padraoAtaque, boolean ehBoss) {
        this.nome        = nome;
        this.descricao   = descricao;
        this.deck        = deck;
        this.padraoAtaque = padraoAtaque;
        this.ehBoss      = ehBoss;
        this.turnoAtual  = 0;
    }


    public List<Carta> getCartasDoTurnoAtual() {
        if (padraoAtaque.isEmpty()) return new ArrayList<>();
        List<Integer> indices = padraoAtaque.get(turnoAtual % padraoAtaque.size());

        List<Carta> cartasDoTurno = new ArrayList<>();
        for (int indice : indices) {
            Carta c = deck.getCarta(indice);
            if (c != null && !c.estaMorta()) {
                cartasDoTurno.add(c);
            }
        }

        turnoAtual++;
        return cartasDoTurno;
    }

  
    public boolean foiDerrotado() {
        return deck.todasMortas();
    }

  
    public void resetarParaBatalha() {
        deck.resetarParaBatalha();
        turnoAtual = 0;
    }
    public static Inimigo criarRingo() {
        Deck deck = new Deck("Deck de Ringo");
        deck.adicionarCarta(FabricaCartas.criarEsqueleto());     
        deck.adicionarCarta(FabricaCartas.criarArqueira());      
        deck.adicionarCarta(FabricaCartas.criarGoblinLanceiro());
        deck.adicionarCarta(FabricaCartas.criarCavaleiro());     

        List<List<Integer>> padrao = new ArrayList<>();
        padrao.add(List.of(0)); 
        padrao.add(List.of(1)); 
        padrao.add(List.of(2)); 
        padrao.add(List.of(3)); 
       

        return new Inimigo(
            "Ringo",
            "Tutorialzinho.",
            deck, padrao, false
        );
    }

 
    public static Inimigo criarDiego() {
        Deck deck = new Deck("Deck de Diego");
        deck.adicionarCarta(FabricaCartas.criarEsqueleto()); 
        deck.adicionarCarta(FabricaCartas.criarMago());      
        deck.adicionarCarta(FabricaCartas.criarArqueira());  
        deck.adicionarCarta(FabricaCartas.criarMorcego());   

        List<List<Integer>> padrao = new ArrayList<>();
        padrao.add(List.of(0, 1)); 
        padrao.add(List.of(2, 3)); 

        return new Inimigo(
            "Diego",
            "",
            deck, padrao, false
        );
    }

 
    public static Inimigo criarValentine() {
        Deck deck = new Deck("Deck de Valentine");
        deck.adicionarCarta(FabricaCartas.criarMegaCavaleiro());
        deck.adicionarCarta(FabricaCartas.criarDragao());        
        deck.adicionarCarta(FabricaCartas.criarGoblin());        
        deck.adicionarCarta(FabricaCartas.criarPrincepe());     

        List<List<Integer>> padrao = new ArrayList<>();
        padrao.add(List.of(1, 2, 3)); 
        padrao.add(List.of(0));       
     

        return new Inimigo(
            "Valentine",
            "O boss final.",
            deck, padrao, true
        );
    }

  
    public String getNome()                      { return nome; }
    public String getDescricao()                 { return descricao; }
    public Deck getDeck()                        { return deck; }
    public int getTurnoAtual()                   { return turnoAtual; }
    public boolean isEhBoss()                    { return ehBoss; }
    public List<List<Integer>> getPadraoAtaque() { return padraoAtaque; }

    @Override
    public String toString() {
        return String.format("Inimigo{nome='%s', boss=%b, turno=%d, vivo=%b}",
                nome, ehBoss, turnoAtual, !foiDerrotado());
    }
}
