import java.util.ArrayList;

/**
 * Created by 350z6_000 on 08.11.2014.
 */
public class Sync {
    final ArrayList<SocketForOneUser> socketForOneUsers = new ArrayList<>();

    public void addSocketForOneUser(SocketForOneUser socketForOneUser) {
        socketForOneUsers.add(socketForOneUser);
    }

    public synchronized void update() {
        for (SocketForOneUser user : socketForOneUsers) {
            user.update();
        }
    }
}
