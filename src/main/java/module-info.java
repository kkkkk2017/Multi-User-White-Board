module org.DS_assignment2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.desktop;
    requires javafx.swing;

    opens org.DS_assignment2 to javafx.fxml;
    exports org.DS_assignment2;
}