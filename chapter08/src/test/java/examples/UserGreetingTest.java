package examples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UserGreetingTest {
    private static final UserId USER_ID = new UserId(1234);
    @Mock
    private UserProfiles profiles;

    @BeforeEach
    void beforeEachTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void formatsGreetingWithName() {
        when(profiles.fetchNicknameFor(USER_ID))
                .thenReturn("Alan");
        var greetings = new UserGreeting(profiles);

        String actual = greetings.formatGreeting(USER_ID);

        assertThat(actual).isEqualTo("Hello and welcome, Alan");
    }
}