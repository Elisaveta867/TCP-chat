import java.io.PrintWriter;
import java.lang.Thread;
import java.lang.Runnable;
import java.util.Vector;
import java.util.Random;

public class Casino extends Thread {
    private Users users;
    private Vector players;
    private Vector bets;
    private boolean isOpen = true;

    public Casino(Users userList) {
        users = userList;
        bets = new Vector();
        players = new Vector();
    }

    private void timer(int seconds, Runnable action) throws InterruptedException {
        while(seconds != 0) {
            action.run();
            sleep(1000);
            seconds--;
        }
    }

    public synchronized void setBet(int bet, String name) {
        for (int i = 0; i < players.size(); i++)
            if(players.get(i) == name){
                sendToAll("You can make only ONE bet");
                return;
            }
        if(this.isOpen){
            bets.add(bet);
            players.add(name);
        }
        else {
            sendToAll("Game has already begun.");
        }
    }

    private void chooseWinner() {
        Random random = new Random();
        if(bets.size() != 0) {
            int winNum = random.nextInt(bets.size());
            sendToAll("And the number is: " + bets.get(winNum));
            for(int i = 0; i < bets.size(); i++){
                if(bets.get(i) == bets.get(winNum)) {
                    //sendToAll("--" + players.get(i));
                    String name = new String("" + players.get(i));
                    sendMessage("\nYou WON!!!!", name);
                }else{
                    String name = new String("" + players.get(i));
                    sendMessage("\nSorry, you lost...", name);
                }
            }
        }
        else {
            sendToAll("No bets were made!");
        }
    }

    @Override
    public void run() {
        try {
            sendToAll("Game will start in 10 seconds.");
            timer(15, () -> {});
            this.isOpen = false;
            sendToAll("Bets are made. Await for your destiny.");
            timer(5, () -> {});
            chooseWinner();
        }
        catch(InterruptedException exception) {

        }
    }

    public void sendToAll(String message) {
        PrintWriter out;
        synchronized (users) {
            for (ClientThread clientThread : users.getList()) {
                out = clientThread.getWriter();
                out.println(message);
            }
        }
    }
    private void sendMessage(String message, String name) {
        PrintWriter out;
        synchronized (users) {
            for (ClientThread clientThread : users.getList()) {
                if (clientThread.getUsername().equals(name)) {
                    out = clientThread.getWriter();
                    out.println(message);
                    break;
                }
            }
        }
    }
}
