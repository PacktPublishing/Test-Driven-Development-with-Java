package examples;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserGreetingTest {

    private static final UserId USER_ID = new UserId("1234");
    @Mock
    private UserProfiles profiles;

    @Test
    void formatsGreetingWithName() {
        when(profiles.fetchNicknameFor(USER_ID)).thenReturn("Alan");

        var greeting = new UserGreeting(profiles);

        String actual = greeting.formatGreeting(USER_ID);

        assertThat(actual)
                .isEqualTo("Hello and welcome, Alan");

    }
}
