package Bank;

import Branch.BranchActions;
import Branch.BranchInfo;
import com.sun.deploy.util.StringUtils;

import java.io.*;
import java.net.Socket;

public class ClientBankThread extends Thread{
    private Socket              mSocket;
    private Bank                mBank;
    private ObjectInputStream   mOIS;

    public ClientBankThread(Socket socket, Bank bank){
        this.mSocket = socket;
        this.mBank = bank;
        try {
            mOIS = new ObjectInputStream(this.mSocket.getInputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        boolean running = true;
        try {


            while (running) {

                int commandID = (Integer) mOIS.readObject();
                /**
                 * Event IDs:
                 *
                 * 1: Update Bank Amount
                 * 3: ServerList Response
                 */


                if(commandID == BankActions.UPDATE_BANK_ACCOUNT.getActionID()) {
                    System.out.println("Bank Amount Updated");

                    int initialCash = (Integer) mOIS.readObject();
                    String ipAddr = (String) mOIS.readObject();
                    Integer listenPort = (Integer) mOIS.readObject();

                    Integer serverId = mBank.getServerID();
                    BranchInfo info = new BranchInfo(serverId, initialCash, "127.0.0.1", listenPort);

                    System.out.println(String.format("La succursale ( ID : %s ) avec l'adresse %s et port %s vient d'ajouter %s au reseau",serverId, ipAddr, listenPort, initialCash));
                    mBank.update(null, initialCash);
                    mBank.addServerToList(info);

                    mBank.getmServerBankThread().getBranchObjectStreamList().get(serverId).writeObject(BranchActions.SET_SELF_UNIQUE_ID.getActionID());
                    mBank.getmServerBankThread().getBranchObjectStreamList().get(serverId).writeObject(serverId);
                }
                else{
                    //Command not known
                }

                commandID = 0;
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    Object parseCommand(Object obj){
        return null;
    }
}
