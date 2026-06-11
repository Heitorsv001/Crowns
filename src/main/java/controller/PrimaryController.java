package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import utils.EstadoJogo;
import utils.GerenciadorCena;

public class PrimaryController {

    @FXML private TextField campoNome;
    @FXML private Label lblErro;

    @FXML
    private void aoClicarJogar() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            lblErro.setText("Digite seu nome para começar!");
            return;
        }
        EstadoJogo.get().iniciarSessao(nome);
        GerenciadorCena.irParaArena();
    }

    @FXML
    private void aoClicarHistorico() {
        GerenciadorCena.irParaHistorico();
    }

    @FXML
    private void aoClicarCreditos() {
        GerenciadorCena.irParaCreditos();
    }

    @FXML
    private void aoClicarSair() {
        Platform.exit();
    }
}
