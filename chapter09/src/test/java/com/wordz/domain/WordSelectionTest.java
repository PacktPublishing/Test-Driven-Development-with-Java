package com.wordz.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WordSelectionTest {

    private static final int HIGHEST_WORD_NUMBER = 3 ;
    private static final int WORD_NUMBER_SHINE = 2 ;
    @Mock
    private WordRepository repository;

    @Mock
    private RandomNumbers random;

    @BeforeEach
    void beforeEachTest() {
        when(repository.highestWordNumber()).thenReturn(HIGHEST_WORD_NUMBER);

        when(repository.fetchWordByNumber(WORD_NUMBER_SHINE)).thenReturn("SHINE");
    }

    @Test
    void selectsWordAtRandom() {
        when(random.next(HIGHEST_WORD_NUMBER)).thenReturn(WORD_NUMBER_SHINE);
        var selector = new WordSelection(repository, random);

        String actual = selector.chooseRandomWord();

        assertThat(actual).isEqualTo("SHINE");
    }
}
