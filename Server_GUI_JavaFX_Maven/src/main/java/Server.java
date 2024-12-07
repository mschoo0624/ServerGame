import javafx.application.Platform;

import java.io.IOException;
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
        Dealer theDealer;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
            this.theDealer = new Dealer();

//            this.deck = new Deck(); // Initialize a new deck for this client
//            this.totalWinnings = 0;
        }

//        public void updateClients(String message) {
//            synchronized (clients) { // Synchronize during iteration
//                for (ClientThread t : clients) {
//                    try {
//                        t.out.writeObject(message);
//                    } catch (Exception e) {
//                        callback.accept("Update client failed");
//                    }
//                }
//            }
//        }

        public void run() {
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                System.out.println("Streams not open");
            }

            //Update client
//            updateClients("new client on server: client #"+count);

            while(true) {
                try {
                    PokerInfo data = (PokerInfo) in.readObject();
                    handleGameLogic(data);

                } catch(Exception e) {
                    callback.accept("Client #" + count + " has disconnected");
                    synchronized (clients) {
                        clients.remove(this);
                    }
                    break;
                }
            }
        }

        public void handleGameLogic(PokerInfo info) throws IOException {
            switch(info.getGameState()) {
                case "NEW_GAME":
                    handleNewGame(info);
                    break;
                case "BETTING":
                    handleBetting(info);
                    break;
                case "DEALT":
                    handleDealt(info);
                    break;
                case "FOLDED":
                    handleFolded(info);
                    break;
            }
        }

        public void handleNewGame(PokerInfo info) throws IOException {

            ArrayList<Card> playerCards = theDealer.dealHand();
            ArrayList<Card> dealerCards = theDealer.dealHand();
            info.setPlayerHand(playerCards);
            info.setDealerHand(dealerCards);

            info.setGameState("WAITING_FOR_BET");
            sendPokerInfo(info);
            callback.accept("Client #" + count + " Started a game");

        }

        private void handleBetting(PokerInfo info) throws IOException {
            callback.accept("Client #" + count + " placed bets - Ante: $" +
                    info.getAnteBet() + ", PairPlus: $" + info.getPairPlusBet());
        }

        private void handleDealt(PokerInfo info) throws IOException {
            Platform.runLater(()->{
                for(Card c : info.getDealerHand()) {
                    System.out.println(c.toString());
                }
                if (ThreeCardLogic.dealerQualifies(info.getDealerHand())) {
                int result = ThreeCardLogic.compareHands(info.getDealerHand(), info.getPlayerHand());

                if (result == 2) {  // Player wins
                    handlePlayerWin(info);
                } else if (result == 1) {  // Dealer wins
                    handleDealerWin(info);
                } else {// Tie
                    info.setMessage("Tie");
                    sendPokerInfo(info);
                    callback.accept("Tie game for Client #" + count);
                }
            } else {
                handleDealerNotQualified(info);
            }
            });
            callback.accept("Client #" + count + " Deals");

        }

        private void handlePlayerWin(PokerInfo info) {
            System.out.println("Player wins the game");
            // Calculate ante and play bet winnings
            int anteBet = info.getAnteBet();
            int playBet = info.getPlayBet();
            info.setAnteBet(anteBet);
            info.setPlayBet(playBet);
            info.setTotalWinnings(anteBet + playBet);
            if(info.getPairPlusBet() != 0){
                int newPP = ThreeCardLogic.evalPPWinnings(info.getPlayerHand(), info.getPairPlusBet());
                info.setTotalWinnings(newPP);
                info.setPairPlusBet(info.getPairPlusBet() * 2);
                callback.accept("Client #" + count + ":Player wins: $" + info.getTotalWinnings());
            }
            info.setMessage("Player Wins");
            sendPokerInfo(info);
        }

        private void handleDealerWin(PokerInfo info) {
            System.out.println("Dealer wins the game");
            info.setMessage("Dealer wins");
            sendPokerInfo(info);
            callback.accept("Client #" + count + ": Dealer wins, You Lose");
        }

        private void handleDealerNotQualified(PokerInfo info) {//FIXME
            System.out.println("Dealer not qualified");
            info.setMessage("Dealer not qualified");
            ArrayList<Card> playerCards = theDealer.dealHand();
            ArrayList<Card> dealerCards = theDealer.dealHand();
            info.setPlayerHand(playerCards);
            info.setDealerHand(dealerCards);

            info.setGameState("WAITING_FOR_BET");
            sendPokerInfo(info);

            callback.accept("Client #" + count + ": Dealer not qualified");
        }

        private void handleFolded(PokerInfo info) throws IOException {
            System.out.println("Folds");
            info.setMessage("Folds");
            callback.accept("Client #" + count + " Folds and Loses all Wager");
        }

        // For sending game information to server
        public void sendPokerInfo(PokerInfo info) {
            try {
                out.writeObject(info);
                out.flush();
                out.reset();
            } catch (IOException e) {
                callback.accept("Failed to send game information");
            }
        }

    }
}
