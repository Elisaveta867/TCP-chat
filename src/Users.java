import java.util.ArrayList;

public class Users {
    private ArrayList<ClientThread> currentUsers;
    public Users() {
        currentUsers = new ArrayList<>();
    }

    public synchronized boolean isOnline(String name) {
        for(ClientThread clientThread: currentUsers) {
            if(clientThread.getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public synchronized ClientThread get(String name) {
        for(ClientThread user : currentUsers) {
            if(user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public long size() { return currentUsers.size(); }

    public synchronized void add(ClientThread user) {
        currentUsers.add(user);
    }

    public synchronized void remove(ClientThread user) {
        currentUsers.remove(user);
    }

    public ArrayList<ClientThread> getList() {
        return currentUsers;
    }
}
