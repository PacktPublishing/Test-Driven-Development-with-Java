package examples;

public class UserNotifications {
    private final MailServer mailServer;

    public UserNotifications(MailServer mailServer) {
        this.mailServer = mailServer;
    }

    public void welcomeNewUser(String emailAddress) {
        try {
            mailServer.sendEmail(emailAddress, "Welcome!", "Welcome to your account");
        } catch (IllegalArgumentException iae) {
            throw new NotificationFailureException();
        }
    }
}
