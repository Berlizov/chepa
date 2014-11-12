import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by 350z6_000 on 20.09.2014.
 */
public class DB {
    private static Connection db;
    private static Statement stmt;

    public static void connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            db = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Opened database successfully");
            stmt = db.createStatement();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    public static void createDefTables() {
        try {
            String sql = "CREATE TABLE USERS " +
                    "(LOGIN     CHAR(50)    PRIMARY KEY NOT NULL UNIQUE, " +
                    " PASS      CHAR(50)    NOT NULL, " +
                    " TYPE      INT         NOT NULL," +
                    " DT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    " );\n";
            sql += "CREATE TABLE PROJECT  " +
                    "(NAME     CHAR(50) PRIMARY KEY  NOT NULL, " +
                    " PRODUCT_OWNER     CHAR(50), " +
                    " DT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    " );\n";
            sql += "CREATE TABLE PROJECT_USER  " +
                    "(ID        INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " PROJECT_ID     INTEGER    NOT NULL, " +
                    " USER_ID     INTEGER     NOT NULL, " +
                    " DT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    " );\n";
            sql += "CREATE TABLE TASK " +
                    "(ID        INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " NAME     CHAR(50)    NOT NULL, " +
                    " DESCRIPTION     TEXT, " +
                    " COMPLEXITY     INTEGER, " +
                    " DT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    " );\n";
            sql += "CREATE TABLE TASK_DEVELOPER " +
                    "(ID        INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " TASK_ID     INTEGER    NOT NULL, " +
                    " DEVELOPER_ID     INTEGER    NOT NULL, " +
                    " DT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    " );\n";
            sql += "CREATE TABLE TASK_DEVELOPER_COMPLEXITY " +
                    "(ID        INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " TASK_ID     INTEGER    NOT NULL, " +
                    " DEVELOPER_ID     INTEGER    NOT NULL, " +
                    " COMPLEXITY    INTEGER    NOT NULL, " +
                    " DT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    " );\n";
            sql += "CREATE TABLE TASK_STAKEHOLDER_RATING" +
                    "(ID        INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " TASK_ID     INTEGER    NOT NULL, " +
                    " STAKEHOLDER_ID     INTEGER    NOT NULL, " +
                    " RATING    INTEGER    NOT NULL, " +
                    " DT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    " );\n";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("FIRST START");
            addUser(new User("admin", "admin", UsersTypes.ADMIN));
        } catch (Exception e) {
            if (!e.getMessage().equals("table USERS already exists")) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    }
    /*Users*/

    public static boolean addUser(User u) {
        try {
            String sql = "INSERT INTO USERS (LOGIN, PASS, TYPE) " +
                    "VALUES ( '" + u.getLogin() + "', '" + u.getPass() + "', " + u.getType().id + " );";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public static UsersTypes getUserType(User u) {
        try {
            String sql = "SELECT * FROM USERS " +
                    "WHERE LOGIN LIKE '" + u.getLogin() + "' " +
                    "AND PASS LIKE '" + u.getPass() + "' ";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return UsersTypes.valueOf(rs.getInt("TYPE"));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return UsersTypes.NO;
    }

    public static boolean setNewPass(User u) {
        try {
            String sql = "UPDATE USERS SET PASS = '" + u.getPass() + "', " +
                    " DT = CURRENT_TIMESTAMP " +
                    "WHERE LOGIN LIKE '" + u.getLogin() + "' " +
                    "AND PASS LIKE '" + u.getPass() + "' ";
            System.out.println(sql);
            stmt.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public static User[] getAllUsersLoginsTypes() {
        try {
            String sql = "SELECT * FROM USERS";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<User> str = new ArrayList<>();
            while (rs.next()) {
                str.add(new User(rs.getString("LOGIN"),
                        "",
                        UsersTypes.valueOf(rs.getInt("TYPE"))));

            }
            return str.toArray(new User[str.size()]);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    public static User[] getUsersByType(UsersTypes newType) {
        ArrayList<User> str = new ArrayList<>();
        try {
            String sql = "SELECT * FROM USERS " +
                    "WHERE TYPE LIKE '" + newType.getIntString() + "'";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                str.add(new User(rs.getString("LOGIN"),
                        "",
                        newType));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return str.toArray(new User[str.size()]);
    }

    public static boolean changeUserType(User user) {
        try {
            String sql = "UPDATE USERS SET TYPE = '" + user.getType().getIntString() + "', " +
                    " DT = CURRENT_TIMESTAMP " +
                    "WHERE LOGIN LIKE '" + user.getLogin() + "' ";
            System.out.println(sql);
            stmt.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    /*Project*/
    public static boolean addProject(String name, String userLogin) {
        try {

            String sql = "SELECT COUNT(*) FROM USERS " +
                    "WHERE LOGIN LIKE '" + userLogin + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt("COUNT(*)") <= 0)
                    userLogin = "";
            }
            sql = "INSERT INTO PROJECT (NAME, PRODUCT_OWNER) " +
                    "VALUES ( '" + name + "', '" + userLogin + "')";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public static String[] getProjects(User user) {
        try {
            String sql;
            switch (user.getType()) {
                case ADMIN:
                    sql = "SELECT * FROM PROJECT ";
                    break;
                case PRODUCT_OWNER:
                    sql = "SELECT * FROM PROJECT WHERE PRODUCT_OWNER LIKE '" + user.getLogin() + "'";
                    break;
                default:
                    System.err.println("Unknown user type!");
                    return new String[0];
            }
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<String> str = new ArrayList<>();
            while (rs.next()) {
                str.add(rs.getString("NAME"));
            }
            return str.toArray(new String[str.size()]);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

   /* public static boolean addTask(String name, String description, Integer projectID) {
        try {
            String sql = "INSERT INTO TASK (PROJECT_ID, NAME,DESCRIPTION ) " +
                    "VALUES ( '" + projectID + "', '"+" '" + name + "', '" + description + "')";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }*/
}

