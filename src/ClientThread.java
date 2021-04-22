import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private PrintWriter out;
    private String userName = "User";
    private BufferedReader in;
    private final Users users;
    private Casino game;

    public ClientThread(Socket client, Users userList, Casino casino) throws IOException {
        users = userList;
        socket = client;
        game = casino;
        System.out.println("client connected to port:" + client.getPort());
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        userName += Integer.toString(client.getPort());
    }

    public void run() {
        try {
            String message;
            while(true) {
                message = in.readLine();
                if(message.equals("")) {
                    continue;
                }
                if(message.charAt(0) == '@') {
                    int spaceInd = message.indexOf(' ');
                    String command;
                    if(spaceInd == -1) {
                        command = message;
                    }
                    else {
                        command = message.substring(0, spaceInd);
                    }
                    if (command.equals("@name")) {
                        String name = message.substring(spaceInd + 1);
                        if (users.isOnline(name)) {
                            out.println("@fail");
                        }
                        else {
                            sendMessage(userName + " has changed their name to " + name);
                            userName = name;
                        }
                    }
                    if (command.equals("@senduser")) {
                        String recipient = "";
                        int secondSpaceInd = message.indexOf(' ', spaceInd + 1);
                        recipient = message.substring(spaceInd + 1, secondSpaceInd);
                        message = message.substring(secondSpaceInd + 1, message.length());
                        sendMessage(userName + ": " + message, recipient);
                    }
                    if(command.equals("@quit")) {
                        sendMessage("User " + userName + " left chat.");
                        users.remove(this);
                        socket.close();
                        synchronized (users) {
                            users.remove(this);
                        }
                        return;
                    }
                    if (command.equals("@startCasino")) {
                        sendToAll("Please enter: @bet 'number of bet'");
                        game.start();
                    }
                    if (command.equals("@bet")) {
                        String bet = message.substring(spaceInd + 1);
                        int betNum = Integer.parseInt(bet);
                        game.setBet(betNum,userName);
                    }
                }
                else {
                    sendMessage(userName + ": " + message);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        PrintWriter out;
        synchronized (users) {
            for (ClientThread clientThread : users.getList()) {
                if (clientThread == this) {
                    continue;
                }
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
    public void sendToAll(String message) {
        PrintWriter out;
        synchronized (users) {
            for (ClientThread clientThread : users.getList()) {
                out = clientThread.getWriter();
                out.println(message);
            }
        }
    }

    public PrintWriter getWriter() {
        return out;
    }

    public String getUsername() {
        return userName;
    }

}
