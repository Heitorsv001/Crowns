package com.mycompany.crowns.dao;
import model.Carta;
import model.FabricaCartas;
import model.Jogador;
import com.mycompany.crowns.dao.CartaDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Deck;

public class JogadorDAO {


    public static void salvarOuAtualizar(Jogador jogador) {
        String sql = """
            INSERT INTO jogadores (nome, moedas, total_vitorias, total_derrotas, arena_atual)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT (nome) DO UPDATE SET
                moedas         = EXCLUDED.moedas,
                total_vitorias = EXCLUDED.total_vitorias,
                total_derrotas = EXCLUDED.total_derrotas,
                arena_atual    = EXCLUDED.arena_atual
            """;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, jogador.getNome());
            ps.setInt(2, jogador.getMoedas());
            ps.setInt(3, jogador.getTotalVitorias());
            ps.setInt(4, jogador.getTotalDerrotas());
            ps.setInt(5, jogador.getArenaAtual());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[JogadorDAO] Erro ao salvar jogador: " + e.getMessage());
        }
    }


    public static int buscarId(String nomeJogador) {
        String sql = "SELECT id FROM jogadores WHERE nome = ?";
        try (Connection con = ConexaoDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nomeJogador);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");

        } catch (SQLException e) {
            System.err.println("[JogadorDAO] Erro ao buscar id: " + e.getMessage());
        }
        return -1;
    }


    public static void salvarCartaNaColecao(String nomeJogador, Carta carta) {
        int jogadorId = buscarId(nomeJogador);
        if (jogadorId == -1) return;

        int cartaId = CartaDAO.buscarId(carta.getNome());
        if (cartaId == -1) return;

        String sql = """
            INSERT INTO jogador_cartas (jogador_id, carta_id, nivel)
            VALUES (?, ?, ?)
            ON CONFLICT (jogador_id, carta_id) DO NOTHING
            """;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jogadorId);
            ps.setInt(2, cartaId);
            ps.setInt(3, carta.getNivel());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[JogadorDAO] Erro ao salvar carta na colecao: " + e.getMessage());
        }
    }
public static void salvarCartaNaColecao(String nomeJogador, List<Carta> cartas) {
    for (Carta carta : cartas) {
        salvarCartaNaColecao(nomeJogador, carta);
    }
}

 
    public static void atualizarNivelCarta(String nomeJogador, Carta carta) {
        int jogadorId = buscarId(nomeJogador);
        int cartaId   = CartaDAO.buscarId(carta.getNome());
        if (jogadorId == -1 || cartaId == -1) return;

        String sql = "UPDATE jogador_cartas SET nivel = ? WHERE jogador_id = ? AND carta_id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, carta.getNivel());
            ps.setInt(2, jogadorId);
            ps.setInt(3, cartaId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[JogadorDAO] Erro ao atualizar nivel: " + e.getMessage());
        }
    }

    public static void salvarDeck(Jogador jogador) {
        int jogadorId = buscarId(jogador.getNome());
        if (jogadorId == -1) return;

        String deletar = "DELETE FROM deck_jogador WHERE jogador_id = ?";
        String inserir = "INSERT INTO deck_jogador (jogador_id, carta_id, posicao) VALUES (?, ?, ?)";

        try (Connection con = ConexaoDB.conectar()) {
            PreparedStatement psDel = con.prepareStatement(deletar);
            psDel.setInt(1, jogadorId);
            psDel.executeUpdate();

            PreparedStatement psIns = con.prepareStatement(inserir);
            List<Carta> cartas = jogador.getDeck().getCartas();
            for (int i = 0; i < cartas.size(); i++) {
                int cartaId = CartaDAO.buscarId(cartas.get(i).getNome());
                if (cartaId == -1) continue;
                psIns.setInt(1, jogadorId);
                psIns.setInt(2, cartaId);
                psIns.setInt(3, i + 1); // posicao 1-based
                psIns.addBatch();
            }
            psIns.executeBatch();

        } catch (SQLException e) {
            System.err.println("[JogadorDAO] Erro ao salvar deck: " + e.getMessage());
        }
    }


    public static void registrarRun(String nomeJogador, int arenasVencidas,
                                     String resultado, int moedasGanhas, String cartaBonus) {
        int jogadorId = buscarId(nomeJogador);
        if (jogadorId == -1) return;

        String sql = """
            INSERT INTO historico_runs
                (jogador_id, arenas_vencidas, resultado, moedas_ganhas, carta_bonus)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jogadorId);
            ps.setInt(2, arenasVencidas);
            ps.setString(3, resultado);
            ps.setInt(4, moedasGanhas);
            ps.setString(5, cartaBonus); // pode ser null
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[JogadorDAO] Erro ao registrar run: " + e.getMessage());
        }
    }
        public static Jogador carregar(String nomeJogador) {
    String sql = "SELECT * FROM jogadores WHERE nome = ?";

    try (Connection con = ConexaoDB.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nomeJogador);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) return null; // jogador nao existe no banco

        Jogador jogador = new Jogador(nomeJogador); // cria com deck inicial
        jogador.setMoedas(rs.getInt("moedas"));
        jogador.setTotalVitorias(rs.getInt("total_vitorias"));
        jogador.setTotalDerrotas(rs.getInt("total_derrotas"));
        jogador.setArenaAtual(rs.getInt("arena_atual"));

        int jogadorId = rs.getInt("id");

        carregarColecao(jogador, jogadorId);

        carregarDeck(jogador, jogadorId);

        return jogador;

    } catch (SQLException e) {
        System.err.println("[JogadorDAO] Erro ao carregar jogador: " + e.getMessage());
        return null;
    }
}

private static void carregarColecao(Jogador jogador, int jogadorId) {
    String sql = """
        SELECT c.nome, jc.nivel
        FROM jogador_cartas jc
        JOIN cartas c ON c.id = jc.carta_id
        WHERE jc.jogador_id = ?
        """;

    try (Connection con = ConexaoDB.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, jogadorId);
        ResultSet rs = ps.executeQuery();

        List<Carta> colecao = new ArrayList<>();
        while (rs.next()) {
            String nome  = rs.getString("nome");
            int nivel    = rs.getInt("nivel");
            Carta carta  = criarCartaPorNome(nome);
            if (carta == null) continue;

            // Aplica upgrades ate o nivel salvo
            for (int i = 1; i < nivel; i++) {
                carta.aplicarUpgrade();
            }
            colecao.add(carta);
        }

        if (!colecao.isEmpty()) {
            jogador.setColecao(colecao);
        }

    } catch (SQLException e) {
        System.err.println("[JogadorDAO] Erro ao carregar colecao: " + e.getMessage());
    }
}

private static void carregarDeck(Jogador jogador, int jogadorId) {
    String sql = """
        SELECT c.nome, d.posicao
        FROM deck_jogador d
        JOIN cartas c ON c.id = d.carta_id
        WHERE d.jogador_id = ?
        ORDER BY d.posicao
        """;

    try (Connection con = ConexaoDB.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, jogadorId);
        ResultSet rs = ps.executeQuery();

        List<String> nomesDeck = new ArrayList<>();
        while (rs.next()) {
            nomesDeck.add(rs.getString("nome"));
        }

        if (nomesDeck.size() != 4) return; // deck incompleto, usa o inicial

        Deck deck = new Deck("Deck Salvo");
        for (String nome : nomesDeck) {
            // Busca a carta ja com upgrade aplicado da colecao
            Carta carta = jogador.getColecao().stream()
                    .filter(c -> c.getNome().equals(nome))
                    .findFirst()
                    .orElse(criarCartaPorNome(nome));
            if (carta != null) deck.adicionarCarta(carta);
        }

        if (deck.estaCompleto()) {
            jogador.setDeck(deck);
        }

    } catch (SQLException e) {
        System.err.println("[JogadorDAO] Erro ao carregar deck: " + e.getMessage());
    }
}

private static Carta criarCartaPorNome(String nome) {
    switch (nome) {
        case "Esqueleto":       return FabricaCartas.criarEsqueleto();
        case "Arqueira":        return FabricaCartas.criarArqueira();
        case "Goblin Lanceiro": return FabricaCartas.criarGoblinLanceiro();
        case "Cavaleiro":       return FabricaCartas.criarCavaleiro();
        case "Mago":            return FabricaCartas.criarMago();
        case "Morcego":         return FabricaCartas.criarMorcego();
        case "Goblin":          return FabricaCartas.criarGoblin();
        case "Dragao":          return FabricaCartas.criarDragao();
        case "Principe":        return FabricaCartas.criarPrincepe();
        case "Curandeira":      return FabricaCartas.criarCurandeira();
        case "PEKA":            return FabricaCartas.criarPEKA();
        case "Golem":           return FabricaCartas.criarGolem();
        case "Lava Hound":      return FabricaCartas.criarLavaHound();
        case "Mega Cavaleiro":  return FabricaCartas.criarMegaCavaleiro();
        default:
            System.err.println("[JogadorDAO] Carta desconhecida: " + nome);
            return null;
    }
}
  
}