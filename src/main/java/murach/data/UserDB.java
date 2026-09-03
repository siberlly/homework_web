package murach.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import murach.business.User;

public final class UserDB {
    private static final List<User> USERS = new ArrayList<>();

    private UserDB() {
    }

    public static void insert(User user) {
        USERS.add(user);
    }

    public static List<User> getUsers() {
        return Collections.unmodifiableList(USERS);
    }
}
