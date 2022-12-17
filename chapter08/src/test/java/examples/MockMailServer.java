package examples;

import examples.MailServer;

public class MockMailServer implements MailServer {
    boolean wasCalled;
    String actualRecipient;
    String actualSubject;
    String actualText;

    @Override
    public void sendEmail(String recipient, String subject, String text) {
        wasCalled = true;
        actualRecipient = recipient;
        actualSubject = subject;
        actualText = text;
    }
}
