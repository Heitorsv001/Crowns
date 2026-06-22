package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerta {

    public static void mostrarInfo(String titulo, String mensagem) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static void mostrarErro(String titulo, String mensagem) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static boolean confirmar(String titulo, String mensagem) {
        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        return alerta.showAndWait().filter(r -> r == ButtonType.OK).isPresent();
    }
}