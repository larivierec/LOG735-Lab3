package Branch;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Damian on 23/06/2015.
 */
public class BranchState implements Serializable{

    static final long serialVersionUID = 345335634534509234L;

    private Integer branchIdStartingMark;
    private Integer branchId;
    private String idMark;
    private Integer amount;
    private boolean waitingForAnswerFucker;
    private boolean printedAlread = false;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean getWaitingForAnswerFucker() {
        return waitingForAnswerFucker;
    }

    public void setWaitingForAnswerFucker(boolean waitingForAnswerFucker) {
        this.waitingForAnswerFucker = waitingForAnswerFucker;
    }

    public BranchState(Integer branchId, String idMark, Integer amount) {
        this.branchId = branchId;
        this.idMark = idMark;
        this.amount = amount;
        waitingForAnswerFucker = true;
    }

    public boolean isPrintedAlread() {
        return printedAlread;
    }

    public void setPrintedAlread(boolean printedAlread) {
        this.printedAlread = printedAlread;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getIdMark() {
        return idMark;
    }

    public void setIdMark(String idMark) {
        this.idMark = idMark;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getBranchIdStartingMark() {
        return branchIdStartingMark;
    }

    public void setBranchIdStartingMark(Integer branchIdStartingMark) {
        this.branchIdStartingMark = branchIdStartingMark;
    }
}
