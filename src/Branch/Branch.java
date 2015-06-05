package Branch;

import Interfaces.ConnectionInfo;
import Interfaces.IServer;

public class Branch extends IServer {

    private int                mCurrentMoney = 0;
    private ServerBranchThread mListenerThread;
    private ClientBranchThread mClientThread;

    public Branch(String ipAddr, int listeningPort, int connectionPort, int moneyOwned){
        super(ipAddr, listeningPort, connectionPort);
        this.mCurrentMoney = moneyOwned;
        this.mClientThread = new ClientBranchThread(this);
        this.mListenerThread = new ServerBranchThread(this);
        this.mClientThread.start();
        this.mListenerThread.start();
    }

    public Branch(String name, int initialMoney){
        this.mCurrentMoney = initialMoney;
    }

    public synchronized int getCurrentMoney(){
        return this.mCurrentMoney;
    }

    public synchronized void setmCurrentMoney(int money){
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
}
