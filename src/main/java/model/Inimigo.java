package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um inimigo do jogo Crowns.
 *
 * Cada inimigo possui 4 cartas e um padrão de ataque FIXO por turno (RF9).
 * Os três inimigos definidos são criados via métodos estáticos de fábrica:
 *
 *  - Ringo    (Inimigo 1 — tutorial): usa 1 carta por turno, em sequência
 *  - Diego    (Inimigo 2):            usa 2 cartas por turno
 *  - Valentine(Inimigo 3 — boss):     usa 3 cartas no turno 1, Mega Cavaleiro no último
 *
 * Regras aplicadas: RF9, RF10 (boss), RN14 (progressão de arena)
 */
public class Inimigo {

    // =========================================================
    // Atributos
    // =========================================================
    private String nome;
    private String descricao;
    private Deck deck;
    private int turnoAtual;
    private boolean ehBoss;

    /**
     * Padrão de ataque: lista de listas de índices.
     * padraoAtaque.get(turno) → índices das cartas jogadas naquele turno.
     * Quando os turnos acabam, o padrão se repete (módulo do tamanho).
     */
    private List<List<Integer>> padraoAtaque;

    // =========================================================
    // Construtor
    // =========================================================
    public Inimigo(String nome, String descricao, Deck deck,
                   List<List<Integer>> padraoAtaque, boolean ehBoss) {
        this.nome        = nome;
        this.descricao   = descricao;
        this.deck        = deck;
        this.padraoAtaque = padraoAtaque;
        this.ehBoss      = ehBoss;
        this.turnoAtual  = 0;
    }

    // =========================================================
    // Lógica de turno
    // =========================================================

    /**
     * Retorna as cartas que o inimigo vai usar no turno atual.
     * Filtra apenas cartas ainda vivas antes de retornar.
     * Avança o contador de turno.
     */
    public List<Carta> getCartasDoTurnoAtual() {
        if (padraoAtaque.isEmpty()) return new ArrayList<>();

        // Para Valentine (boss), o último turno sempre usa o Mega Cavaleiro (índice 0)
        // Os turnos iniciais usam índices 1,2,3. Quando só sobrar índice 0, usa ele.
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

    /**
     * Verifica se o inimigo foi derrotado (todas as cartas mortas — RN11).
     */
    public boolean foiDerrotado() {
        return deck.todasMortas();
    }

    /**
     * Reseta o inimigo para um novo combate (mantém upgrades de nível — RN3).
     */
    public void resetarParaBatalha() {
        deck.resetarParaBatalha();
        turnoAtual = 0;
    }

    // =========================================================
    // Fábricas dos três inimigos
    // =========================================================

    /**
     * RINGO — Inimigo 1 (tutorial).
     * Deck: Esqueleto, Arqueira, Goblin Lanceiro, Cavaleiro
     * Padrão: 1 carta por turno, em sequência (índices 0→1→2→3→0→...)
     */
    public static Inimigo criarRingo() {
        Deck deck = new Deck("Deck de Ringo");
        deck.adicionarCarta(FabricaCartas.criarEsqueleto());     // índice 0
        deck.adicionarCarta(FabricaCartas.criarArqueira());      // índice 1
        deck.adicionarCarta(FabricaCartas.criarGoblinLanceiro());// índice 2
        deck.adicionarCarta(FabricaCartas.criarCavaleiro());     // índice 3

        // Padrão: 1 carta por turno, rotacionando em ordem
        List<List<Integer>> padrao = new ArrayList<>();
        padrao.add(List.of(0)); // turno 1: Esqueleto
        padrao.add(List.of(1)); // turno 2: Arqueira
        padrao.add(List.of(2)); // turno 3: Goblin Lanceiro
        padrao.add(List.of(3)); // turno 4: Cavaleiro
        // depois do turno 4 repete do início (módulo)

        return new Inimigo(
            "Ringo",
            "Um iniciante imprudente. Bom para aprender as mecânicas.",
            deck, padrao, false
        );
    }

    /**
     * DIEGO — Inimigo 2.
     * Deck: Esqueleto, Mago, Arqueira, Morcego
     * Padrão: 2 cartas por turno
     *   Turno 1: Esqueleto + Mago     (índices 0 e 1)
     *   Turno 2: Arqueira + Morcego   (índices 2 e 3)
     *   Depois repete
     */
    public static Inimigo criarDiego() {
        Deck deck = new Deck("Deck de Diego");
        deck.adicionarCarta(FabricaCartas.criarEsqueleto()); // índice 0
        deck.adicionarCarta(FabricaCartas.criarMago());      // índice 1
        deck.adicionarCarta(FabricaCartas.criarArqueira());  // índice 2
        deck.adicionarCarta(FabricaCartas.criarMorcego());   // índice 3

        List<List<Integer>> padrao = new ArrayList<>();
        padrao.add(List.of(0, 1)); // turno 1: Esqueleto + Mago
        padrao.add(List.of(2, 3)); // turno 2: Arqueira + Morcego
        // repete

        return new Inimigo(
            "Diego",
            "Um duelista que sempre ataca em pares. Não subestime.",
            deck, padrao, false
        );
    }

    /**
     * VALENTINE — Inimigo 3 (boss final).
     * Deck: Mega Cavaleiro, Dragão, Goblin, Príncipe
     *   [0] Mega Cavaleiro — reservado para o último turno (RF10)
     *   [1] Dragão
     *   [2] Goblin
     *   [3] Príncipe
     *
     * Padrão:
     *   Turno 1: Dragão + Goblin + Príncipe (índices 1, 2, 3) — pressão máxima
     *   Turno 2: Mega Cavaleiro sozinho     (índice 0)         — golpe final
     *   Depois repete (Mega Cavaleiro cicla sozinho enquanto vivo)
     */
    public static Inimigo criarValentine() {
        Deck deck = new Deck("Deck de Valentine");
        deck.adicionarCarta(FabricaCartas.criarMegaCavaleiro()); // índice 0
        deck.adicionarCarta(FabricaCartas.criarDragao());        // índice 1
        deck.adicionarCarta(FabricaCartas.criarGoblin());        // índice 2
        deck.adicionarCarta(FabricaCartas.criarPrincepe());      // índice 3

        List<List<Integer>> padrao = new ArrayList<>();
        padrao.add(List.of(1, 2, 3)); // turno 1: Dragão + Goblin + Príncipe
        padrao.add(List.of(0));       // turno 2: Mega Cavaleiro (RF10)
        // repete: pressão + boss alternados

        return new Inimigo(
            "Valentine",
            "O boss final. Comanda uma horda e reserva o Mega Cavaleiro para o golpe decisivo.",
            deck, padrao, true
        );
    }

    // =========================================================
    // Getters
    // =========================================================
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
