/**
 * Created by 350z6_000 on 20.09.2014.
 */

public enum UsersTypes {
    NO(0), ADMIN(1), PRODUCT_OWNER(2), STAKEHOLDERS(4), DEVELOPER(8);
    final int id;

    private UsersTypes(int i) {
        this.id = i;
    }

    public static UsersTypes valueOf(int id) {
        for (UsersTypes u : UsersTypes.values()) {
            if (u.id == id) return u;
        }
        throw new IllegalArgumentException("User type not found.");
    }

    public static String[] getUserTypes() {
        String[] types = new String[values().length];

        for (int i = 0; i < types.length; i++) {
            types[i] = values()[i].getIntString();
        }
        return types;
    }

    public String getIntString() {
        return Integer.toString(id);
    }
}

