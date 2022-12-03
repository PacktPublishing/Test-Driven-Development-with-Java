package com.wordz.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.wordz.domain.GameAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewGameTest {
    public static final String TARGET_WORD = "ARISE";
    @Mock
    private GameRepository gameRepository;

    @Captor
    private ArgumentCaptor<Game> gameArgument ;

    @Mock
    private RandomNumbers random ;

    @Mock
    private WordRepository wordRepository;

    private Wordz wordz;
    private final Player player = new Player("player1");

    @BeforeEach
    void setUp() {
        wordz = new Wordz(gameRepository, wordRepository, random);
    }

    @Test
    void startsNewGame() {
        givenWordInRepository();
        givenNoGameExists(player);

        boolean succeeded = wordz.newGame(player);

        assertThat(succeeded).isTrue();

        assertGameInRepository(player);
    }

    @Test
    void selectsRandomWord() {
        givenWordInRepository();
        givenNoGameExists(player);

        wordz.newGame(player);

        verify(gameRepository).create(gameArgument.capture());
        var game = gameArgument.getValue();

        assertThat(game.getWord()).isEqualTo(TARGET_WORD);
    }

    @Test
    void cannotStartNewGameDuringPlay() {
        givenGameInProgress(player);

        boolean succeeded = wordz.newGame(player);

        assertThat(succeeded).isFalse();
    }

    @Test
    void startsNewGameAfterGameOver() {
        givenWordInRepository();
        givenGameOver(player);

        boolean succeeded = wordz.newGame(player);

        assertThat(succeeded).isTrue();
        assertGameInRepository(player);
    }

    private void givenWordInRepository() {
        int wordNumber = 2;
        when(random.next(anyInt())).thenReturn(wordNumber);
        when(wordRepository.fetchWordByNumber(wordNumber)).thenReturn(TARGET_WORD);
    }

    private void givenGameInProgress(Player player) {
        var gameInProgress = new Game(player, TARGET_WORD, 3, false);
        when(gameRepository.fetchForPlayer(eq(player)))
                .thenReturn(Optional.of(gameInProgress));
    }

    private void givenGameOver(Player player) {
        var gameInProgress = new Game(player, TARGET_WORD, 3, true);
        when(gameRepository.fetchForPlayer(eq(player)))
                .thenReturn(Optional.of(gameInProgress));
    }

    private void givenNoGameExists(Player player) {
        when(gameRepository.fetchForPlayer(eq(player)))
                .thenReturn(Optional.empty());
    }

    private void assertGameInRepository(Player player) {
        verify(gameRepository).create(gameArgument.capture());
        var game = gameArgument.getValue();

        assertThat(game)
                .hasWord(TARGET_WORD)
                .hasAttemptNumber(0)
                .hasPlayer(player);
    }
}
