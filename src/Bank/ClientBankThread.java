package Bank;

import Interfaces.IServer;

import java.io.*;
import java.net.Socket;

public class ClientBankThread extends Thread{
    private Socket              mSocket;
    private ObjectInputStream   mOIS;
    private ObjectOutputStream  mOOS;


    public ClientBankThread(Socket socket, IServer server){
        this.mSocket = socket;
        try {
            mOIS = new ObjectInputStream(this.mSocket.getInputStream());
            mOOS = new ObjectOutputStream(this.mSocket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        boolean running = true;
        try {
            while (running) {
                Object command = mOIS.readObject();
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
