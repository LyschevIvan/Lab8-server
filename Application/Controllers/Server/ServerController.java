package com.company.Application.Controllers.Server;


import com.company.Application.Commands.CommandInvoker;
import com.company.Application.Controllers.DbUsers;
import com.company.Application.Data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerController{
    private final static int BUFFER_SIZE = 2048;
    int port;
    private DbUsers dataBase;
    private static CommandInvoker commandInvoker;
    private ExecutorService fixedPool = Executors.newFixedThreadPool(4);
    private ExecutorService cachedPool = Executors.newCachedThreadPool();
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();
    public ServerController(int port, DbUsers dataBase, CommandInvoker commandInvoker){
        this.dataBase = dataBase;
        ServerController.commandInvoker = commandInvoker;
        this.port = port;

    }

    public void nexConnection() throws IOException {

            try(ServerSocket serverSocket = new ServerSocket(port)){
                System.out.println("Waiting for connection...");
                Socket socket = serverSocket.accept();

                ClientHandler client = new ClientHandler(socket, dataBase,commandInvoker, fixedPool, cachedPool);
                clients.add(client);
                Thread clientThread = new Thread(client);
                clientThread.start();
            }
    }
    public static void collectionChainged(){
        ArrayList<Data> responses = commandInvoker.executeCommand(new Data("show",true));
        for(ClientHandler client : clients){
            new Thread(()->client.sendUpdate(responses)).start();
        }
    }


}
