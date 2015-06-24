package Branch;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Bank.BankActions;


public class ClientBranchThread extends Thread{
    private Socket  mSocket;
    private Branch  mBranch;
    private ObjectInputStream mOIS;
    private ObjectOutputStream mOOS;

    public ClientBranchThread(Branch branch){
        this.mBranch = branch;
        try{
            this.mSocket = new Socket(branch.getIpAddress(), branch.getDestinationPort());
            this.mOOS = new ObjectOutputStream(this.mSocket.getOutputStream());
            this.mOIS = new ObjectInputStream(this.mSocket.getInputStream());
        }catch(IOException e){
            System.out.println("Client Connection vers: " + branch.getDestinationPort() + " port: " + branch.getIpAddress());
        }
    }

    public ClientBranchThread(Socket socket){
        this.mSocket = socket;
    }

    @Override
    public void run(){
        boolean running = true;
        try {

            mOOS.writeObject(BankActions.UPDATE_BANK_ACCOUNT.getActionID());
            mOOS.writeObject(mBranch.getCurrentMoney());
            mOOS.writeObject(mBranch.getIpAddress());
            mOOS.writeObject(mBranch.getListeningPort());
            while (running) {
                int actionID = (Integer) mOIS.readObject();
                if(actionID == BranchActions.ADD_NEW_BRANCH_TO_BRANCH.getActionID()){

                    //number of elements
                    int numberOfBranches = (Integer) mOIS.readObject();
                    //branch infos
                    for(int i = 0; i < numberOfBranches; i++){
                        //create connection for each branch

                        int branchID = (Integer) mOIS.readObject();
                        String branchIP = (String) mOIS.readObject();
                        int branchListenPort = (Integer) mOIS.readObject();
                        int bankMoney = (Integer) mOIS.readObject();

                        mBranch.setBankMoney(bankMoney);
                        BranchInfo info = new BranchInfo(branchID,branchIP,branchListenPort);

                        if(!mBranch.getBranchId().equals(branchID) ) {

                            if(!this.mBranch.isBranchContainingBranchToBranchId(branchID)) {
                                this.mBranch.getBranches().add(info);
                            }

                            if (numberOfBranches - 1 == mBranch.getBranchId()) {

                                System.out.println("ok" + " " + info.getBranchID());

                                Thread.sleep(200);
                                BranchToBranchThread branchToBranchThread = new BranchToBranchThread(info, mBranch);

                                System.out.println(String.format("Connexion a la branche ID etablie : %s ", branchID));

                            }
                        }
                    }

                    if(numberOfBranches-1 == mBranch.getBranchId()) {

                        System.out.println("");
                        System.out.println(String.format("---------------------- Liste des succursale connecte a la succursale ID : %s ----------------------", this.mBranch.getBranchId()));
                        System.out.println("Id\tAdresseIp\tPort\t");

                        for (int i = 0; i < mBranch.getBranches().size(); i++) {

                            BranchInfo branchInfo = this.mBranch.getBranches().get(i);
                            System.out.println(String.format("%s\t%s\t%s\t", branchInfo.getBranchID(), branchInfo.getIPAddress(), branchInfo.getListenPort()));
                        }

                        System.out.println(String.format("---------------------------------------------------------------------------------------------------"));
                        System.out.println("");
                    }


                } else if (actionID == BranchActions.SET_SELF_UNIQUE_ID.getActionID()) {

                    int myBranchId = (Integer) mOIS.readObject();
                    this.mBranch.setBranchId(myBranchId);

                    System.out.println(String.format("Mon nouveau ID, provenant de la banque, est %s", myBranchId));

                } else if(actionID == 4){
                    int listenPort = (Integer) mOIS.readObject();
                    String ip   =   (String) mOIS.readObject();


                } else if(actionID == 10){
                    System.exit(0);
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
