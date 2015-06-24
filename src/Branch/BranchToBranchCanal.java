package Branch;

import java.io.Serializable;

/**
 * Created by Damian on 23/06/2015.
 */
public class BranchToBranchCanal implements Serializable {

    private Integer branchIdRecipient;
    private Integer branchIdAdresser;

    private String idMark;

    private Integer amount;

    static final long serialVersionUID = 345345345345123634L;

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

    public String getIdMark() {
        return idMark;
    }

    public void setIdMark(String idMark) {
        this.idMark = idMark;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
