package model;

import java.util.ArrayList;
import java.util.List;
public class Jogador {

    public static final int HP_INICIAL       = 1000;
    public static final int MAX_ELIXIR       = 10;
    public static final int ELIXIR_INICIAL   = 5;
    public static final int REGENERACAO_ELIXIR = 2; 

    private String nome;
    private Deck deck;
    private List<Carta> colecao; 
    private int moedas;
    private int totalVitorias;
    private int totalDerrotas;

  
    private int hpAtual;
    private int hpMaximo;
    private int elixirAtual;
    private int arenaAtual;       

  
    public Jogador(String nome) {
        this.nome          = nome;
        this.moedas        = 0;
        this.totalVitorias = 0;
        this.totalDerrotas = 0;
        this.colecao       = new ArrayList<>();

        this.deck = criarDeckInicial();

        iniciarRun();
    }


    private Deck criarDeckInicial() {
        Deck d = new Deck("Deck Inicial");
        d.adicionarCarta(FabricaCartas.criarCavaleiro());
        d.adicionarCarta(FabricaCartas.criarArqueira());
        d.adicionarCarta(FabricaCartas.criarEsqueleto());
        d.adicionarCarta(FabricaCartas.criarGoblinLanceiro());

        for (Carta c : d.getCartas()) {
            colecao.add(c);
        }
        return d;
    }

 
    public void iniciarRun() {
        this.hpMaximo   = HP_INICIAL;
        this.hpAtual    = HP_INICIAL;
        this.elixirAtual = ELIXIR_INICIAL;
        this.arenaAtual  = 0;
        deck.resetarParaBatalha();
    }

    public void registrarDerrota() {
        totalDerrotas++;
        iniciarRun();
    }

   
    public void registrarVitoria() {
        totalVitorias++;
        arenaAtual++;
    }

    
    public void regenerarElixir() {
        elixirAtual = Math.min(MAX_ELIXIR, elixirAtual + REGENERACAO_ELIXIR);
    }

    public boolean gastarElixir(int custo) {
        if (elixirAtual < custo) return false;
        elixirAtual = Math.max(0, elixirAtual - custo);
        return true;
    }



    public void receberDano(int dano) {
        hpAtual = Math.max(0, hpAtual - dano);
    }

    public boolean estaDerrotado() {
        return hpAtual <= 0;
    }


    public void adicionarMoedas(int quantidade) {
        moedas += quantidade;
    }

  
    public boolean fazerUpgrade(Carta carta) {
        int custo = carta.getCustoUpgrade();
        if (moedas < custo) return false; 
        moedas -= custo;
        carta.aplicarUpgrade();
        return true;
    }

    public boolean comprarCarta(Carta carta) {
        int preco = carta.getPrecoCompra();
        if (moedas < preco) return false;
        moedas -= preco;
        colecao.add(carta);
        return true;
    }

    public void substituirCartaNoDeck(int posicao, Carta novaCarta) {
        deck.substituirCarta(posicao, novaCarta);
    }

  
    public String getNome()              { return nome; }
    public Deck getDeck()                { return deck; }
    public List<Carta> getColecao()      { return colecao; }
    public int getMoedas()               { return moedas; }
    public int getHpAtual()              { return hpAtual; }
    public int getHpMaximo()             { return hpMaximo; }
    public int getElixirAtual()          { return elixirAtual; }
    public int getArenaAtual()           { return arenaAtual; }
    public int getTotalVitorias()        { return totalVitorias; }
    public int getTotalDerrotas()        { return totalDerrotas; }

    public void setNome(String nome)     { this.nome = nome; }
    public void setMoedas(int moedas)    { this.moedas = moedas; }
    public void setHpAtual(int hp)       { this.hpAtual = Math.max(0, hp); }
    public void setHpMaximo(int hp)      { this.hpMaximo = hp; }
    public void setElixirAtual(int e)    { this.elixirAtual = Math.min(MAX_ELIXIR, Math.max(0, e)); }
    public void setArenaAtual(int arena) { this.arenaAtual = arena; }
    public void setTotalVitorias(int v)  { this.totalVitorias = v; }
    public void setTotalDerrotas(int d)  { this.totalDerrotas = d; }
    public void setDeck(Deck deck)       { this.deck = deck; }
    public void setColecao(List<Carta> c){ this.colecao = c; }

    @Override
    public String toString() {
        return String.format("Jogador{nome='%s', hp=%d/%d, elixir=%d, moedas=%d, arena=%d}",
                nome, hpAtual, hpMaximo, elixirAtual, moedas, arenaAtual);
    }
}
