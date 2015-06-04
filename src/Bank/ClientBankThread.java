package Bank;

import Interfaces.IServer;

import java.io.*;
import java.net.Socket;

public class ClientBankThread extends Thread{
    private Socket  mSocket;
    private ObjectInputStream ois;

    public ClientBankThread(){}
    public ClientBankThread(IServer IServer){
        try{
            this.mSocket = new Socket(IServer.getIpAddress(), IServer.getDestinationPort());
        }catch(IOException e){
            System.out.println("Client Connection vers: " + IServer.getDestinationPort() + " port: " + IServer.getIpAddress());
        }
    }

    public ClientBankThread(Socket socket){
        this.mSocket = socket;
        try {
            ois = new ObjectInputStream(this.mSocket.getInputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
        this.start();

    }
    public ClientBankThread(String ipAddr, int destPort){
        try{
            this.mSocket = new Socket(ipAddr,destPort);
        }catch(IOException e){
            System.out.println("Client Connection vers: " + ipAddr + " port: " + destPort);
        }
    }

    @Override
    public void start(){
        boolean running = true;
        try {
            //ObjectOutputStream oos = new ObjectOutputStream(mSocket.getOutputStream());
            while (running) {
                Object command = ois.readObject();
                if(command instanceof String) {
                    System.out.println("ServerList Requested");
                }
                else if(command instanceof Integer) {
                    System.out.println("Received a number");

                }
                else{
                    System.out.println("Default executed");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
