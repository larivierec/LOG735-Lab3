package Branch;

import java.io.Serializable;

public class BranchInfo implements Serializable{

    private int mBranchID;
    private int mInitialMoney;
    private String mIPAddress;
    private int mListenPort;

    public BranchInfo(int id, int initialMoney, String ipAddr, int listenPort){
        this.mBranchID = id;
        this.mInitialMoney = initialMoney;
        this.mIPAddress = ipAddr;
        this.mListenPort = listenPort;
    }

    public int getBranchID() {
        return mBranchID;
    }

    public void setBranchID(int mBranchID) {
        this.mBranchID = mBranchID;
    }

    public int getInitialMoney() {
        return mInitialMoney;
    }

    public void setInitialMoney(int mInitialMoney) {
        this.mInitialMoney = mInitialMoney;
    }

    public String getIPAddress() {
        return mIPAddress;
    }

    public void setIPAddress(String mIPAddress) {
        this.mIPAddress = mIPAddress;
    }

    public int getListenPort() {
        return mListenPort;
    }

    public void setListenPort(int mListenPort) {
        this.mListenPort = mListenPort;
    }
}
