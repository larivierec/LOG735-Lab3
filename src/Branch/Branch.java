package Branch;

import Interfaces.ConnectionInfo;
import Interfaces.IServer;

public class Branch extends IServer {

    private int                mCurrentMoney = 0;
    private ServerBranchThread mListenerThread;
    private ClientBranchThread mOutgoingThread;

    public Branch(String ipAddr, int listeningPort, int connectionPort, int moneyOwned){
        super(ipAddr, listeningPort, connectionPort);
        this.mCurrentMoney = moneyOwned;
        this.mListenerThread = new ServerBranchThread(this);
        this.mOutgoingThread = new ClientBranchThread(this);
        this.mListenerThread.start();
    }

    public Branch(String name, int initialMoney){
        this.mCurrentMoney = initialMoney;
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
