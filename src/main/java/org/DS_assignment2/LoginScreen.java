package org.DS_assignment2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginScreen {
    @FXML private TextField username;
    @FXML private TextField password;

    @FXML
    private void login(ActionEvent actionEvent) throws IOException {
        String user = username.getText();
        String pw = password.getText();
        if (user != null && pw != null){
            ClientController.getClientController().setClient(user, pw);
            if(ClientController.getClientController().joinServer()){
                GUIBuilder.setRoot("mainscreen");
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Join error");
                alert.setHeaderText("User existed.");
                alert.setContentText("Please enter another user name.");
                alert.showAndWait();
            }
        }
    }
}
