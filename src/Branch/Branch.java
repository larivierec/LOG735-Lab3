package Branch;

import Interfaces.ConnectionInfo;
import Interfaces.IObserver;
import Interfaces.IServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Branch extends IServer implements IObserver{

    private int                mCurrentMoney = 0;
    private CopyOnWriteArrayList<BranchInfo> mBranches;
    private Queue<BranchInfo>                mNewConnections;
    private ServerBranchThread mListenerThread;
    private ClientBranchThread mClientThread;
    private Integer branchId;
    private BranchTransactionManager branchTransactionManager;
    private CopyOnWriteArrayList<BranchToBranchThread> branchToBranchThread;
    private BranchStateManager branchStateManager;


    public Branch(String ipAddr, int listeningPort, int connectionPort, int moneyOwned){
        super(ipAddr, listeningPort, connectionPort);
        this.mCurrentMoney = moneyOwned;
        this.mBranches = new CopyOnWriteArrayList<BranchInfo>();
        this.branchToBranchThread = new CopyOnWriteArrayList<BranchToBranchThread>();
        this.mClientThread = new ClientBranchThread(this);
        this.mListenerThread = new ServerBranchThread(this);
        this.branchTransactionManager = new BranchTransactionManager(this);
        this.branchStateManager = new BranchStateManager();

        this.mClientThread.start();
        branchTransactionManager.start();
        this.mListenerThread.start();
    }

    public CopyOnWriteArrayList<BranchToBranchThread> getBranchToBranchThread() {
        return branchToBranchThread;
    }

    public void setBranchToBranchThread(CopyOnWriteArrayList<BranchToBranchThread> branchToBranchThread) {
        this.branchToBranchThread = branchToBranchThread;
    }

    public synchronized int getCurrentMoney(){
        return this.mCurrentMoney;
    }

    public void setCurrentMoney(int money){
        this.mCurrentMoney += money;
    }

    public synchronized Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public CopyOnWriteArrayList<BranchInfo> getBranches() {
        return mBranches;
    }

    public Boolean isBranchContainingBranchToBranchId(Integer id) {

        for(BranchInfo branchInfo : mBranches) {

            if( id.equals(branchInfo.getBranchID())) {
                return true;
            }
        }

        return false;
    }

    public static void main(String [] args){

        if(args.length > 1) {

            Integer montantIntial=null;
            try {
                System.out.println("Veuillez entrer le montant initial de la succursale : ");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(System.in));
                montantIntial = Integer.parseInt(in.readLine());

            }catch (Exception exception) {

                System.out.print("La valeur recue n'est pas valide. Terminaison de l'application");
                System.exit(1);
            }

            Branch td = new Branch(ConnectionInfo.ConnectionEnum.IP_ADDRESS_SERVER.getNumber(),
                    Integer.parseInt(args[0]),
            Integer.parseInt(args[1]),
            montantIntial);
        }
        System.exit(0);
    }

    public BranchStateManager getBranchStateManager() {
        return branchStateManager;
    }

    @Override
    public void update(Object observable, Object arg) {

    }
}
