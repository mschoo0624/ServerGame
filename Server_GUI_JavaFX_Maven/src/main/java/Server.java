import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    int count = 1;
<<<<<<< HEAD
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;
=======
    final ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private static Consumer<Serializable> callback;
>>>>>>> origin/main

    public Server(Consumer<Serializable> call) {
        callback = call;
        server = new TheServer();
        server.start();
    }

    //Listening Server
    public class TheServer extends Thread {
<<<<<<< HEAD
        private int portNum; //The port the server listens from

        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(portNum)){
                callback.accept("Server started on port: " + portNum);

                while (true){
                    Socket ClientSocket = serverSocket.accept();
                    ClientThread clientThread = new ClientThread(clientSocket, count);
                    synchronized (this) { clients.add(clientThread); }
                    clientThread.start();
                    callback.accept("Client #" + count + " connected.");
                    count++;
=======
        private int portNum;//The port the server listens from

        public void run() {
            portNum = 5555;
            try(ServerSocket mysocket = new ServerSocket(5555)){
                System.out.println("Listening on port " + portNum);
                callback.accept("Server is waiting for a client!");

                while(true){
                    try {
                        ClientThread c = new ClientThread(mysocket.accept(), count);
                        callback.accept("client has connected to server: " + "client #" + count);
                        synchronized (clients) { // Synchronize addition to the list
                            clients.add(c);
                        }
                        c.start();
                        count++;
                    }
                    catch(Exception e) {
                        callback.accept("Error accepting a client connection");
                    }
>>>>>>> origin/main
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

//        // Game state variables
//        Deck deck;
//        ArrayList<Card> playerHand;
//        ArrayList<Card> dealerHand;
//        double anteBet;
//        double pairPlusBet;
//        double totalWinnings;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;

//            this.deck = new Deck(); // Initialize a new deck for this client
//            this.totalWinnings = 0;
        }

        public void updateClients(String message) {
            synchronized (clients) { // Synchronize during iteration
                for (ClientThread t : clients) {
                    try {
                        t.out.writeObject(message);
                    } catch (Exception e) {
                        callback.accept("Update client failed");
                    }
                }
            }
        }

        public void run() {
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                System.out.println("Streams not open");
            }

            //Update client
            updateClients("new client on server: client #"+count);

            while (true){
                try {
                    String data = in.readObject().toString();
                    callback.accept("client" + count + data);
                } catch (Exception e){
                    callback.accept("Client" + count + " has Shut Down");
                    synchronized (clients) {
                        clients.remove(this);
                    }
                    break;
                }
            }
        }
    }
}
