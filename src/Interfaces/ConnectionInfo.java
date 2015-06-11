package Interfaces;

public class ConnectionInfo {
    public enum ConnectionEnum {
        IPADDRESS("127.0.0.1"),
        LISTENINGPORT("54321"),
        CONNECTIONPORT("12345");

        String mNumber;

        ConnectionEnum(String number){
            mNumber = number;
        }

        public String getNumber() {
            return mNumber;
        }
    }
}
