public class Server{
    private String mIpAddress;
    private int mListenPort;
    private int mDestinationPort;

    public Server(){}

    public Server(String ipAddr, int port){
        this.mIpAddress = ipAddr;
        this.mListenPort = port;
    }

    public Server(String ipAddr, int listenPort, int destPort){
        this.mIpAddress = ipAddr;
        this.mListenPort = listenPort;
        this.mDestinationPort = destPort;
    }

    public int getListeningPort(){
        return this.mListenPort;
    }

    public int getDestinationPort(){
        return this.mDestinationPort;
    }

    public String getIpAddress(){
        return this.mIpAddress;
    }
}
