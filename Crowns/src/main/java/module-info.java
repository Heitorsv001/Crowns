module com.mycompany.crowns {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.crowns to javafx.fxml;
    exports com.mycompany.crowns;
}
