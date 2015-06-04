
public class Branch extends Server{

    private int    mCurrentMoney = 0;
    private ServerThread        mBranchThread;
    private ClientThread        mOutgoingThread;
    public Branch(String ipAddr, int listeningPort, int connectionPort){
        super(ipAddr, listeningPort, connectionPort);
        this.mBranchThread = new ServerThread(this);
        this.mOutgoingThread = new ClientThread(this);
        this.mBranchThread.start();
        this.mOutgoingThread.start();
    }

    public Branch(String name, int initialMoney){
        this.mCurrentMoney = initialMoney;
    }

    public static void main(String [] args){
        Branch td = new Branch(Connection.ConnectionEnum.IPADDRESS.getNumber(),
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]));
    }

}
