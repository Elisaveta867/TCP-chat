import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*   Сервер представляет собой казино.
Сервер объявляет начало тура.
 После этого в течении 10 секунд пользователи могут сделать ставку на число
  (@bet number). После этого сервер разыгрывает число и объявляет пользователя победителя.
 */

public class Server {
    private static final Users users = new Users();

    public static void main(String[] args) {
        if(args.length != 1) {
            throw new RuntimeException("1 argument required");
        }
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server started");
            Casino game = new Casino(users);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clientSocket, users, game);
                clientThread.start();
                users.add(clientThread);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}