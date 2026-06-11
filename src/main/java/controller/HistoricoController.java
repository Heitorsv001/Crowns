package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Jogador;
import utils.EstadoJogo;
import utils.GerenciadorCena;

import java.net.URL;
import java.util.ResourceBundle;

public class HistoricoController implements Initializable {

    @FXML private Label lblEstatisticas;
    @FXML private TextArea areaHistorico;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Jogador j = EstadoJogo.get().getJogador();
        if (j == null) {
            lblEstatisticas.setText("Nenhuma sessão iniciada.");
            return;
        }
        lblEstatisticas.setText("Jogador: " + j.getNome()
            + "   |   Vitórias: " + j.getTotalVitorias()
            + "   |   Derrotas: " + j.getTotalDerrotas()
            + "   |   Moedas: " + j.getMoedas());
        areaHistorico.setText(
            "Vitórias totais: " + j.getTotalVitorias() + "\n"
            + "Derrotas totais: " + j.getTotalDerrotas() + "\n"
            + "Arena atual: " + (j.getArenaAtual() + 1) + "\n\n"
            + "(Histórico detalhado disponível após integração com banco de dados)");
    }

    @FXML private void aoVoltar() { GerenciadorCena.irParaMenu(); }
}
