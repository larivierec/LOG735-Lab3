package Bank;

import Branch.BranchActions;
import Branch.BranchInfo;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ServerBankThread extends Thread {
    private ServerSocket mServerSocket;
    private Bank mBank;
    private List<ObjectOutputStream> mBranchObjectStreamList;
    private Semaphore                mSemaphore = new Semaphore(0,true);

    public ServerBankThread(Bank bank){
        this.mBank = bank;
        this.mBranchObjectStreamList = new ArrayList<ObjectOutputStream>();
    }


    /**
     * La méthode accept() déclanche cette méthode
     * @param socket, le socket de la connection
     * @description Fourni une nouvelle thread client qui est ajouté lorsquil
     */
    public void onNewConnection(Socket socket){
        if(this.mBranchObjectStreamList.indexOf(socket) == -1){
            //System.out.println("Connexion accepte sur le port : " + socket.getLocalPort() + " ip: " + socket.getLocalAddress());
            try{
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                this.mBranchObjectStreamList.add(oos);
                ClientBankThread clientThread = new ClientBankThread(socket, mBank);
                clientThread.start();

            }catch(IOException e) {
                e.printStackTrace();
            }
            //after receiving the bank information send the serverlist!
            sendServerList();
        }
    }

    /**
     * Nous ajoutons un petit délai de 50 milli secondes avant d'envoyer le prochain liste au serveur.
     * Si cette délai n'est pas présent le serveur n'a pas eu de temps d'envoyer l'autre liste à un autre client
     */

    public void sendServerList(){
        for(ObjectOutputStream oos : mBranchObjectStreamList){
            try{
                this.sleep(50);
                //event id for sending branch lists
                oos.writeObject(BranchActions.ADD_NEW_BRANCH_TO_BRANCH.getActionID());
                oos.writeObject(mBank.getServerList().size());
                for(BranchInfo branchInfo : mBank.getServerList()){
                    oos.writeObject(branchInfo.getBranchID());
                    oos.writeObject(branchInfo.getIPAddress());
                    oos.writeObject(branchInfo.getListenPort());
                }
                oos.flush();
            }
            catch(IOException e){
                e.printStackTrace();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public List<ObjectOutputStream> getBranchObjectStreamList() {
        return mBranchObjectStreamList;
    }

    @Override
    public void start(){
        try {
            this.mServerSocket = new ServerSocket(mBank.getListeningPort());
            //System.out.println("Le serveur ecoute sur le port: " + mBank.getListeningPort());
            while (true) {
                onNewConnection(this.mServerSocket.accept());
            }
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }
}
