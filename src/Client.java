import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String args[]) {
        if(args.length != 2) {
            throw new RuntimeException("2 arguments required");
        }
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ReaderThread readerThread = new ReaderThread(in);
            readerThread.start();
            String message;
            while(!(message = inFromUser.readLine()).equals("@quit")) {
                out.println(message);
            }
            out.println(message);
            System.exit(0);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

    }


    private static class ReaderThread extends Thread {
        private BufferedReader in;

        public ReaderThread(BufferedReader reader) {
            in = reader;
        }

        public void run() {
            String messageFromServer;
            try {
                while ((messageFromServer = in.readLine()) != null) {
                    if (messageFromServer.equals("@fail")) {
                        System.out.println("Error! This name is already taken!");
                        continue;
                    }
                    System.out.println(messageFromServer);
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
