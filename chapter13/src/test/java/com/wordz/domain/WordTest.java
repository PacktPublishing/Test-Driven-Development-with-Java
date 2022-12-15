package com.wordz.domain;

import org.junit.jupiter.api.Test;

import static com.wordz.domain.Letter.*;
import static org.assertj.core.api.Assertions.assertThat;

public class WordTest {
    @Test
    public void oneIncorrectLetter() {
        var word = new Word("A");
        var score = word.guess("Z");
        assertScoreForGuess(score, INCORRECT);
    }

    @Test
    public void oneCorrectLetter() {
        var word = new Word("A");
        var score = word.guess("A");
        assertScoreForGuess(score, CORRECT);
    }

    @Test
    public void secondLetterWrongPosition() {
        var word = new Word("AR");
        var score = word.guess("ZA");
        assertScoreForGuess(score,  INCORRECT,
                                    PART_CORRECT);
    }

    @Test
    public void allScoreCombinations() {
        var word = new Word("ARI");
        var score = word.guess("ZAI");
        assertScoreForGuess(score,  INCORRECT,
                                    PART_CORRECT,
                                    CORRECT);
    }

    @Test
    void reportsAllCorrect() {
        var word = new Word("ARISE");
        var score = word.guess("ARISE");
        assertThat(score.allCorrect()).isTrue();
    }

    @Test
    void reportsNotAllCorrect() {
        var word = new Word("ARISE");
        var score = word.guess("ARI*E");
        assertThat(score.allCorrect()).isFalse();
    }

    @Test
    void accessesLetters() {
        var word = new Word("ARISE");
        var score = word.guess("ARI*E");
        assertThat(score.letters()).hasSize(5);
    }

    private void assertScoreForGuess(Score score, Letter... expectedScores) {
        for (int position = 0; position < expectedScores.length; position++) {
            Letter expected = expectedScores[position];
            assertThat(score.letter(position))
                    .isEqualTo(expected);
        }
    }
}
