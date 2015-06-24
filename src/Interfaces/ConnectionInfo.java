package Interfaces;

public class ConnectionInfo {
    public enum ConnectionEnum {

        IP_ADDRESS_SERVER("127.0.0.1"),
        IP_ADDRESS_BRANCH("127.0.0.1"),
        LISTENING_PORT("10119"),
        CONNECTION_PORT("10119"),
        LATENCE_RECEPTION("4000"),
        AUTO_TRANSACTION_TIME("5000");

        String mNumber;

        ConnectionEnum(String number){
            mNumber = number;
        }

        public String getNumber() {
            return mNumber;
        }
    }
}
