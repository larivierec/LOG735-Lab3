package Branch;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientBranchThread extends Thread{
    private Socket  mSocket;
    private Branch  mBranch;
    private ObjectInputStream mOIS;
    private ObjectOutputStream mOOS;

    public ClientBranchThread(Branch branch){
        this.mBranch = branch;
        try{
            this.mSocket = new Socket(branch.getIpAddress(), branch.getDestinationPort());
            this.mOOS = new ObjectOutputStream(this.mSocket.getOutputStream());
            this.mOIS = new ObjectInputStream(this.mSocket.getInputStream());
        }catch(IOException e){
            System.out.println("Client Connection vers: " + branch.getDestinationPort() + " port: " + branch.getIpAddress());
        }
    }

    public ClientBranchThread(Socket socket){
        this.mSocket = socket;
    }

    @Override
    public void run(){
        boolean running = true;
        try {

            mOOS.writeObject(1);
            mOOS.writeObject(mBranch.getCurrentMoney());
            while (running) {
                int commandID = (Integer) mOIS.readObject();
                if(commandID == 3){
                    mBranch.update(this,(List) mOIS.readObject());
                }
                if(commandID == 10){
                    System.exit(0);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
