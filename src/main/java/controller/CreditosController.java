package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utils.GerenciadorCena;

public class CreditosController {

    // Alunos
    @FXML private Label labelAluno1;
    @FXML private Label labelAluno2;

    // Disciplina
    @FXML private Label labelDisciplina;

    // Referências de jogos
    @FXML private Label labelRef1;
    @FXML private Label labelRef2;

    // Tecnologias
    @FXML private Label labelTec1;
    @FXML private Label labelTec2;
    @FXML private Label labelTec3;

    // Versão
    @FXML private Label labelVersao;

    // ─── Inicialização ───────────────────────────────────────────────────────

    @FXML
    public void initialize() {

        labelAluno1.setText("Heitor Vareiro");
        labelAluno2.setText("Guilherme Scarpellini");

        labelDisciplina.setText("Tópicos em Linguagem de Programação 2");

        labelRef1.setText("Clash Royale");
        labelRef2.setText("Inscryption");

        labelTec1.setText("Java 17");
        labelTec2.setText("JavaFX 11");
        labelTec3.setText("Maven");

        labelVersao.setText("v1.0");
    }

    // ─── Navegação ───────────────────────────────────────────────────────────

    @FXML
    private void aoVoltar() {
        GerenciadorCena.irParaMenu();
    }
}