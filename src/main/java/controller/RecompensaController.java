package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Recompensa;
import utils.EstadoJogo;
import utils.GerenciadorCena;

import java.net.URL;
import java.util.ResourceBundle;

public class RecompensaController implements Initializable {

    @FXML private Label lblMensagem, lblMoedas, lblCartaBonus;
    @FXML private Button btnContinuar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Recompensa r = EstadoJogo.get().getUltimaRecompensa();
        if (r == null) { GerenciadorCena.irParaMenu(); return; }

        lblMensagem.setText(r.getMensagem());
        lblMoedas.setText("" + r.getMoedas() + " moedas recebidas");
        lblCartaBonus.setText(r.isTemCartaBonus()
            ? "🎴 Carta bônus: " + r.getCartaBonus().getNome()
            : "");

        if (EstadoJogo.get().jogoCompleto()) {
            btnContinuar.setText("🏁 Fim de Jogo");
        }
    }

    @FXML
    private void aoContinuar() {
        if (EstadoJogo.get().jogoCompleto()) {
            GerenciadorCena.irParaMenu();
        } else {
            GerenciadorCena.irParaArena();
        }
    }

    @FXML
    private void aoIrLoja() {
        GerenciadorCena.irParaLoja();
    }
}
