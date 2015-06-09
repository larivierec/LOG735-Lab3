package Bank;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerBankThread extends Thread {
    private ServerSocket mServerSocket;
    private Bank mServer;

    private List<Socket> mBranchList;

    public ServerBankThread(Bank bank){
        this.mServer = bank;
        this.mBranchList = new ArrayList<Socket>();
    }

    public void onNewConnection(Socket server){
        if(this.mBranchList.indexOf(server) == -1){
            System.out.println("Connection accepted on port: " + server.getPort() + " ip: " + server.getLocalAddress());
            this.mBranchList.add(server);
            ClientBankThread clientThread = new ClientBankThread(server,mServer);
            clientThread.start();

            //after receiving the bank information send the serverlist!
            sendServerList();
        }
    }

    public void sendServerList(){
        for(Socket branch : mBranchList){
            try{
                ObjectOutputStream oos = new ObjectOutputStream(branch.getOutputStream());
                //event id for sending branch lists
                oos.writeObject(3);
                //oos.writeObject(mBranchList);
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
            this.mServerSocket = new ServerSocket(mServer.getListeningPort());
            System.out.println("Le serveur ecoute sur le port: " + mServer.getListeningPort());
            while (true) {
                onNewConnection(this.mServerSocket.accept());

            }
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }
}
