package org.DS_assignment2;

import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientStarter {

    public static void main(String[] args){

        try {
            String hostName = "localhost";
            int port = Integer.valueOf("4444");
            int clientPort = 3000;

            if (args[0] != null){
                hostName = args[0];
            }

            if (args[1] != null){
                port = Integer.valueOf(args[1]);
            }

            if (args[2] != null){
                clientPort = Integer.valueOf(args[2]);
            }

            Registry registry = LocateRegistry.getRegistry(Integer.valueOf(port));
            ServerService server = (ServerService) registry.lookup(hostName);

            ClientService clientMessage = (ClientService) UnicastRemoteObject.exportObject(
                    new ClientMessage(), clientPort);
            registry.rebind("client", clientMessage);

            ClientController.getClientController().init(server, clientMessage);
            ClientController.getClientController().run();

        } catch (ConnectException e){
            System.out.println("Server is not running.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
