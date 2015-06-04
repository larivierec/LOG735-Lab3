package Bank;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import Interfaces.IServer;


public class ServerBankThread extends Thread {
    private ServerSocket mServerSocket;
    private IServer mServer;

    private List<Socket> mServerList;
    private List<ClientBankThread> mClientList;

    public ServerBankThread(IServer IServer){
        this.mServer = IServer;
        this.mServerList = new ArrayList<Socket>();
    }

    public void onNewConnection(Socket server){
        if(this.mServerList.indexOf(server) == -1){
            System.out.println("Connection accepted on port: " + server.getPort() + " ip: " + server.getLocalAddress());
            this.mServerList.add(server);
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
