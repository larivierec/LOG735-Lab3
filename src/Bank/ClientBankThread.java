package Bank;

import Branch.BranchInfo;

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

            int commandID = (Integer) mOIS.readObject();

            while (running) {

                /**
                 * Event IDs:
                 *
                 * 1: Update Bank Amount
                 * 3: ServerList Response
                 */


                if(commandID == 1) {
                    System.out.println("Bank Amount Updated");

                    int initialCash = (Integer) mOIS.readObject();
                    String ipAddr = (String) mOIS.readObject();
                    Integer listenPort = (Integer) mOIS.readObject();

                    BranchInfo info = new BranchInfo(mBank.getServerID(), initialCash, ipAddr, listenPort);

                    mBank.update(null, initialCash);
                    mBank.addServerToList(info);
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
