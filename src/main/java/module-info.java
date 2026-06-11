module com.mycompany.crowns {
    requires javafx.controls;
    requires javafx.fxml;

    // abre pacotes ao JavaFX para reflexão (necessário para FXML e controllers)
    opens com.mycompany.crowns to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.fxml;

    exports com.mycompany.crowns;
    exports model;
    exports controller;
    exports utils;
}
