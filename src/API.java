/**
 * Created by 350z6_000 on 17.10.2014.
 */
public enum API {
    UPDATE(0),
    LOGIN(2),
    ADD_USER(1),
    CHANGE_USER_TYPE(1),
    CHANGE_USER_PASS(1),
    GET_USERS_BY_TYPES(1),
    GET_ALL_USERS_AND_TYPES(0),
    GET_PROJECTS(0),
    ADD_PROJECTS(2),
    CHANGE_PROJECT_PRODUCT_OWNER(2),
    GET_PROJECT_PRODUCT_OWNER(1),
    GET_PROJECT_USERS(1),
    CHANGE_PROJECT_USERS(-1),
    ADD_PROJECT_TASK(1),
    GET_PROJECT_TASKS(1),
    SET_PROJECT_TASK_COMPLEXITY(1);
    final int argCount;

    private API(int argCount) {
        this.argCount = argCount;
    }

    public int getArgCount() {
        return argCount;
    }
}
