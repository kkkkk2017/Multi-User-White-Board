package org.DS_assignment2;

import javafx.scene.control.Alert;
import java.rmi.RemoteException;
import java.util.ArrayList;


public class ClientController{
    private ServerService SERVER;
    private GUIBuilder gui;
    private Client client;
    private ClientService clientService;
    private static ClientController clientController = new ClientController();

    public static ClientController getClientController() {
        return clientController;
    }

    private ClientController() {
    }

    public void init(ServerService server, ClientService clientService){
        this.SERVER = server;
        this.clientService = clientService;
        this.gui = new GUIBuilder();
    }

    public GUIBuilder getGui() {
        return gui;
    }

    public void setClient(String name, String password){
        this.client = new Client(name);
        client.setPassword(password);
    }

    public boolean joinServer(){
        try {
            if (this.clientService != null){
                return SERVER.approvalJoin(this.clientService);
            }
        } catch (RemoteException e) {
            System.err.println("error on join server [ClientController]");
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean quitServer(){
        try {
            return SERVER.disconnect(this.clientService);
        } catch (RemoteException e) {
            System.err.println("error on quit server [ClientController]");
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void sendImage(int[] imageData){
        try {
            SERVER.setImageData(clientService, imageData);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateCanvas(int[] imageData) {
        if (imageData != null){
            gui.updateCanvas(imageData);
        }
    }

    public Client getClient(){
        return this.client;
    }


    public ArrayList<String> getAllClients(){
        try {
            return SERVER.getClients();
        } catch (RemoteException e) {
           System.err.println(e.getMessage());
        }
        return null;
    }

    public void run() {
        getGui().buildGUI();
    }

    public void updateUser(){
        gui.updateClientList();
    }

    public void quit(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Disconnect");
        alert.setHeaderText(message);
        alert.showAndWait();
        System.exit(0);
    }

}
