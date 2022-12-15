package com.wordz.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewGameTest {
    private static final Player PLAYER = new Player();
    @Mock
    private GameRepository gameRepository;
    @Mock
    private WordRepository wordRepository ;
    @Mock
    private RandomNumbers randomNumbers ;
    @InjectMocks
    private Wordz wordz;

    @Test
    void startsNewGame() {
        givenWordToSelect("ARISE");

        wordz.newGame(PLAYER);

        Game game = getGameInRepository();
        assertThat(game.getWord()).isEqualTo("ARISE");
        assertThat(game.getAttemptNumber()).isZero();
        assertThat(game.getPlayer()).isSameAs(PLAYER);
    }

    private void givenWordToSelect(String wordToSelect) {
        int wordNumber = 2;

        when(randomNumbers.next(anyInt()))
                .thenReturn(wordNumber);

        when(wordRepository
                .fetchWordByNumber(wordNumber))
                .thenReturn(wordToSelect);
    }

    private Game getGameInRepository() {
        var gameArgument = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository)
                .create(gameArgument.capture());
        var game = gameArgument.getValue();
        return game;
    }
}

