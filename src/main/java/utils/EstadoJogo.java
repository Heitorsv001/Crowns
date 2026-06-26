package utils;
import com.mycompany.crowns.dao.JogadorDAO;
import model.Arena;
import model.Jogador;
import model.Recompensa;
import java.util.ArrayList;
import java.util.List;
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

    public void iniciarSessao(String nomeJogador) {
  
    Jogador jogadorExistente = JogadorDAO.carregar(nomeJogador);

    if (jogadorExistente != null) {
      
        this.jogador = jogadorExistente;
        this.jogador.iniciarRun(); 
    } else {
        this.jogador = new Jogador(nomeJogador);
        JogadorDAO.salvarOuAtualizar(this.jogador);
        JogadorDAO.salvarCartaNaColecao(nomeJogador, this.jogador.getColecao());
    }

    this.arenas = new ArrayList<>();
    arenas.add(Arena.criarArena1());
    arenas.add(Arena.criarArena2());
    arenas.add(Arena.criarArena3());

    // Desbloqueia arenas ja conquistadas
    for (int i = 0; i < this.jogador.getArenaAtual() && i < arenas.size(); i++) {
        arenas.get(i).desbloquear();
    }

    this.ultimaRecompensa = null;
}
    public Arena getArenaAtual() {
        int idx = jogador.getArenaAtual();
        if (idx >= arenas.size()) return null; 
        return arenas.get(idx);
    }

    public boolean jogoCompleto() {
        return jogador.getArenaAtual() >= arenas.size();
    }

    public Jogador getJogador()                        { return jogador; }
    public List<Arena> getArenas()                     { return arenas; }
    public Recompensa getUltimaRecompensa()             { return ultimaRecompensa; }
    public void setUltimaRecompensa(Recompensa r)      { this.ultimaRecompensa = r; }
}
