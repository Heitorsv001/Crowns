package utils;

import model.Arena;
import model.Jogador;
import model.Recompensa;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton que guarda o estado global da sessão de jogo.
 * Compartilhado entre todos os controllers via EstadoJogo.get().
 *
 * Contém: jogador atual, lista de arenas, arena ativa e última recompensa.
 */
public class EstadoJogo {

    private static EstadoJogo instancia;

    private Jogador jogador;
    private List<Arena> arenas;
    private Recompensa ultimaRecompensa;

    private EstadoJogo() {}

    public static EstadoJogo get() {
        if (instancia == null) instancia = new EstadoJogo();
        return instancia;
    }

    /** Inicia uma nova sessão com um jogador. Cria as 3 arenas. */
    public void iniciarSessao(String nomeJogador) {
        this.jogador = new Jogador(nomeJogador);
        this.arenas  = new ArrayList<>();
        arenas.add(Arena.criarArena1()); // índice 0 — desbloqueada
        arenas.add(Arena.criarArena2()); // índice 1
        arenas.add(Arena.criarArena3()); // índice 2 — boss
        this.ultimaRecompensa = null;
    }

    /** Arena que o jogador deve enfrentar agora. */
    public Arena getArenaAtual() {
        int idx = jogador.getArenaAtual();
        if (idx >= arenas.size()) return null; // jogo concluído
        return arenas.get(idx);
    }

    /** Verifica se o jogador completou todas as arenas. */
    public boolean jogoCompleto() {
        return jogador.getArenaAtual() >= arenas.size();
    }

    public Jogador getJogador()                        { return jogador; }
    public List<Arena> getArenas()                     { return arenas; }
    public Recompensa getUltimaRecompensa()             { return ultimaRecompensa; }
    public void setUltimaRecompensa(Recompensa r)      { this.ultimaRecompensa = r; }
}
