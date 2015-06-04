package Branch;

import Bank.ClientBankThread;
import Bank.ServerBankThread;
import Interfaces.ConnectionInfo;
import Interfaces.IServer;

public class Branch extends IServer {

    private int    mCurrentMoney = 0;
    private ServerBankThread mBranchThread;
    private ClientBankThread mOutgoingThread;

    public Branch(String ipAddr, int listeningPort, int connectionPort){
        super(ipAddr, listeningPort, connectionPort);
        //this.mBranchThread = new Bank.ServerThread(this);
        this.mOutgoingThread = new ClientBankThread(this);
        //this.mBranchThread.start();
        this.mOutgoingThread.start();
    }

    public Branch(String name, int initialMoney){
        this.mCurrentMoney = initialMoney;
    }

    public static void main(String [] args){
        Branch td = new Branch(ConnectionInfo.ConnectionEnum.IPADDRESS.getNumber(),
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]));
    }

}
