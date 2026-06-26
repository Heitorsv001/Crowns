package model;

public class Carta {

    private String nome;
    private TipoCarta tipo;
    private String imagemPath; 

    private final int vidaBase;
    private final int danoBase;

    private int vidaMaxima;
    private int vidaAtual;
    private int dano;
    private int custo;  
    private int nivel;   

    private boolean emRecarga;      
    private long tempoUso;        
    private static final long TEMPO_RECARGA_MS = 3000; 

    private double chanceCritico;  
    private double chanceCura;      
    private double percentualCura;  


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

    public static int calcularCusto(int vida) {
        if (vida < 100) return 1;
        if (vida < 200) return 2;
        if (vida < 400) return 3;
        return 4;
    }


    public void aplicarUpgrade() {
        this.nivel++;
        this.vidaMaxima = (int) (vidaBase * (1 + 0.10 * (nivel - 1)));
        this.dano       = (int) (danoBase * (1 + 0.10 * (nivel - 1)));
        this.vidaAtual  = this.vidaMaxima;
        this.custo      = calcularCusto(this.vidaMaxima);
    }

    public int getCustoUpgrade() {
        return nivel * 20;
    }

    public int getPrecoCompra() {
        return custo * 10;
    }


    public void receberDano(int danoRecebido) {
        this.vidaAtual = Math.max(0, this.vidaAtual - danoRecebido);
    }

    public boolean estaMorta() {
        return this.vidaAtual <= 0;
    }

    public void curar(int quantidade) {
        this.vidaAtual = Math.min(vidaMaxima, this.vidaAtual + quantidade);
    }

    public int calcularCura() {
        return (int) (vidaMaxima * percentualCura);
    }

    public void iniciarRecarga() {
        this.emRecarga = true;
        this.tempoUso  = System.currentTimeMillis();
    }

    public void atualizarRecarga() {
        if (emRecarga && (System.currentTimeMillis() - tempoUso) >= TEMPO_RECARGA_MS) {
            this.emRecarga = false;
        }
    }

    public boolean podeSerUsada(int elixirAtual) {
        return !emRecarga && elixirAtual >= custo;
    }

    public void resetarVida() {
        this.vidaAtual = this.vidaMaxima;
        this.emRecarga = false;
    }

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
