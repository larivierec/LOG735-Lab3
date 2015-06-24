package Branch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by d.budka on 2015-06-15.
 */
public class BranchTransactionManager extends Thread {

    private Branch branch;
    private Boolean continueAutomatiqueTransaction=false;

    public BranchTransactionManager(Branch branch) {
        this.branch = branch;
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
            while (continueAutomatiqueTransaction) {
                try {

                    if(branch.getBranches() != null) {
                        for (int i = 0; i < this.branch.getBranches().size(); i++) {

                            BranchInfo branchInfo = this.branch.getBranches().get(i);

                            getRandomIntervalSleep();
                            BranchTransaction branchTransaction = BranchTransaction.validateAndPrepareBrancheRandomTransaction(branchInfo.getBranchID(), BranchTransaction.Direction.OUTGOING, branch);
                            sendTransaction(branchTransaction, i);
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

                            branch.getBranchStateManager().getTransactions().add(branchTransaction);
                            sendTransaction(branchTransaction,idProchainNoeud);
                        } else
                            errorCommandMessages(command);
                    } else if (splitedCommand.length == 1) {

                        if (splitedCommand[0].equals("a")) {

                            System.out.println(String.format("Le montant disponible dans la succursale est de %s $", branch.getCurrentMoney()));
                        } else if (splitedCommand[0].equals("s")) {

                            if(continueAutomatiqueTransaction) {
                                continueAutomatiqueTransaction = false;
                            } else {
                                continueAutomatiqueTransaction = true;
                                BranchTransactionManagerAuto branchTransactionManagerAuto = new BranchTransactionManagerAuto(branch);
                                branchTransactionManagerAuto.start();
                            }
                        } else if (splitedCommand[0].equals("e")) {

                            branch.setCurrentMoney(-1000);
                        } else if (splitedCommand[0].equals("m")) {

                            CopyOnWriteArrayList<BranchInfo> branches = branch.getBranches();

                            String idMark = branch.getBranchStateManager().addNewMarkAndReturnId(branch);
                            for (int i = 0; i < branches.size(); i++) {

                                BranchInfo branchInfo = branches.get(i);
                                BranchToBranchThread branchToBranchThread =  branch.getBranchToBranchThread().get(i);

                                branch.getBranchStateManager().sendMark(branchToBranchThread.getmOOS(), this.branch, this.branch.getBranchId(),idMark,false);
                            }
                        }
                        else
                            errorCommandMessages(command);
                    } else {

                        errorCommandMessages(command);
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                    errorCommandMessages("OUPS");
                }
            }
        }
    }

    private void errorCommandMessages(String value) {
        System.out.println("you entered [" + value +"]");
        System.out.println("La valeur recue n'est pas valide. Voici les syntaxe d'une transaction possible :");
        System.out.println("\033[32m t [idDestinataire] [montant]  // Permet d'effectuer une transaction");
        System.out.println("\033[32m s         // permet de montrer le montant disponible ");
        System.out.println("\033[32m e         // simulation perte d'argent (perte 1000 $) ");
    }

    public void sendTransaction(BranchTransaction branchTransaction, int idSender) throws IOException {
        if (branchTransaction != null) {

            branch.setCurrentMoney(-branchTransaction.getAmount());


            ObjectOutputStream stream = this.branch.getBranchToBranchThread().get(branchTransaction.getPositionSourceDestination()).getmOOS();

            String[] strings = new String[]{BranchActions.TRANSACTION_BRANCH_TO_BRANCH.getActionID().toString(),branchTransaction.getAmount().toString(), String.valueOf(idSender)};

            stream.writeObject(strings);

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
