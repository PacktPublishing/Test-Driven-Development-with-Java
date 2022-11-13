package com.wordz.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.wordz.domain.GameAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GuessTest {
    private static final String CORRECT_WORD = "ARISE";
    private static final String WRONG_WORD = "RXXXX";
    @Mock
    private GameRepository gameRepository;
    @Mock
    private WordRepository wordRepository;
    @Mock
    private RandomNumbers randomNumbers;
    private Player player;
    private Game game;
    private Wordz wordz;

    @BeforeEach
    void setup() {
        player = new Player();
        wordz = new Wordz(gameRepository, wordRepository, randomNumbers);
    }

    @Test
    void returnsScoreForGuess() {
        givenGame(0, false);

        GuessResult result = wordz.assess(player, WRONG_WORD);

        Letter firstLetter = result.score().letter(0);
        assertThat(firstLetter).isEqualTo(Letter.PART_CORRECT);
    }

    @Test
    void updatesAttemptNumber() {
        int initialAttemptNumber = 0;
        givenGame(initialAttemptNumber, false);

        wordz.assess(player, WRONG_WORD);

        Game actual = verifyUpdatedGame();
        assertThat(actual).hasAttemptNumber(initialAttemptNumber+1);
    }

    @Test
    void gameOverOnCorrectGuess(){
        givenGame(0, false);

        GuessResult result = wordz.assess(player, CORRECT_WORD);
        assertThat(result.isGameOver()).isTrue();
    }

    @Test
    void recordsGameOverOnCorrectGuess(){
        givenGame(0, false);

        wordz.assess(player, CORRECT_WORD);

        Game updatedGame = verifyUpdatedGame();
        assertThat(updatedGame).hasGameOver(true);
    }

    @Test
    void gameOverOnTooManyIncorrectGuesses(){
        int maximumGuesses = 5;
        givenGame(maximumGuesses-1, false);

        GuessResult result = wordz.assess(player, WRONG_WORD);

        assertThat(result.isGameOver()).isTrue();
    }


    @Test
    void recordsGameOverOnTooManyGuesses(){
        int maximumAttempts = 5;
        givenGame(maximumAttempts-1, false);

        wordz.assess(player, WRONG_WORD);

        Game updatedGame = verifyUpdatedGame();
        assertThat(updatedGame).hasGameOver(true);
    }

    @Test
    void rejectsGuessAfterGameOver(){
        givenGame( 1, true);

        GuessResult result = wordz.assess(player, WRONG_WORD);

        assertThat(result.isError()).isTrue();
    }

    private void givenGame(int attemptNumber, boolean isGameOver) {
        game = new Game(player, CORRECT_WORD, attemptNumber, isGameOver);
        when(gameRepository.fetchForPlayer(player)).thenReturn(game);
    }

    private Game verifyUpdatedGame() {
        ArgumentCaptor<Game> argument = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).update(argument.capture());
        return argument.getValue();
    }
}
