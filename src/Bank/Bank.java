package Bank;

import java.util.ArrayList;
import java.util.List;

import Interfaces.IObserver;
import Interfaces.IServer;

public class Bank extends IServer implements IObserver{

    private List<IServer>    mServerList;
    private ServerBankThread mServerBankThread;
    private int              mArgentTotal;

    public Bank(String ipAddr, int port){
        super(ipAddr, port);
        mServerList = new ArrayList<IServer>();
        mServerBankThread = new ServerBankThread(this);
    }

    @Override
    public void start(){
        mServerBankThread.start();
    }

    @Override
    public void update(Object observable, Object arg) {
        this.mArgentTotal += (Integer) arg;
    }

    public static void main(String [] args){
        Bank bank = new Bank("127.0.0.1",12345);
        bank.start();
    }
}
