package com.company.Application.Controllers.Server;

import com.company.Application.Commands.CommandInvoker;
import com.company.Application.Controllers.DbUsers;
import com.company.Application.Data;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;


public class ClientHandler implements  Runnable {
    volatile Data data;
    final Socket socket;
    final private DbUsers dataBase;
    final private CommandInvoker commandInvoker;
    final private ExecutorService fixedPool;
    final private ExecutorService cachedPool;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

//    private volatile boolean ready = false;
    private ArrayList<Data> responses = new ArrayList<>();

    public ClientHandler(Socket socket, DbUsers dataBase, CommandInvoker commandInvoker, ExecutorService fixedPool, ExecutorService cachedPool) throws IOException {
        this.socket = socket;
        this.dataBase = dataBase;
        this.commandInvoker = commandInvoker;
        this.fixedPool = fixedPool;
        this.cachedPool = cachedPool;
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());



    }

    @Override
    public void run() {

        fixedPool.submit(this::handleRequest);


    }
    private boolean handleRequest() throws IOException {
        System.out.println("New client connected");

        try{
            while (!socket.isClosed()){
                System.out.println("waiting for data...");

                System.out.println("got output Stream");
                data = (Data)objectInputStream.readObject();
                System.out.println("received\n"+data.toString());
                Thread handle = new Thread(this::handleData);
                handle.start();

            }
        }
        catch (SocketException e) {

            System.out.println("disconnected");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }



    private void handleData(){

        System.out.println("handle data");
        String command = data.getCommand();
        String login = data.getLogin();
        String pass = data.getPassword();
        if (command.equals("login")||command.equals("reg")){
            responses= commandInvoker.executeCommand(data);
        }
        else{
            if(dataBase.checkPass(login, pass)){
                responses = commandInvoker.executeCommand(data);
            }
            else{

                responses.add(new Data("err", false));
            }
        }

        cachedPool.submit(this::sendResponse);

    }
    private  void sendResponse(){
        try {
            synchronized (objectOutputStream){
                for (Data response : responses){

                    objectOutputStream.writeObject(response);
                    objectOutputStream.flush();

                    System.out.println("sent:\n"+ response.toString());
                }
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
    public  void sendUpdate(ArrayList<Data> responses) {
        synchronized (objectOutputStream){
            try {
                for (Data response : responses) {
                    objectOutputStream.writeObject(response);
                    objectOutputStream.flush();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
