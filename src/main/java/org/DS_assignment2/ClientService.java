package org.DS_assignment2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientService extends Remote {
    Client getClient() throws RemoteException;

    void receiveMessage(int[] imageData) throws RemoteException;

    void receiveMessage(String msg) throws RemoteException;

}
