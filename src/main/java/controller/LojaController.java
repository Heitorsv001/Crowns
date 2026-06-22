package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.Carta;
import model.FabricaCartas;
import model.Jogador;
import utils.Alerta;
import utils.EstadoJogo;
import utils.GerenciadorCena;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LojaController implements Initializable {

    @FXML private Label lblMoedas;
    @FXML private FlowPane painelUpgrade;
    @FXML private FlowPane painelComprar;

    private Jogador jogador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jogador = EstadoJogo.get().getJogador();
        if (jogador == null) return;

        atualizarMoedas();
        renderizarCartasParaUpgrade();
        renderizarCartasParaComprar();
    }

    private void atualizarMoedas() {
        lblMoedas.setText("💰 Suas moedas: " + jogador.getMoedas());
    }

    // ===================== MELHORAR (UPGRADE) =====================

    private void renderizarCartasParaUpgrade() {
        painelUpgrade.getChildren().clear();

        if (jogador.getColecao().isEmpty()) {
            painelUpgrade.getChildren().add(criarLabelVazio("Você ainda não possui cartas."));
            return;
        }

        for (Carta carta : jogador.getColecao()) {
            painelUpgrade.getChildren().add(criarTileUpgrade(carta));
        }
    }

    private VBox criarTileUpgrade(Carta carta) {
        VBox tile = criarTileBase("#22243f");

        ImageView iv = carregarImagem(carta.getImagemPath());
        if (iv != null) tile.getChildren().add(iv);

        Label nome = new Label(carta.getNome() + " (Nv. " + carta.getNivel() + ")");
        nome.setWrapText(true);
        nome.setStyle("-fx-text-fill: #e2b96f; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label stats = new Label("❤ " + carta.getVidaMaxima() + "   ⚔ " + carta.getDano() + "   ⚡ " + carta.getCusto());
        stats.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 11px;");

        int custoUpgrade = carta.getCustoUpgrade();
        Button btnUpgrade = new Button("⬆ Melhorar\n(" + custoUpgrade + " moedas)");
        btnUpgrade.setWrapText(true);
        btnUpgrade.setPrefWidth(140);
        btnUpgrade.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        btnUpgrade.setStyle(
            "-fx-background-color: #2e5d3a; -fx-text-fill: white; -fx-font-size: 11px;" +
            "-fx-background-radius: 8; -fx-cursor: hand;"
        );
        btnUpgrade.setOnAction(e -> aoMelhorarCarta(carta));

        tile.getChildren().addAll(nome, stats, btnUpgrade);
        return tile;
    }

    private void aoMelhorarCarta(Carta carta) {
        int custo = carta.getCustoUpgrade();
        boolean sucesso = jogador.fazerUpgrade(carta);

        if (sucesso) {
            Alerta.mostrarInfo("Upgrade realizado!",
                carta.getNome() + " subiu para o nível " + carta.getNivel() + "!");
        } else {
            Alerta.mostrarErro("Moedas insuficientes",
                "Você precisa de " + custo + " moedas para melhorar " + carta.getNome()
                + ".\nVocê possui: " + jogador.getMoedas() + " moedas.");
        }

        atualizarMoedas();
        renderizarCartasParaUpgrade();
    }

    // ===================== COMPRAR NOVAS CARTAS =====================

    private void renderizarCartasParaComprar() {
        painelComprar.getChildren().clear();

        List<Carta> disponiveis = catalogoDeCompra();
        if (disponiveis.isEmpty()) {
            painelComprar.getChildren().add(criarLabelVazio("Você já possui todas as cartas disponíveis!"));
            return;
        }

        for (Carta carta : disponiveis) {
            painelComprar.getChildren().add(criarTileCompra(carta));
        }
    }

    private VBox criarTileCompra(Carta carta) {
        VBox tile = criarTileBase("#2a2238");

        ImageView iv = carregarImagem(carta.getImagemPath());
        if (iv != null) tile.getChildren().add(iv);

        Label nome = new Label(carta.getNome());
        nome.setWrapText(true);
        nome.setStyle("-fx-text-fill: #e2b96f; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label stats = new Label("❤ " + carta.getVidaMaxima() + "   ⚔ " + carta.getDano() + "   ⚡ " + carta.getCusto());
        stats.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 11px;");

        int preco = carta.getPrecoCompra();
        Button btnComprar = new Button("🛒 Comprar\n(" + preco + " moedas)");
        btnComprar.setWrapText(true);
        btnComprar.setPrefWidth(140);
        btnComprar.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        btnComprar.setStyle(
            "-fx-background-color: #5d4a1a; -fx-text-fill: white; -fx-font-size: 11px;" +
            "-fx-background-radius: 8; -fx-cursor: hand;"
        );
        btnComprar.setOnAction(e -> aoComprarCarta(carta));

        tile.getChildren().addAll(nome, stats, btnComprar);
        return tile;
    }

    private void aoComprarCarta(Carta carta) {
        int preco = carta.getPrecoCompra();
        boolean sucesso = jogador.comprarCarta(carta);

        if (sucesso) {
            Alerta.mostrarInfo("Compra realizada!",
                "Você comprou " + carta.getNome() + "!\nVá até o Deck para colocá-la em batalha.");
        } else {
            Alerta.mostrarErro("Moedas insuficientes",
                "Você precisa de " + preco + " moedas para comprar " + carta.getNome()
                + ".\nVocê possui: " + jogador.getMoedas() + " moedas.");
        }

        atualizarMoedas();
        renderizarCartasParaUpgrade();
        renderizarCartasParaComprar();
    }

    /** Catálogo de cartas que ainda não estão na coleção do jogador. */
    private List<Carta> catalogoDeCompra() {
        List<Carta> catalogoCompleto = new ArrayList<>();
        catalogoCompleto.add(FabricaCartas.criarMago());
        catalogoCompleto.add(FabricaCartas.criarMorcego());
        catalogoCompleto.add(FabricaCartas.criarGoblin());
        catalogoCompleto.add(FabricaCartas.criarDragao());
        catalogoCompleto.add(FabricaCartas.criarPrincepe());
        catalogoCompleto.add(FabricaCartas.criarCurandeira());
        catalogoCompleto.add(FabricaCartas.criarPEKA());
        catalogoCompleto.add(FabricaCartas.criarGolem());
        catalogoCompleto.add(FabricaCartas.criarLavaHound());
        catalogoCompleto.add(FabricaCartas.criarMegaCavaleiro());

        List<Carta> disponiveis = new ArrayList<>();
        for (Carta candidata : catalogoCompleto) {
            boolean jaPossui = jogador.getColecao().stream()
                    .anyMatch(c -> c.getNome().equals(candidata.getNome()));
            if (!jaPossui) disponiveis.add(candidata);
        }
        return disponiveis;
    }

    // ===================== AUXILIARES DE UI =====================

    private VBox criarTileBase(String corFundo) {
        VBox tile = new VBox(6);
        tile.setPrefWidth(150);
        tile.setAlignment(Pos.CENTER);
        tile.setStyle(
            "-fx-background-color: " + corFundo + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 12;" +
            "-fx-border-color: #444;" +
            "-fx-border-radius: 10;"
        );
        return tile;
    }

    private Label criarLabelVazio(String texto) {
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-text-fill: #888; -fx-font-size: 13px;");
        return lbl;
    }

    private ImageView carregarImagem(String caminho) {
        try {
            java.io.InputStream stream = getClass().getResourceAsStream(
                "/com/mycompany/crowns/images/" + caminho);
            if (stream == null) return null;

            ImageView iv = new ImageView(new Image(stream));
            iv.setFitWidth(70);
            iv.setFitHeight(70);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            return iv;
        } catch (Exception e) {
            return null;
        }
    }

    @FXML
    private void aoVoltar() {
        GerenciadorCena.irParaRecompensa();
    }
}