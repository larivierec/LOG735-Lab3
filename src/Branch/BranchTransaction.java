package Branch;

import java.util.Random;

/**
 * Created by d.budka on 2015-06-15.
 */
public class BranchTransaction {

    public enum Direction {

        OUTGOING(1),
        INCOMING(2);

        private Integer id;

        Direction(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return id;
        }
    }

    private Integer amount;
    private Integer positionSourceDestination;
    private Integer positionToWhom;
    private Direction direction;

    public BranchTransaction(Integer positionSourceDestination,Direction direction, Integer amount, Integer positionToWhom) {

        this.amount = amount;
        this.positionSourceDestination = positionSourceDestination;
        this.direction = direction;
        this.positionToWhom= positionToWhom;
    }

    public Integer getPositionToWhom() {
        return positionToWhom;
    }

    public void setPositionToWhom(Integer positionTowhom) {
        this.positionToWhom = positionTowhom;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getPositionSourceDestination() {
        return positionSourceDestination;
    }

    public BranchTransaction.Direction getDirection() {
        return direction;
    }

    public static BranchTransaction validateAndPrepareBrancheRandomTransaction(Integer idSourceDestination,Direction direction, Branch branch) {

        Integer minimumAmount = 1;
        Integer maximumAmount = branch.getCurrentMoney();

        double x = Math.random();

        Integer delta = maximumAmount - minimumAmount;
        Integer amountRandomized = (int)(x * delta) + minimumAmount;



        return validateAndPrepareBranchTransaction(idSourceDestination, direction, amountRandomized, branch);
    }

    public static BranchTransaction validateAndPrepareBranchTransaction(Integer idSourceDestination,Direction direction, Integer amount, Branch branch) {

        if(direction.equals(Direction.OUTGOING) && branch.getCurrentMoney()-amount>0) {

            java.util.concurrent.CopyOnWriteArrayList<BranchInfo> branches = branch.getBranches();
            for (int i = 0; i < branches.size(); i++) {
                BranchInfo branchInfo = branches.get(i);

                if (idSourceDestination.equals(branchInfo.getBranchID())) {


                    return new BranchTransaction(i, direction, amount, idSourceDestination);
                }
            }

            System.out.println("Numero d'identification invalid");

        } else if(direction.equals(Direction.INCOMING)) {

            return new BranchTransaction( idSourceDestination, direction,  amount, idSourceDestination);
        }

        return null;
    }

}
