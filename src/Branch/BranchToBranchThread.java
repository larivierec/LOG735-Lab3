package Branch;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BranchToBranchThread extends Thread{
    private Socket                          mSocket;
    private ObjectOutputStream              mOOS;
    private ObjectInputStream               mOIS;

    public BranchToBranchThread(BranchInfo connInfo){
        try {
            System.out.println("Tentative de connexion à : " + connInfo.getIPAddress() + " sur le port : " + connInfo.getListenPort());
            this.mSocket = new Socket(connInfo.getIPAddress(), connInfo.getListenPort());
            this.mOOS = new ObjectOutputStream(this.mSocket.getOutputStream());
            this.mOIS = new ObjectInputStream(this.mSocket.getInputStream());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        boolean running = true;
        while(running){

        }
    }

}
