package Branch;

import java.io.ObjectOutputStream;
import java.util.concurrent.CopyOnWriteArrayList;

public class BranchStateManager {

    private int count = 0;
    private CopyOnWriteArrayList<BranchState> states = new CopyOnWriteArrayList<BranchState>();
    private CopyOnWriteArrayList<BranchToBranchCanal> canals = new CopyOnWriteArrayList<BranchToBranchCanal>();
    private CopyOnWriteArrayList<BranchTransaction> transactions = new CopyOnWriteArrayList<BranchTransaction>();


    public void addBranchTransactionForNextMark(BranchTransaction branchTransaction) {

        transactions.add(branchTransaction);
    }

    public CopyOnWriteArrayList<BranchTransaction> getTransactions() {
        return transactions;
    }

    public String addNewMarkAndReturnId(Branch branchSendingTheRequest){
        return  branchSendingTheRequest.getBranchId() + "N" + (count += 1);
    }

    public void sendMark(ObjectOutputStream objectOutputStream, Branch branchSendingTheRequest, Integer branchIdRoot, String markId, Boolean isAcknoledge) {

        int branchAmount = branchSendingTheRequest.getCurrentMoney();

        if(markId == null && !isAcknoledge) {

            markId = branchSendingTheRequest.getBranchId() + "N" + (count += 1);
        }

        BranchState state = new BranchState(branchSendingTheRequest.getBranchId(), markId, branchAmount);

        if(getStateAlreadySaved(markId) == null) {

            states.add(state);
        }

            String action;
            if(!isAcknoledge) {

                action = BranchActions.SEND_MARK_TO_BRANCH.getActionID().toString();
            } else {

                action = BranchActions.SEND_MARK_ACKNOLEDGE.getActionID().toString();
            }

            String[] strings = new String[]{action,state.getIdMark(), branchSendingTheRequest.getBranchId().toString(), branchIdRoot.toString()};

            sendObjectTillWorks(objectOutputStream, strings);
    }

    public void sendObjectTillWorks( ObjectOutputStream objectOutputStream,String[] strings) {

        try {
            if(objectOutputStream != null && strings != null ) {

                objectOutputStream.writeObject(strings);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void receiveMark(String markId, Integer rootId,Integer idBranchIncomingFrom, Branch branch, Boolean isAcknoledge, Boolean sendMessage) {

        BranchState branchState = getStateAlreadySaved(markId);
        if(branchState == null) {
            BranchState state = new BranchState(branch.getBranchId(), markId, branch.getCurrentMoney());
            state.setWaitingForAnswer(true);
            states.add(state);
            sendObjectToRoot(rootId, branch, state);
            sendNextMarkOrAcknowledge(markId, rootId, idBranchIncomingFrom, branch, sendMessage);
        } else {

            if(isAcknoledge){
                BranchToBranchCanal canal = getCalculateBranchToBranchCanal(markId, idBranchIncomingFrom, branch);
                this.canals.add(canal);
                if(sendMessage) 
                    sendObjectToRoot(rootId, branch, canal);
            }
        }

        if(!isAcknoledge && ( branchState == null || ( branchState.getWaitingForAnswer())) && !canalIsAlreadyInserted(idBranchIncomingFrom,branch.getBranchId(),markId)) {

            CopyOnWriteArrayList<BranchToBranchThread> branchToBranchThread1 = branch.getBranchToBranchThread();
            for (int i = 0; i < branch.getBranches().size(); i++) {

                BranchInfo branchInfo = branch.getBranches().get(i);
                BranchToBranchThread branchToBranchThread = branchToBranchThread1.get(i);
                ObjectOutputStream objectOutputStream = branchToBranchThread.getmOOS();

                if(idBranchIncomingFrom.equals(branchInfo.getBranchID()) ) {

                    sendMark(objectOutputStream, branch, rootId, markId, true);
                    if(branchState != null) {
                        branchState.setWaitingForAnswer(false);
                    }
                    continue;
                }
            }
        }

    }

    public BranchToBranchCanal getCalculateBranchToBranchCanal(String markId, Integer idBranchIncomingFrom, Branch branch) {
        int total = 0;

        for (BranchTransaction branchTransaction : transactions) {

            if (branchTransaction.getDirection().equals(BranchTransaction.Direction.INCOMING) && idBranchIncomingFrom.equals(branchTransaction.getPositionToWhom())) {
                total += branchTransaction.getAmount();
            }
            if(branchTransaction.getDirection().equals(BranchTransaction.Direction.INCOMING) ) 
                transactions.remove(branchTransaction);

            if (branchTransaction.getDirection().equals(BranchTransaction.Direction.OUTGOING) && idBranchIncomingFrom.equals(branchTransaction.getPositionToWhom())) {
                total += branchTransaction.getAmount();
            }
            if (branchTransaction.getDirection().equals(BranchTransaction.Direction.OUTGOING)) transactions.remove(branchTransaction);
        }

        BranchToBranchCanal canal = new BranchToBranchCanal(branch.getBranchId(), idBranchIncomingFrom, total);
        canal.setIdMark(markId);
        return canal;
    }

    private void sendNextMarkOrAcknowledge(String markId, Integer rootId, Integer idBranchIncomingFrom, Branch branch, Boolean sendMessage) {
        CopyOnWriteArrayList<BranchToBranchThread> branchToBranchThread1 = branch.getBranchToBranchThread();
        for (int i = 0; i < branch.getBranches().size(); i++) {

            BranchInfo branchInfo = branch.getBranches().get(i);
            BranchToBranchThread branchToBranchThread = branchToBranchThread1.get(i);
            ObjectOutputStream objectOutputStream = branchToBranchThread.getmOOS();

            if(!idBranchIncomingFrom.equals(branchInfo.getBranchID())) {

                if(sendMessage)
                    sendMark(objectOutputStream, branch, rootId,markId,false);
            }
        }
    }

    private void sendObjectToRoot(Integer rootId, Branch branch, Object object) {
        ObjectOutputStream objectOutputStream = getRootOutputStream(branch, rootId);

            BranchObjectSender branchObjectSender = new BranchObjectSender();
            branchObjectSender.setIdAction(BranchActions.SEND_MARK_INFO_TO_ROOT_BRANCH.getActionID());

            String type;
            String[] strings;
            if(object instanceof BranchToBranchCanal) {
                BranchToBranchCanal canal = (BranchToBranchCanal)object;
                type="canal";
                strings = new String[]{BranchActions.SEND_MARK_INFO_TO_ROOT_BRANCH.getActionID().toString(),type,canal.getIdMark(),canal.getAmount().toString(),canal.getBranchIdAdresser().toString(),canal.getBranchIdRecipient().toString()};
            } else {
                BranchState state = (BranchState)object;
                type="mark";
                strings = new String[]{BranchActions.SEND_MARK_INFO_TO_ROOT_BRANCH.getActionID().toString(),type,state.getIdMark(),state.getAmount().toString(),state.getBranchId().toString()};
            }

            sendObjectTillWorks(  objectOutputStream, strings);
    }

    public BranchState getStateAlreadySaved(String markId){

        for(BranchState state : states){
            if(state.getIdMark().equals(markId)) {

                return state;
            }
        }

        return null;
    }

    public BranchState getMyIfStateAlreadySaved(String markId, Branch branch){

        for(BranchState state : states){
            if(state.getIdMark().equals(markId) && branch.getBranchId().equals(state.getBranchId())) {

                return state;
            }
        }

        return null;
    }


    private ObjectOutputStream getRootOutputStream(Branch branch, Integer rootId){

        CopyOnWriteArrayList<BranchInfo> branches = branch.getBranches();

        for (int i = 0; i < branches.size(); i++) {

            BranchInfo branch1 = branches.get(i);

            if (branch1.getBranchID() == rootId) {

                return branch.getBranchToBranchThread().get(i).getmOOS();
            }
        }

        return null;
    }

    public Boolean canalIsAlreadyInserted(Integer canalId1, Integer canalId2, String markId ) {

        for(BranchToBranchCanal canal : this.canals){

            BranchToBranchCanal canal1 = new BranchToBranchCanal(canalId2,canalId1,0);
            if(canal.getIdMark().equals(markId) && canal.isSameCanal(canal1)) {
                return true;
            }
        }

        return false;
    }


    public CopyOnWriteArrayList<BranchToBranchCanal> getCanals() {
        return canals;
    }

    public CopyOnWriteArrayList<BranchState> getStates() {
        return states;
    }
}
