/**
 * Created by 350z6_000 on 06.11.2014.
 */
public class User {
    private String login = "";
    private String pass = "";
    private UsersTypes type = UsersTypes.NO;

    public User() {
    }

    public User(String login, String pass, UsersTypes type) {
        this.type = type;
        this.login = login;
        this.pass = pass;
    }

    public UsersTypes getType() {
        return type;
    }

    public void setType(UsersTypes type) {
        this.type = type;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public static Boolean checkPass(String pass)
    {
        return (pass.length()>=5);
    }
    public static Boolean checkLogin(String login)
    {
        return (login.length()>=5);
    }
    public static Boolean checkLoginParameters(String login, String password) {
        return User.checkLogin(login)&&User.checkPass(password);
    }
}
