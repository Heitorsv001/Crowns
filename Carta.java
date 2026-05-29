package model;


public class Carta {


    private int id;
    private String nome;
    private int vidaMaxima;
    private int vidaAtual;
    private int dano;
    private int custoElixir;
    private int nivel;
    private boolean disponivel; // false quando está em recarga (RF7)

    private int vidaBase;
    private int danoBase;

    // -------------------------------------------------------------------------
    // Construtores
    // -------------------------------------------------------------------------
    public Carta(int id, String nome, int vidaMaxima, int dano) {
        this.id = id;
        this.nome = nome;
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.dano = dano;
        this.nivel = 1;
        this.disponivel = true;
        this.vidaBase = vidaMaxima;
        this.danoBase = dano;
        this.custoElixir = calcularCustoElixir(vidaMaxima);
    }
    public Carta(Carta outra) {
        this.id = outra.id;
        this.nome = outra.nome;
        this.vidaMaxima = outra.vidaMaxima;
        this.vidaAtual = outra.vidaAtual;
        this.dano = outra.dano;
        this.custoElixir = outra.custoElixir;
        this.nivel = outra.nivel;
        this.disponivel = outra.disponivel;
        this.vidaBase = outra.vidaBase;
        this.danoBase = outra.danoBase;
    }

    public static int calcularCustoElixir(int vidaMaxima) {
        if (vidaMaxima < 100) return 1;
        if (vidaMaxima < 200) return 2;
        if (vidaMaxima < 400) return 3;
        return 4; // >= 400
    }

    
 
    public int getPrecoCompra() {
        return custoElixir * 10;
    }

    public int getCustoUpgrade() {
        return nivel * 20;
    }


    public boolean aplicarUpgrade() {
        nivel++;
        double multiplicador = 1.0 + (nivel - 1) * 0.15;
        vidaMaxima = (int) (vidaBase * multiplicador);
        vidaAtual = vidaMaxima;
        dano = (int) (danoBase * multiplicador);
        custoElixir = calcularCustoElixir(vidaMaxima);
        return true;
    }


    public void receberDano(int quantidade) {
        vidaAtual = Math.max(0, vidaAtual - quantidade);
    }

    public void curar(int quantidade) {
        vidaAtual = Math.min(vidaMaxima, vidaAtual + quantidade);
    }


    public boolean estaViva() {
        return vidaAtual > 0;
    }


    public void entrarEmRecarga() {
        disponivel = false;
    }

    public void voltarDaRecarga() {
        disponivel = true;
    }

    public void resetarHP() {
        vidaAtual = vidaMaxima;
    }

  

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getVidaMaxima() { return vidaMaxima; }
    public void setVidaMaxima(int vidaMaxima) { this.vidaMaxima = vidaMaxima; }

    public int getVidaAtual() { return vidaAtual; }
    public void setVidaAtual(int vidaAtual) {
        this.vidaAtual = Math.max(0, Math.min(vidaMaxima, vidaAtual));
    }

    public int getDano() { return dano; }
    public void setDano(int dano) { this.dano = dano; }

    public int getCustoElixir() { return custoElixir; }
    public void setCustoElixir(int custoElixir) { this.custoElixir = custoElixir; }

    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    public int getVidaBase() { return vidaBase; }
    public int getDanoBase() { return danoBase; }

    @Override
    public String toString() {
        return String.format(
            "[%s | HP: %d/%d | Dano: %d | Custo: %d | Nível: %d | %s]",
            nome, vidaAtual, vidaMaxima, dano, custoElixir,
            nivel, disponivel ? "Disponível" : "Em recarga"
        );
    }
}