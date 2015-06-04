import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread{
    private Socket  mSocket;

    public ClientThread(){}
    public ClientThread(Server server){
        try{
            this.mSocket = new Socket(server.getIpAddress(), server.getDestinationPort());
        }catch(IOException e){
            System.out.println("Client Connection vers: " + server.getDestinationPort() + " port: " + server.getIpAddress());
        }
    }
    public ClientThread(String ipAddr, int destPort){
        try{
            this.mSocket = new Socket(ipAddr,destPort);
        }catch(IOException e){
            System.out.println("Client Connection vers: " + ipAddr + " port: " + destPort);
        }
    }

    @Override
    public void start(){

    }
}
