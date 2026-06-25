module com.mycompany.crowns {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.mycompany.crowns to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.fxml;

    exports com.mycompany.crowns;
    exports com.mycompany.crowns.dao;
    exports model;
    exports controller;
    exports utils;
}