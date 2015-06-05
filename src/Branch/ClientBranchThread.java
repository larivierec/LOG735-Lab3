package Branch;

import Interfaces.IServer;

import java.io.*;
import java.net.Socket;

public class ClientBranchThread extends Thread{
    private Socket  mSocket;
    private Branch  mBranch;
    private ObjectInputStream  ois;
    private ObjectOutputStream oos;

    public ClientBranchThread(Branch branch){
        this.mBranch = branch;
        try{
            this.mSocket = new Socket(branch.getIpAddress(), branch.getDestinationPort());
        }catch(IOException e){
            System.out.println("Client Connection vers: " + branch.getDestinationPort() + " port: " + branch.getIpAddress());
        }
    }

    public ClientBranchThread(Socket socket){
        this.mSocket = socket;
    }

    @Override
    public void start(){
        boolean running = true;
        try {
            oos = new ObjectOutputStream(this.mSocket.getOutputStream());

            oos.writeObject(1);
            oos.writeObject(mBranch.getCurrentMoney());

            ois = new ObjectInputStream(this.mSocket.getInputStream());

            while (running) {
                /*Object command = ois.readObject();
                if(command instanceof String) {
                    System.out.println("ServerList Requested");
                }
                else if(command instanceof Integer) {
                    System.out.println("Received a number");

                }
                else{
                    System.out.println("Default executed");
                }*/
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
