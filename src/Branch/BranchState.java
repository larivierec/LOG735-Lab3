package Branch;

/**
 * Created by Damian on 23/06/2015.
 */
public class BranchState {

    Integer branchIdStartingMark;
    Integer branchId;
    String idMark;
    Integer amount;
    BranchToBranchCanal branchToBranchCanal;

    public BranchState(Integer branchId, String idMark, Integer amount, BranchToBranchCanal branchToBranchCanal) {
        this.branchId = branchId;
        this.idMark = idMark;
        this.amount = amount;
        this.branchToBranchCanal = branchToBranchCanal;
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

    public BranchToBranchCanal getBranchToBranchCanal() {
        return branchToBranchCanal;
    }

    public void setBranchToBranchCanal(BranchToBranchCanal branchToBranchCanal) {
        this.branchToBranchCanal = branchToBranchCanal;
    }

    public Integer getBranchIdStartingMark() {
        return branchIdStartingMark;
    }

    public void setBranchIdStartingMark(Integer branchIdStartingMark) {
        this.branchIdStartingMark = branchIdStartingMark;
    }
}
