package Bank;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import Branch.BranchInfo;
import Interfaces.IObserver;
import Interfaces.IServer;

public class Bank extends IServer implements IObserver{

    private CopyOnWriteArrayList<BranchInfo> mServerList;
    private ServerBankThread mServerBankThread;
    private Integer          mArgentTotal = 0;
    private Integer          mServerID = 0;

    public Bank(String ipAddr, int port){
        super(ipAddr, port);
        mServerList = new CopyOnWriteArrayList<BranchInfo>();
        mServerBankThread = new ServerBankThread(this);
    }

    private synchronized Integer incrementServerID(){
        return (this.mServerID += 1);
    }

    public synchronized void addServerToList(BranchInfo branchInfo){
        this.mServerList.add(getServerID(), branchInfo);
        incrementServerID();
    }

    public int getArgentTotal(){
        return this.mArgentTotal;
    }

    public synchronized int getServerID(){
        return this.mServerID;
    }

    public synchronized ServerBankThread getmServerBankThread() {
        return mServerBankThread;
    }


    public synchronized List<BranchInfo> getServerList(){
        return this.mServerList;
    }

    public void printAmount(){
        System.out.println("La banque contient : " + this.mArgentTotal);
    }

    @Override
    public void start(){
        mServerBankThread.start();
    }

    @Override
    public void update(Object observable, Object arg) {
        this.mArgentTotal += (Integer) arg;
        printAmount();
    }

    public static void main(String [] args){
        Bank bank = new Bank("127.0.0.1", 10118);
        bank.start();
    }
}
