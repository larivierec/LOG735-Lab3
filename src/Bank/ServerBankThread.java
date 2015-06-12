package Bank;

import Branch.BranchInfo;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerBankThread extends Thread {
    private ServerSocket mServerSocket;
    private Bank mBank;

    private Vector<ObjectOutputStream> mBranchObjectStreamList;

    public ServerBankThread(Bank bank){
        this.mBank = bank;
        this.mBranchObjectStreamList = new Vector<ObjectOutputStream>();
    }

    public void onNewConnection(Socket server){
        if(this.mBranchObjectStreamList.indexOf(server) == -1){
            System.out.println("Connexion accepte sur le port : " + server.getLocalPort() + " ip: " + server.getLocalAddress());
            try{
                ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
                this.mBranchObjectStreamList.add(oos);
                ClientBankThread clientThread = new ClientBankThread(server, mBank);
                clientThread.start();
            }catch(IOException e){
                e.printStackTrace();
            }
            //after receiving the bank information send the serverlist!
            sendServerList();
        }
    }

    public void sendServerList(){
        for(ObjectOutputStream oos : mBranchObjectStreamList){
            try{

                //event id for sending branch lists
                oos.writeObject(3);
                oos.writeObject(mBank.getServerList().size());
                for(BranchInfo branchInfo : mBank.getServerList()){
                    oos.writeObject(branchInfo.getBranchID());
                    oos.writeObject(branchInfo.getIPAddress());
                    oos.writeObject(branchInfo.getListenPort());
                }
                oos.flush();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start(){
        try {
            this.mServerSocket = new ServerSocket(mBank.getListeningPort());
            System.out.println("Le serveur ecoute sur le port: " + mBank.getListeningPort());
            while (true) {
                onNewConnection(this.mServerSocket.accept());

            }
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }
}
