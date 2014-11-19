import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

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
            e.printStackTrace();
            System.err.println("Error on client connect");
            sync.removeSocketForOneUser(this);
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

    public void switcher() throws IOException, IllegalArgumentException {
        while (true) {
            try {
                Packet pock = readMassage();
                try {
                    if (!pock.checkArgCount()) {
                        writeMassage(pock);
                        throw new IllegalArgumentException("Wrong number of arguments - " + pock.func + " " +
                                pock.arguments.length + "/" + pock.func.getArgCount());
                    }
                    boolean update = false;

                    switch (pock.func) {
                        case LOGIN:
                            user.setLogin((String) pock.arguments[0]);
                            user.setPass((String) pock.arguments[1]);
                            user.setType(DBConnector.getUserType(user));
                            pock.setArguments(user.getType());
                            break;
                        case ADD_USER:
                            pock.setArguments(DBConnector.addUser((User) pock.arguments[0]));
                            update = true;
                            break;
                        case CHANGE_USER_TYPE:
                            User TempUser = (User) pock.arguments[0];
                            pock.setArguments(DBConnector.changeUserType(TempUser));
                            update = true;
                            break;
                        case CHANGE_USER_PASS:
                            user.setPass((String) pock.arguments[0]);
                            pock.setArguments(DBConnector.setNewPass(user));
                            break;
                        case GET_USERS_BY_TYPES:
                            pock.setArguments(DBConnector.getUsersByType((UsersTypes) pock.arguments[0]));
                            break;
                        case GET_ALL_USERS_AND_TYPES:
                            pock.setArguments(DBConnector.getAllUsersLoginsTypes());
                            break;
                        case GET_PROJECTS:
                            pock.setArguments(DBConnector.getProjects(user));
                            break;
                        case ADD_PROJECTS:
                            pock.setArguments(DBConnector.addProject((String) pock.arguments[0],
                                                                    (String) pock.arguments[1]));
                            update = true;
                            break;
                        case CHANGE_PROJECT_PRODUCT_OWNER:
                            pock.setArguments(DBConnector.changeProjectProductOwner((String) pock.arguments[0],
                                                                                    (String) pock.arguments[1]));
                            update = true;
                            break;
                        case GET_PROJECT_PRODUCT_OWNER:
                            pock.setArguments(DBConnector.getProjectProductOwner((String) pock.arguments[0]));
                            break;
                        case GET_PROJECT_USERS:
                            pock.setArguments(DBConnector.getProjectUsers((String) pock.arguments[0]));
                            break;
                        case GET_PROJECT_USERS_BY_TYPE:
                            pock.setArguments(DBConnector.getProjectUsersByType((String) pock.arguments[0],(UsersTypes) pock.arguments[1]));
                            break;
                        case CHANGE_PROJECT_USERS:
                            String[] s = pock.getArrayOfArgs(String[].class);
                            pock.setArguments(DBConnector.changeProjectUsers(s[0], Arrays.copyOfRange(s, 1, s.length)));
                            update = true;
                            break;
                        case ADD_PROJECT_TASK:
                            pock.setArguments(DBConnector.addTask((Task) pock.arguments[0]));
                            update = true;
                            break;
                        case GET_PROJECT_TASKS:
                            pock.setArguments(DBConnector.getProjectTasks((String) pock.arguments[0]));
                            break;
                        case SET_PROJECT_TASK_COMPLEXITY:
                            pock.setArguments(DBConnector.setTaskComplexity((Task) pock.arguments[0]));
                            break;
                    }
                    writeMassage(pock);
                    if (update)
                        sync.update(this);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    pock.setArguments(null);
                    writeMassage(pock);
                }
            } catch (JAXBException e) {
                e.printStackTrace();
            }

        }
    }
}
