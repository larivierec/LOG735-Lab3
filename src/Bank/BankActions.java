package Bank;

/**
 * Created by d.budka on 2015-06-15.
 */
public enum BankActions {

    UPDATE_BANK_ACCOUNT(-222222222);

    private Integer actionID;

    BankActions(Integer actionID) {

        this.actionID =actionID ;
    }

    public Integer getActionID() {
        return actionID;
    }
}
