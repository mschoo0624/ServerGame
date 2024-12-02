import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
    int count = 1;
    final ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private static Consumer<Serializable> callback;

    public Server(Consumer<Serializable> call) {
        callback = call;
        server = new TheServer();
        server.start();
    }

    //Listening Server
    public class TheServer extends Thread {
        private int portNum;//The port the server listens from

        public void run() {
            try(ServerSocket mysocket = new ServerSocket(portNum);){
                System.out.println("Server is waiting for a client!");

                while(true) {
                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count);
                    clients.add(c);
                    c.start();
                    count++;
                }
            }//end of try
            catch(Exception e) {
                callback.accept("Server socket did not launch");
            }
        }

        public void setPortNum(int portNum) {
            this.portNum = portNum;
        }
        public int getPortNum() {
            return portNum;
        }
    }

    public class ClientThread extends Thread {
        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;

        // Game state variables
        Deck deck;
        ArrayList<Card> playerHand;
        ArrayList<Card> dealerHand;
        double anteBet;
        double pairPlusBet;
        double totalWinnings;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;

            this.deck = new Deck(); // Initialize a new deck for this client
            this.totalWinnings = 0;
        }

        public void run() {
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                System.out.println("Streams not open");
            }
            while (true){
                try {
                    String data = in.readObject().toString();
                    callback.accept("client: " + count + " sent: " + data);
                } catch (Exception e){
                    callback.accept("Something wrong with the socket from client: " + count + " closing down!");
                    synchronized (clients) {
                        clients.remove(this);
                    }
                    break;
                }
            }
        }
    }
}
