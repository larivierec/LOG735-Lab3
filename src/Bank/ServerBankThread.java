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
    private Bank mServer;

    private List<Socket> mServerList;
    private List<ClientBankThread> mClientList;

    public ServerBankThread(Bank bank){
        this.mServer = bank;
        this.mServerList = new ArrayList<Socket>();
        this.mClientList = new ArrayList<ClientBankThread>();
    }

    public void onNewConnection(Socket server){
        if(this.mServerList.indexOf(server) == -1){
            System.out.println("Connection accepted on port: " + server.getPort() + " ip: " + server.getLocalAddress());
            this.mServerList.add(server);
            ClientBankThread clientThread = new ClientBankThread(server,mServer);
            this.mClientList.add(clientThread);
            clientThread.run();
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
