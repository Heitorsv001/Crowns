package model;

/**
 * Representa uma carta/herói do jogo Crowns.
 *
 * Regras de negócio aplicadas:
 *  - RN10: custo calculado automaticamente pela vida máxima
 *  - RN6:  custo de upgrade = nível_atual × 20
 *  - RN12: atributos aumentam ao receber upgrades
 *  - RN4:  curandeira tem 20% de chance de curar 40% ao atacar
 *  - RN7:  carta só pode ser usada se elixir >= custo
 */
public class Carta {

    // --- Identidade ---
    private String nome;
    private TipoCarta tipo;
    private String imagemPath; // caminho relativo em resources/images/

    // --- Atributos base (imutáveis, referência para upgrades) ---
    private final int vidaBase;
    private final int danoBase;

    // --- Atributos atuais (modificados por upgrades) ---
    private int vidaMaxima;
    private int vidaAtual;
    private int dano;
    private int custo;   // elixir — calculado por RN10
    private int nivel;   // começa em 1

    // --- Estado de batalha ---
    private boolean emRecarga;      // RF7: após uso, some da mão
    private long tempoUso;          // timestamp do último uso (ms)
    private static final long TEMPO_RECARGA_MS = 3000; // 3 segundos de recarga

    // --- Habilidade especial ---
    private double chanceCritico;   // Príncipe 25%, Mega Cavaleiro 30%
    private double chanceCura;      // Curandeira 20%
    private double percentualCura;  // Curandeira cura 40% da vidaMaxima

    // =========================================================
    // Construtor principal
    // =========================================================
    public Carta(String nome, TipoCarta tipo, int vidaBase, int danoBase, String imagemPath) {
        this.nome       = nome;
        this.tipo       = tipo;
        this.vidaBase   = vidaBase;
        this.danoBase   = danoBase;
        this.imagemPath = imagemPath;

        this.nivel      = 1;
        this.vidaMaxima = vidaBase;
        this.vidaAtual  = vidaBase;
        this.dano       = danoBase;
        this.custo      = calcularCusto(vidaBase);

        this.emRecarga      = false;
        this.chanceCritico  = 0.0;
        this.chanceCura     = 0.0;
        this.percentualCura = 0.0;
    }

    // =========================================================
    // RN10 — cálculo de custo pelo HP máximo
    // vida < 100 → custo 1 | < 200 → custo 2 | < 400 → custo 3 | >= 400 → custo 4
    // =========================================================
    public static int calcularCusto(int vida) {
        if (vida < 100) return 1;
        if (vida < 200) return 2;
        if (vida < 400) return 3;
        return 4;
    }

    // =========================================================
    // RN12 — upgrade: aumenta vida e dano proporcionalmente ao nível
    // Incremento: +10% de vida e +10% de dano por nível
    // =========================================================
    public void aplicarUpgrade() {
        this.nivel++;
        this.vidaMaxima = (int) (vidaBase * (1 + 0.10 * (nivel - 1)));
        this.dano       = (int) (danoBase * (1 + 0.10 * (nivel - 1)));
        this.vidaAtual  = this.vidaMaxima; // cura ao evoluir
        this.custo      = calcularCusto(this.vidaMaxima);
    }

    // RN6 — custo de upgrade em moedas
    public int getCustoUpgrade() {
        return nivel * 20;
    }

    // RN5 — preço de compra na loja
    public int getPrecoCompra() {
        return custo * 10;
    }

    // =========================================================
    // Mecânica de batalha
    // =========================================================

    /** Aplica dano à carta. HP nunca fica negativo (RNF5). */
    public void receberDano(int danoRecebido) {
        this.vidaAtual = Math.max(0, this.vidaAtual - danoRecebido);
    }

    /** Retorna true se a carta está morta (HP = 0). */
    public boolean estaMorta() {
        return this.vidaAtual <= 0;
    }

    /**
     * Cura a carta (habilidade da Curandeira — RN4).
     * Não ultrapassa a vida máxima.
     */
    public void curar(int quantidade) {
        this.vidaAtual = Math.min(vidaMaxima, this.vidaAtual + quantidade);
    }

    /** Quantidade de cura que a Curandeira aplica (40% da vida máxima). */
    public int calcularCura() {
        return (int) (vidaMaxima * percentualCura);
    }

    /** Marca a carta como em recarga após uso (RF7). */
    public void iniciarRecarga() {
        this.emRecarga = true;
        this.tempoUso  = System.currentTimeMillis();
    }

    /** Verifica se a recarga já terminou e libera a carta. */
    public void atualizarRecarga() {
        if (emRecarga && (System.currentTimeMillis() - tempoUso) >= TEMPO_RECARGA_MS) {
            this.emRecarga = false;
        }
    }

    /** Verifica se pode ser jogada (elixir suficiente e fora de recarga — RN7). */
    public boolean podeSerUsada(int elixirAtual) {
        return !emRecarga && elixirAtual >= custo;
    }

    /** Reseta HP para o máximo (entre batalhas). */
    public void resetarVida() {
        this.vidaAtual = this.vidaMaxima;
        this.emRecarga = false;
    }

    // =========================================================
    // Getters e Setters
    // =========================================================
    public String getNome()           { return nome; }
    public TipoCarta getTipo()        { return tipo; }
    public String getImagemPath()     { return imagemPath; }
    public int getVidaBase()          { return vidaBase; }
    public int getDanoBase()          { return danoBase; }
    public int getVidaMaxima()        { return vidaMaxima; }
    public int getVidaAtual()         { return vidaAtual; }
    public int getDano()              { return dano; }
    public int getCusto()             { return custo; }
    public int getNivel()             { return nivel; }
    public boolean isEmRecarga()      { return emRecarga; }
    public double getChanceCritico()  { return chanceCritico; }
    public double getChanceCura()     { return chanceCura; }
    public double getPercentualCura() { return percentualCura; }

    public void setNivel(int nivel)             { this.nivel = nivel; }
    public void setVidaAtual(int vidaAtual)     { this.vidaAtual = Math.max(0, vidaAtual); }
    public void setVidaMaxima(int vidaMaxima)   { this.vidaMaxima = vidaMaxima; }
    public void setDano(int dano)               { this.dano = dano; }
    public void setChanceCritico(double v)      { this.chanceCritico = v; }
    public void setChanceCura(double v)         { this.chanceCura = v; }
    public void setPercentualCura(double v)     { this.percentualCura = v; }
    public void setImagemPath(String path)      { this.imagemPath = path; }

    @Override
    public String toString() {
        return String.format("Carta{nome='%s', tipo=%s, vida=%d/%d, dano=%d, custo=%d, nivel=%d}",
                nome, tipo, vidaAtual, vidaMaxima, dano, custo, nivel);
    }
}
