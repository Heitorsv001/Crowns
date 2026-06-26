package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utils.GerenciadorCena;

public class CreditosController {

    @FXML private Label labelAluno1;
    @FXML private Label labelAluno2;

    @FXML private Label labelDisciplina;

    @FXML private Label labelRef1;
    @FXML private Label labelRef2;

    @FXML private Label labelTec1;
    @FXML private Label labelTec2;
    @FXML private Label labelTec3;



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

    }


    @FXML
    private void aoVoltar() {
        GerenciadorCena.irParaMenu();
    }
}