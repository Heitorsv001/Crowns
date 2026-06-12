package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Jogador;
import utils.EstadoJogo;
import utils.GerenciadorCena;

import java.net.URL;
import java.util.ResourceBundle;

public class LojaController implements Initializable {

    @FXML private Label lblMoedas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Jogador j = EstadoJogo.get().getJogador();
        if (j != null) lblMoedas.setText(" Suas moedas: " + j.getMoedas());
    }

    @FXML private void aoVoltar() { GerenciadorCena.irParaRecompensa(); }
}
