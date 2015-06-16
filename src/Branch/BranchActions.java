package Branch;

/**
 * Created by d.budka on 2015-06-15.
 */
public enum BranchActions {

    SET_SELF_UNIQUE_ID(2),
    ADD_NEW_BRANCH_TO_BRANCH(3),
    TRANSACTION_BRANCH_TO_BRANCH(4);

    Integer actionID;

    BranchActions(Integer actionID) {

        this.actionID =actionID ;
    }

    public Integer getActionID() {
        return actionID;
    }
}
