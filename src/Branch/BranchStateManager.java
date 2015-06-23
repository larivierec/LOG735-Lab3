package Branch;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Damian on 23/06/2015.
 */
public class BranchStateManager {

    private int count = 0;
    private CopyOnWriteArrayList<BranchState> states = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<BranchTransaction> transactions = new CopyOnWriteArrayList<>();


    public void addBranchTransactionForNextMark(BranchTransaction branchTransaction) {

        transactions.add(branchTransaction);
    }

    public void sendMark(ObjectOutputStream objectOutputStream, Branch branch, BranchInfo branchInfo) {

        int branchAmount = branch.getCurrentMoney();

        BranchState state = new BranchState(branch.getBranchId(), branch.getBranchId() + "N" + (count += 1), branchAmount, null);

        states.add(state);

        try {
            objectOutputStream.writeObject(BranchActions.SEND_MARK_TO_BRANCH.getActionID());
            objectOutputStream.writeObject(state.getIdMark());
            objectOutputStream.writeObject(state.branchIdStartingMark);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMark(String markId, Integer idBranchIncomingFrom,Branch branch) {

        CopyOnWriteArrayList<BranchToBranchThread> branchToBranchThread1 = branch.getBranchToBranchThread();
        for (int i = 0; i < branchToBranchThread1.size(); i++) {

            BranchInfo branchInfo = branch.getBranches().get(i);
            if(!idBranchIncomingFrom.equals(branchInfo.getBranchID())) {

                BranchToBranchThread branchToBranchThread = branchToBranchThread1.get(i);

                ObjectOutputStream objectOutputStream = branchToBranchThread.getmOOS();
                sendMark(objectOutputStream, branch, branchInfo);
            }
        }

        int total = 0;

        for (BranchTransaction branchTransaction : transactions) {

            if (branchTransaction.getDirection().equals(BranchTransaction.Direction.INCOMING)) {
                total += branchTransaction.getAmount();
            }
        }
        transactions.clear();

        //BranchToBranchCanal canal = new BranchToBranchCanal(branch.getBranchId(), branchInfo.getBranchID(), total);
    }
}
