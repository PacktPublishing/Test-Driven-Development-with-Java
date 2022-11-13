package com.wordz.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.wordz.domain.GameAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewGameTest {
    @Mock
    private GameRepository gameRepository;

    @Captor
    private ArgumentCaptor<Game> gameArgument ;

    @Mock
    private RandomNumbers random ;

    @Mock
    private WordRepository wordRepository;

    private Wordz wordz;

    @BeforeEach
    void setUp() {
        wordz = new Wordz(gameRepository, wordRepository, random);

        int wordNumber = 2;
        when(random.next(anyInt())).thenReturn(wordNumber);
        when(wordRepository.fetchWordByNumber(wordNumber)).thenReturn("ARISE");
    }

    @Test
    void startsNewGame() {
        var player1 = new Player();
        wordz.newGame(player1);

        verify(gameRepository).create(gameArgument.capture());
        var game = gameArgument.getValue();

        assertThat(game)
            .hasWord("ARISE")
            .hasAttemptNumber(0)
            .hasPlayer(player1);
    }

    @Test
    void selectsRandomWord() {
        var player1 = new Player();
        wordz.newGame(player1);

        verify(gameRepository).create(gameArgument.capture());
        var game = gameArgument.getValue();

        assertThat(game.getWord()).isEqualTo("ARISE");
    }
}
