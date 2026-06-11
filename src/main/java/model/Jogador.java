package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o jogador no jogo Crowns.
 *
 * Regras aplicadas:
 *  - RN2:  derrota encerra a run (HP = 0)
 *  - RN3:  nível das cartas persiste entre runs
 *  - RN15: reset de run mantém upgrades permanentes
 *  - RF1:  deck inicial com 4 cartas
 *  - RF6:  elixir regenera +2 a cada turno (RN9: começa fixo)
 *  - RNF4: elixir entre 0 e MAX_ELIXIR
 *  - RNF5: HP não fica negativo
 *  - RNF6: upgrades só com moedas suficientes
 */
public class Jogador {

    // =========================================================
    // Constantes
    // =========================================================
    public static final int HP_INICIAL       = 1000;
    public static final int MAX_ELIXIR       = 10;
    public static final int ELIXIR_INICIAL   = 5;
    public static final int REGENERACAO_ELIXIR = 2; // RF6: +2 por turno

    // =========================================================
    // Atributos persistentes (mantidos entre runs — RN3/RN15)
    // =========================================================
    private String nome;
    private Deck deck;
    private List<Carta> colecao;   // todas as cartas que o jogador possui
    private int moedas;
    private int totalVitorias;
    private int totalDerrotas;

    // =========================================================
    // Atributos de run (resetados ao perder — RN15)
    // =========================================================
    private int hpAtual;
    private int hpMaximo;
    private int elixirAtual;
    private int arenaAtual;        // índice da arena (0 = Ringo, 1 = Diego, 2 = Valentine)

    // =========================================================
    // Construtores
    // =========================================================
    public Jogador(String nome) {
        this.nome          = nome;
        this.moedas        = 0;
        this.totalVitorias = 0;
        this.totalDerrotas = 0;
        this.colecao       = new ArrayList<>();

        // Cria deck inicial com as 4 cartas de início de jogo (RF1)
        this.deck = criarDeckInicial();

        iniciarRun();
    }

    // =========================================================
    // Deck inicial (RF1)
    // =========================================================
    private Deck criarDeckInicial() {
        Deck d = new Deck("Deck Inicial");
        d.adicionarCarta(FabricaCartas.criarCavaleiro());
        d.adicionarCarta(FabricaCartas.criarArqueira());
        d.adicionarCarta(FabricaCartas.criarEsqueleto());
        d.adicionarCarta(FabricaCartas.criarGoblinLanceiro());

        // Adiciona as cartas iniciais também à coleção
        for (Carta c : d.getCartas()) {
            colecao.add(c);
        }
        return d;
    }

    // =========================================================
    // Gerenciamento de run
    // =========================================================

    /**
     * Inicia (ou reinicia) os atributos de run.
     * Mantém nível das cartas, moedas e coleção (RN15).
     */
    public void iniciarRun() {
        this.hpMaximo   = HP_INICIAL;
        this.hpAtual    = HP_INICIAL;
        this.elixirAtual = ELIXIR_INICIAL;
        this.arenaAtual  = 0;
        deck.resetarParaBatalha();
    }

    /**
     * Registra derrota: incrementa contador e reinicia run (RN2 / RN15).
     */
    public void registrarDerrota() {
        totalDerrotas++;
        iniciarRun();
    }

    /**
     * Registra vitória na arena atual e avança para a próxima (RN14).
     */
    public void registrarVitoria() {
        totalVitorias++;
        arenaAtual++;
    }

    // =========================================================
    // Elixir — RF6 / RNF4
    // =========================================================

    /** Regenera elixir no turno (+2, sem ultrapassar MAX_ELIXIR). */
    public void regenerarElixir() {
        elixirAtual = Math.min(MAX_ELIXIR, elixirAtual + REGENERACAO_ELIXIR);
    }

    /** Consome elixir ao usar uma carta. Nunca fica negativo (RNF4). */
    public boolean gastarElixir(int custo) {
        if (elixirAtual < custo) return false;
        elixirAtual = Math.max(0, elixirAtual - custo);
        return true;
    }

    // =========================================================
    // HP — RNF5
    // =========================================================

    /** Recebe dano. HP nunca fica negativo (RNF5). */
    public void receberDano(int dano) {
        hpAtual = Math.max(0, hpAtual - dano);
    }

    /** Verifica derrota (RN2). */
    public boolean estaDerrotado() {
        return hpAtual <= 0;
    }

    // =========================================================
    // Moedas e upgrades — RN6 / RNF6
    // =========================================================

    /** Adiciona moedas (recompensa de batalha). */
    public void adicionarMoedas(int quantidade) {
        moedas += quantidade;
    }

    /**
     * Tenta fazer upgrade de uma carta (RN6 / RNF6).
     * Custo = nível_atual × 20.
     * Retorna true se o upgrade foi realizado.
     */
    public boolean fazerUpgrade(Carta carta) {
        int custo = carta.getCustoUpgrade();
        if (moedas < custo) return false; // RNF6
        moedas -= custo;
        carta.aplicarUpgrade();
        return true;
    }

    /**
     * Tenta comprar uma carta da loja (RN5).
     * Preço = custo_elixir × 10.
     * Retorna true se a compra foi realizada.
     */
    public boolean comprarCarta(Carta carta) {
        int preco = carta.getPrecoCompra();
        if (moedas < preco) return false;
        moedas -= preco;
        colecao.add(carta);
        return true;
    }

    // =========================================================
    // Deck — RN13 (só fora da batalha)
    // =========================================================

    /**
     * Substitui carta no deck (RF8 / RN13).
     * Deve ser chamado apenas fora de batalha.
     */
    public void substituirCartaNoDeck(int posicao, Carta novaCarta) {
        deck.substituirCarta(posicao, novaCarta);
    }

    // =========================================================
    // Getters e Setters
    // =========================================================
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
