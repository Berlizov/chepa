import java.util.ArrayList;

/**
 * Created by 350z6_000 on 08.11.2014.
 */
public class Sync {
    private final ArrayList<SocketForOneUser> socketForOneUsers = new ArrayList<>();

    public void addSocketForOneUser(SocketForOneUser socketForOneUser) {
        socketForOneUsers.add(socketForOneUser);
    }

    public void removeSocketForOneUser(SocketForOneUser socketForOneUser) {
        socketForOneUsers.remove(socketForOneUser);
    }

    public synchronized void update(SocketForOneUser u) {
        for (SocketForOneUser user : socketForOneUsers) {
            if (u!=user)
                user.update();
        }
    }
}
