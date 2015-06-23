package Branch;

/**
 * Created by Damian on 23/06/2015.
 */
public class BranchToBranchCanal {

    private Integer branchIdRecipient;
    private Integer branchIdAdresser;

    private Integer amount;

    public BranchToBranchCanal(Integer branchIdRecipient, Integer branchIdAdresser, Integer amountMoney) {

        this.branchIdRecipient = branchIdRecipient;
        this.branchIdAdresser = branchIdAdresser;
        this.amount = amountMoney;
    }

    public Boolean isSameCanal(BranchToBranchCanal otherBranch){

        if((branchIdRecipient.equals(otherBranch.branchIdAdresser) && branchIdAdresser.equals(otherBranch.branchIdRecipient)) ||
                (branchIdAdresser.equals(otherBranch.branchIdAdresser) && branchIdRecipient.equals(otherBranch.branchIdRecipient))){

            return true;
        }

        return false;
    }

    public Integer getBranchIdRecipient() {
        return branchIdRecipient;
    }

    public void setBranchIdRecipient(Integer branchIdRecipient) {
        this.branchIdRecipient = branchIdRecipient;
    }

    public Integer getBranchIdAdresser() {
        return branchIdAdresser;
    }

    public void setBranchIdAdresser(Integer branchIdAdresser) {
        this.branchIdAdresser = branchIdAdresser;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
