import java.util.ArrayList;

public class Players {
    private ArrayList<ClientThread> currentUsers;
    public Players() {
        currentUsers = new ArrayList<>();
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
