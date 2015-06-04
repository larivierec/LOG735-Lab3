import java.util.ArrayList;
import java.util.List;

public class Bank extends Server{

    private List<Server>  mServerList;
    private ServerThread  mServerThread;

    public Bank(String ipAddr, int port){
        super(ipAddr, port);
        mServerList = new ArrayList<Server>();
        mServerThread = new ServerThread(this);
        mServerThread.start();
    }

    public static void main(String [] args){
        Bank bank = new Bank("127.0.0.1",12345);
    }

}
