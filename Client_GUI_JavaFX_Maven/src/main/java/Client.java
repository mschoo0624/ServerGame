import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {
    private Socket socketClient;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int port; // for getting the port numnber.
//    private String IPAddress;

    private Consumer<Serializable> callback;

    public Client(Consumer<Serializable> callback) {
        this.callback = callback;
    }

    public void configureConnection(/*String IPAddress,*/ int port){
        this.port = port;
//        this.IPAddress = IPAddress;
    }

    public void run() {
        try {
            socketClient = new Socket("127.0.0.1", 5555);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
            callback.accept("Connected to the server!");
        } catch (Exception e) {
            callback.accept("Error connecting to server: " + e.getMessage());
            return;
        }

        while (!socketClient.isClosed()) {
            try {
                String message = in.readObject().toString();
                callback.accept(message);
            } catch (IOException | ClassNotFoundException e) {
                callback.accept("Connection lost: " + e.getMessage());
                break;
            }
        }
    }

    public void send(String data) {
        try {
            if (out != null) {
                out.writeObject(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (socketClient != null && !socketClient.isClosed()) {
                socketClient.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
