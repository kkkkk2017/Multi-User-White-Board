package org.DS_assignment2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerStarter extends Application {

    private static Scene scene;
    private static FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("serverloginscreen"));
        stage.setTitle("Shared WhiteBoard - Server[Admin]");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        fxmlLoader = new FXMLLoader(ServerStarter.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public static void main(String[] args){
        String hostName = "localhost";
        if (args[0] != null){
            hostName = args[0];
        }
        String port = "4444";
        if (args[1] != null){
            port = args[1];
        }

        try {
            ServerService server = new Server();

            //System.setSecurityManager(new RMISecurityManager());
            Registry registry = LocateRegistry.createRegistry(Integer.valueOf(port));
            registry.rebind(hostName,server);

            launch();

        } catch (RemoteException e1) {
            System.err.println(e1.getMessage());
        }


    }

}
