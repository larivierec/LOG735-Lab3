package Bank;

import java.util.ArrayList;
import java.util.List;
import Interfaces.IServer;

public class Bank extends IServer {

    private List<IServer>  mServerList;
    private ServerBankThread mServerBankThread;

    private int           mArgentTotal;

    public Bank(String ipAddr, int port){
        super(ipAddr, port);
        mServerList = new ArrayList<IServer>();
        mServerBankThread = new ServerBankThread(this);
        mServerBankThread.start();
    }

    public static void main(String [] args){
        Bank bank = new Bank("127.0.0.1",12345);
    }

}
