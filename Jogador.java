package model;


public class Jogador {

    public static final int ELIXIR_MAXIMO = 10;
    public static final int ELIXIR_INICIAL = 5;
    public static final int HP_INICIAL = 1000;

    private int id;
    private String nome;
    private int hp;
    private int hpMaximo;
    private int moedas;
    private int elixir;
    private int arenaAtual;
    private Deck deck;

    public Jogador(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.hp = HP_INICIAL;
        this.hpMaximo = HP_INICIAL;
        this.moedas = 0;
        this.elixir = ELIXIR_INICIAL;
        this.arenaAtual = 1;
        this.deck = new Deck();
    }


    public void regenerarElixir() {
        elixir = Math.min(ELIXIR_MAXIMO, elixir + 2);
    }

    public boolean podUsarCarta(Carta carta) {
        return elixir >= carta.getCustoElixir();
    }

    public boolean gastarElixir(int custo) {
        if (elixir < custo) return false;
        elixir = Math.max(0, elixir - custo);
        return true;
    }

    public void resetarElixir() {
        elixir = ELIXIR_INICIAL;
    }


    public void receberDano(int dano) {
        hp = Math.max(0, hp - dano);
    }

    public void curar(int quantidade) {
        hp = Math.min(hpMaximo, hp + quantidade);
    }

    public boolean estaVivo() {
        return hp > 0;
    }

    public void resetarHP() {
        hp = hpMaximo;
    }


    public void adicionarMoedas(int quantidade) {
        moedas += quantidade;
    }

    public boolean gastarMoedas(int quantidade) {
        if (moedas < quantidade) return false;
        moedas -= quantidade;
        return true;
    }


    public void avancarArena() {
        arenaAtual++;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(hpMaximo, hp)); }

    public int getHpMaximo() { return hpMaximo; }
    public void setHpMaximo(int hpMaximo) { this.hpMaximo = hpMaximo; }

    public int getMoedas() { return moedas; }
    public void setMoedas(int moedas) { this.moedas = moedas; }

    public int getElixir() { return elixir; }
    public void setElixir(int elixir) { this.elixir = Math.max(0, Math.min(ELIXIR_MAXIMO, elixir)); }

    public int getArenaAtual() { return arenaAtual; }
    public void setArenaAtual(int arenaAtual) { this.arenaAtual = arenaAtual; }

    public Deck getDeck() { return deck; }
    public void setDeck(Deck deck) { this.deck = deck; }

    @Override
    public String toString() {
        return String.format("[%s | HP: %d/%d | Elixir: %d | Moedas: %d | Arena: %d]",
                nome, hp, hpMaximo, elixir, moedas, arenaAtual);
    }
}