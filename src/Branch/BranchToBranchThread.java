package Branch;

import Bank.BankActions;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BranchToBranchThread extends Thread{
    private Socket                          mSocket;
    private ObjectOutputStream              mOOS;
    private ObjectInputStream               mOIS;
    private Branch                          mBranch;
    private BranchInfo                      connInfo;

    public BranchToBranchThread(BranchInfo connInfo, Branch branch){
        try {
                mBranch = branch;

                this.mSocket = new Socket(connInfo.getIPAddress(), connInfo.getListenPort());
                branch.getBranchToBranchThread().add(this);
                this.mOOS = new ObjectOutputStream(this.mSocket.getOutputStream());
                this.start();
                this.mOIS = new ObjectInputStream(this.mSocket.getInputStream());

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

    public String[] readObjectTillWorks() {
        try {

            return (String[])mOIS.readObject();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run(){
        try {
            boolean running = true;


                while (running) {

                if (mOIS != null) {

                    String[] branchObjectSender = readObjectTillWorks();
                    if(branchObjectSender != null) {
                        if (Integer.parseInt(branchObjectSender[0]) == BankActions.UPDATE_BANK_ACCOUNT.getActionID()) {

                            int branchID = Integer.parseInt(branchObjectSender[1]);
                            String branchIP = branchObjectSender[2];
                            int branchListenPort = Integer.parseInt(branchObjectSender[3]);

                            connInfo = new BranchInfo(branchID, branchIP, branchListenPort);
                        }
                        if (Integer.parseInt(branchObjectSender[0]) ==BranchActions.TRANSACTION_BRANCH_TO_BRANCH.getActionID()) {

                            Integer amount = Integer.parseInt(branchObjectSender[1]);
                            Integer id = Integer.parseInt(branchObjectSender[2]);
                            Integer whom = Integer.parseInt(branchObjectSender[3]);
                            Thread t = new Thread(new TransferRunable(id, amount,whom));
                            t.start();
                            //System.out.println(String.format("\033[32m Le montant recu est de %s $ / Le montant disponible dans la succursale est de %s $", amount, this.mBranch.getCurrentMoney() ));
                        }
                        if (Integer.parseInt(branchObjectSender[0]) == BranchActions.SEND_MARK_TO_BRANCH.getActionID()) {

                            String markId = branchObjectSender[1];
                            Integer incomingFromBranchId = Integer.parseInt(branchObjectSender[2]);
                            Integer rootMarkBranchId = Integer.parseInt(branchObjectSender[3]);

                            mBranch.getBranchStateManager().receiveMark(markId, rootMarkBranchId, incomingFromBranchId, mBranch, false, true);
                        }
                        if (Integer.parseInt(branchObjectSender[0]) == BranchActions.SEND_MARK_ACKNOLEDGE.getActionID()) {

                            String markId = branchObjectSender[1];
                            Integer incomingFromBranchId = Integer.parseInt(branchObjectSender[2]);
                            Integer rootMarkBranchId = Integer.parseInt(branchObjectSender[3]);

                            if (!rootMarkBranchId.equals(mBranch.getBranchId())) {

                                mBranch.getBranchStateManager().receiveMark(markId, rootMarkBranchId, incomingFromBranchId, mBranch, true, true);
                            } else {

                                mBranch.getBranchStateManager().receiveMark(markId, rootMarkBranchId, incomingFromBranchId, mBranch, true, false);
                                verifyIfReadyToPrintAndPrint(markId, null);
                            }
                        }
                        if (Integer.parseInt(branchObjectSender[0]) == BranchActions.SEND_MARK_INFO_TO_ROOT_BRANCH.getActionID()) {

                            String idMark = "";
                            String idAmount = "";
                            String idSender= "";
                            String idReceipient= "";

                            if(branchObjectSender[1].toString().equals("canal")){

                                idMark = branchObjectSender[2];
                                idAmount = branchObjectSender[3];
                                idSender = branchObjectSender[4];
                                idReceipient = branchObjectSender[5];
                                System.out.println("mmm"+idAmount);
                                System.out.println("mmm"+idSender);
                                System.out.println("mmm"+idReceipient);

                                BranchToBranchCanal canal = new BranchToBranchCanal(Integer.parseInt(idReceipient),Integer.parseInt(idSender),Integer.parseInt(idAmount));

                                canal.setIdMark(idMark);
                                verifyIfReadyToPrintAndPrint(idMark, canal);
                            } else {

                                idMark = branchObjectSender[2];
                                idAmount = branchObjectSender[3];
                                idSender = branchObjectSender[4];

                                BranchState state = new BranchState(Integer.parseInt(idSender),idMark,Integer.parseInt(idAmount));

                                verifyIfReadyToPrintAndPrint(idMark, state);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void verifyIfReadyToPrintAndPrint(String idMark,Object object) throws IOException, ClassNotFoundException {

        if(object!= null) {

            if (object instanceof BranchToBranchCanal) {

                BranchToBranchCanal canal = ((BranchToBranchCanal) object);
                idMark = canal.getIdMark();

                if(!mBranch.getBranchStateManager().canalIsAlreadyInserted(canal.getBranchIdRecipient(),canal.getBranchIdAdresser(),idMark)) {

                    canal.setAmount(canal.getAmount());
                    mBranch.getBranchStateManager().getCanals().add(canal);
                }
            } else {

                BranchState branchState = (BranchState) object;
                idMark = branchState.getIdMark();
                mBranch.getBranchStateManager().getStates().add(branchState);
            }
        }


        int countStatesWithGoodId = getCountResponsesStates(idMark);
        int countCanalsWithGoodId = getCountResponsesCanals(idMark);


        int nbBranch = mBranch.getBranches().size() + 1;
        int topologyMaxConnection = ((nbBranch*(nbBranch-1))/2);

        BranchState myState = mBranch.getBranchStateManager().getMyIfStateAlreadySaved(idMark, mBranch);


        if(countStatesWithGoodId == nbBranch && (countCanalsWithGoodId == (nbBranch*(nbBranch-1)/2)) && !myState.isPrintedAlread() ) {

            myState.setPrintedAlread(true);
            int total = 0;
            for(BranchState state : mBranch.getBranchStateManager().getStates()) {

                if(state.getIdMark().equals(idMark)) {

                    System.out.println("sucursale " + state.getBranchId() + " : " + state.getAmount());
                    total+=state.getAmount();
                }
            }

            for(BranchToBranchCanal canal : mBranch.getBranchStateManager().getCanals()) {

                if(canal.getIdMark().equals(idMark)) {

                    System.out.println("canal " + canal.getBranchIdAdresser() + "-" + canal.getBranchIdRecipient() + " : " + canal.getAmount());
                    total+=canal.getAmount();
                }
            }
            System.out.println("total $ dans la banque : "+total+"$");
            System.out.println("total $ dans le systeme : "+mBranch.getBankMoney()+"$");
            System.out.print("Systeme is " + (mBranch.getBankMoney().equals(total)? " Coherent" : "Incoherent"));
        }
    }

    private int getCountResponsesCanals(String id) {

        int countCanalsWithGoodId = 0;

        for(BranchToBranchCanal canal : mBranch.getBranchStateManager().getCanals()) {

            if(canal.getIdMark().equals(id)) {
                countCanalsWithGoodId++;
                //System.out.println("-----c " + canal.getBranchIdRecipient() + "-" + canal.getBranchIdAdresser() );
            }
        }

        return countCanalsWithGoodId;
    }


    private int getCountResponsesStates(String id) {

        int countStatesWithGoodId = 0;

        for(BranchState state : mBranch.getBranchStateManager().getStates()) {

            if(state.getIdMark().equals(id)) {
                countStatesWithGoodId++;
                //System.out.println("-----c " +state.getBranchId() );
            }
        }

        return countStatesWithGoodId;
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

    public class TransferRunable implements Runnable {

        Integer money;
        Integer id;
        Integer whom;

        public TransferRunable(Integer id, Integer amount, Integer whom) {
            this.money = amount;
            this.id = id;
            this.whom= whom;
        }

        @Override
        public void run() {

            int unoId = id;


            System.out.println(String.format("\033[33m Transaction arrivant ... ", mBranch.getCurrentMoney() < 0 ? 0 : mBranch.getCurrentMoney()));
            System.out.println("good money nigga " + money);
            BranchTransaction transaction = new BranchTransaction(unoId, BranchTransaction.Direction.INCOMING, money,whom);
            System.out.println("inbound " + transaction.getAmount());
            mBranch.getBranchStateManager().addBranchTransactionForNextMark(transaction);
            getTimeout();
            mBranch.setCurrentMoney(money);


        }
    }

}
