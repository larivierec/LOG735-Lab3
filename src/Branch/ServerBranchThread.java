package Branch;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;


public class ServerBranchThread extends Thread {
    private ServerSocket mServerSocket;
    private Branch branch;

    private CopyOnWriteArrayList<Socket> mServerList;
    private CopyOnWriteArrayList<ClientBranchThread> mClientList;

    public ServerBranchThread(Branch branch){
        this.branch = branch;
        this.mServerList = new CopyOnWriteArrayList<Socket>();
        this.mClientList = new CopyOnWriteArrayList<ClientBranchThread>();
    }

    public void onNewConnection(Socket server, int count){
        if(this.mServerList.indexOf(server) == -1){
            //System.out.println("Connection accepted on port: " + server.getPort() + " ip: " + server.getLocalAddress());
            this.mServerList.add(server);
            //sendBranchInfo(server);

            BranchToBranchThread branchToBranchThread = new BranchToBranchThread(server, branch);
        }
    }

    void sendBranchInfo(Socket sock){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(BranchActions.TRANSACTION_BRANCH_TO_BRANCH.getActionID());
            oos.writeObject(branch.getCurrentMoney());
            oos.writeObject(branch.getIpAddress());
            oos.writeObject(branch.getListeningPort());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void start(){
        try {
            int count =0;
            System.out.println("Le serveur ecoute sur le port: " + branch.getListeningPort());
            this.mServerSocket = new ServerSocket(branch.getListeningPort());

            while (true) {
                onNewConnection(this.mServerSocket.accept(), count);
            }
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }
}
