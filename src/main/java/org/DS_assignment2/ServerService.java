package org.DS_assignment2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerService extends Remote {
    //approve client join request
    boolean approvalJoin(ClientService client) throws RemoteException;

    boolean disconnect(ClientService client) throws RemoteException;

    boolean setImageData(ClientService client, int[] imageData) throws RemoteException;

    ArrayList<String> getClients() throws RemoteException;
}
