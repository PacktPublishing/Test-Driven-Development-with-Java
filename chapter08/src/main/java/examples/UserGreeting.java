package examples;

public class UserGreeting {
    private final UserProfiles profiles ;

    public UserGreeting(UserProfiles profiles) {
        this.profiles = profiles;
    }

    public String formatGreeting(UserId id) {
        return String.format("Hello and Welcome, %s",
                profiles.fetchNicknameFor(id));
    }
}
