package examples;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DiceRollTest {

    @Test
    void producesMessage() {
        var stub = new StubRandomNumbers();
        var roll = new DiceRoll(stub);

        var actual = roll.asText();

        assertThat(actual).isEqualTo("You rolled a 5");
    }
}