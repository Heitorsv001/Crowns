package controller;
import com.mycompany.crowns.dao.JogadorDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import model.Carta;
import model.Jogador;
import utils.EstadoJogo;
import utils.GerenciadorCena;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DeckController implements Initializable {

    @FXML private HBox painelDeck;
    @FXML private HBox painelColecao;
    @FXML private Label lblStatus;

    private Jogador jogador;
    private Carta cartaSelecionadaDoDeck; // carta do deck que o jogador quer substituir

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jogador = EstadoJogo.get().getJogador();
        renderizarDeck();
        renderizarColecao();
    }


    private void selecionarCartaDoDeck(Carta carta) {
        cartaSelecionadaDoDeck = carta;
        lblStatus.setText("Carta selecionada: " + carta.getNome()
                + " — agora escolha uma carta da colecao para substituir.");
        lblStatus.setStyle("-fx-text-fill: #e2b96f; -fx-font-size: 13px;");
        renderizarDeck();
        renderizarColecao();
    }

    private void trocarCarta(Carta cartaDaColecao) {
        if (cartaSelecionadaDoDeck == null) {
            lblStatus.setText("Selecione primeiro uma carta do deck.");
            lblStatus.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 13px;");
            return;
        }

        // Verifica se a carta da colecao ja esta no deck
        boolean jaEstaNoDeck = jogador.getDeck().getCartas().contains(cartaDaColecao);
        if (jaEstaNoDeck) {
            lblStatus.setText("Essa carta ja esta no deck.");
            lblStatus.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 13px;");
            return;
        }

        int posicao = jogador.getDeck().getCartas().indexOf(cartaSelecionadaDoDeck);
        jogador.substituirCartaNoDeck(posicao, cartaDaColecao);

        cartaDaColecao.iniciarRecarga();

        lblStatus.setText("Trocado: " + cartaSelecionadaDoDeck.getNome()
                + " por " + cartaDaColecao.getNome() + ". Carta em recarga.");
        lblStatus.setStyle("-fx-text-fill: #88ff88; -fx-font-size: 13px;");

        cartaSelecionadaDoDeck = null;
        JogadorDAO.salvarDeck(jogador);
        JogadorDAO.salvarOuAtualizar(jogador);
        renderizarDeck();
        renderizarColecao();
    }


    private void renderizarDeck() {
        painelDeck.getChildren().clear();
        for (Carta carta : jogador.getDeck().getCartas()) {
            boolean selecionada = carta == cartaSelecionadaDoDeck;
            Button btn = criarBotaoCarta(carta, selecionada, true);
            btn.setOnAction(e -> selecionarCartaDoDeck(carta));
            painelDeck.getChildren().add(btn);
        }
    }

    private void renderizarColecao() {
        painelColecao.getChildren().clear();
        List<Carta> colecao = jogador.getColecao();

        if (colecao.isEmpty()) {
            Label vazio = new Label("Nenhuma carta na colecao alem das do deck.");
            vazio.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");
            painelColecao.getChildren().add(vazio);
            return;
        }

        for (Carta carta : colecao) {
            boolean jaNoDecK = jogador.getDeck().getCartas().contains(carta);
            Button btn = criarBotaoCarta(carta, false, false);

            if (jaNoDecK) {
                // Carta ja esta no deck — desabilita e indica visualmente
                btn.setDisable(true);
                btn.setOpacity(0.4);
            } else {
                btn.setOnAction(e -> trocarCarta(carta));
            }

            painelColecao.getChildren().add(btn);
        }
    }

    private Button criarBotaoCarta(Carta carta, boolean selecionada, boolean ehDoDeck) {
        Button btn = new Button();
        btn.setPrefWidth(115);
        btn.setPrefHeight(140);
        btn.setWrapText(true);
        btn.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);

        // Imagem
        try {
            java.io.InputStream stream = getClass().getResourceAsStream(
                    "/com/mycompany/crowns/images/" + carta.getImagemPath());
            if (stream != null) {
                ImageView iv = new ImageView(new Image(stream));
                iv.setFitWidth(75);
                iv.setFitHeight(75);
                iv.setPreserveRatio(true);
                iv.setSmooth(true);

                if (carta.isEmRecarga()) {
                    ColorAdjust escurecer = new ColorAdjust();
                    escurecer.setBrightness(-0.5);
                    iv.setEffect(escurecer);
                }
                btn.setGraphic(iv);
            }
        } catch (Exception e) {
            System.err.println("Imagem nao encontrada: " + carta.getImagemPath());
        }

        // Texto
        String status = carta.isEmRecarga() ? "Em recarga"
                : "Vida: " + carta.getVidaAtual()
                + "  Dano: " + carta.getDano()
                + "  Custo: " + carta.getCusto();
        btn.setText(carta.getNome() + "\nNivel " + carta.getNivel() + "\n" + status);

        // Cor — carta selecionada fica destacada em dourado
        String corFundo = selecionada   ? "#4a3a00"
                        : ehDoDeck      ? "#1a3a1a"
                        :                 "#2a2a4a";

        String corBorda = selecionada ? "#e2b96f" : "#555555";

        btn.setStyle(
                "-fx-background-color: " + corFundo + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 10px;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: " + corBorda + ";" +
                "-fx-border-width: " + (selecionada ? "2" : "1") + ";" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;"
        );

        // Destaque com sombra dourada quando selecionada
        if (selecionada) {
            DropShadow sombra = new DropShadow();
            sombra.setColor(Color.web("#e2b96f"));
            sombra.setRadius(12);
            btn.setEffect(sombra);
        }

        return btn;
    }


    @FXML
    private void aoVoltar() {
        GerenciadorCena.irParaArena();
    }
}