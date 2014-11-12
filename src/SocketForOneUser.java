import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.Socket;

/**
 * Created by 350z6_000 on 25.09.2014.
 */
public class SocketForOneUser extends Thread {
    private final Socket socket;
    private final User user = new User();
    private final Sync sync;
    private PrintWriter out;
    private BufferedReader in;

    public SocketForOneUser(Socket socket, Sync sync) {
        this.sync = sync;
        this.socket = socket;
        System.out.println("New Client connected");
    }

    @Override
    public void run() {
        super.run();
        try {
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            switcher();
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error on client connect");
            currentThread().interrupt();
        }
    }

    public void close() {
        try {
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Error on client socket close");
        }
    }

    private void writeMassage(Packet packet) throws JAXBException {
        out.println(packet.xmlGenerate());
        out.flush();
        System.out.println("Write-" + packet);
    }

    private Packet readMassage() throws IOException, JAXBException {
        Packet pack = Packet.xmlParse(in.readLine());
        System.out.println("Read-" + pack);
        return pack;

    }

    public void update() {
        try {
            writeMassage(new Packet(API.UPDATE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switcher() throws IOException {
        while (true) {
            try {
                Packet pock = readMassage();
                if (pock.checkArgCount()) {
                    //todo
                }
                boolean update = false;

                switch (pock.func) {
                    case LOGIN:
                        user.setLogin((String) pock.arguments[0]);
                        user.setPass((String) pock.arguments[1]);
                        user.setType(DB.getUserType(user));
                        pock.setArguments(user.getType());
                        break;
                    case ADD_USER:
                        pock.setArguments(DB.addUser((User) pock.arguments[0]));
                        update = true;
                        break;
                    case CHANGE_USER_TYPE:
                        User TempUser = (User) pock.arguments[0];
                        pock.setArguments(DB.changeUserType(TempUser));
                        update = true;
                        break;
                    case GET_USERS_BY_TYPES:
                        pock.setArgumentsArray(DB.getUsersByType((UsersTypes) pock.arguments[0]));
                        break;
                    case GET_ALL_USERS_AND_TYPES:
                        pock.setArgumentsArray(DB.getAllUsersLoginsTypes());
                        break;
                    case GET_PROJECTS:
                        pock.setArguments(DB.getProjects(user));
                        break;
                    case ADD_PROJECTS:
                        pock.setArguments(DB.addProject((String) pock.arguments[0], (String) pock.arguments[1]));
                        update = true;
                }
                writeMassage(pock);
                if (update)
                    sync.update();
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    }
}
