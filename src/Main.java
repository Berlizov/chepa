import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 350z6_000 on 20.09.2014.
 */

public class Main {
    static Sync sync;

    public static void main(String[] args) {
        DB.connectDB();
        DB.createDefTables();
        startServer(4567);

    }

    private static void startServer(int portNumber) {
        sync = new Sync();
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server start.");
            while (true) {
                Socket s = serverSocket.accept();
                SocketForOneUser sfou = new SocketForOneUser(s, sync);
                sync.addSocketForOneUser(sfou);
                sfou.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Server end.");
        }
    }
}
