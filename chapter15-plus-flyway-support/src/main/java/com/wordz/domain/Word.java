package com.wordz.domain;

public class Word {
    private final String word;

    public Word(String correctWord) {
        this.word = correctWord;
    }

    public Score guess(String attempt) {
        var score = new Score(word);

        score.assess(attempt);
        return score;
    }
}
