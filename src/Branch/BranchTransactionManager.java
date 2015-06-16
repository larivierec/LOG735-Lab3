package Branch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by d.budka on 2015-06-15.
 */
public class BranchTransactionManager extends Thread {

    private Branch branch;
    private CopyOnWriteArrayList<BranchTransaction> mTransactions;

    public BranchTransactionManager(Branch branch) {
        this.branch = branch;
        mTransactions = new CopyOnWriteArrayList();
    }

    @Override
    public void start() {
        BranchTransactionManagerManual branchTransactionManagerManual = new BranchTransactionManagerManual(branch);
        branchTransactionManagerManual.start();

        BranchTransactionManagerAuto branchTransactionManagerAuto = new BranchTransactionManagerAuto(branch);
        branchTransactionManagerAuto.start();

    }

    private class BranchTransactionManagerAuto extends Thread {
        private Branch branch;

        public BranchTransactionManagerAuto(Branch branch) {
            this.branch = branch;
        }

        @Override
        public void run() {
            while (true) {
                try {

                    if(branch.getBranches() != null) {
                        for (int i = 0; i < this.branch.getBranches().size(); i++) {

                            BranchInfo branchInfo = this.branch.getBranches().get(i);

                            getRandomIntervalSleep();
                            BranchTransaction branchTransaction = BranchTransaction.validateAndPrepareBrancheRandomTransaction(branchInfo.getBranchID(), BranchTransaction.Direction.OUTGOING, branch);
                            sendTransaction(branchTransaction);
                        }
                    }

                } catch (Exception exception) {

                    System.out.println("Erreur s'est produite ");
                }
            }
        }
    }

    private class BranchTransactionManagerManual extends Thread {
        private Branch branch;

        public BranchTransactionManagerManual(Branch branch) {
            this.branch = branch;
        }

        @Override
        public void run() {
            while (true) {
                try {

                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

                    String command = in.readLine();

                    String[] splitedCommand = command.split(" ");

                    if (splitedCommand.length == 3) {

                        if (splitedCommand[0].equals("t")) {

                            Integer idProchainNoeud = Integer.parseInt(splitedCommand[1]);
                            Integer amount = Integer.parseInt(splitedCommand[2]);
                            BranchTransaction branchTransaction = BranchTransaction.validateAndPrepareBranchTransaction(idProchainNoeud, BranchTransaction.Direction.OUTGOING, amount, branch);

                            sendTransaction(branchTransaction);
                        } else
                            errorCommandMessages();
                    } else if (splitedCommand.length == 1) {

                        if (splitedCommand[0].equals("s")) {

                            System.out.println(String.format("Le montant disponible dans la succursale est de %s $", branch.getCurrentMoney()));
                        } else if (splitedCommand[0].equals("e")) {

                            branch.setCurrentMoney(-1000);
                        }
                        else
                            errorCommandMessages();
                    } else {

                        errorCommandMessages();
                    }

                } catch (Exception exception) {

                    errorCommandMessages();
                }
            }
        }
    }

    private void errorCommandMessages() {
        System.out.println("La valeur recue n'est pas valide. Voici les syntaxe d'une transaction possible :");
        System.out.println("\033[32m t [idDestinataire] [montant]  // Permet d'effectuer une transaction");
        System.out.println("\033[32m s         // permet de montrer le montant disponible ");
        System.out.println("\033[32m e         // simulation perte d'argent (perte 1000 $) ");
    }

    public void sendTransaction(BranchTransaction branchTransaction) throws IOException {
        if (branchTransaction != null) {

            branch.setCurrentMoney(-branchTransaction.getAmount());
            mTransactions.add(branchTransaction);

            this.branch.getBranchToBranchThread().get(branchTransaction.getPositionSourceDestination()).getmOOS().writeObject(BranchActions.TRANSACTION_BRANCH_TO_BRANCH.getActionID());
            this.branch.getBranchToBranchThread().get(branchTransaction.getPositionSourceDestination()).getmOOS().writeObject(branchTransaction.getAmount());

            System.out.println(String.format("\033[35m La prochaine en cours d'envoi est de %s $ / Le montant disponible dans la succursale est de %s $", branchTransaction.getAmount(), branch.getCurrentMoney() < 0 ? 0 : branch.getCurrentMoney() ));

        } else {

            System.out.println(String.format("\033[34m La succursale ou le montant disponible est invalid"));
        }
    }

    public void getRandomIntervalSleep() {


        Integer minimumTime = 5000;
        Integer maximumTime = 10000;

        double x = Math.random();

        Integer delta = maximumTime - minimumTime;
        Integer amountRandomized = (int)(x * delta) + minimumTime;

        try {
            //System.out.println(String.format("Timeout de %s", amountRandomized));
            Thread.sleep(amountRandomized);
        } catch (Exception ex) {
            System.out.println(String.format("Erreur timeout : Timeout de %s", amountRandomized));
        }
    }
}