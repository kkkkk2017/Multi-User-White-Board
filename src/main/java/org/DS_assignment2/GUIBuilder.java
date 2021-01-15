package org.DS_assignment2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;

/**
 * JavaFX App
 */
public class GUIBuilder extends Application implements Serializable{

    private static Scene scene;
    private static FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("loginscreen"));
        stage.setTitle("Shared WhiteBoard - Client");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        fxmlLoader = new FXMLLoader(GUIBuilder.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void buildGUI() {
        launch();
    }

    public void updateCanvas(int[] imageData) {
        MainScreen controller = fxmlLoader.getController();
        if (controller != null){
            controller.updateCanvas(imageData);
        }
    }

    public void updateClientList(){
        if (fxmlLoader.getController() instanceof  MainScreen){
            MainScreen controller = fxmlLoader.getController();
            if (controller != null){
                controller.updateClients();
            }
        }
    }
}