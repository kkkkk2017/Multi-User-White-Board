package org.DS_assignment2;

import javafx.application.Platform;

public class ClientMessage extends Thread implements ClientService{

    public ClientMessage() {
    }

    @Override
    public Client getClient(){
        return ClientController.getClientController().getClient();
    }

    @Override
    public void receiveMessage(String msg){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this){
                        handleMessage(msg);
                    }
                } catch (Exception e){
                    System.err.println(e.getMessage());
                }
            }
        });
    }

    @Override
    public void receiveMessage(int[] imageData){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this){
                        ClientController.getClientController().updateCanvas(imageData);
                    }
                } catch (Exception e){
                    System.err.println(e.getMessage());
                }
            }
        });
    }

    //update the display client list
    private void handleMessage(String msg){
        synchronized (this) {
            switch (msg) {
                case "update_clients":
                    ClientController.getClientController().updateUser();
                    break;
                case "kickout":
                    ClientController.getClientController().quit("You have been kick out by Admin User.");
                    break;
                case "server_close":
                    ClientController.getClientController().quit("Server close.");
                    break;
            }
        }
    }
}
