package Branch;

import Interfaces.ConnectionInfo;
import Interfaces.IObserver;
import Interfaces.IServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Branch extends IServer implements IObserver{

    private int                mCurrentMoney = 0;
    private CopyOnWriteArrayList<BranchInfo> mBranches;
    private Queue<BranchInfo>                mNewConnections;
    private ServerBranchThread mListenerThread;
    private ClientBranchThread mClientThread;

    public Branch(String ipAddr, int listeningPort, int connectionPort, int moneyOwned){
        super(ipAddr, listeningPort, connectionPort);
        this.mCurrentMoney = moneyOwned;
        this.mBranches = new CopyOnWriteArrayList<BranchInfo>();
        this.mClientThread = new ClientBranchThread(this);
        this.mListenerThread = new ServerBranchThread(this);

        this.mClientThread.start();
        this.mListenerThread.start();
    }

    public synchronized int getCurrentMoney(){
        return this.mCurrentMoney;
    }

    public void setCurrentMoney(int money){
        this.mCurrentMoney += money;
    }

    public static void main(String [] args){
        if(args.length == 3) {
            Branch td = new Branch(ConnectionInfo.ConnectionEnum.IPADDRESS.getNumber(),
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]));
        }
        System.exit(0);
    }

    @Override
    public void update(Object observable, Object arg) {

    }
}
