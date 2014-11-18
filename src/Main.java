import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 350z6_000 on 20.09.2014.
 */
public class Main {
    private static Sync sync;

    public Main() {
        DBConnector.connectDB();
        DBConnector.createDefTables();
    }

    public static void main(String[] args) {
        Main m = new Main();
        int port = 4567;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
                System.err.println("The first argument must be a number.");
                return;
            }
        }
        m.startServer(port);
    }

    private void startServer(int portNumber) {
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
        } catch (BindException e) {
            System.err.println("Cant start the server. The port #" + portNumber + " is busy.");
        } catch (IllegalArgumentException e) {
            System.err.println("Port value out of range.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Server end.");
        }
    }
}
