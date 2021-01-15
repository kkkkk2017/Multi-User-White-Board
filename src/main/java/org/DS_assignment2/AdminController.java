package org.DS_assignment2;

import javafx.scene.control.Alert;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AdminController {
    private ConcurrentLinkedQueue<ClientService> clientList;
    private int[] imageData;
    private int HEIGHT = 400;
    private int WIDTH = 500;
    private Client admin;

    private static AdminController adminController = new AdminController();

    private AdminController() { }

    public static AdminController getAdmin() {
        return adminController;
    }

    public void init(){
        clientList = new ConcurrentLinkedQueue<>();
        imageData = new int[WIDTH * HEIGHT];
    }

    public void setClient(String name, String password){
        this.admin = new Client(name);
        admin.setPassword(password);
    }

    public Client getAdminClient(){
        return this.admin;
    }

    public int[] getImageData() {
        return imageData;
    }

    public void setImageData(int[] imageData) {
        this.imageData = imageData;
    }

    public ConcurrentLinkedQueue<ClientService> getClientList() {
        return clientList;
    }

    public ArrayList<String> getClientName(){
        ArrayList<String> result = new ArrayList<>();
        for (ClientService c: getClientList()) {
            try {
                result.add(c.getClient().getName());
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    //admin user join server
    public boolean joinServer(String username, String password){
        if (getAdminClient() == null){
            setClient(username, password);
            return true;
        }
        return false;
    }

    //admin user shut down server
    public void closeServer(){
        if (getClientList() != null){
            for (ClientService c: getClientList()) {
                synchronized (c){
                    try {
                        getClientList().remove(c);
                        c.receiveMessage("server_close");
                    } catch (RemoteException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
    }

    //remove one user
    public boolean removeUser(String name){
        if (getClientList() != null){
            try {
                for (ClientService client: getClientList()) {
                    String currentName = client.getClient().getName();
                    if (currentName.equals(name)){
                        this.clientList.remove(client);
                        client.receiveMessage("kickout");
                    } else {
                        broadcastMessage("user_quit");
                    }
                }
                return true;
            } catch (RemoteException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("[Admin] Error on remove User.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        return false;
    }

    public synchronized void updateClientCanvas(ClientService client){
        for (ClientService c:  getClientList()) {
            try {
                if (client != null){
                    if (!c.getClient().getName().equals(client.getClient().getName())){
                        c.receiveMessage(getImageData());
                    }
                } else {
                    c.receiveMessage(getImageData());
                }
            } catch (RemoteException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void broadcastMessage(String msg){
        try{
            if (AdminController.getAdmin().getClientList().size() != 0){
                for (ClientService c: AdminController.getAdmin().getClientList()) {
                    synchronized (c){
                        c.receiveMessage(msg);
                    }
                }
            }
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean containClient(ClientService compare){
        for (ClientService client: clientList) {
            synchronized (client){
                try {
                    if (client.getClient().getName().equals(compare.getClient().getName())){
                        return true;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
