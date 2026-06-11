package model;

/**
 * Representa uma arena do jogo Crowns.
 *
 * Cada arena está associada a um inimigo específico.
 * Regras aplicadas:
 *  - RN14: jogador só desbloqueia nova arena após vencer a anterior
 *  - RF9:  cada arena possui 1 inimigo com padrão único
 *  - RF10: última arena contém o boss Valentine com Mega Cavaleiro
 */
public class Arena {

    // =========================================================
    // Atributos
    // =========================================================
    private int    numero;       // 1, 2 ou 3
    private String nome;
    private String descricao;
    private Inimigo inimigo;
    private boolean desbloqueada;
    private int    recompensaMoedas; // moedas ganhas ao vencer

    // =========================================================
    // Construtor
    // =========================================================
    public Arena(int numero, String nome, String descricao,
                 Inimigo inimigo, int recompensaMoedas, boolean desbloqueada) {
        this.numero           = numero;
        this.nome             = nome;
        this.descricao        = descricao;
        this.inimigo          = inimigo;
        this.recompensaMoedas = recompensaMoedas;
        this.desbloqueada     = desbloqueada;
    }

    // =========================================================
    // Fábricas das três arenas
    // =========================================================

    /**
     * Arena 1 — Pátio dos Iniciantes
     * Inimigo: Ringo | Recompensa: 50 moedas | Desbloqueada de início
     */
    public static Arena criarArena1() {
        return new Arena(
            1,
            "Pátio dos Iniciantes",
            "A primeira batalha. Ringo é inexperiente — aprenda as mecânicas aqui.",
            Inimigo.criarRingo(),
            50,
            true // aberta desde o início
        );
    }

    /**
     * Arena 2 — Salão das Sombras
     * Inimigo: Diego | Recompensa: 100 moedas | Desbloqueada após vencer Arena 1
     */
    public static Arena criarArena2() {
        return new Arena(
            2,
            "Salão das Sombras",
            "Diego ataca em pares. Gerencie bem o elixir ou será sobrecarregado.",
            Inimigo.criarDiego(),
            100,
            false
        );
    }

    /**
     * Arena 3 — Trono de Ferro (Boss Final)
     * Inimigo: Valentine | Recompensa: 200 moedas | Desbloqueada após vencer Arena 2
     */
    public static Arena criarArena3() {
        return new Arena(
            3,
            "Trono de Ferro",
            "Valentine reserva o Mega Cavaleiro para o golpe final. Sobreviva à primeira onda.",
            Inimigo.criarValentine(),
            200,
            false
        );
    }

    // =========================================================
    // Lógica
    // =========================================================

    /** Desbloqueia esta arena (chamado ao vencer a anterior — RN14). */
    public void desbloquear() {
        this.desbloqueada = true;
        this.inimigo.resetarParaBatalha();
    }

    /** Verifica se é a arena final (boss). */
    public boolean isArenaFinal() {
        return inimigo.isEhBoss();
    }

    // =========================================================
    // Getters e Setters
    // =========================================================
    public int     getNumero()            { return numero; }
    public String  getNome()              { return nome; }
    public String  getDescricao()         { return descricao; }
    public Inimigo getInimigo()           { return inimigo; }
    public boolean isDesbloqueada()       { return desbloqueada; }
    public int     getRecompensaMoedas()  { return recompensaMoedas; }

    public void setDesbloqueada(boolean d){ this.desbloqueada = d; }
    public void setInimigo(Inimigo i)     { this.inimigo = i; }

    @Override
    public String toString() {
        return String.format("Arena{numero=%d, nome='%s', inimigo='%s', desbloqueada=%b}",
                numero, nome, inimigo.getNome(), desbloqueada);
    }
}
