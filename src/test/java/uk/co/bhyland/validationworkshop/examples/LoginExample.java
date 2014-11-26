package uk.co.bhyland.validationworkshop.examples;

import uk.co.bhyland.validationworkshop.Validation;

import static uk.co.bhyland.validationworkshop.Validation.failure;
import static uk.co.bhyland.validationworkshop.Validation.success;

public class LoginExample {

    public static void main(final String... args) {

        new LoginExample().loginExample();

    }

    public void loginExample() {

        Validation<String, User> aliceLogin = login(new Credentials("alice", "20 random characters"));

        Validation<String, User> bobLogin = login(new Credentials("bob", "pet's birthday"));

        String aliceMessage = formatLoginResult(aliceLogin);
        String bobMessage = formatLoginResult(bobLogin);

        System.out.println("loginExample");
        System.out.println(aliceMessage);
        System.out.println(bobMessage);
    }

    private Validation<String, User> login(final Credentials credentials) {
        return loadUser(credentials.username).flatMap(user -> checkPassword(credentials.password, user));
    }

    private Validation<String, User> loadUser(String username) {
        if("alice".equals(username)) {
            return success(new User("alice", "20 random characters"));
        }

        if("bob".equals(username)) {
            return success(new User("bob", "new password, easily forgotten"));
        }

        return failure("user not found");
    }

    private Validation<String, User> checkPassword(String password, User user) {
        if(user.password.equals(password)) {
            return success(user);
        }
        return failure("bad password");
    }

    private String formatLoginResult(Validation<String, User> result) {
        return result.fold(
                user -> "logged in " + user.username,
                errors -> "failed to log in: " + errors.get(0)
        );
    }

    private final class Credentials {
        final String username;
        final String password;

        private Credentials(final String username, final String password) {
            this.username = username;
            this.password = password;
        }
    }

    private final class User {
        final String username;
        final String password;

        private User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
