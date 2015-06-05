package Branch;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import Interfaces.IServer;


public class ServerBranchThread extends Thread {
    private ServerSocket mServerSocket;
    private Branch mServer;

    private List<Socket> mServerList;
    private List<ClientBranchThread> mClientList;

    public ServerBranchThread(Branch branch){
        this.mServer = branch;
        this.mServerList = new ArrayList<Socket>();
    }

    public void onNewConnection(Socket server){
        if(this.mServerList.indexOf(server) == -1){
            System.out.println("Connection accepted on port: " + server.getPort() + " ip: " + server.getLocalAddress());
            this.mServerList.add(server);
            sendBranchInfo(server);
            this.mClientList.add(new ClientBranchThread(server));
        }
    }

    void sendBranchInfo(Socket sock){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(1);
            oos.writeObject(mServer.getCurrentMoney());
            oos.flush();
        }catch(IOException e){
            e.printStackTrace();
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
