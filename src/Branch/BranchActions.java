package Branch;

/**
 * Created by d.budka on 2015-06-15.
 */
public enum BranchActions {

    SET_SELF_UNIQUE_ID(-22222222),
    ADD_NEW_BRANCH_TO_BRANCH(-33333333),
    TRANSACTION_BRANCH_TO_BRANCH(-44444444),
    SEND_MARK_TO_BRANCH(-77777777),
    SEND_MARK_INFO_TO_ROOT_BRANCH(-88888888),
    SEND_MARK_ACKNOLEDGE(-99999999);

    Integer actionID;

    BranchActions(Integer actionID) {

        this.actionID =actionID ;
    }

    public Integer getActionID() {
        return actionID;
    }
}
