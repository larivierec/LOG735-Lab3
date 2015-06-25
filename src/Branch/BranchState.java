package Branch;

import java.io.Serializable;

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
    private boolean printedAlready = false;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean getWaitingForAnswer() {
        return waitingForAnswerFucker;
    }

    public void setWaitingForAnswer(boolean waitingForAnswer) {
        this.waitingForAnswerFucker = waitingForAnswer;
    }

    public BranchState(Integer branchId, String idMark, Integer amount) {
        this.branchId = branchId;
        this.idMark = idMark;
        this.amount = amount;
        waitingForAnswerFucker = true;
    }

    public boolean isPrintedAlready() {
        return printedAlready;
    }

    public void setPrintedAlready(boolean printedAlready) {
        this.printedAlready = printedAlready;
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
