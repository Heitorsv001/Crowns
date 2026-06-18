package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.*;
import utils.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.control.ContentDisplay;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ArenaController implements Initializable {

    @FXML private Label lblArena, lblInimigo, lblHpJogador, lblHpInimigo;
    @FXML private Label lblElixir, lblTurno;
    @FXML private ProgressBar barraHpJogador, barraHpInimigo, barraElixir;
    @FXML private HBox cartasJogador, cartasInimigo;
    @FXML private TextArea logBatalha;
    @FXML private Button btnPassarTurno;

    private Jogador jogador;
    private Arena arenaAtual;
    private Inimigo inimigo;
    private int numeroTurno = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jogador    = EstadoJogo.get().getJogador();
        arenaAtual = EstadoJogo.get().getArenaAtual();

        if (arenaAtual == null) {
            log("Erro: nenhuma arena disponível.");
            return;
        }

        inimigo = arenaAtual.getInimigo();
        inimigo.resetarParaBatalha();
        jogador.getDeck().resetarParaBatalha();
        jogador.setElixirAtual(Jogador.ELIXIR_INICIAL);

        lblArena.setText("Arena " + arenaAtual.getNumero() + " — " + arenaAtual.getNome());
        lblInimigo.setText("Inimigo: " + inimigo.getNome());

        log("Batalha iniciada... Você enfrenta " + inimigo.getNome() + ".");
        log(arenaAtual.getDescricao());

        atualizarUI();
        renderizarCartasJogador();
        renderizarCartasInimigo();
    }


    private void usarCarta(Carta carta) {
        if (!carta.podeSerUsada(jogador.getElixirAtual())) {
            if (carta.isEmRecarga()) {
                log(" " + carta.getNome() + " ainda está recarregando.");
            } else {
                log("⚡ Elixir insuficiente. Precisa de " + carta.getCusto()
                        + ", você tem " + jogador.getElixirAtual() + ".");
            }
            return;
        }

        jogador.gastarElixir(carta.getCusto());
        carta.iniciarRecarga();

        int dano = CalcularDano.calcularDanoAtaque(carta);

        if (carta.getTipo() == TipoCarta.SUPORTE) {
            int cura = CalcularDano.calcularCura(carta);
            if (cura > 0) {
                carta.curar(cura);
                log("💚 " + carta.getNome() + " curou " + cura + " de HP");
            }
        }

        List<Carta> alvoInimigo = inimigo.getDeck().getCartasVivas();
        if (!alvoInimigo.isEmpty()) {
            Carta alvo = alvoInimigo.get(0); 
            alvo.receberDano(dano);
            log( carta.getNome() + " causou " + dano + " de dano em "
                    + alvo.getNome() + "  (HP: " + alvo.getVidaAtual() + ")");
            if (alvo.estaMorta()) {
                log("💀 " + alvo.getNome() + " foi derrotado");
            }
        }

        atualizarUI();
        renderizarCartasJogador();
        renderizarCartasInimigo();

        verificarFimDeBatalha();
    }

    @FXML
    private void aoPassarTurno() {
        if (verificarFimDeBatalha()) return;

        log("\n--- Turno " + numeroTurno + " do Inimigo ---");

        List<Carta> cartasDoTurno = inimigo.getCartasDoTurnoAtual();
        if (cartasDoTurno.isEmpty()) {
            log("O inimigo não tem cartas para usar neste turno.");
        } else {
            for (Carta cartaInimigo : cartasDoTurno) {
                int dano = CalcularDano.calcularDanoAtaque(cartaInimigo);
                jogador.receberDano(dano);
                log("💥 " + inimigo.getNome() + " usou " + cartaInimigo.getNome()
                        + " causando " + dano + " de dano. Seu HP: " + jogador.getHpAtual());
                if (verificarFimDeBatalha()) return;
            }
        }

        jogador.regenerarElixir();
        log(" Elixir regenerado: " + jogador.getElixirAtual() + "/" + Jogador.MAX_ELIXIR);

        jogador.getDeck().atualizarRecargas();

        numeroTurno++;
        lblTurno.setText("Turno " + numeroTurno);
        atualizarUI();
        renderizarCartasJogador();
        renderizarCartasInimigo();
    }

    @FXML
    private void aoAbrirDeck() {
        GerenciadorCena.irParaDeck();
    }

    @FXML
    private void aoVoltarMenu() {
        GerenciadorCena.irParaMenu();
    }

    private boolean verificarFimDeBatalha() {
        if (inimigo.foiDerrotado()) {
            log("\n VITÓRIA! Você derrotou " + inimigo.getNome() + "!");
            btnPassarTurno.setDisable(true);

            boolean darCarta = RandomUtils.chance(0.6); 
            Carta cartaBonus = darCarta ? RandomUtils.sortearCartaBonus(arenaAtual.getNumero()) : null;
            int moedas       = CalcularDano.calcularRecompensa(
                                   arenaAtual.getRecompensaMoedas(), arenaAtual.getNumero());

            Recompensa r = new Recompensa(moedas, cartaBonus,
                "Você venceu a arena " + arenaAtual.getNumero() + "!");
            r.aplicar(jogador);
            jogador.registrarVitoria();
            EstadoJogo.get().setUltimaRecompensa(r);

            List<Arena> arenas = EstadoJogo.get().getArenas();
            int proxIdx = arenaAtual.getNumero();
            if (proxIdx < arenas.size()) {
                arenas.get(proxIdx).desbloquear();
            }

            log(" Recompensa: " + moedas + " moedas"
                    + (cartaBonus != null ? " + carta: " + cartaBonus.getNome() : "") + "!");
            log("Redirecionando para recompensas em 3 segundos...");

            javafx.animation.PauseTransition pausa =
                new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2.5));
            pausa.setOnFinished(e -> GerenciadorCena.irParaRecompensa());
            pausa.play();
            return true;
        }

        if (jogador.estaDerrotado()) {
            log("\n💀 DERROTA! Você foi derrotado por " + inimigo.getNome() + ".");
            log("Sua run foi encerrada.");
            btnPassarTurno.setDisable(true);

            jogador.registrarDerrota();

            javafx.animation.PauseTransition pausa =
                new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2.5));
            pausa.setOnFinished(e -> GerenciadorCena.irParaMenu());
            pausa.play();
            return true;
        }

        return false;
    }


    private void atualizarUI() {
        double percHpJogador = (double) jogador.getHpAtual() / jogador.getHpMaximo();
        barraHpJogador.setProgress(percHpJogador);
        lblHpJogador.setText(jogador.getHpAtual() + "/" + jogador.getHpMaximo());

        double percElixir = (double) jogador.getElixirAtual() / Jogador.MAX_ELIXIR;
        barraElixir.setProgress(percElixir);
        lblElixir.setText(jogador.getElixirAtual() + "/" + Jogador.MAX_ELIXIR);

        List<Carta> vivasInimigo = inimigo.getDeck().getCartasVivas();
        int hpTotalInimigo = vivasInimigo.stream().mapToInt(Carta::getVidaAtual).sum();
        int hpMaxInimigo   = inimigo.getDeck().getCartas().stream()
                                    .mapToInt(Carta::getVidaMaxima).sum();
        double percHpInimigo = hpMaxInimigo > 0 ? (double) hpTotalInimigo / hpMaxInimigo : 0;
        barraHpInimigo.setProgress(percHpInimigo);
        lblHpInimigo.setText(hpTotalInimigo + "/" + hpMaxInimigo);
    }

    private void renderizarCartasJogador() {
        cartasJogador.getChildren().clear();
        for (Carta carta : jogador.getDeck().getCartas()) {
            Button btn = criarBotaoCarta(carta, true);
            cartasJogador.getChildren().add(btn);
        }
    }

    private void renderizarCartasInimigo() {
        cartasInimigo.getChildren().clear();
        for (Carta carta : inimigo.getDeck().getCartas()) {
            Button btn = criarBotaoCarta(carta, false);
            cartasInimigo.getChildren().add(btn);
        }
    }

   private Button criarBotaoCarta(Carta carta, boolean clicavel) {
    Button btn = new Button();
    btn.setPrefWidth(120);
    btn.setPrefHeight(140);
    btn.setWrapText(true);
    btn.setContentDisplay(ContentDisplay.TOP);

    try {
        java.io.InputStream stream = getClass().getResourceAsStream(
            "/com/mycompany/crowns/images/" + carta.getImagemPath());
        if (stream != null) {
            ImageView iv = new ImageView(new Image(stream));
            iv.setFitWidth(80);
            iv.setFitHeight(80);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);

            if (carta.estaMorta() || carta.isEmRecarga()) {
                ColorAdjust escurecer = new ColorAdjust();
                escurecer.setBrightness(-0.6);
                iv.setEffect(escurecer);
            }
            btn.setGraphic(iv);
        }
    } catch (Exception e) {
        System.err.println("Imagem não encontrada: " + carta.getImagemPath());
    }

    String status = carta.estaMorta()    ? "💀 MORTA"
                  : carta.isEmRecarga()  ? " Recarga"
                  : "❤ " + carta.getVidaAtual() + "  ⚔ " + carta.getDano() + "  ⚡ " + carta.getCusto();
    btn.setText(carta.getNome() + "\n" + status);

    String cor = carta.estaMorta()    ? "#333333"
               : carta.isEmRecarga()  ? "#3a3a5a"
               : clicavel             ? "#1a3a1a"
               :                        "#3a1a1a";

    btn.setStyle(
        "-fx-background-color: " + cor + ";" +
        "-fx-text-fill: white;" +
        "-fx-font-size: 10px;" +
        "-fx-background-radius: 8;" +
        "-fx-border-color: #555;" +
        "-fx-cursor: " + (clicavel ? "hand" : "default") + ";"
    );

    if (clicavel && !carta.estaMorta()) {
        btn.setOnAction(e -> usarCarta(carta));
    }
    btn.setDisable(carta.estaMorta());

    return btn;
}

    private void log(String msg) {
        logBatalha.appendText(msg + "\n");
    }
}
