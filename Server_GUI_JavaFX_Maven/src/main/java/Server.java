import java.io.Serializable;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    int count = 1;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;

    public Server(Consumer<Serializable> call) {
        callback = call;
        server = new TheServer();
        server.start();
    }

    //Listening Server
    public class TheServer extends Thread {
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
                }
            }
            catch (IOException e) {
                callback.accept("Server exception: " + e.getMessage());
            }
        }

        public int getPortNum() {
            return portNum;
        }

        public void setPortNum(int portNum) {
            this.portNum = portNum;
        }
    }

    public class ClientThread extends Thread {

    }

}
