package org.DS_assignment2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ServerLoginScreen {
    @FXML
    private TextField username;
    @FXML private TextField password;

    @FXML
    protected void initialize(){
        this.username.setText("admin");
        this.password.setText("admin");
    }

    @FXML
    private void login(ActionEvent actionEvent) throws IOException {
        String user = username.getText();
        String pw = password.getText();
        if (user != "admin" && pw != "admin"){
            if(AdminController.getAdmin().joinServer(user, pw)){
                ServerStarter.setRoot("servermainscreen");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("admin account details");
            alert.setContentText("username: admin\npassword: admin");
            alert.showAndWait();
        }
    }
}
