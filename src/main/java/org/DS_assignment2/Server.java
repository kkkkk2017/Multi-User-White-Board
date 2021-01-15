package org.DS_assignment2;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements ServerService{

    public Server() throws RemoteException {
        AdminController.getAdmin().init();
    }

    @Override
    public boolean approvalJoin(ClientService client) throws RemoteException {
        if (!AdminController.getAdmin().getClientName().contains(client.getClient().getName())){
            AdminController.getAdmin().getClientList().add(client);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Client Join");
                        alert.setHeaderText("[Client] " + client.getClient().getName() + " wants to share your whiteboard");
                        alert.showAndWait();

                        client.receiveMessage(AdminController.getAdmin().getImageData());
                        updateClientList();
                        AdminController.getAdmin().broadcastMessage("update_clients");
                    } catch (RemoteException e) {
                        System.err.println(e.getMessage());
                    }
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean disconnect(ClientService client) {
        if (AdminController.getAdmin().containClient(client)){
            AdminController.getAdmin().getClientList().remove(client);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Client Quit");
                        alert.setHeaderText("Client " + client.getClient().getName() + " quit.");
                        alert.showAndWait();
                        updateClientList();
                        AdminController.getAdmin().broadcastMessage("update_clients");
                    }catch (RemoteException e){
                        System.err.println(e.getMessage());
                    }
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean setImageData(ClientService client, int[] imageData) {
        if (imageData != null){
            AdminController.getAdmin().setImageData(imageData);
            if (ServerStarter.getFxmlLoader().getController() instanceof  ServerMainScreen){
                ServerMainScreen controller = ServerStarter.getFxmlLoader().getController();
                if (controller != null){
                    controller.updateCanvas();
                }
            }
            AdminController.getAdmin().updateClientCanvas(client);
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<String> getClients() {
        ArrayList<String> result = new ArrayList<>();
        for (ClientService c: AdminController.getAdmin().getClientList()) {
            try {
                result.add(c.getClient().getName());
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }


    private void updateClientList(){
        if (ServerStarter.getFxmlLoader().getController() instanceof  ServerMainScreen){
            ServerMainScreen controller = ServerStarter.getFxmlLoader().getController();
            if (controller != null){
                controller.updateClients();
            }
        }
    }


}

