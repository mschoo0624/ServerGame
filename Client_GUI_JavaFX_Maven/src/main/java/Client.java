import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Client extends Thread{


    Socket socketClient;

    ObjectOutputStream out;
    ObjectInputStream in;

    private Consumer<Serializable> callback;
    private PokerInfo data;

    Client(Consumer<Serializable> call){

        callback = call;
    }

    public void run() {

        try {
            socketClient= new Socket("127.0.0.1",5555);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception e) {}

        while(true) {
            try {
                data = (PokerInfo) in.readObject();
//                setPokerInfo(data);
                System.out.println("Received data from server: " + data.getGameState());
                if (data.getPlayerHand() != null) {
                    System.out.println("Number of cards received: " + data.getPlayerHand().size());
                }

                callback.accept(data);
            } catch(Exception e) {
                callback.accept("Server disconnected");
                e.printStackTrace();
                break;
            }
        }
    }

    public void send(PokerInfo data) {

        try {
            out.writeObject(data);
            out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setPokerInfo(PokerInfo data) {
        this.data = data;
    }
    public PokerInfo getPokerInfo() {
        return this.data;
    }


}
