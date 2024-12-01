import java.io.Serializable;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
    private int count = 1;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    static TheServer server;
    private Consumer<Serializable> callback;

    public Server(Consumer<Serializable> call) {
        callback = call;
        server = new TheServer();
        server.start();
    }

    //Listening Server
    public static class TheServer extends Thread {
        private int portNum;//The port the server listens from
        public void run() {
        }

        public int getPortNum() {
            return portNum;
        }

        public void setPortNum(int portNum) {
            this.portNum = portNum;
        }
    }

    public class ClientThread extends Thread {}

}
