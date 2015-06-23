package Branch;

import Bank.BankActions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class BranchToBranchThread extends Thread{
    private Socket                          mSocket;
    private ObjectOutputStream              mOOS;
    private ObjectInputStream               mOIS;
    private Branch                          mBranch;
    private BranchInfo                      connInfo;

    public BranchToBranchThread(BranchInfo connInfo, Branch branch){
        try {
            // System.out.println("Tentative de connexion à : " + connInfo.getIPAddress() + " sur le port : " + connInfo.getListenPort());

            boolean shouldAdd = true;

            for (BranchToBranchThread b : branch.getBranchToBranchThread()) {
                if(b.mSocket.getLocalPort() == connInfo.getListenPort()) {

                    shouldAdd = false;
                }
            }

            if(shouldAdd) {
                mBranch = branch;

                this.mSocket = new Socket(connInfo.getIPAddress(), connInfo.getListenPort());
                branch.getBranchToBranchThread().add(this);
                this.mOOS = new ObjectOutputStream(this.mSocket.getOutputStream());
                this.start();
                this.mOIS = new ObjectInputStream(this.mSocket.getInputStream());

            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public BranchToBranchThread(Socket socket, Branch branch){
        try {
            branch.getBranchToBranchThread().add(this);
            mBranch = branch;
            // System.out.println("Tentative de connexion à : " + connInfo.getIPAddress() + " sur le port : " + connInfo.getListenPort());
            this.mSocket = socket;

            this.mOOS = new ObjectOutputStream(this.mSocket.getOutputStream());
            this.start();
            this.mOIS = new ObjectInputStream(this.mSocket.getInputStream());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getmOOS() {
        return mOOS;
    }

    @Override
    public void run(){
        try {
            boolean running = true;


            /*this.mOOS.writeObject(4);
            this.mOOS.writeObject(mBranch.getListeningPort());
            this.mOOS.writeObject(mBranch.getIpAddress());
            */while (running) {

                if (mOIS != null) {
                    Integer actionID = (Integer) mOIS.readObject();

                    if (actionID.equals(BankActions.UPDATE_BANK_ACCOUNT.getActionID())) {

                        int branchID = (Integer) mOIS.readObject();
                        String branchIP = (String) mOIS.readObject();
                        int branchListenPort = (Integer) mOIS.readObject();

                        connInfo = new BranchInfo(branchID, branchIP, branchListenPort);
                    }
                    if (actionID.equals(BranchActions.TRANSACTION_BRANCH_TO_BRANCH.getActionID())) {

                        System.out.println(String.format("\033[33m Transaction arrivant ... ", mBranch.getCurrentMoney() < 0 ? 0 : mBranch.getCurrentMoney()));

                        Integer amount = (Integer) mOIS.readObject();
                        Integer id = (Integer) mOIS.readObject();
                        this.mBranch.setCurrentMoney(amount);

                        BranchTransaction transaction = new BranchTransaction(id, BranchTransaction.Direction.INCOMING, amount);
                        mBranch.getBranchStateManager().addBranchTransactionForNextMark(transaction);

                        System.out.println(String.format("\033[32m Le montant recu est de %s $ / Le montant disponible dans la succursale est de %s $", amount, this.mBranch.getCurrentMoney() ));

                        getTimeout();
                    }
                    if (actionID.equals(BranchActions.SEND_MARK_TO_BRANCH.getActionID())) {

                        String markId = (String) mOIS.readObject();
                        Integer rootMarkBranchId = (Integer) mOIS.readObject();

                        System.out.println("Received mark from ");
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void getTimeout() {

        Integer minimumTime = 5000;
        Integer maximumTime = 10000;

        double x = Math.random();

        Integer delta = maximumTime - minimumTime;
        Integer amountRandomized = (int)(x * delta) + minimumTime;

        try {
            //System.out.println(String.format("Timeout de %s", amountRandomized));
            Thread.sleep(amountRandomized);
        } catch (Exception ex) {
            System.out.println(String.format("Erreur timeout : Timeout de %s", amountRandomized));
        }
    }
}
